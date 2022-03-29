package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;

import javax.ws.rs.core.GenericType;

import org.marinespecies.aphia.v1_0.model.AphiaRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class VernacularsApi {
  private ApiClient apiClient;

  public VernacularsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public VernacularsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get one or more Aphia Records (max. 50) for a given vernacular
   * Get one or more Aphia Records (max. 50) for a given vernacular
   * @param vernacular The vernacular to find records for (required)
   * @param like Add a &#x27;%&#x27;-sign before and after the input (SQL LIKE &#x27;%vernacular%&#x27; function). Default&#x3D;false (optional, default to false)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional)
   * @return List&lt;AphiaRecord&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AphiaRecord> aphiaRecordsByVernacular(String vernacular, Boolean like, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'vernacular' is set
    if (vernacular == null) {
      throw new ApiException(400, "Missing the required parameter 'vernacular' when calling aphiaRecordsByVernacular");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordsByVernacular/{Vernacular}"
      .replaceAll("\\{" + "Vernacular" + "\\}", apiClient.escapeString(vernacular.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "like", like));
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
   * Get all vernaculars for a given AphiaID
   * Get all vernaculars for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @throws ApiException if fails to make API call
   */
  public void aphiaVernacularsByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaVernacularsByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaVernacularsByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();



    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, null);
  }
}
