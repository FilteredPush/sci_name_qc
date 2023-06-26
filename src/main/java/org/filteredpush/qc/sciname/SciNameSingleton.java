/**
 * SciNameSingleton.java
 */
package org.filteredpush.qc.sciname;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @author mole
 *
 */
public class SciNameSingleton {

	private static final Log logger = LogFactory.getLog(SciNameSingleton.class);

	private static final SciNameSingleton instance = new SciNameSingleton();
	private Map<String,Boolean> tgnCountries;
	
	private RankAuthorityLoader loader;
	
	
	private SciNameSingleton() { 
		init();
	}
	
	private void init() { 
		tgnCountries = new HashMap<String,Boolean>();
		loader = new RankAuthorityLoader();
		try {
			loader.load();
		} catch (IOException | ParserConfigurationException | SAXException e) {
		   logger.debug(e.getMessage(), e);
		}
		
	}
	
	public static synchronized SciNameSingleton getInstance() {
		return instance;
	}

	public String getRank(String key) { 
		return loader.getValuesCopy().get(key);
	}
	
	public Boolean checkRankKnown(String rank) { 
		Boolean retval = null;
		
		retval = loader.getValuesCopy().containsKey(rank);
		
		return retval;
	}
	
	/**
	 * Check cache of TGNCountry matches against a provided country.
	 * 
	 * @param country to check for previous lookup
	 * @return null if no match was found, otherwise the cached true or false value for
	 * a match on that country.
	 */
	public Boolean getTgnCountriesEntry(String country) { 
		Boolean retval = null;
		if (tgnCountries.containsKey(country)) { 
			Boolean value = tgnCountries.get(country);
			if (value!=null) { 
				retval = value;
			}
		}
		return retval;
	}
	
	/**
	 * Cache a match on a country name.
	 * 
	 * @param country key to cache
	 * @param match true or false to store in the cache, if null
	 *  not added to the cache.
	 */
	public void addTgnCountry(String country, Boolean match) { 
		if (match!=null) { 
			tgnCountries.put(country, match);
		}
	}
	
}