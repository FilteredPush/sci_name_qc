/** 
 * BatchRunner.java
 * 
 * Copyright 2022 President and Fellows of Harvard College
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.ServiceException;
import org.filteredpush.qc.sciname.services.Validator;

import edu.harvard.mcz.nametools.NameUsage;

/**
 * <p>BatchRunner class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class BatchRunner {

	private static final Log logger = LogFactory.getLog(BatchRunner.class);

	private File inputFile;
	private File outputFile;
	private Validator validator;
	
	/**
	 * Constructor for a BatchRunner to read scientific names from an input file, run them against
	 * specfied validator class, and write the output to an output file.
	 *
	 * @param inputFileName containing a list of taxon names to evaluate
	 * @param outputFileName into which to write results.
	 * @param validator to apply to the names
	 * @throws org.filteredpush.qc.sciname.FileException if there is a problem reading the input file
	 *  or writing the output file.
	 */
	public BatchRunner(String inputFileName, String outputFileName, Validator validator) throws FileException {
	   File targetInputFile = new File(inputFileName);
	   if (targetInputFile.exists() && targetInputFile.canRead()) { 
		   this.inputFile = targetInputFile;
	   } else { 
		   throw new FileException("Unable to read input file [" + inputFileName + "].");
	   }
	   if (outputFileName==null || outputFileName.trim().length()==0) { 
		   outputFileName = inputFile.getName().substring(0, inputFile.getName().indexOf(".")).concat("_out_").concat(LocalDate.now().toString().replace("-","_")).concat(".csv");
	   }
	   File targetOutputFile = new File(outputFileName);
	   if (targetOutputFile.exists()) { 
		   throw new FileException("Output file [" + outputFileName + "] exists, won't overwrite.");
	   }
	   this.outputFile = targetOutputFile;
	   this.validator = validator;
	}

	/**
	 * Execute the batch operation.
	 *
	 * @return success of the batch operation
	 */
	public boolean runBatch() { 
		boolean result = false;
		
		logger.debug("Reading from " + inputFile.getName());
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			CSVParser records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

			List<String> headerNames = new ArrayList<String>();
			headerNames.add("dbpk");
			headerNames.add("scientificName");
			headerNames.add("authorship");
			headerNames.add("guid");
			headerNames.add("match");
			headerNames.add("nameMatch");
			headerNames.add("sciNameWas");
			headerNames.add("authorWas");
			headerNames.add("authorSimilarity");
			headerNames.add("nameSimilarity");
			if (validator.supportedExtensionTerms().size() > 0) {
				headerNames.addAll(validator.supportedExtensionTerms());
			}
			headerNames.add("acceptedAsName");
			headerNames.add("acceptedAsNameAuthorship");

			BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
			CSVPrinter printer = new CSVPrinter(writer, 
					CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL).withHeader(headerNames.toArray(new String[headerNames.size()])));

			Iterator<CSVRecord> recordIterator = records.iterator();
			while (recordIterator.hasNext()) {
				CSVRecord line = recordIterator.next();
				logger.debug(line.get("dbpk"));
				NameUsage usage = new NameUsage();
				usage.setInputDbPK(Integer.parseInt(line.get("dbpk")));
				usage.setScientificName(line.get("scientificName"));
				usage.setAuthorship(line.get("authorship"));
				if (line.toMap().containsKey("kingdom")) { 
					usage.setKingdom(line.get("kingdom"));
				} 
				if (line.toMap().containsKey("family")) { 
					usage.setFamily(line.get("family"));
				} 

				try { 
					NameUsage vUsage = validator.validate(usage);
					if (validator.supportedExtensionTerms().size() > 0) {
						List<String> columns = new ArrayList<String>();
						if (vUsage!=null) {
							Map<String,String> terms = vUsage.getExtension();
							//String[] added = terms.values().toArray(new String[terms.size()]);
							logger.debug(vUsage.getGuid());
							columns.add(Integer.valueOf(vUsage.getInputDbPK()).toString());
							columns.add(vUsage.getScientificName());
							columns.add(vUsage.getAuthorship());
							columns.add(vUsage.getGuid());
							columns.add(vUsage.getMatchDescription());
							columns.add(vUsage.getNameMatchDescription());
							columns.add(vUsage.getOriginalScientificName());
							columns.add(vUsage.getOriginalAuthorship());
							columns.add(Double.valueOf(vUsage.getAuthorshipStringEditDistance()).toString());
							columns.add(Double.valueOf(vUsage.getScientificNameStringEditDistance()).toString());
							Iterator<String> addedTermIterator = validator.supportedExtensionTerms().iterator();
							while (addedTermIterator.hasNext()) { 
								// add in the order of the supportedExtenstionTerms list, to match the header.
								columns.add(terms.get(addedTermIterator.next()));
							}
							columns.add(vUsage.getAcceptedName());
							columns.add(vUsage.getAcceptedAuthorship());
							printer.printRecord(columns);
							//printer.printRecord(Integer.valueOf(vUsage.getInputDbPK()).toString(),vUsage.getScientificName(),vUsage.getAuthorship(),vUsage.getGuid(),vUsage.getMatchDescription(),vUsage.getOriginalScientificName(), vUsage.getOriginalAuthorship(), Double.valueOf(vUsage.getAuthorshipStringEditDistance()).toString(), added);
						} else { 
							String[] added = new String[validator.supportedExtensionTerms().size()];
							usage.setMatchDescription("Not Found");
							logger.debug("Not found");
							columns.add(Integer.valueOf(usage.getInputDbPK()).toString());
							columns.add(usage.getScientificName());
							columns.add(usage.getAuthorship());
							columns.add(usage.getGuid());
							columns.add(usage.getMatchDescription());
							columns.add("");
							columns.add(usage.getOriginalScientificName());
							columns.add(usage.getOriginalAuthorship());
							columns.add(Double.valueOf(usage.getAuthorshipStringEditDistance()).toString());
							columns.add(Double.valueOf(usage.getScientificNameStringEditDistance()).toString());
							columns.addAll(Arrays.asList(added));
							printer.printRecord(columns);
							//printer.printRecord(Integer.valueOf(usage.getInputDbPK()).toString(),usage.getScientificName(),usage.getAuthorship(),usage.getGuid(),usage.getMatchDescription(),usage.getOriginalScientificName(), usage.getOriginalAuthorship(), Double.valueOf(usage.getAuthorshipStringEditDistance()).toString(), added);
						}	
					} else { 
						if (vUsage!=null) {
							logger.debug(vUsage.getGuid());
							printer.printRecord(Integer.valueOf(vUsage.getInputDbPK()).toString(),vUsage.getScientificName(),vUsage.getAuthorship(),vUsage.getGuid(),vUsage.getMatchDescription(),vUsage.getOriginalScientificName(), vUsage.getOriginalAuthorship(), Double.valueOf(vUsage.getAuthorshipStringEditDistance()).toString(),Double.valueOf(usage.getScientificNameStringEditDistance()).toString());
						} else { 
							usage.setMatchDescription("Not Found");
							logger.debug("Not found");
							printer.printRecord(Integer.valueOf(usage.getInputDbPK()).toString(),usage.getScientificName(),usage.getAuthorship(),usage.getGuid(),usage.getMatchDescription(),usage.getOriginalScientificName(), usage.getOriginalAuthorship(), Double.valueOf(usage.getAuthorshipStringEditDistance()).toString(),Double.valueOf(usage.getScientificNameStringEditDistance()).toString());
						}	
					}
				} catch (ServiceException ex) { 
					logger.error(ex.getMessage());
					printer.printRecord(Integer.valueOf(usage.getInputDbPK()).toString(),"","","","Error, Lookup Failed.",usage.getOriginalScientificName(), usage.getOriginalAuthorship(),"");
				}
				
				printer.flush();
			}
			records.close();
			reader.close();
			printer.close();
			writer.close();
		} catch (Exception e) { 
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
}
