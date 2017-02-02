/** 
 * SciNameUtils.java 
 * 
 * Copyright 2016 President and Fellows of Harvard College
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


import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globalnames.parser.ScientificNameParser;
import org.globalnames.parser.ScientificNameParser.Result;
import org.json4s.JsonAST.JValue;

import scala.collection.immutable.List;

/**
 * Utility functions for working with DarwinCore scientific name terms.
 * 
 * @author mole
 *
 */
public class SciNameUtils {

	private static final Log logger = LogFactory.getLog(SciNameUtils.class);
	
	public SciNameUtils() { 
		
	} 
	
	public static void main(String[] args) { 
		Result parse = ScientificNameParser.instance().fromString("Buccinum canetae Clench & Aguayo, 1944");
		System.out.println(parse.ambiguousAuthorship());
		System.out.println(parse.delimitedString("|"));
		System.out.println(parse.authorshipDelimited().toString());
		System.out.println(parse.canonized(true));
		System.out.println(parse.normalized());
		System.out.println(parse.scientificName());
		List<JValue> parseBits = parse.detailed().children(); 
		scala.collection.Iterator<JValue> i = parseBits.iterator();
		while (i.hasNext()) {
			JValue bit = i.next();
			System.out.println(bit.toString());
			scala.collection.Iterator<JValue> it = bit.children().iterator();
			while (it.hasNext()) { 
				JValue bit2 = it.next();
				System.out.println(bit2.toString());
			}
		}
		System.out.println();
		System.out.println(parse.json(true));
		
	}
}
