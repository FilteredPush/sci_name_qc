/**
 * AphiaNameServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public interface AphiaNameServicePortType extends java.rmi.Remote {

    /**
     * <strong>Get the AphiaID for a given name.<br />
     * <br/>Parameters:
     *    <ul>
     *     <li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     * </ul>
     * <br/>Returns:
     * <ul>
     * 	<li> NULL when no matches are found</li>
     * 	<li> -999 when multiple matches was found</li>
     * 	<li> an integer (AphiaID) when one exact match was found</li>
     * </ul>
     *   </strong>
     */
    public int getAphiaID(java.lang.String scientificname, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>Get one or more matching (max. 50) AphiaRecords for
     * a given name.<br/>Parameters:
     *    <ul>
     *     <li><u>like</u>: add a '%'-sign added after the ScientificName
     * (SQL LIKE function). Default=true.</li>
     * 	<li><u>fuzzy</u>: this parameter is deprecated (and ignored since
     * 2013-07-17). Please use the function matchAphiaRecordsByNames() for
     * fuzzy/near matching.</li>
     * 	<li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     * 	<li><u>offset</u>: starting recordnumber, when retrieving next chunk
     * of (50) records. Default=1.</li>
     *    </ul>
     *   </strong><br /> For the structure of the returned AphiaRecords,
     * please refer to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecords(java.lang.String scientificname, boolean like, boolean fuzzy, boolean marine_only, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get the correct name for a given AphiaID</strong>.
     */
    public java.lang.String getAphiaNameByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the complete AphiaRecord for a given AphiaID.</strong><br
     * />
     *  The returned AphiaRecord has this format:<br /><ul><li><u><b>AphiaID</b></u>:
     * unique and persistent identifier within WoRMS. Primary key in the
     * database.</li><li><u><b>url</b></u>: HTTP URL to the AphiaRecord</li><li><u><b>scientificname</b></u>:
     * the full scientific name without authorship</li><li><u><b>authority</b></u>:
     * the authorship information for the scientificname formatted according
     * to the conventions of the applicable nomenclaturalCode</li><li><u><b>rank</b></u>:
     * the taxonomic rank of the most specific name in the scientificname</li><li><u><b>status</b></u>:
     * the status of the use of the scientificname as a label for a taxon.
     * Requires taxonomic opinion to define the scope of a taxon</li><li><u><b>unacceptreason</b></u>:
     * the reason why a scientificname is unaccepted</li><li><u><b>valid_AphiaID</b></u>:
     * the AphiaID (for the scientificname) of the currently accepted taxon</li><li><u><b>valid_name</b></u>:
     * the scientificname of the currently accepted taxon</li><li><u><b>valid_authority</b></u>:
     * the authorship information for the scientificname of the currently
     * accepted taxon</li><li><u><b>kingdom</b></u>: the full scientific
     * name of the kingdom in which the taxon is classified</li><li><u><b>phylum</b></u>:
     * the full scientific name of the phylum or division in which the taxon
     * is classified</li><li><u><b>class</b></u>: the full scientific name
     * of the class in which the taxon is classified</li><li><u><b>order</b></u>:
     * the full scientific name of the order in which the taxon is classified</li><li><u><b>family</b></u>:
     * the full scientific name of the family in which the taxon is classified</li><li><u><b>genus</b></u>:
     * the full scientific name of the genus in which the taxon is classified</li><li><u><b>citation</b></u>:
     * a bibliographic reference for the resource as a statement indicating
     * how this record should be cited (attributed) when used</li><li><u><b>lsid</b></u>:
     * LifeScience Identifier. Persistent GUID for an AphiaID</li><li><u><b>isMarine</b></u>:
     * a boolean flag indicating whether the taxon is a marine organism,
     * i.e. can be found in/above sea water. Possible values: 0/1/NULL</li><li><u><b>isBrackish</b></u>:
     * a boolean flag indicating whether the taxon occurrs in brackish habitats.
     * Possible values: 0/1/NULL</li><li><u><b>isFreshwater</b></u>: a boolean
     * flag indicating whether the taxon occurrs in freshwater habitats,
     * i.e. can be found in/above rivers or lakes. Possible values: 0/1/NULL</li><li><u><b>isTerrestrial</b></u>:
     * a boolean flag indicating the taxon is a terrestial organism, i.e.
     * occurrs on land as opposed to the sea. Possible values: 0/1/NULL</li><li><u><b>isExtinct</b></u>:
     * a flag indicating an extinct organism. Possible values: 0/1/NUL</li><li><u><b>match_type</b></u>:
     * Type of match. Possible values: exact/like/phonetic/near_1/near_2</li><li><u><b>modified</b></u>:
     * The most recent date-time in GMT on which the resource was changed</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the Aphia Record for a given external identifier.
     * <br/>Parameters:
     * <ul>
     *  <li>
     *   <u>type</u>: Should have one of the following values:
     *    <ul>
     *     <li><u>bold</u>: Barcode of Life Database (BOLD) TaxID</li>
     *     <li><u>dyntaxa</u>: Dyntaxa ID</li>
     * 	<li><u>eol</u>: Encyclopedia of Life (EoL) page identifier</li>
     * 	<li><u>fishbase</u>: FishBase species ID</li>
     * 	<li><u>iucn</u>: IUCN Red List Identifier</li>
     *     <li><u>lsid</u>: Life Science Identifier</li>
     *     <li><u>ncbi</u>: NCBI Taxonomy ID (Genbank)</li>
     *     <li><u>tsn</u>: ITIS Taxonomic Serial Number</li>
     *   </ul>
     *  </li>
     * </ul></strong>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByExtID(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException;

    /**
     * <strong>Get any external identifier(s) for a given AphiaID.
     * <br/>Parameters:
     * <ul>
     *  <li>
     *   <u>type</u>: Should have one of the following values:
     *    <ul>
     *     <li><u>bold</u>: Barcode of Life Database (BOLD) TaxID</li>
     *     <li><u>dyntaxa</u>: Dyntaxa ID</li>
     * 	<li><u>eol</u>: Encyclopedia of Life (EoL) page identifier</li>
     * 	<li><u>fishbase</u>: FishBase species ID</li>
     * 	<li><u>iucn</u>: IUCN Red List Identifier</li>
     *     <li><u>lsid</u>: Life Science Identifier</li>
     *     <li><u>ncbi</u>: NCBI Taxonomy ID (Genbank)</li>
     *     <li><u>tsn</u>: ITIS Taxonomic Serial Number</li>
     *   </ul>
     *  </li>
     * </ul></strong>
     */
    public java.lang.String[] getExtIDbyAphiaID(int aphiaID, java.lang.String type) throws java.rmi.RemoteException;

    /**
     * <strong>For each given scientific name, try to find one or
     * more AphiaRecords.<br/>
     *   This allows you to match multiple names in one call. Limited to
     * 500 names at once for performance reasons.
     *   <br/>Parameters:
     *    <ul>
     *     <li><u>like</u>: add a '%'-sign after the ScientificName (SQL
     * LIKE function). Default=false.</li>
     * 	<li><u>fuzzy</u>: this parameter is deprecated (and ignored since
     * 2013-07-17). Please use the function matchAphiaRecordsByNames() for
     * fuzzy/near matching.</li>
     * 	<li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     *    </ul></strong>
     *    <br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[][] getAphiaRecordsByNames(java.lang.String[] scientificnames, boolean like, boolean fuzzy, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>Get one or more Aphia Records (max. 50) for a given
     * vernacular.</strong><br/>Parameters:
     *    <ul>
     *     <li><u>like</u>: add a '%'-sign before and after the input (SQL
     * LIKE '%vernacular%' function). Default=false.</li>
     * 	<li><u>offset</u>: starting record number, when retrieving next chunk
     * of (50) records. Default=1.</li>
     *    </ul>
     *   </strong>
     *   <br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByVernacular(java.lang.String vernacular, boolean like, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Lists all AphiaRecords (taxa) modified or added between
     * a specific time interval.<br/>
     *  <br/>Parameters:
     *   <ul>
     *    <li><u>startdate</u>: ISO 8601 formatted start date(time). Default=today().
     * i.e. 2015-03-06T15:38:40+00:00</li>
     *    <li><u>enddate</u>: ISO 8601 formatted end date(time). Default=today().i.e.
     * 2015-03-06T15:38:40+00:00</li>
     *    <li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     * <li><u>offset</u>: starting record number, when retrieving next chunk
     * of (50) records. Default=1.</li>
     *   </ul></strong>
     *   <br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByDate(java.lang.String startdate, java.lang.String enddate, boolean marine_only, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get the complete classification for one taxon. This
     * also includes any sub or super ranks.</strong>
     */
    public org.marinespecies.aphia.v1_0.Classification getAphiaClassificationByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get one or more sources/references including links,
     * for one AphiaID</strong>
     */
    public org.marinespecies.aphia.v1_0.Source[] getSourcesByAphiaID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get all synonyms for a given AphiaID.</strong>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaSynonymsByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get all vernaculars for a given AphiaID.</strong>
     */
    public org.marinespecies.aphia.v1_0.Vernacular[] getAphiaVernacularsByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the direct children (max. 50) for a given AphiaID.</strong><br
     * />Parameters:
     *    <ul>
     * 	<li><u>offset</u>: starting record number, when retrieving next chunk
     * of (50) records. Default=1.</li>
     * 	<li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     *    </ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaChildrenByID(int aphiaID, int offset, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>For each given scientific name (may include authority),
     * try to find one or more AphiaRecords, using the TAXAMATCH fuzzy matching
     * algorithm by Tony Rees.<br/>
     *  This allows you to (fuzzy) match multiple names in one call. Limited
     * to 50 names at once for performance reasons.
     *  <br/>Parameters:
     *   <ul>
     * 	<li><u>marine_only</u>: limit to marine taxa. Default=true.</li>
     *   </ul></strong>
     *   <br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[][] matchAphiaRecordsByNames(java.lang.String[] scientificnames, boolean marine_only) throws java.rmi.RemoteException;
}
