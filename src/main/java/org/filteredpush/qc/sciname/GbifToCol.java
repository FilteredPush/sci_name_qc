/**
 * GbifToCol.java
 *
 * Copyright 2026 President and Fellows of Harvard College
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.filteredpush.qc.sciname;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.COLService;
import org.filteredpush.qc.sciname.services.GBIFService;

import edu.harvard.mcz.nametools.AuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;

/**
 * Find the Catalogue of Life identifiers (col:{id}) for GBIF Backbone Taxonomy identifiers (gbif:{key}, or
 * links to the backbone such as https://www.gbif.org/species/{key}).
 *
 * Each GBIF name usage is looked up in GBIF, and the same name (canonical name, at the same rank, in the same
 * kingdom) is looked up in the Catalogue of Life; the authorships are then compared (AuthorNameComparator,
 * with IPNI standard forms for botanical names).  The identifier is for the name given: a GBIF synonym maps to
 * the COL name usage of that synonym, not to COL's accepted name.  An identifier is proposed only when one COL
 * name usage matches; otherwise the candidates are listed in the remark.
 *
 * Command line: java -cp sci_name_qc-{version}-executable.jar org.filteredpush.qc.sciname.GbifToCol -f {file}
 *
 * @author mole
 */
public class GbifToCol {

	private static final Log logger = LogFactory.getLog(GbifToCol.class);

	/** How a GBIF name usage matched a COL name usage. */
	public enum MatchType {
		/** The same name, rank, kingdom, and authorship (ignoring spacing and punctuation). */
		EXACT,
		/** The same name, rank, and kingdom, with a form of the same authorship (e.g. abbreviated, ex author omitted). */
		SAME_AUTHORSHIP,
		/** The same name, rank, and kingdom, where one of the two has no authorship to compare. */
		NAME_ONLY,
		/** More than one COL name usage matches equally well; no identifier is proposed. */
		AMBIGUOUS,
		/** The name is in COL, but with a different authorship (a different name, e.g. a homonym); no identifier is proposed. */
		AUTHORSHIP_DIFFERS,
		/** The name is not in COL at that rank and kingdom. */
		NOT_FOUND,
		/** The input is not a GBIF Backbone Taxonomy identifier. */
		NOT_GBIF_IDENTIFIER,
		/** GBIF has no name usage with the key. */
		NOT_IN_GBIF,
		/** A service could not be queried. */
		ERROR
	}

	/** The result of mapping one GBIF identifier. */
	public static class Mapping {
		public String input;
		public Integer gbifKey;
		public String gbifScientificName = "";
		public String gbifAuthorship = "";
		public String gbifRank = "";
		public String gbifStatus = "";
		public String kingdom = "";
		public String colId = "";
		public String colScientificName = "";
		public String colAuthorship = "";
		public String colStatus = "";
		public MatchType match;
		public String remark = "";

		/** @return the CSV header for mappings */
		public static String[] header() {
			return new String[] { "input", "gbifKey", "gbifScientificName", "gbifAuthorship", "gbifRank", "gbifStatus", "kingdom",
					"colId", "colScientificName", "colAuthorship", "colStatus", "match", "remark" };
		}

		/** @return the values of this mapping, for CSV */
		public Object[] values() {
			return new Object[] { input, gbifKey==null ? "" : gbifKey, gbifScientificName, gbifAuthorship, gbifRank, gbifStatus, kingdom,
					colId, colScientificName, colAuthorship, colStatus, match, remark };
		}
	}

	private static final Pattern GBIF_ID = Pattern.compile(
			"^(?:gbif:|GBIF:|https?://(?:www\\.)?gbif\\.org/species/|https?://api\\.gbif\\.org/v1/species/)([0-9]+)/?$");

	/**
	 * The GBIF Backbone Taxonomy key in an identifier.
	 *
	 * @param identifier gbif:{key}, https://www.gbif.org/species/{key}, or https://api.gbif.org/v1/species/{key}
	 * @return the key, or null if identifier is not in one of those forms
	 */
	public static Integer gbifKey(String identifier) {
		if (identifier==null) { return null; }
		Matcher m = GBIF_ID.matcher(identifier.trim());
		return m.matches() ? Integer.valueOf(m.group(1)) : null;
	}

	private final COLService col;

	/**
	 * Map to a release of the Catalogue of Life.
	 *
	 * @param release a ChecklistBank dataset key or release alias (e.g. COL25), or null or Latest for the latest release
	 */
	public GbifToCol(String release) {
		this.col = new COLService(release);
	}

	/**
	 * Find the Catalogue of Life identifier for a GBIF Backbone Taxonomy identifier.
	 *
	 * @param identifier gbif:{key} or a link to the GBIF backbone
	 * @return the mapping
	 */
	public Mapping map(String identifier) {
		Mapping result = new Mapping();
		result.input = identifier==null ? "" : identifier.trim();
		result.gbifKey = gbifKey(identifier);
		if (result.gbifKey==null) {
			result.match = MatchType.NOT_GBIF_IDENTIFIER;
			result.remark = "Expected gbif:{key} or https://www.gbif.org/species/{key}";
			return result;
		}
		NameUsage gbif;
		try {
			gbif = GBIFService.lookupNameByID(Integer.toString(result.gbifKey));
		} catch (java.io.FileNotFoundException e) {
			// HTTP 404: no name usage with this key
			result.match = MatchType.NOT_IN_GBIF;
			return result;
		} catch (IOException e) {
			result.match = MatchType.ERROR;
			result.remark = "GBIF not available: " + e.getMessage();
			return result;
		}
		if (gbif==null || SciNameUtils.isEmpty(gbif.getCanonicalName())) {
			result.match = MatchType.NOT_IN_GBIF;
			return result;
		}
		result.gbifScientificName = gbif.getCanonicalName();
		result.gbifAuthorship = gbif.getAuthorship()==null ? "" : gbif.getAuthorship().trim();
		result.gbifRank = gbif.getRank()==null ? "" : gbif.getRank();
		result.gbifStatus = gbif.getTaxonomicStatus();
		result.kingdom = gbif.getKingdom()==null ? "" : gbif.getKingdom();
		return match(result);
	}

	/**
	 * Match a name (given in mapping: gbifScientificName, gbifAuthorship, gbifRank, kingdom) in the Catalogue of Life.
	 *
	 * @param result a mapping with the GBIF name filled in, to complete
	 * @return result
	 */
	Mapping match(Mapping result) {
		List<NameUsage> candidates;
		try {
			candidates = col.lookupTaxonAtRank(result.gbifScientificName, result.gbifRank.isEmpty() ? null : result.gbifRank);
		} catch (IOException e) {
			result.match = MatchType.ERROR;
			result.remark = COLService.SERVICE_NAME + " not available: " + e.getMessage();
			return result;
		}
		boolean knownKingdom = !result.kingdom.isEmpty() && !result.kingdom.equalsIgnoreCase("incertae sedis");
		List<NameUsage> sameKingdom = new ArrayList<NameUsage>();
		for (NameUsage c : candidates) {
			if (!knownKingdom || c.getKingdom()==null || c.getKingdom().isEmpty() || c.getKingdom().equalsIgnoreCase(result.kingdom)) {
				sameKingdom.add(c);
			}
		}
		if (sameKingdom.isEmpty()) {
			result.match = MatchType.NOT_FOUND;
			if (!candidates.isEmpty()) {
				result.remark = "In another kingdom in COL: " + COLService.describe(candidates);
			}
			return result;
		}
		List<NameUsage> exact = new ArrayList<NameUsage>();
		List<NameUsage> same = new ArrayList<NameUsage>();
		List<NameUsage> nameOnly = new ArrayList<NameUsage>();
		List<String> differs = new ArrayList<String>();
		for (NameUsage c : sameKingdom) {
			String colAuthorship = c.getAuthorship()==null ? "" : c.getAuthorship().trim();
			if (result.gbifAuthorship.isEmpty() || colAuthorship.isEmpty()) {
				if (result.gbifAuthorship.isEmpty() && colAuthorship.isEmpty()) {
					exact.add(c);
				} else {
					nameOnly.add(c);
				}
				continue;
			}
			AuthorNameComparator comparator = AuthorNameComparator.authorNameComparatorFactory(result.gbifAuthorship, result.kingdom);
			NameComparison comparison = comparator.compare(result.gbifAuthorship, colAuthorship);
			if (comparison.getMatchType().equals(NameComparison.MATCH_EXACT)) {
				exact.add(c);
			} else if (NameComparison.isPlausibleAuthorMatch(comparison.getMatchType())) {
				same.add(c);
			} else {
				differs.add(c.getGuid() + " " + colAuthorship + " (" + comparison.getMatchType()
						+ (comparison.getRemark()==null || comparison.getRemark().equals(comparison.getMatchType()) ? "" : ": " + comparison.getRemark()) + ")");
			}
		}
		if (exact.size()==1) {
			return found(result, exact.get(0), MatchType.EXACT);
		}
		if (exact.size()>1) {
			return ambiguous(result, exact);
		}
		if (same.size()==1) {
			return found(result, same.get(0), MatchType.SAME_AUTHORSHIP);
		}
		if (same.size()>1) {
			return ambiguous(result, same);
		}
		if (nameOnly.size()==1) {
			found(result, nameOnly.get(0), MatchType.NAME_ONLY);
			result.remark = "No authorship to compare in " + (result.gbifAuthorship.isEmpty() ? "GBIF" : "COL");
			return result;
		}
		if (nameOnly.size()>1) {
			return ambiguous(result, nameOnly);
		}
		result.match = MatchType.AUTHORSHIP_DIFFERS;
		result.remark = "In COL with a different authorship: " + String.join("; ", differs);
		return result;
	}

	private static Mapping found(Mapping result, NameUsage c, MatchType match) {
		result.colId = c.getGuid();
		result.colScientificName = c.getCanonicalName();
		result.colAuthorship = c.getAuthorship()==null ? "" : c.getAuthorship();
		result.colStatus = c.getTaxonomicStatus().toLowerCase();
		result.match = match;
		return result;
	}

	private static Mapping ambiguous(Mapping result, List<NameUsage> candidates) {
		result.match = MatchType.AMBIGUOUS;
		result.remark = "More than one name usage in COL: " + COLService.describe(candidates);
		return result;
	}

	/**
	 * Map the GBIF identifiers in a file (one per line, or the first column of a CSV file; lines that are not
	 * GBIF identifiers, such as a header, are reported as NOT_GBIF_IDENTIFIER) and write the mappings as CSV.
	 *
	 * @param args command line arguments, see --help
	 */
	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("f", "file", true, "Input file: GBIF identifiers (gbif:{key}, https://www.gbif.org/species/{key}), one per line, or in the first column of a CSV file.");
		options.addOption("o", "output", true, "Output CSV file, default gbif_to_col.csv.");
		options.addOption("r", "release", true, "Catalogue of Life release: Latest (default), or a ChecklistBank dataset key or release alias, e.g. 317005, COL25.");
		options.addOption("h", "help", false, "Print this message.");
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h") || !cmd.hasOption("f")) {
				new HelpFormatter().printHelp("GbifToCol", options);
				return;
			}
			GbifToCol mapper = new GbifToCol(cmd.getOptionValue("r", "Latest"));
			String output = cmd.getOptionValue("o", "gbif_to_col.csv");
			int count = 0;
			try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(cmd.getOptionValue("f")), StandardCharsets.UTF_8));
					CSVPrinter out = new CSVPrinter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8)), CSVFormat.DEFAULT)) {
				out.printRecord((Object[])Mapping.header());
				String line;
				while ((line = in.readLine())!=null) {
					String identifier = line.split(",", 2)[0].replace("\"", "").trim();
					if (identifier.isEmpty()) { continue; }
					out.printRecord(mapper.map(identifier).values());
					count++;
				}
			}
			System.err.println("Mapped " + count + " identifiers to " + output);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			new HelpFormatter().printHelp("GbifToCol", options);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.err.println(e.getMessage());
		}
	}
}
