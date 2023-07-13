/**
 * ZooBankService.java
 *
 * Copyright 2015 President and Fellows of Harvard College
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
package org.filteredpush.qc.sciname.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.filteredpush.qc.sciname.services.ZooBankNomenclaturalAct;
import org.gbif.nameparser.NameParserGBIF;
import org.gbif.nameparser.api.NameParser;
import org.gbif.nameparser.api.ParsedName;
import org.gbif.nameparser.api.UnparsableNameException;

import edu.harvard.mcz.nametools.ICZNAuthorNameComparator;
import edu.harvard.mcz.nametools.NameComparison;
import edu.harvard.mcz.nametools.NameUsage;
import edu.harvard.mcz.nametools.ScientificNameComparator;

/**
 * @author mole
 *
 */
public class ZooBankService implements Validator {

	private static final Log log = LogFactory.getLog(ZooBankService.class);
	
	public ZooBankService() throws IOException { 
		init();
    }

    protected void init()  throws IOException { 
		URL url = new URL("http://zoobank.org/NomenclaturalActs.json/Pseudanthias_carlsoni");
		InputStream is = url.openStream();
	    log.debug(url.toString());
    }
	
	
	/* (non-Javadoc)
	 * @see edu.harvard.mcz.nametools.Validator#validate(edu.harvard.mcz.nametools.NameUsage)
	 */
	@Override
	public NameUsage validate(NameUsage taxonToValidate) {
		NameUsage returnValue = null;
		
		// Documentation at: http://zoobank.org/Api
		// example call http://zoobank.org/NomenclaturalActs.json/Pseudanthias_carlsoni
		
		/* example result:
[{"tnuuuid":"","OriginalReferenceUUID":"427D7953-E8FC-41E8-BEA7-8AE644E6DE77",
"protonymuuid": "6EA8BB2A-A57B-47C1-953E-042D8CD8E0E2", "label": "carlsoni Randall & Pyle 2001", 
"value": "carlsoni Randall & Pyle 2001",
"lsid":"urn:lsid:zoobank.org:act:6EA8BB2A-A57B-47C1-953E-042D8CD8E0E2",
"parentname":"","namestring":"carlsoni","rankgroup":"Species","usageauthors":"",
"taxonnamerankid":"","parentusageuuid":"",
"cleanprotonym" : "Pseudanthias carlsoni Randall & Pyle, 2001","NomenclaturalCode":"ICZN"}]		  
		
		 */
		String author = taxonToValidate.getAuthorship();
		try { 
		    URL url = new URL("http://zoobank.org/NomenclaturalActs.json/" + taxonToValidate.getScientificName().replace(' ', '_'));
	        log.debug(url.toString());
			InputStream is = url.openStream();
			log.debug("1");
		    JsonReader rdr = Json.createReader(is);
			log.debug("2");
		    JsonArray results = rdr.readArray();
		    log.debug(results.toString());
		    double bestSimilarity = -1d;
		    for (JsonObject result : results.getValuesAs(JsonObject.class)) {
		    	ZooBankNomenclaturalAct act = new ZooBankNomenclaturalAct(result);
		    	if (act.getCleanprotonym().startsWith("[Genus?]")) { 
		    		// Incomplete entry in ZooBank, original genus is not recorded
		    		// therefore can't match on this entry.
		    		log.debug(act.getCleanprotonym());
		    	} else { 
		    	    ScientificNameComparator sciComp = new ScientificNameComparator();
		    		NameUsage usage = new NameUsage();
		    		String scientificName = act.getCleanprotonym();
		    		String foundAuthor = "";
		    		NameParser nameParser = new NameParserGBIF();
		    		try {
		    			ParsedName parse = nameParser.parse(scientificName);
		    			foundAuthor = parse.authorshipComplete();
		    			if (foundAuthor!=null && foundAuthor.length()>0) { 
		    				scientificName = parse.canonicalNameWithoutAuthorship();
		    			}
		    			log.debug(scientificName);
		    			if (act.getValue().startsWith(act.getNamestring())) { 
		    				foundAuthor = act.getValue().replaceFirst(act.getNamestring(), "").trim();
		    			}
		    		} catch (UnparsableNameException e) {
		    			log.error(e.getMessage());
		    		}
		    		try {
						nameParser.close();
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
		    		NameComparison nameComparison = sciComp.compareWithoutAuthor(taxonToValidate.getScientificName(), scientificName);
		    		if (nameComparison.equals(NameComparison.MATCH_EXACT)) { 
		    			usage.setScientificName(scientificName);
		    			usage.setAuthorship(foundAuthor);
		    			usage.setGuid(act.getLsid());
		    			usage.setAuthorComparator(new ICZNAuthorNameComparator(.75d,.5d));
		    			usage.setOriginalAuthorship(author);
		    			usage.setOriginalScientificName(taxonToValidate.getScientificName());
		    			usage.setInputDbPK(taxonToValidate.getInputDbPK());
		    			usage.setScientificNameStringEditDistance(nameComparison.getSimilarity());
		    			usage.setNameMatchDescription(nameComparison.getMatchType());
		    			NameComparison comparison = usage.getAuthorComparator().compare(author, foundAuthor);
		    			usage.setMatchDescription(comparison.getMatchType());
		    			if (bestSimilarity < 0 || comparison.getMatchType().equals(NameComparison.MATCH_EXACT) || comparison.getSimilarity() > bestSimilarity) { 
		    				returnValue = usage;
		    			}
		    		} 
		    	}
		    }
		} catch (FileNotFoundException ex) { 
			// expected ZooBank exception when no match is found
			log.debug(ex.getMessage());
		} catch (IOException e) { 
			log.error(e.getMessage(),e);
		}
		
		return returnValue;
	}

	@Override
	public List<String> supportedExtensionTerms() {
		return new ArrayList<String>();
	}

}