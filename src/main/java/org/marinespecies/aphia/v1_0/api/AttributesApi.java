package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;

import javax.ws.rs.core.GenericType;

import org.marinespecies.aphia.v1_0.model.Attribute;
import org.marinespecies.aphia.v1_0.model.AttributeKey;
import org.marinespecies.aphia.v1_0.model.AttributeValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")public class AttributesApi {
  private ApiClient apiClient;

  public AttributesApi() {
    this(Configuration.getDefaultApiClient());
  }

  public AttributesApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Get attribute definitions
   * Get attribute definitions. To refer to root items specify ID &#x3D; &#x27;0&#x27;
   * @param ID The attribute definition id to search for (required)
   * @param includeChildren Include the tree of children. Default&#x3D;true (optional, default to true)
   * @return List&lt;AttributeKey&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AttributeKey> aphiaAttributeKeysByID(Integer ID, Boolean includeChildren) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaAttributeKeysByID");
    }
    // create path and map variables
    String localVarPath = "/AphiaAttributeKeysByID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_children", includeChildren));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<AttributeKey>> localVarReturnType = new GenericType<List<AttributeKey>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get list values that are grouped by an CateogryID
   * Get list values that are grouped by an CateogryID
   * @param ID The CateogryID to search for (required)
   * @return List&lt;AttributeValue&gt;
   * @throws ApiException if fails to make API call
   */
  public List<AttributeValue> aphiaAttributeValuesByCategoryID(Integer ID) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaAttributeValuesByCategoryID");
    }
    // create path and map variables
    String localVarPath = "/AphiaAttributeValuesByCategoryID/{ID}"
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

    GenericType<List<AttributeValue>> localVarReturnType = new GenericType<List<AttributeValue>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get a list of attributes for a given AphiaID
   * Get a list of attributes for a given AphiaID
   * @param ID The AphiaID to search for (required)
   * @param includeInherited Include attributes inherited from the taxon its parent(s). Default&#x3D;false (optional, default to false)
   * @return List&lt;Attribute&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Attribute> aphiaAttributesByAphiaID(Integer ID, Boolean includeInherited) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaAttributesByAphiaID");
    }
    // create path and map variables
    String localVarPath = "/AphiaAttributesByAphiaID/{ID}"
      .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

    // query params
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPairs("", "include_inherited", includeInherited));


    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    GenericType<List<Attribute>> localVarReturnType = new GenericType<List<Attribute>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
  /**
   * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
   * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
   * @param ID The attribute definition id to search for (required)
   * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
   * @return List&lt;Attribute&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Attribute> aphiaIDsByAttributeKeyID(Integer ID, Integer offset) throws ApiException {
    Object localVarPostBody = null;
    // verify the required parameter 'ID' is set
    if (ID == null) {
      throw new ApiException(400, "Missing the required parameter 'ID' when calling aphiaIDsByAttributeKeyID");
    }
    // create path and map variables
    String localVarPath = "/AphiaIDsByAttributeKeyID/{ID}"
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

    GenericType<List<Attribute>> localVarReturnType = new GenericType<List<Attribute>>() {};
    return apiClient.invokeAPI(localVarPath, "GET", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
  }
}
