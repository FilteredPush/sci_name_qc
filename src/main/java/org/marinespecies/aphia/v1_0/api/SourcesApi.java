package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;

import javax.ws.rs.core.GenericType;

import org.marinespecies.aphia.v1_0.model.Source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class SourcesApi {
  private ApiClient apiClient;

  public SourcesApi() {
    this(Configuration.getDefaultApiClient());
  }

  public SourcesApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get one or more sources/references including links, for one AphiaID
   * Get one or more sources/references including links, for one AphiaID
   * @param ID The AphiaID to search for (required)
   * @return List&lt;Source&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Source> aphiaSourcesByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaSourcesByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaSourcesByAphiaID/{ID}"
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

    GenericType<List<Source>> localVarReturnType = new GenericType<List<Source>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
