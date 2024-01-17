/**
 * ZooBankNomenclaturalAct.java
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

import javax.json.JsonObject;

/**
 * <p>ZooBankNomenclaturalAct class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class ZooBankNomenclaturalAct {
	
	/*
	 
	[{"tnuuuid":"","OriginalReferenceUUID":"427D7953-E8FC-41E8-BEA7-8AE644E6DE77",
"protonymuuid": "6EA8BB2A-A57B-47C1-953E-042D8CD8E0E2", "label": "carlsoni Randall & Pyle 2001", 
"value": "carlsoni Randall & Pyle 2001",
"lsid":"urn:lsid:zoobank.org:act:6EA8BB2A-A57B-47C1-953E-042D8CD8E0E2",
"parentname":"","namestring":"carlsoni","rankgroup":"Species","usageauthors":"",
"taxonnamerankid":"","parentusageuuid":"",
"cleanprotonym" : "Pseudanthias carlsoni Randall & Pyle, 2001","NomenclaturalCode":"ICZN"}]		  
		 
	 */
	
	private String tnuuuid;
	private String originalReferenceUUID;
	private String protonymUUID;
	private String label;
	private String value;
	private String lsid;
	private String parentname;
	private String namestring;
	private String rankgroup;
	private String usageauthors;
	private String taxonnameRankId;
	private String parentUsageUUID;
	private String cleanprotonym;
	private String nomenclaturalCode;
	
	/**
	 * <p>Constructor for ZooBankNomenclaturalAct.</p>
	 *
	 * @param jsonTNU a {@link javax.json.JsonObject} object.
	 */
	public ZooBankNomenclaturalAct(JsonObject jsonTNU) { 
		tnuuuid = jsonTNU.getString("tnuuuid");
		originalReferenceUUID = jsonTNU.getString("OriginalReferenceUUID");
		protonymUUID = jsonTNU.getString("protonymuuid");
		label = jsonTNU.getString("label");
		value = jsonTNU.getString("value");
		lsid = jsonTNU.getString("lsid");
		parentname = jsonTNU.getString("parentname");
		namestring = jsonTNU.getString("namestring");
		rankgroup = jsonTNU.getString("rankgroup");
		usageauthors = jsonTNU.getString("usageauthors");
		taxonnameRankId = jsonTNU.getString("taxonnamerankid");
		parentUsageUUID = jsonTNU.getString("parentusageuuid");
		cleanprotonym = jsonTNU.getString("cleanprotonym");
		nomenclaturalCode = jsonTNU.getString("NomenclaturalCode");
	}

	/**
	 * <p>Getter for the field <code>tnuuuid</code>.</p>
	 *
	 * @return the tnuuuid
	 */
	public String getTnuuuid() {
		return tnuuuid;
	}

	/**
	 * <p>Setter for the field <code>tnuuuid</code>.</p>
	 *
	 * @param tnuuuid the tnuuuid to set
	 */
	public void setTnuuuid(String tnuuuid) {
		this.tnuuuid = tnuuuid;
	}

	/**
	 * <p>Getter for the field <code>originalReferenceUUID</code>.</p>
	 *
	 * @return the originalReferenceUUID
	 */
	public String getOriginalReferenceUUID() {
		return originalReferenceUUID;
	}

	/**
	 * <p>Setter for the field <code>originalReferenceUUID</code>.</p>
	 *
	 * @param originalReferenceUUID the originalReferenceUUID to set
	 */
	public void setOriginalReferenceUUID(String originalReferenceUUID) {
		this.originalReferenceUUID = originalReferenceUUID;
	}

	/**
	 * <p>Getter for the field <code>protonymUUID</code>.</p>
	 *
	 * @return the protonymUUID
	 */
	public String getProtonymUUID() {
		return protonymUUID;
	}

	/**
	 * <p>Setter for the field <code>protonymUUID</code>.</p>
	 *
	 * @param protonymUUID the protonymUUID to set
	 */
	public void setProtonymUUID(String protonymUUID) {
		this.protonymUUID = protonymUUID;
	}

	/**
	 * <p>Getter for the field <code>label</code>.</p>
	 *
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <p>Setter for the field <code>label</code>.</p>
	 *
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * <p>Getter for the field <code>lsid</code>.</p>
	 *
	 * @return the lsid
	 */
	public String getLsid() {
		return lsid;
	}

	/**
	 * <p>Setter for the field <code>lsid</code>.</p>
	 *
	 * @param lsid the lsid to set
	 */
	public void setLsid(String lsid) {
		this.lsid = lsid;
	}

	/**
	 * <p>Getter for the field <code>parentname</code>.</p>
	 *
	 * @return the parentname
	 */
	public String getParentname() {
		return parentname;
	}

	/**
	 * <p>Setter for the field <code>parentname</code>.</p>
	 *
	 * @param parentname the parentname to set
	 */
	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	/**
	 * <p>Getter for the field <code>namestring</code>.</p>
	 *
	 * @return the namestring
	 */
	public String getNamestring() {
		return namestring;
	}

	/**
	 * <p>Setter for the field <code>namestring</code>.</p>
	 *
	 * @param namestring the namestring to set
	 */
	public void setNamestring(String namestring) {
		this.namestring = namestring;
	}

	/**
	 * <p>Getter for the field <code>rankgroup</code>.</p>
	 *
	 * @return the rankgroup
	 */
	public String getRankgroup() {
		return rankgroup;
	}

	/**
	 * <p>Setter for the field <code>rankgroup</code>.</p>
	 *
	 * @param rankgroup the rankgroup to set
	 */
	public void setRankgroup(String rankgroup) {
		this.rankgroup = rankgroup;
	}

	/**
	 * <p>Getter for the field <code>usageauthors</code>.</p>
	 *
	 * @return the usageauthors
	 */
	public String getUsageauthors() {
		return usageauthors;
	}

	/**
	 * <p>Setter for the field <code>usageauthors</code>.</p>
	 *
	 * @param usageauthors the usageauthors to set
	 */
	public void setUsageauthors(String usageauthors) {
		this.usageauthors = usageauthors;
	}

	/**
	 * <p>Getter for the field <code>taxonnameRankId</code>.</p>
	 *
	 * @return the taxonnameRankId
	 */
	public String getTaxonnameRankId() {
		return taxonnameRankId;
	}

	/**
	 * <p>Setter for the field <code>taxonnameRankId</code>.</p>
	 *
	 * @param taxonnameRankId the taxonnameRankId to set
	 */
	public void setTaxonnameRankId(String taxonnameRankId) {
		this.taxonnameRankId = taxonnameRankId;
	}

	/**
	 * <p>Getter for the field <code>parentUsageUUID</code>.</p>
	 *
	 * @return the parentUsageUUID
	 */
	public String getParentUsageUUID() {
		return parentUsageUUID;
	}

	/**
	 * <p>Setter for the field <code>parentUsageUUID</code>.</p>
	 *
	 * @param parentUsageUUID the parentUsageUUID to set
	 */
	public void setParentUsageUUID(String parentUsageUUID) {
		this.parentUsageUUID = parentUsageUUID;
	}

	/**
	 * <p>Getter for the field <code>cleanprotonym</code>.</p>
	 *
	 * @return the cleanprotonym
	 */
	public String getCleanprotonym() {
		return cleanprotonym;
	}

	/**
	 * <p>Setter for the field <code>cleanprotonym</code>.</p>
	 *
	 * @param cleanprotonym the cleanprotonym to set
	 */
	public void setCleanprotonym(String cleanprotonym) {
		this.cleanprotonym = cleanprotonym;
	}

	/**
	 * <p>Getter for the field <code>nomenclaturalCode</code>.</p>
	 *
	 * @return the nomenclaturalCode
	 */
	public String getNomenclaturalCode() {
		return nomenclaturalCode;
	}

	/**
	 * <p>Setter for the field <code>nomenclaturalCode</code>.</p>
	 *
	 * @param nomenclaturalCode the nomenclaturalCode to set
	 */
	public void setNomenclaturalCode(String nomenclaturalCode) {
		this.nomenclaturalCode = nomenclaturalCode;
	}

}
