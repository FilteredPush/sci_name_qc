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

package org.marinespecies.aphia.v1_0.handler.auth;

import org.marinespecies.aphia.v1_0.handler.Pair;

import java.util.Map;
import java.util.List;

/**
 * <p>OAuth class.</p>
 *
 * @author mole
 * @version $Id: $Id
 */
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-04-27T19:29:57.442Z[GMT]")public class OAuth implements Authentication {
  private String accessToken;

  /**
   * <p>Getter for the field <code>accessToken</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  public String getAccessToken() {
    return accessToken;
  }

  /**
   * <p>Setter for the field <code>accessToken</code>.</p>
   *
   * @param accessToken a {@link java.lang.String} object.
   */
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  /** {@inheritDoc} */
  @Override
  public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
    if (accessToken != null) {
      headerParams.put("Authorization", "Bearer " + accessToken);
    }
  }
}
