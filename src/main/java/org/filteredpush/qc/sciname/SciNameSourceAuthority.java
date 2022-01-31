/** 
 * SciNameSourceAuthority.java 
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

import org.filteredpush.qc.sciname.services.GBIFService;

/**
 * Identify source authorities for scientific names, handling both specific services
 * and services which can take some form of dataset identifier (such as GBIF's API) 
 * to add to queries.
 * 
 * @author mole
 *
 */
public class SciNameSourceAuthority {
	
	private EnumSciNameSourceAuthority authority;
	private String authoritySubDataset;
	
	/**
	 * Create a SciNameSourceAuthority for the GBIF Backbone Taxonomy.
	 */
	public SciNameSourceAuthority() { 
		authority = EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY;
		updateDefaultSubAuthorities();
	}
	
	/**
	 * Construct a scientific name source authority descriptor where additional information on a sub data set 
	 * is not needed.
	 * 
	 * @param authority the authority
	 * @throws SourceAuthorityException if the authority specified requires a sub data set specification
	 */
	public SciNameSourceAuthority(EnumSciNameSourceAuthority authority) throws SourceAuthorityException { 
	    if (authority.equals(EnumSciNameSourceAuthority.GBIF_ARBITRARY)) {
	    	throw new SourceAuthorityException("You must specify which GBIF checklist you wish to use with " + EnumSciNameSourceAuthority.GBIF_ARBITRARY.getName());
	    }
		this.authority = authority;
		authoritySubDataset = null;
		updateDefaultSubAuthorities();
	}
	
	/**
	 * Construct a scientific name source authority descriptor.
	 * 
	 * @param authority the authority to use
	 * @param authoritySubDataset the specific authority (e.g. GBIF checklist) to use.
	 */
	public SciNameSourceAuthority(EnumSciNameSourceAuthority authority, String authoritySubDataset) {
		this.authority = authority;
		this.authoritySubDataset = authoritySubDataset;
		updateDefaultSubAuthorities();
	}
	
	public EnumSciNameSourceAuthority getAuthority() {
		return authority;
	}

	public String getAuthoritySubDataset() {
		return authoritySubDataset;
	}	
	
	/**
	 * For those authorities which have sub datasets (GBIF Checklist Bank, specific checklists)
	 * Set the authoritySubDataset to the correct value for the specified authority, e.g. 
	 * for GBIF_BACKBONE_TAXONOMY in GBIF checklist bank, set the authoritySubDatset to 
	 * GBIFService.KEY_GBIFBACKBONE (=d7dddbf4-2cf0-4f39-9b2a-bb099caae36c).
	 */
	private void updateDefaultSubAuthorities() { 
		switch (this.authority) {
		case GBIF_BACKBONE_TAXONOMY: 
			this.authoritySubDataset = GBIFService.KEY_GBIFBACKBONE;
			break;
		case GBIF_COL:
			this.authoritySubDataset = GBIFService.KEY_COL;
			break;
		case GBIF_PALEOBIOLOGY_DATABASE:
			this.authoritySubDataset = GBIFService.KEY_PALEIOBIOLOGY_DATABASE;
			break;
		case GBIF_IPNI:
			this.authoritySubDataset = GBIFService.KEY_IPNI;
			break;
		case GBIF_INDEX_FUNGORUM:
			this.authoritySubDataset = GBIFService.KEY_INDEXFUNGORUM;
			break;
		case GBIF_ITIS:
			this.authoritySubDataset = GBIFService.KEY_ITIS;
			break;
		case GBIF_UKSI:
			this.authoritySubDataset = GBIFService.KEY_UKSI;
			break;
		case GBIF_FAUNA_EUROPAEA:
			this.authoritySubDataset = GBIFService.KEY_FAUNA_EUROPAEA;
			break;
		default:
			// don't overwrite a specified sub authority/
			break;
		}
	}
	
	public boolean isGBIFChecklist() { 
		boolean result = false;
		switch (this.authority) {
		case GBIF_BACKBONE_TAXONOMY: 
			result = true;
			break;
		case GBIF_COL:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_COL;
			break;
		case GBIF_PALEOBIOLOGY_DATABASE:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_PALEIOBIOLOGY_DATABASE;
			break;
		case GBIF_IPNI:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_IPNI;
			break;
		case GBIF_INDEX_FUNGORUM:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_INDEXFUNGORUM;
			break;
		case GBIF_ITIS:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_ITIS;
			break;
		case GBIF_UKSI:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_UKSI;
			break;
		case GBIF_FAUNA_EUROPAEA:
			result = true;
			this.authoritySubDataset = GBIFService.KEY_FAUNA_EUROPAEA;
			break;
		case GBIF_ARBITRARY: 
			if (this.authoritySubDataset!=null && this.authoritySubDataset.length() > 0)
			result = true;
			break;
		default:
			break;
		}
		
		return result;
	}

	public String getName() {
		
		return authority.getName();
	}

}
