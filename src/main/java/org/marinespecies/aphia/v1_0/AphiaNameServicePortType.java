/**
 * AphiaNameServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public interface AphiaNameServicePortType extends java.rmi.Remote {

    /**
     * <strong>Get the AphiaID for a given name.
     * 
     * Output: NULL when no match is found; -999 when multiple matches are
     * found; an integer (AphiaID) when one exact match was found</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 		</ul>
     * 		<br />Returns:
     * 		<ul>
     * 			<li> NULL when no match is found</li>
     * 			<li> -999 when multiple matches are found</li>
     * 			<li> an integer (AphiaID) when one exact match was found</li>
     * 		</ul>
     */
    public int getAphiaID(java.lang.String scientificname, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>Get one or more matching (max. 50) AphiaRecords for
     * a given name</strong>
     * 		<br/>Parameters:
     * 		<ul>
     * 			<li><u>like</u>: Add a '%'-sign added after the ScientificName
     * (SQL LIKE function). Default=true</li>
     * 			<li><u>fuzzy</u>: This parameter is deprecated (and ignored since
     * '2013-07-17'). Please use the function matchAphiaRecordsByNames()
     * for fuzzy/near matching</li>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 		</ul>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecords(java.lang.String scientificname, boolean like, boolean fuzzy, boolean marine_only, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get the name for a given AphiaID</strong>.
     */
    public java.lang.String getAphiaNameByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the complete AphiaRecord for a given AphiaID</strong>
     * 		<br /> The returned AphiaRecord has this format:
     * 		<br /> <ul><li>	<u><b>AphiaID</b></u> : Unique and persistent identifier
     * within WoRMS. Primary key in the database</li><li>	<u><b>url</b></u>
     * : HTTP URL to the AphiaRecord</li><li>	<u><b>scientificname</b></u>
     * : the full scientific name without authorship</li><li>	<u><b>authority</b></u>
     * : the authorship information for the scientificname formatted according
     * to the conventions of the applicable nomenclaturalCode</li><li>	<u><b>taxonRankID</b></u>
     * : the taxonomic rank identifier of the most specific name in the scientificname</li><li>
     * 	<u><b>rank</b></u> : the taxonomic rank of the most specific name
     * in the scientificname</li><li>	<u><b>status</b></u> : the status of
     * the use of the scientificname (usually a taxonomic opinion). Additional
     * technical statuses are (1) <u>quarantined</u>: hidden from public
     * interface after decision from an editor and (2) <u>deleted</u>: AphiaID
     * should NOT be used anymore, please replace it by the valid_AphiaID</li><li>
     * 	<u><b>unacceptreason</b></u> : the reason why a scientificname is
     * unaccepted</li><li>	<u><b>valid_AphiaID</b></u> : the AphiaID (for
     * the scientificname) of the currently accepted taxon. NULL if there
     * is no currently accepted taxon.</li><li>	<u><b>valid_name</b></u>
     * : the scientificname of the currently accepted taxon</li><li>	<u><b>valid_authority</b></u>
     * : the authorship information for the scientificname of the currently
     * accepted taxon</li><li>	<u><b>parentNameUsageID</b></u> : the AphiaID
     * (for the scientificname) of the direct, most proximate higher-rank
     * parent taxon (in a classification)</li><li>	<u><b>kingdom</b></u>
     * : the full scientific name of the kingdom in which the taxon is classified</li><li>
     * 	<u><b>phylum</b></u> : the full scientific name of the phylum or
     * division in which the taxon is classified</li><li>	<u><b>class</b></u>
     * : the full scientific name of the class in which the taxon is classified</li><li>
     * 	<u><b>order</b></u> : the full scientific name of the order in which
     * the taxon is classified</li><li>	<u><b>family</b></u> : the full scientific
     * name of the family in which the taxon is classified</li><li>	<u><b>genus</b></u>
     * : the full scientific name of the genus in which the taxon is classified</li><li>
     * 	<u><b>citation</b></u> : a bibliographic reference for the resource
     * as a statement indicating how this record should be cited (attributed)
     * when used</li><li>	<u><b>lsid</b></u> : LifeScience Identifier. Persistent
     * GUID for an AphiaID</li><li>	<u><b>isMarine</b></u> : a boolean flag
     * indicating whether the taxon is a marine organism, i.e. can be found
     * in/above sea water. Possible values: 0/1/NULL</li><li>	<u><b>isBrackish</b></u>
     * : a boolean flag indicating whether the taxon occurrs in brackish
     * habitats. Possible values: 0/1/NULL</li><li>	<u><b>isFreshwater</b></u>
     * : a boolean flag indicating whether the taxon occurrs in freshwater
     * habitats, i.e. can be found in/above rivers or lakes. Possible values:
     * 0/1/NULL</li><li>	<u><b>isTerrestrial</b></u> : a boolean flag indicating
     * the taxon is a terrestial organism, i.e. occurrs on land as opposed
     * to the sea. Possible values: 0/1/NULL</li><li>	<u><b>isExtinct</b></u>
     * : a flag indicating an extinct organism. Possible values: 0/1/NULL</li><li>
     * 	<u><b>match_type</b></u> : Type of match. Possible values: exact/like/phonetic/near_1/near_2</li><li>
     * 	<u><b>modified</b></u> : The most recent date-time in GMT on which
     * the resource was changed</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get an AphiaRecord for multiple AphiaIDs in one go
     * (max: 50)</strong>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByIDs(int[] aphiaids) throws java.rmi.RemoteException;

    /**
     * <strong>Get the Aphia Record for a given external identifier</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>Type</u>: Type of external identifier to search for. The
     * type should be one of the following values:<br/><ul><li><u>algaebase</u>:
     * Algaebase species ID</li><li><u>bold</u>: Barcode of Life Database
     * (BOLD) TaxID</li><li><u>dyntaxa</u>: Dyntaxa ID</li><li><u>fishbase</u>:
     * FishBase species ID</li><li><u>iucn</u>: IUCN Red List Identifier</li><li><u>lsid</u>:
     * Life Science Identifier</li><li><u>ncbi</u>: NCBI Taxonomy ID (Genbank)</li><li><u>tsn</u>:
     * ITIS Taxonomic Serial Number</li><li><u>gisd</u>: Global Invasive
     * Species Database</li></ul></li>
     * 		</ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByExtID(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException;

    /**
     * <strong>Get any external identifier(s) for a given AphiaID</strong>
     * 		<br/>Parameters:
     * 		<ul>
     * 			<li><u>Type</u>: Type of external identifier to return. The type
     * should be one of the following values:<br/><ul><li><u>algaebase</u>:
     * Algaebase species ID</li><li><u>bold</u>: Barcode of Life Database
     * (BOLD) TaxID</li><li><u>dyntaxa</u>: Dyntaxa ID</li><li><u>fishbase</u>:
     * FishBase species ID</li><li><u>iucn</u>: IUCN Red List Identifier</li><li><u>lsid</u>:
     * Life Science Identifier</li><li><u>ncbi</u>: NCBI Taxonomy ID (Genbank)</li><li><u>tsn</u>:
     * ITIS Taxonomic Serial Number</li><li><u>gisd</u>: Global Invasive
     * Species Database</li></ul></li>
     * 		</ul>
     */
    public java.lang.String[] getExtIDbyAphiaID(int aphiaID, java.lang.String type) throws java.rmi.RemoteException;

    /**
     * <strong>For each given scientific name, try to find one or
     * more AphiaRecords. This allows you to match multiple names in one
     * call. Limited to 500 names at once for performance reasons.</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>like</u>: Add a '%'-sign after the ScientificName (SQL LIKE
     * function). Default=false</li>
     * 			<li><u>fuzzy</u>: This parameter is deprecated (and ignored since
     * '2013-07-17'). Please use the function matchAphiaRecordsByNames()
     * for fuzzy/near matching</li>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 		</ul>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[][] getAphiaRecordsByNames(java.lang.String[] scientificnames, boolean like, boolean fuzzy, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>Get one or more Aphia Records (max. 50) for a given
     * vernacular</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>like</u>: Add a '%'-sign before and after the input (SQL
     * LIKE '%vernacular%' function). Default=false</li>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 		</ul>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByVernacular(java.lang.String vernacular, boolean like, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Lists all AphiaRecords (taxa) that have their last
     * edit action (modified or added) during the specified period</strong>
     * 		<br/>Parameters:
     * 		<ul>
     * 			<li><u>startdate</u>: ISO 8601 formatted start date(time). Default=today().
     * i.e. 2021-08-31T13:33:40+00:00</li>
     * 			<li><u>enddate</u>: ISO 8601 formatted end date(time). Default=today().i.e.
     * 2021-08-31T13:33:40+00:00</li>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 		</ul>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
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
     * 	<br />Output fields include:
     * 	<br /> <ul><li>	<u><b>source_id</b></u> : Unique identifier for the
     * source within WoRMS</li><li>	<u><b>use</b></u> : Usage of the source
     * for this taxon. See <a href='http://marinespecies.org/aphia.php?p=manual#topic6'
     * target='_blank'>here</a> for all values</li><li>	<u><b>reference</b></u>
     * : Full citation string</li><li>	<u><b>page</b></u> : Page(s) where
     * the taxon is mentioned</li><li>	<u><b>url</b></u> : Direct link to
     * the source record</li><li>	<u><b>link</b></u> : External link (i.e.
     * journal, data system, etc..)</li><li>	<u><b>fulltext</b></u> : Full
     * text link (PDF)</li><li>	<u><b>doi</b></u> : Digital Object Identifier</li></ul>
     */
    public org.marinespecies.aphia.v1_0.Source[] getSourcesByAphiaID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get all synonyms for a given AphiaID</strong>
     *         <br />Parameters:
     * 		<ul>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 		</ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaSynonymsByID(int aphiaID, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get all vernaculars for a given AphiaID</strong>
     */
    public org.marinespecies.aphia.v1_0.Vernacular[] getAphiaVernacularsByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the direct children (max. 50) for a given AphiaID</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 		</ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaChildrenByID(int aphiaID, int offset, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>For each given scientific name (may include authority),
     * try to find one or more AphiaRecords, using the TAXAMATCH fuzzy matching
     * algorithm by Tony Rees.<br/>This allows you to (fuzzy) match multiple
     * names in one call. Limited to 50 names at once for performance reasons</strong>
     * 		<br/>Parameters:
     * 		<ul>
     * 			<li><u>marine_only</u>: Limit to marine taxa. Default=true</li>
     * 		</ul>
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[][] matchAphiaRecordsByNames(java.lang.String[] scientificnames, boolean marine_only) throws java.rmi.RemoteException;

    /**
     * <strong>Get all distributions for a given AphiaID</strong>
     * 		<br />Output fields include:
     * 		<br /> <ul><li>	<u><b>locality</b></u> : The specific description
     * of the place</li><li>	<u><b>locationID</b></u> : An identifier for
     * the locality. Using the Marine Regions Geographic IDentifier (MRGID),
     * see <a href='https://www.marineregions.org/mrgid.php' target='_blank'>https://www.marineregions.org/mrgid.php</a></li><li>
     * 	<u><b>higherGeography</b></u> : A geographic name less specific than
     * the information captured in the locality term. Possible values: an
     * IHO Sea Area or Nation, derived from the MarineRegions <a href='https://www.marineregions.org/gazetteer.php'
     * target='_blank'>gazetteer</a></li><li>	<u><b>higherGeographyID</b></u>
     * : An identifier for the geographic region within which the locality
     * occurred, using MRGID</li><li>	<u><b>recordStatus</b></u> : The status
     * of the distribution record. Possible values are 'valid' ,'doubtful'
     * or 'inaccurate'. See <a href='http://marinespecies.org/aphia.php?p=manual#topic8b'
     * target=_blank>here</a> for explanation of the statuses</li><li>	<u><b>typeStatus</b></u>
     * : The type status of the distribution. Possible values: 'holotype'
     * or empty.</li><li>	<u><b>establishmentMeans</b></u> : The process
     * by which the biological individual(s) represented in the Occurrence
     * became established at the location. Possible values: values listed
     * as Origin <a href='http://www.marinespecies.org/introduced/wiki/Traits:Origin'
     * target=_blank>in WRIMS</a></li><li>	<u><b>decimalLatitude</b></u>
     * : The geographic latitude (in decimal degrees, WGS84)</li><li>	<u><b>decimalLongitude</b></u>
     * : The geographic longitude (in decimal degrees, WGS84)</li><li>	<u><b>qualityStatus</b></u>
     * : Quality status of the record. Possible values: 'checked', 'trusted'
     * or 'unreviewed'. See <a href='http://marinespecies.org/aphia.php?p=manual#topic22'
     * target='_blank'>http://marinespecies.org/aphia.php?p=manual#topic22</a></li></ul>
     */
    public org.marinespecies.aphia.v1_0.Distribution[] getAphiaDistributionsByID(int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get taxonomic ranks by their identifier</strong>
     * 		<br />Output fields include:
     * 		<br /> <ul><li>	<u><b>taxonRankID</b></u> : A taxonomic rank identifier</li><li>
     * 	<u><b>taxonRank</b></u> : A taxonomic rank name</li><li>	<u><b>AphiaID</b></u>
     * : AphiaID of the kingdom</li><li>	<u><b>kingdom</b></u> : The name
     * of a taxonomic kingdom the rank is used in</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRank[] getAphiaTaxonRanksByID(int taxonRankID, int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get taxonomic ranks by their name</strong>
     * 		<br />Output fields include:
     * 		<br /> <ul><li>	<u><b>taxonRankID</b></u> : A taxonomic rank identifier</li><li>
     * 	<u><b>taxonRank</b></u> : A taxonomic rank name</li><li>	<u><b>AphiaID</b></u>
     * : AphiaID of the kingdom</li><li>	<u><b>kingdom</b></u> : The name
     * of a taxonomic kingdom the rank is used in</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AphiaRank[] getAphiaTaxonRanksByName(java.lang.String taxonRank, int aphiaID) throws java.rmi.RemoteException;

    /**
     * <strong>Get the AphiaRecords for a given taxonRankID (max 50)</strong>
     * 		<br />Output fields include:
     * 		<br /> For the structure of the returned AphiaRecords, please refer
     * to the function getAphiaRecordByID()
     */
    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByTaxonRankID(int taxonRankID, int belongsTo, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get attribute definitions. To refer to root items specify
     * ID = '0'</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>id</u>: The attribute definition id to search for</li>
     * 			<li><u>include_children</u>: Include the tree of children. Default=true</li>
     * 		</ul>
     * 		<br />Returns definition objects that have the following structure:
     * 		<br /> <ul><li>	<u><b>measurementTypeID</b></u> : An internal identifier
     * for the measurementType</li><li>	<u><b>measurementType</b></u> : The
     * nature of the measurement, fact, characteristic, or assertion <a href='http://www.marinespecies.org/traits/wiki'
     * target='_blank'>http://www.marinespecies.org/traits/wiki</a></li><li>
     * 	<u><b>input_id</b></u> : The data type that is expected as value
     * for this attribute definition</li><li>	<u><b>CategoryID</b></u> :
     * The category identifier to list possible attribute values for this
     * attribute definition</li><li>	<u><b>children</b></u> : The possible
     * child attribute keys that help to describe to current attribute</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AttributeKey[] getAphiaAttributeKeysByID(int id, boolean include_children) throws java.rmi.RemoteException;

    /**
     * <strong>Get list values that are grouped by an CateogryID</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>id</u>: The CateogryID to search for</li>
     * 		</ul>
     * 		<br />Returns definition objects that have the following structure:
     * 		<br /> <ul><li>	<u><b>measurementValueID</b></u> : An identifier
     * for facts stored in the column measurementValue</li><li>	<u><b>measurementValue</b></u>
     * : The value of the measurement, fact, characteristic, or assertion</li><li>
     * 	<u><b>measurementValueCode</b></u> : Additional info/code that helps
     * to the describe/define the measurementValue</li><li>	<u><b>children</b></u>
     * : Child measurementValues that are more specific</li></ul>
     */
    public org.marinespecies.aphia.v1_0.AttributeValue[] getAphiaAttributeValuesByCategoryID(int id) throws java.rmi.RemoteException;

    /**
     * <strong>Get a list of AphiaIDs (max 50) with attribute tree
     * for a given attribute definition ID</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>id</u>: The attribute definition id to search for</li>
     * 			<li><u>offset</u>: Starting recordnumber, when retrieving next
     * chunk of (50) records. Default=1</li>
     * 		</ul>
     * 		<br />Returns a list of AphiaIDs and the corresponding list of attributes
     * that have the following structure:
     * 		<br /> <ul><li>	<u><b>AphiaID</b></u> : Unique and persistent identifier
     * within WoRMS</li><li>	<u><b>measurementTypeID</b></u> : The corresponding
     * AttributeKey its measurementTypeID</li><li>	<u><b>measurementType</b></u>
     * : The corresponding AttributeKey its measurementType</li><li>	<u><b>measurementValue</b></u>
     * : The value of the measurement, fact, characteristic, or assertion</li><li>
     * 	<u><b>source_id</b></u> : The identifier for the AphiaSource for
     * this attribute</li><li>	<u><b>reference</b></u> : The AphiaSource
     * reference for this attribute</li><li>	<u><b>qualitystatus</b></u>
     * : Quality status of the record. Possible values: 'checked', 'trusted'
     * or 'unreviewed'. See <a href='http://marinespecies.org/aphia.php?p=manual#topic22'
     * target='_blank'>http://marinespecies.org/aphia.php?p=manual#topic22</a></li><li>
     * 	<u><b>CategoryID</b></u> : The category identifier to list possible
     * attribute values for this attribute definition</li><li>	<u><b>AphiaID_Inherited</b></u>
     * : The AphiaID from where this attribute is inherited</li><li>	<u><b>children</b></u>
     * : The possible child attributes that help to describe to current attribute</li></ul>
     */
    public int[] getAphiaIDsByAttributeKeyID(int id, int offset) throws java.rmi.RemoteException;

    /**
     * <strong>Get a list of attributes for a given AphiaID</strong>
     * 		<br />Parameters:
     * 		<ul>
     * 			<li><u>id</u>: The AphiaID to search for</li>
     * 			<li><u>include_inherited</u>: Include attributes inherited from
     * the taxon its parent(s). Default=false</li>
     * 		</ul>
     * 		<br />Returns attributes that have the following structure:
     * 		<br /> <ul><li>	<u><b>AphiaID</b></u> : Unique and persistent identifier
     * within WoRMS</li><li>	<u><b>measurementTypeID</b></u> : The corresponding
     * AttributeKey its measurementTypeID</li><li>	<u><b>measurementType</b></u>
     * : The corresponding AttributeKey its measurementType</li><li>	<u><b>measurementValue</b></u>
     * : The value of the measurement, fact, characteristic, or assertion</li><li>
     * 	<u><b>source_id</b></u> : The identifier for the AphiaSource for
     * this attribute</li><li>	<u><b>reference</b></u> : The AphiaSource
     * reference for this attribute</li><li>	<u><b>qualitystatus</b></u>
     * : Quality status of the record. Possible values: 'checked', 'trusted'
     * or 'unreviewed'. See <a href='http://marinespecies.org/aphia.php?p=manual#topic22'
     * target='_blank'>http://marinespecies.org/aphia.php?p=manual#topic22</a></li><li>
     * 	<u><b>CategoryID</b></u> : The category identifier to list possible
     * attribute values for this attribute definition</li><li>	<u><b>AphiaID_Inherited</b></u>
     * : The AphiaID from where this attribute is inherited</li><li>	<u><b>children</b></u>
     * : The possible child attributes that help to describe to current attribute</li></ul>
     */
    public org.marinespecies.aphia.v1_0.Attribute[] getAphiaAttributesByAphiaID(int id, boolean include_inherited) throws java.rmi.RemoteException;
}
