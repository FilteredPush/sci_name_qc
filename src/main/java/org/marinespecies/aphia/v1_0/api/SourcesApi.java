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


import org.marinespecies.aphia.v1_0.model.Source;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>SourcesApi class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
public class SourcesApi {
    private ApiClient apiClient;

    /**
     * <p>Constructor for SourcesApi.</p>
     */
    public SourcesApi() {
        this(Configuration.getDefaultApiClient());
    }

    /**
     * <p>Constructor for SourcesApi.</p>
     *
     * @param apiClient a {@link org.marinespecies.aphia.v1_0.handler.ApiClient} object.
     */
    public SourcesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * <p>Getter for the field <code>apiClient</code>.</p>
     *
     * @return a {@link org.marinespecies.aphia.v1_0.handler.ApiClient} object.
     */
    public ApiClient getApiClient() {
        return apiClient;
    }

    /**
     * <p>Setter for the field <code>apiClient</code>.</p>
     *
     * @param apiClient a {@link org.marinespecies.aphia.v1_0.handler.ApiClient} object.
     */
    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for aphiaSourcesByAphiaID
     *
     * @param ID The AphiaID to search for (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws org.marinespecies.aphia.v1_0.handler.ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call aphiaSourcesByAphiaIDCall(Integer ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;
        
        // create path and map variables
        String localVarPath = "/AphiaSourcesByAphiaID/{ID}"
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
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
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
    private com.squareup.okhttp.Call aphiaSourcesByAphiaIDValidateBeforeCall(Integer ID, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        // verify the required parameter 'ID' is set
        if (ID == null) {
            throw new ApiException("Missing the required parameter 'ID' when calling aphiaSourcesByAphiaID(Async)");
        }
        
        com.squareup.okhttp.Call call = aphiaSourcesByAphiaIDCall(ID, progressListener, progressRequestListener);
        return call;

        
        
        
        
    }

    /**
     * Get one or more sources/references including links, for one AphiaID
     * Get one or more sources/references including links, for one AphiaID
     *
     * @param ID The AphiaID to search for (required)
     * @return List&lt;Source&gt;
     * @throws org.marinespecies.aphia.v1_0.handler.ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<Source> aphiaSourcesByAphiaID(Integer ID) throws ApiException {
        ApiResponse<List<Source>> resp = aphiaSourcesByAphiaIDWithHttpInfo(ID);
        return resp.getData();
    }

    /**
     * Get one or more sources/references including links, for one AphiaID
     * Get one or more sources/references including links, for one AphiaID
     *
     * @param ID The AphiaID to search for (required)
     * @return ApiResponse&lt;List&lt;Source&gt;&gt;
     * @throws org.marinespecies.aphia.v1_0.handler.ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<Source>> aphiaSourcesByAphiaIDWithHttpInfo(Integer ID) throws ApiException {
        com.squareup.okhttp.Call call = aphiaSourcesByAphiaIDValidateBeforeCall(ID, null, null);
        Type localVarReturnType = new TypeToken<List<Source>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get one or more sources/references including links, for one AphiaID (asynchronously)
     * Get one or more sources/references including links, for one AphiaID
     *
     * @param ID The AphiaID to search for (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws org.marinespecies.aphia.v1_0.handler.ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call aphiaSourcesByAphiaIDAsync(Integer ID, final ApiCallback<List<Source>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = aphiaSourcesByAphiaIDValidateBeforeCall(ID, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<Source>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
}
