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

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class ExternalIdentifiersApi {
  private ApiClient apiClient;

  public ExternalIdentifiersApi() {
    this(Configuration.getDefaultApiClient());
  }

  public ExternalIdentifiersApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get any external identifier(s) for a given AphiaID
   * Get any external identifier(s) for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @param type Type of external identifier to return. The type should be one of the following values:&lt;br/&gt;&lt;ul&gt;&lt;li&gt;&lt;u&gt;algaebase&lt;/u&gt;: Algaebase species ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;bold&lt;/u&gt;: Barcode of Life Database (BOLD) TaxID&lt;/li&gt;&lt;li&gt;&lt;u&gt;dyntaxa&lt;/u&gt;: Dyntaxa ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;fishbase&lt;/u&gt;: FishBase species ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;iucn&lt;/u&gt;: IUCN Red List Identifier&lt;/li&gt;&lt;li&gt;&lt;u&gt;lsid&lt;/u&gt;: Life Science Identifier&lt;/li&gt;&lt;li&gt;&lt;u&gt;ncbi&lt;/u&gt;: NCBI Taxonomy ID (Genbank)&lt;/li&gt;&lt;li&gt;&lt;u&gt;tsn&lt;/u&gt;: ITIS Taxonomic Serial Number&lt;/li&gt;&lt;li&gt;&lt;u&gt;gisd&lt;/u&gt;: Global Invasive Species Database&lt;/li&gt;&lt;/ul&gt; (optional, default to tsn)
   * @return List&lt;String&gt;
   * @throws ApiException if fails to make API call
   */
  public List<String> aphiaExternalIDByAphiaID(Integer ID, String type) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaExternalIDByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaExternalIDByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));


    final String[] localVarAccepts = {
      "text/plain"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<String>> localVarReturnType = new GenericType<List<String>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get the Aphia Record for a given external identifier
   * Get the Aphia Record for a given external identifier
   * @param ID The external identifier to search for (required)
   * @param type Type of external identifier to search for. The type should be one of the following values:&lt;br/&gt;&lt;ul&gt;&lt;li&gt;&lt;u&gt;algaebase&lt;/u&gt;: Algaebase species ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;bold&lt;/u&gt;: Barcode of Life Database (BOLD) TaxID&lt;/li&gt;&lt;li&gt;&lt;u&gt;dyntaxa&lt;/u&gt;: Dyntaxa ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;fishbase&lt;/u&gt;: FishBase species ID&lt;/li&gt;&lt;li&gt;&lt;u&gt;iucn&lt;/u&gt;: IUCN Red List Identifier&lt;/li&gt;&lt;li&gt;&lt;u&gt;lsid&lt;/u&gt;: Life Science Identifier&lt;/li&gt;&lt;li&gt;&lt;u&gt;ncbi&lt;/u&gt;: NCBI Taxonomy ID (Genbank)&lt;/li&gt;&lt;li&gt;&lt;u&gt;tsn&lt;/u&gt;: ITIS Taxonomic Serial Number&lt;/li&gt;&lt;li&gt;&lt;u&gt;gisd&lt;/u&gt;: Global Invasive Species Database&lt;/li&gt;&lt;/ul&gt; (optional, default to tsn)
   * @return AphiaRecord
   * @throws ApiException if fails to make API call
   */
  public AphiaRecord aphiaRecordByExternalID(String ID, String type) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaRecordByExternalID");
    }
    // create path and map variables
    String localVarPath = "/AphiaRecordByExternalID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "type", type));


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
}
