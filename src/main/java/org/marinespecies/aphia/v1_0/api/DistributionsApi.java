package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;

import javax.ws.rs.core.GenericType;

import org.marinespecies.aphia.v1_0.model.Distribution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class DistributionsApi {
  private ApiClient apiClient;

  public DistributionsApi() {
    this(Configuration.getDefaultApiClient());
  }

  public DistributionsApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get all distributions for a given AphiaID
   * Get all distributions for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @return List&lt;Distribution&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Distribution> aphiaDistributionsByAphiaID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaDistributionsByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaDistributionsByAphiaID/{ID}"
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

    GenericType<List<Distribution>> localVarReturnType = new GenericType<List<Distribution>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
