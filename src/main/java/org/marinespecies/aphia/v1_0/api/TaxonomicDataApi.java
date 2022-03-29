package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;

import javax.ws.rs.core.GenericType;

import org.marinespecies.aphia.v1_0.model.AphiaRank;
import org.marinespecies.aphia.v1_0.model.AphiaRecord;
import org.marinespecies.aphia.v1_0.model.AphiaRecordsArray;
import org.marinespecies.aphia.v1_0.model.Classification;
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class TaxonomicDataApi {
  private ApiClient apiClient;

  public TaxonomicDataApi() {
    this(Configuration.getDefaultApiClient());
  }

  public TaxonomicDataApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get the direct children (max. 50) for a given AphiaID
   * Get the direct children (max. 50) for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaChildrenByAphiaID(Integer ID, Boolean marineOnly, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaChildrenByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaChildrenByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the complete classification for one taxon. This also includes any sub or super ranks.
   * Get the complete classification for one taxon. This also includes any sub or super ranks.
   * @param ID The AphiaID to search for (required)
   * @return Classification
   * @throws ApiException if fails to make API call
   */
  public Classification aphiaClassificationByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaClassificationByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaClassificationByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<Classification> localVarReturnType = new GenericType<Classification>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the AphiaID for a given name.
   * Get the AphiaID for a given name.  Output: NULL when no match is found; -999 when multiple matches are found; an integer (AphiaID) when one exact match was found
   * @param scientificName Name to search for (required)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @return Integer
   * @throws ApiException if fails to make API call
   */
  public Integer aphiaIDByName(String scientificName, Boolean marineOnly) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'scientificName' is set
    if (scientificName == null) {
      throw new ApiException(400, "Missing the required parameter 'scientificName' when calling aphiaIDByName");
    }
    // create path and map variables
    String localVarPath = "/AphiaIDByName/{ScientificName}"
      .replaceAll("\\{" + "ScientificName" + "\\}", apiClient.escapeString(scientificName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));


    final String[] localVarAccepts = {
      "text/plain"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<Integer> localVarReturnType = new GenericType<Integer>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the name for a given AphiaID
   * Get the name for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @return Integer
   * @throws ApiException if fails to make API call
   */
  public Integer aphiaNameByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaNameByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaNameByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "text/plain"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<Integer> localVarReturnType = new GenericType<Integer>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the complete AphiaRecord for a given AphiaID
   * Get the complete AphiaRecord for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @return AphiaRecord
   * @throws ApiException if fails to make API call
   */
  public AphiaRecord aphiaRecordByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaRecordByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<AphiaRecord> localVarReturnType = new GenericType<AphiaRecord>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get an AphiaRecord for multiple AphiaIDs in one go (max: 50)
   * Get an AphiaRecord for multiple AphiaIDs in one go (max: 50)
   * @param aphiaids The AphiaIDs to search for (required)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByAphiaIDs(List<Integer> aphiaids) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'aphiaids' is set
    if (aphiaids == null) {
      throw new ApiException(400, "Missing the required parameter 'aphiaids' when calling aphiaRecordsByAphiaIDs");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByAphiaIDs";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "aphiaids[]", aphiaids));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Lists all AphiaRecords (taxa) that have their last edit action (modified or added) during the specified period
   * Lists all AphiaRecords (taxa) that have their last edit action (modified or added) during the specified period
   * @param startdate ISO 8601 formatted start date(time). Default&#x3D;today(). i.e. 2022-02-02T01:21:53+00:00 (required)
   * @param enddate ISO 8601 formatted end date(time). Default&#x3D;today().i.e. 2022-02-02T01:21:53+00:00 (optional)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByDate(OffsetDateTime startdate, OffsetDateTime enddate, Boolean marineOnly, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'startdate' is set
    if (startdate == null) {
      throw new ApiException(400, "Missing the required parameter 'startdate' when calling aphiaRecordsByDate");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByDate";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "startdate", startdate));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "enddate", enddate));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Try to find AphiaRecords using the TAXAMATCH fuzzy matching algorithm by Tony Rees
   * For each given scientific name (may include authority), try to find one or more AphiaRecords, using the TAXAMATCH fuzzy matching algorithm by Tony Rees.&lt;br/&gt;This allows you to (fuzzy) match multiple names in one call. Limited to 50 names at once for performance reasons
   * @param scientificnames Names to search for (required)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @return List&lt;AphiaRecordsArray&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecordsArray> aphiaRecordsByMatchNames(List<String> scientificnames, Boolean marineOnly) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'scientificnames' is set
    if (scientificnames == null) {
      throw new ApiException(400, "Missing the required parameter 'scientificnames' when calling aphiaRecordsByMatchNames");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByMatchNames";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "scientificnames[]", scientificnames));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecordsArray>> localVarReturnType = new GenericType<List<AphiaRecordsArray>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get one or more matching (max. 50) AphiaRecords for a given name
   * Get one or more matching (max. 50) AphiaRecords for a given name
   * @param scientificName Name to search for (required)
   * @param like Add a &#x27;%&#x27;-sign added after the ScientificName (SQL LIKE function). Default&#x3D;true (optional, default to true)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByName(String scientificName, Boolean like, Boolean marineOnly, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'scientificName' is set
    if (scientificName == null) {
      throw new ApiException(400, "Missing the required parameter 'scientificName' when calling aphiaRecordsByName");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByName/{ScientificName}"
      .replaceAll("\\{" + "ScientificName" + "\\}", apiClient.escapeString(scientificName.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "like", like));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * For each given scientific name, try to find one or more AphiaRecords
   * For each given scientific name, try to find one or more AphiaRecords. This allows you to match multiple names in one call. Limited to 500 names at once for performance reasons.
   * @param scientificnames Names to search for (required)
   * @param like Add a &#x27;%&#x27;-sign after the ScientificName (SQL LIKE function). Default&#x3D;false (optional, default to false)
   * @param marineOnly Limit to marine taxa. Default&#x3D;true (optional, default to true)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByNames(List<String> scientificnames, Boolean like, Boolean marineOnly) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'scientificnames' is set
    if (scientificnames == null) {
      throw new ApiException(400, "Missing the required parameter 'scientificnames' when calling aphiaRecordsByNames");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByNames";

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("multi", "scientificnames[]", scientificnames));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "like", like));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "marine_only", marineOnly));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the AphiaRecords for a given taxonRankID (max 50)
   * Get the AphiaRecords for a given taxonRankID (max 50)
   * @param ID A taxonomic rank identifier (required)
   * @param belongsTo Limit the results to descendants of the given AphiaID (optional)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByTaxonRankID(Integer ID, Integer belongsTo, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaRecordsByTaxonRankID");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByTaxonRankID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "belongsTo", belongsTo));
    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get all synonyms for a given AphiaID
   * Get all synonyms for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaSynonymsByAphiaID(Integer ID, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaSynonymsByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaSynonymsByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "offset", offset));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRecord>> localVarReturnType = new GenericType<List<AphiaRecord>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get taxonomic ranks by their identifier
   * Get taxonomic ranks by their identifier
   * @param ID A taxonomic rank identifier. Default&#x3D;-1 (required)
   * @param aphiaID The AphiaID of the kingdom (optional)
   * @return List&lt;AphiaRank&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRank> aphiaTaxonRanksByID(Integer ID, Integer aphiaID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaTaxonRanksByID");
    }
    // create path and map variables
    String localVarPath = "/AphiaTaxonRanksByID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "AphiaID", aphiaID));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRank>> localVarReturnType = new GenericType<List<AphiaRank>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get taxonomic ranks by their name
   * Get taxonomic ranks by their name
   * @param taxonRank A taxonomic rank. Default&#x3D;empty (required)
   * @param aphiaID The AphiaID of the kingdom (optional)
   * @return List&lt;AphiaRank&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRank> aphiaTaxonRanksByName(String taxonRank, Integer aphiaID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'taxonRank' is set
    if (taxonRank == null) {
      throw new ApiException(400, "Missing the required parameter 'taxonRank' when calling aphiaTaxonRanksByName");
    }
    // create path and map variables
    String localVarPath = "/AphiaTaxonRanksByName/{taxonRank}"
      .replaceAll("\\{" + "taxonRank" + "\\}", apiClient.escapeString(taxonRank.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "AphiaID", aphiaID));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AphiaRank>> localVarReturnType = new GenericType<List<AphiaRank>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
