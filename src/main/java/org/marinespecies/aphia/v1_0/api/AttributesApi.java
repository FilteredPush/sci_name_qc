/*
 * WoRMS REST webservice
 * The definitions and operations are listed below. Click on an operation name to view it's details, and test it.
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.marinespecies.aphia.v1_0.api;

import org.marinespecies.aphia.v1_0.handler.ApiCallback;
import org.marinespecies.aphia.v1_0.handler.ApiClient;
import org.marinespecies.aphia.v1_0.handler.ApiException;
import org.marinespecies.aphia.v1_0.handler.ApiResponse;
import org.marinespecies.aphia.v1_0.handler.Configuration;
import org.marinespecies.aphia.v1_0.handler.Pair;
import org.marinespecies.aphia.v1_0.handler.ProgressRequestBody;
import org.marinespecies.aphia.v1_0.handler.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import org.marinespecies.aphia.v1_0.model.Attribute;
import org.marinespecies.aphia.v1_0.model.AttributeKey;
import org.marinespecies.aphia.v1_0.model.AttributeValue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesApi {
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
     * Build call for aphiaAttributeKeysByID
     * @param ID The attribute definition id to search for (required)
     * @param includeChildren Include the tree of children. Default&#x3D;true (optional, default to true)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call aphiaAttributeKeysByIDCall(Integer ID, Boolean includeChildren, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/AphiaAttributeKeysByID/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (includeChildren != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include_children", includeChildren));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call aphiaAttributeKeysByIDValidateBeforeCall(Integer ID, Boolean includeChildren, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling aphiaAttributeKeysByID(Async)");
        }

        okhttp3.Call call = aphiaAttributeKeysByIDCall(ID, includeChildren, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get attribute definitions
     * Get attribute definitions. To refer to root items specify ID &#x3D; &#x27;0&#x27;
     * @param ID The attribute definition id to search for (required)
     * @param includeChildren Include the tree of children. Default&#x3D;true (optional, default to true)
     * @return List&lt;AttributeKey&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<AttributeKey> aphiaAttributeKeysByID(Integer ID, Boolean includeChildren) throws ApiException {
        ApiResponse<List<AttributeKey>> resp = aphiaAttributeKeysByIDWithHttpInfo(ID, includeChildren);
        return resp.getData();
    }

    /**
     * Get attribute definitions
     * Get attribute definitions. To refer to root items specify ID &#x3D; &#x27;0&#x27;
     * @param ID The attribute definition id to search for (required)
     * @param includeChildren Include the tree of children. Default&#x3D;true (optional, default to true)
     * @return ApiResponse&lt;List&lt;AttributeKey&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<AttributeKey>> aphiaAttributeKeysByIDWithHttpInfo(Integer ID, Boolean includeChildren) throws ApiException {
        okhttp3.Call call = aphiaAttributeKeysByIDValidateBeforeCall(ID, includeChildren, null, null);
        Type localVarReturnType = new TypeToken<List<AttributeKey>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get attribute definitions (asynchronously)
     * Get attribute definitions. To refer to root items specify ID &#x3D; &#x27;0&#x27;
     * @param ID The attribute definition id to search for (required)
     * @param includeChildren Include the tree of children. Default&#x3D;true (optional, default to true)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call aphiaAttributeKeysByIDAsync(Integer ID, Boolean includeChildren, final ApiCallback<List<AttributeKey>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = aphiaAttributeKeysByIDValidateBeforeCall(ID, includeChildren, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<AttributeKey>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for aphiaAttributeValuesByCategoryID
     * @param ID The CateogryID to search for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call aphiaAttributeValuesByCategoryIDCall(Integer ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/AphiaAttributeValuesByCategoryID/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call aphiaAttributeValuesByCategoryIDValidateBeforeCall(Integer ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling aphiaAttributeValuesByCategoryID(Async)");
        }

        okhttp3.Call call = aphiaAttributeValuesByCategoryIDCall(ID, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get list values that are grouped by an CateogryID
     * Get list values that are grouped by an CateogryID
     * @param ID The CateogryID to search for (required)
     * @return List&lt;AttributeValue&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<AttributeValue> aphiaAttributeValuesByCategoryID(Integer ID) throws ApiException {
        ApiResponse<List<AttributeValue>> resp = aphiaAttributeValuesByCategoryIDWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get list values that are grouped by an CateogryID
     * Get list values that are grouped by an CateogryID
     * @param ID The CateogryID to search for (required)
     * @return ApiResponse&lt;List&lt;AttributeValue&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<AttributeValue>> aphiaAttributeValuesByCategoryIDWithHttpInfo(Integer ID) throws ApiException {
        okhttp3.Call call = aphiaAttributeValuesByCategoryIDValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<List<AttributeValue>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get list values that are grouped by an CateogryID (asynchronously)
     * Get list values that are grouped by an CateogryID
     * @param ID The CateogryID to search for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call aphiaAttributeValuesByCategoryIDAsync(Integer ID, final ApiCallback<List<AttributeValue>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = aphiaAttributeValuesByCategoryIDValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<AttributeValue>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for aphiaAttributesByAphiaID
     * @param ID The AphiaID to search for (required)
     * @param includeInherited Include attributes inherited from the taxon its parent(s). Default&#x3D;false (optional, default to false)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call aphiaAttributesByAphiaIDCall(Integer ID, Boolean includeInherited, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/AphiaAttributesByAphiaID/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (includeInherited != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("include_inherited", includeInherited));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call aphiaAttributesByAphiaIDValidateBeforeCall(Integer ID, Boolean includeInherited, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling aphiaAttributesByAphiaID(Async)");
        }

        okhttp3.Call call = aphiaAttributesByAphiaIDCall(ID, includeInherited, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get a list of attributes for a given AphiaID
     * Get a list of attributes for a given AphiaID
     * @param ID The AphiaID to search for (required)
     * @param includeInherited Include attributes inherited from the taxon its parent(s). Default&#x3D;false (optional, default to false)
     * @return List&lt;Attribute&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Attribute> aphiaAttributesByAphiaID(Integer ID, Boolean includeInherited) throws ApiException {
        ApiResponse<List<Attribute>> resp = aphiaAttributesByAphiaIDWithHttpInfo(ID, includeInherited);
        return resp.getData();
    }

    /**
     * Get a list of attributes for a given AphiaID
     * Get a list of attributes for a given AphiaID
     * @param ID The AphiaID to search for (required)
     * @param includeInherited Include attributes inherited from the taxon its parent(s). Default&#x3D;false (optional, default to false)
     * @return ApiResponse&lt;List&lt;Attribute&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Attribute>> aphiaAttributesByAphiaIDWithHttpInfo(Integer ID, Boolean includeInherited) throws ApiException {
        okhttp3.Call call = aphiaAttributesByAphiaIDValidateBeforeCall(ID, includeInherited, null, null);
        Type localVarReturnType = new TypeToken<List<Attribute>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a list of attributes for a given AphiaID (asynchronously)
     * Get a list of attributes for a given AphiaID
     * @param ID The AphiaID to search for (required)
     * @param includeInherited Include attributes inherited from the taxon its parent(s). Default&#x3D;false (optional, default to false)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call aphiaAttributesByAphiaIDAsync(Integer ID, Boolean includeInherited, final ApiCallback<List<Attribute>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = aphiaAttributesByAphiaIDValidateBeforeCall(ID, includeInherited, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Attribute>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for aphiaIDsByAttributeKeyID
     * @param ID The attribute definition id to search for (required)
     * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public okhttp3.Call aphiaIDsByAttributeKeyIDCall(Integer ID, Integer offset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/AphiaIDsByAttributeKeyID/{ID}"
            .replaceAll("\\{" + "ID" + "\\}", apiClient.escapeString(ID.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        if (offset != null)
        localVarQueryParams.addAll(apiClient.parameterToPair("offset", offset));

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new okhttp3.Interceptor() {
                @Override
                public okhttp3.Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] {  };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call aphiaIDsByAttributeKeyIDValidateBeforeCall(Integer ID, Integer offset, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling aphiaIDsByAttributeKeyID(Async)");
        }

        okhttp3.Call call = aphiaIDsByAttributeKeyIDCall(ID, offset, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
     * @param ID The attribute definition id to search for (required)
     * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
     * @return List&lt;Attribute&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Attribute> aphiaIDsByAttributeKeyID(Integer ID, Integer offset) throws ApiException {
        ApiResponse<List<Attribute>> resp = aphiaIDsByAttributeKeyIDWithHttpInfo(ID, offset);
        return resp.getData();
    }

    /**
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
     * @param ID The attribute definition id to search for (required)
     * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
     * @return ApiResponse&lt;List&lt;Attribute&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Attribute>> aphiaIDsByAttributeKeyIDWithHttpInfo(Integer ID, Integer offset) throws ApiException {
        okhttp3.Call call = aphiaIDsByAttributeKeyIDValidateBeforeCall(ID, offset, null, null);
        Type localVarReturnType = new TypeToken<List<Attribute>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID (asynchronously)
     * Get a list of AphiaIDs (max 50) with attribute tree for a given attribute definition ID
     * @param ID The attribute definition id to search for (required)
     * @param offset Starting recordnumber, when retrieving next chunk of (50) records. Default&#x3D;1 (optional, default to 1)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public okhttp3.Call aphiaIDsByAttributeKeyIDAsync(Integer ID, Integer offset, final ApiCallback<List<Attribute>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        okhttp3.Call call = aphiaIDsByAttributeKeyIDValidateBeforeCall(ID, offset, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Attribute>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
