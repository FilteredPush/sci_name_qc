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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.datakurator.ffdq.api.DQResponse;
import org.datakurator.ffdq.api.result.AmendmentValue;
import org.filteredpush.qc.sciname.services.GBIFService;

import com.squareup.okhttp.logging.HttpLoggingInterceptor.Logger;

/**
 * Identify source authorities for scientific names, handling both specific services
 * and services which can take some form of dataset identifier (such as GBIF's API)
 * to add to queries.
 *
 * @author mole
 * @version $Id: $Id
 */
public class SciNameSourceAuthority {
	
	private EnumSciNameSourceAuthority authority;
	private String authoritySubDataset;
	
	private static final Log logger = LogFactory.getLog(SciNameSourceAuthority.class);
	
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
	 * @throws org.filteredpush.qc.sciname.SourceAuthorityException if the authority specified requires a sub data set specification
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
	 * Utility constructor to construct a scientific name source authority from a string instead of the enum.
	 *
	 * @param authorityString a value matching the name of an item in EnumSciNameSourceAuthority
	 * @throws org.filteredpush.qc.sciname.SourceAuthorityException if the string is not matched to the enumeration, or if the specified
	 *   source authority requires the specification of an authoritySubDataset.
	 */
	public SciNameSourceAuthority(String authorityString) throws SourceAuthorityException {
		logger.debug(authorityString);
		if (authorityString==null) { authorityString = ""; }
	    if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_ARBITRARY.getName())) {
	    	throw new SourceAuthorityException("You must specify which GBIF checklist you wish to use with " + EnumSciNameSourceAuthority.GBIF_ARBITRARY.getName());
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.WORMS.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.WORMS;	
	    } else if (authorityString.toUpperCase().equals("MARINESPECIES.ORG")) {
	    	this.authority = EnumSciNameSourceAuthority.WORMS;	
	    } else if (authorityString.toUpperCase().equals("WORMS")) {
	    	this.authority = EnumSciNameSourceAuthority.WORMS;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.IRMNG.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.IRMNG;	
	    } else if (authorityString.toUpperCase().equals("IRMNG.ORG")) {
	    	this.authority = EnumSciNameSourceAuthority.IRMNG;	
	    } else if (authorityString.toUpperCase().equals("IRMNG")) {
	    	this.authority = EnumSciNameSourceAuthority.IRMNG;	
	    } else if (authorityString.toUpperCase().equals("https://api.biodiversity.org.au/name/check?dataset=APNI".toUpperCase())) {
	    	this.authority = EnumSciNameSourceAuthority.ANSL_APNI;	
	    } else if (authorityString.toUpperCase().equals("APNI")) {
	    	this.authority = EnumSciNameSourceAuthority.ANSL_APNI;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_BACKBONE_TAXONOMY;
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_COL.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_COL;
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_FAUNA_EUROPAEA.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_FAUNA_EUROPAEA;
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_INDEX_FUNGORUM.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_INDEX_FUNGORUM;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_IPNI.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_IPNI;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_ITIS.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_ITIS;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_PALEOBIOLOGY_DATABASE.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_PALEOBIOLOGY_DATABASE;	
	    } else if (authorityString.toUpperCase().equals(EnumSciNameSourceAuthority.GBIF_UKSI.getName())) {
	    	this.authority = EnumSciNameSourceAuthority.GBIF_UKSI;	
	    } else if (authorityString.toUpperCase().startsWith("HTTPS://INVALID/")) { 
	    	this.authority = EnumSciNameSourceAuthority.INVALID;	
	    } else { 
	    	throw new SourceAuthorityException("Unable to construct a SourceAuthority from string [" + authorityString + "]");
	    }
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
	
	/**
	 * <p>Getter for the field <code>authority</code>.</p>
	 *
	 * @return a {@link org.filteredpush.qc.sciname.EnumSciNameSourceAuthority} object.
	 */
	public EnumSciNameSourceAuthority getAuthority() {
		return authority;
	}

	/**
	 * <p>Getter for the field <code>authoritySubDataset</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
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
	
	/**
	 * <p>isGBIFChecklist.</p>
	 *
	 * @return a boolean.
	 */
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

	/**
	 * <p>getName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		
		return authority.getName();
	}

}
