/*
 * IRMNG REST webservice
 * The definitions and operations are listed below. Click on an operation name to view it's details, and test it.
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package org.irmng.aphia.v1_0.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.math.BigDecimal;
/**
 * Distribution
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-09-09T18:22:45.022Z[GMT]")
public class Distribution {
  @SerializedName("locality")
  private String locality = null;

  @SerializedName("locationID")
  private String locationID = null;

  @SerializedName("higherGeography")
  private String higherGeography = null;

  @SerializedName("higherGeographyID")
  private String higherGeographyID = null;

  @SerializedName("recordStatus")
  private String recordStatus = null;

  @SerializedName("typeStatus")
  private String typeStatus = null;

  @SerializedName("establishmentMeans")
  private String establishmentMeans = null;

  @SerializedName("decimalLatitude")
  private BigDecimal decimalLatitude = null;

  @SerializedName("decimalLongitude")
  private BigDecimal decimalLongitude = null;

  @SerializedName("qualityStatus")
  private String qualityStatus = null;

  public Distribution locality(String locality) {
    this.locality = locality;
    return this;
  }

   /**
   * The specific description of the place
   * @return locality
  **/
  @Schema(description = "The specific description of the place")
  public String getLocality() {
    return locality;
  }

  public void setLocality(String locality) {
    this.locality = locality;
  }

  public Distribution locationID(String locationID) {
    this.locationID = locationID;
    return this;
  }

   /**
   * An identifier for the locality. Using the Marine Regions Geographic IDentifier (MRGID), see &lt;a href&#x3D;&#x27;https://www.marineregions.org/mrgid.php&#x27; target&#x3D;&#x27;_blank&#x27;&gt;https://www.marineregions.org/mrgid.php&lt;/a&gt;
   * @return locationID
  **/
  @Schema(description = "An identifier for the locality. Using the Marine Regions Geographic IDentifier (MRGID), see <a href='https://www.marineregions.org/mrgid.php' target='_blank'>https://www.marineregions.org/mrgid.php</a>")
  public String getLocationID() {
    return locationID;
  }

  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }

  public Distribution higherGeography(String higherGeography) {
    this.higherGeography = higherGeography;
    return this;
  }

   /**
   * A geographic name less specific than the information captured in the locality term. Possible values: an IHO Sea Area or Nation, derived from the MarineRegions &lt;a href&#x3D;&#x27;https://www.marineregions.org/gazetteer.php&#x27; target&#x3D;&#x27;_blank&#x27;&gt;gazetteer&lt;/a&gt;
   * @return higherGeography
  **/
  @Schema(description = "A geographic name less specific than the information captured in the locality term. Possible values: an IHO Sea Area or Nation, derived from the MarineRegions <a href='https://www.marineregions.org/gazetteer.php' target='_blank'>gazetteer</a>")
  public String getHigherGeography() {
    return higherGeography;
  }

  public void setHigherGeography(String higherGeography) {
    this.higherGeography = higherGeography;
  }

  public Distribution higherGeographyID(String higherGeographyID) {
    this.higherGeographyID = higherGeographyID;
    return this;
  }

   /**
   * An identifier for the geographic region within which the locality occurred, using MRGID
   * @return higherGeographyID
  **/
  @Schema(description = "An identifier for the geographic region within which the locality occurred, using MRGID")
  public String getHigherGeographyID() {
    return higherGeographyID;
  }

  public void setHigherGeographyID(String higherGeographyID) {
    this.higherGeographyID = higherGeographyID;
  }

  public Distribution recordStatus(String recordStatus) {
    this.recordStatus = recordStatus;
    return this;
  }

   /**
   * The status of the distribution record. Possible values are &#x27;valid&#x27; ,&#x27;doubtful&#x27; or &#x27;inaccurate&#x27;. See &lt;a href&#x3D;&#x27;https://www.irmng.org/aphia.php?p&#x3D;manual#topic8b&#x27; target&#x3D;_blank&gt;here&lt;/a&gt; for explanation of the statuses
   * @return recordStatus
  **/
  @Schema(description = "The status of the distribution record. Possible values are 'valid' ,'doubtful' or 'inaccurate'. See <a href='https://www.irmng.org/aphia.php?p=manual#topic8b' target=_blank>here</a> for explanation of the statuses")
  public String getRecordStatus() {
    return recordStatus;
  }

  public void setRecordStatus(String recordStatus) {
    this.recordStatus = recordStatus;
  }

  public Distribution typeStatus(String typeStatus) {
    this.typeStatus = typeStatus;
    return this;
  }

   /**
   * The type status of the distribution. Possible values: &#x27;holotype&#x27; or empty.
   * @return typeStatus
  **/
  @Schema(description = "The type status of the distribution. Possible values: 'holotype' or empty.")
  public String getTypeStatus() {
    return typeStatus;
  }

  public void setTypeStatus(String typeStatus) {
    this.typeStatus = typeStatus;
  }

  public Distribution establishmentMeans(String establishmentMeans) {
    this.establishmentMeans = establishmentMeans;
    return this;
  }

   /**
   * The process by which the biological individual(s) represented in the Occurrence became established at the location. Possible values: values listed as Origin &lt;a href&#x3D;&#x27;https://www.marinespecies.org/introduced/wiki/Traits:Origin&#x27; target&#x3D;_blank&gt;in WRiMS&lt;/a&gt;
   * @return establishmentMeans
  **/
  @Schema(description = "The process by which the biological individual(s) represented in the Occurrence became established at the location. Possible values: values listed as Origin <a href='https://www.marinespecies.org/introduced/wiki/Traits:Origin' target=_blank>in WRiMS</a>")
  public String getEstablishmentMeans() {
    return establishmentMeans;
  }

  public void setEstablishmentMeans(String establishmentMeans) {
    this.establishmentMeans = establishmentMeans;
  }

  public Distribution decimalLatitude(BigDecimal decimalLatitude) {
    this.decimalLatitude = decimalLatitude;
    return this;
  }

   /**
   * The geographic latitude (in decimal degrees, WGS84)
   * @return decimalLatitude
  **/
  @Schema(description = "The geographic latitude (in decimal degrees, WGS84)")
  public BigDecimal getDecimalLatitude() {
    return decimalLatitude;
  }

  public void setDecimalLatitude(BigDecimal decimalLatitude) {
    this.decimalLatitude = decimalLatitude;
  }

  public Distribution decimalLongitude(BigDecimal decimalLongitude) {
    this.decimalLongitude = decimalLongitude;
    return this;
  }

   /**
   * The geographic longitude (in decimal degrees, WGS84)
   * @return decimalLongitude
  **/
  @Schema(description = "The geographic longitude (in decimal degrees, WGS84)")
  public BigDecimal getDecimalLongitude() {
    return decimalLongitude;
  }

  public void setDecimalLongitude(BigDecimal decimalLongitude) {
    this.decimalLongitude = decimalLongitude;
  }

  public Distribution qualityStatus(String qualityStatus) {
    this.qualityStatus = qualityStatus;
    return this;
  }

   /**
   * Quality status of the record. Possible values: &#x27;checked&#x27;, &#x27;trusted&#x27; or &#x27;unreviewed&#x27;. See &lt;a href&#x3D;&#x27;https://www.irmng.org/aphia.php?p&#x3D;manual#topic22&#x27; target&#x3D;&#x27;_blank&#x27;&gt;https://www.irmng.org/aphia.php?p&#x3D;manual#topic22&lt;/a&gt;
   * @return qualityStatus
  **/
  @Schema(description = "Quality status of the record. Possible values: 'checked', 'trusted' or 'unreviewed'. See <a href='https://www.irmng.org/aphia.php?p=manual#topic22' target='_blank'>https://www.irmng.org/aphia.php?p=manual#topic22</a>")
  public String getQualityStatus() {
    return qualityStatus;
  }

  public void setQualityStatus(String qualityStatus) {
    this.qualityStatus = qualityStatus;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Distribution distribution = (Distribution) o;
    return Objects.equals(this.locality, distribution.locality) &&
        Objects.equals(this.locationID, distribution.locationID) &&
        Objects.equals(this.higherGeography, distribution.higherGeography) &&
        Objects.equals(this.higherGeographyID, distribution.higherGeographyID) &&
        Objects.equals(this.recordStatus, distribution.recordStatus) &&
        Objects.equals(this.typeStatus, distribution.typeStatus) &&
        Objects.equals(this.establishmentMeans, distribution.establishmentMeans) &&
        Objects.equals(this.decimalLatitude, distribution.decimalLatitude) &&
        Objects.equals(this.decimalLongitude, distribution.decimalLongitude) &&
        Objects.equals(this.qualityStatus, distribution.qualityStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(locality, locationID, higherGeography, higherGeographyID, recordStatus, typeStatus, establishmentMeans, decimalLatitude, decimalLongitude, qualityStatus);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Distribution {\n");
    
    sb.append("    locality: ").append(toIndentedString(locality)).append("\n");
    sb.append("    locationID: ").append(toIndentedString(locationID)).append("\n");
    sb.append("    higherGeography: ").append(toIndentedString(higherGeography)).append("\n");
    sb.append("    higherGeographyID: ").append(toIndentedString(higherGeographyID)).append("\n");
    sb.append("    recordStatus: ").append(toIndentedString(recordStatus)).append("\n");
    sb.append("    typeStatus: ").append(toIndentedString(typeStatus)).append("\n");
    sb.append("    establishmentMeans: ").append(toIndentedString(establishmentMeans)).append("\n");
    sb.append("    decimalLatitude: ").append(toIndentedString(decimalLatitude)).append("\n");
    sb.append("    decimalLongitude: ").append(toIndentedString(decimalLongitude)).append("\n");
    sb.append("    qualityStatus: ").append(toIndentedString(qualityStatus)).append("\n");
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