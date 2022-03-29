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

package org.marinespecies.aphia.v1_0.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.marinespecies.aphia.v1_0.model.Classification;
/**
 * Classification
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-03-28T23:57:28.350Z[GMT]")
public class Classification {
  @JsonProperty("AphiaID")
  private Integer aphiaID = 126132;

  @JsonProperty("rank")
  private String rank = null;

  @JsonProperty("scientificname")
  private String scientificname = null;

  @JsonProperty("child")
  private Classification child = null;

  public Classification aphiaID(Integer aphiaID) {
    this.aphiaID = aphiaID;
    return this;
  }

   /**
   * Get aphiaID
   * @return aphiaID
  **/
  @Schema(description = "")
  public Integer getAphiaID() {
    return aphiaID;
  }

  public void setAphiaID(Integer aphiaID) {
    this.aphiaID = aphiaID;
  }

  public Classification rank(String rank) {
    this.rank = rank;
    return this;
  }

   /**
   * Get rank
   * @return rank
  **/
  @Schema(description = "")
  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public Classification scientificname(String scientificname) {
    this.scientificname = scientificname;
    return this;
  }

   /**
   * Get scientificname
   * @return scientificname
  **/
  @Schema(description = "")
  public String getScientificname() {
    return scientificname;
  }

  public void setScientificname(String scientificname) {
    this.scientificname = scientificname;
  }

  public Classification child(Classification child) {
    this.child = child;
    return this;
  }

   /**
   * Get child
   * @return child
  **/
  @Schema(description = "")
  public Classification getChild() {
    return child;
  }

  public void setChild(Classification child) {
    this.child = child;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Classification classification = (Classification) o;
    return Objects.equals(this.aphiaID, classification.aphiaID) &&
        Objects.equals(this.rank, classification.rank) &&
        Objects.equals(this.scientificname, classification.scientificname) &&
        Objects.equals(this.child, classification.child);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aphiaID, rank, scientificname, child);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Classification {\n");
    
    sb.append("    aphiaID: ").append(toIndentedString(aphiaID)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    scientificname: ").append(toIndentedString(scientificname)).append("\n");
    sb.append("    child: ").append(toIndentedString(child)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}