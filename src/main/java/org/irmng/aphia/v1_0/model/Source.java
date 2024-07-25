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
/**
 * Source
 *
 * @author mole
 * @version $Id: $Id
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-09-09T19:25:08.764Z[GMT]")
public class Source {
  @SerializedName("source_id")
  private Integer sourceId = 1;

  @SerializedName("use")
  private String use = null;

  @SerializedName("reference")
  private String reference = null;

  @SerializedName("page")
  private String page = null;

  @SerializedName("url")
  private String url = null;

  @SerializedName("link")
  private String link = null;

  @SerializedName("fulltext")
  private String fulltext = null;

  @SerializedName("doi")
  private String doi = null;

  /**
   * <p>sourceId.</p>
   *
   * @param sourceId a {@link java.lang.Integer} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source sourceId(Integer sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Unique identifier for the source within IRMNG --
   *
   * @return sourceId
   */
  @Schema(description = "Unique identifier for the source within IRMNG -- ")
  public Integer getSourceId() {
    return sourceId;
  }

  /**
   * <p>Setter for the field <code>sourceId</code>.</p>
   *
   * @param sourceId a {@link java.lang.Integer} object.
   */
  public void setSourceId(Integer sourceId) {
    this.sourceId = sourceId;
  }

  /**
   * <p>use.</p>
   *
   * @param use a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source use(String use) {
    this.use = use;
    return this;
  }

  /**
   * Usage of the source for this taxon. See &lt;a href&#x3D;&#x27;https://www.irmng.org/aphia.php?p&#x3D;manual#topic6&#x27; target&#x3D;&#x27;_blank&#x27;&gt;here&lt;/a&gt; for all values
   *
   * @return use
   */
  @Schema(description = "Usage of the source for this taxon. See <a href='https://www.irmng.org/aphia.php?p=manual#topic6' target='_blank'>here</a> for all values")
  public String getUse() {
    return use;
  }

  /**
   * <p>Setter for the field <code>use</code>.</p>
   *
   * @param use a {@link java.lang.String} object.
   */
  public void setUse(String use) {
    this.use = use;
  }

  /**
   * <p>reference.</p>
   *
   * @param reference a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source reference(String reference) {
    this.reference = reference;
    return this;
  }

  /**
   * Full citation string
   *
   * @return reference
   */
  @Schema(description = "Full citation string")
  public String getReference() {
    return reference;
  }

  /**
   * <p>Setter for the field <code>reference</code>.</p>
   *
   * @param reference a {@link java.lang.String} object.
   */
  public void setReference(String reference) {
    this.reference = reference;
  }

  /**
   * <p>page.</p>
   *
   * @param page a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source page(String page) {
    this.page = page;
    return this;
  }

  /**
   * Page(s) where the taxon is mentioned
   *
   * @return page
   */
  @Schema(description = "Page(s) where the taxon is mentioned")
  public String getPage() {
    return page;
  }

  /**
   * <p>Setter for the field <code>page</code>.</p>
   *
   * @param page a {@link java.lang.String} object.
   */
  public void setPage(String page) {
    this.page = page;
  }

  /**
   * <p>url.</p>
   *
   * @param url a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source url(String url) {
    this.url = url;
    return this;
  }

  /**
   * Direct link to the source record
   *
   * @return url
   */
  @Schema(description = "Direct link to the source record")
  public String getUrl() {
    return url;
  }

  /**
   * <p>Setter for the field <code>url</code>.</p>
   *
   * @param url a {@link java.lang.String} object.
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * <p>link.</p>
   *
   * @param link a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source link(String link) {
    this.link = link;
    return this;
  }

  /**
   * External link (i.e. journal, data system, etc..)
   *
   * @return link
   */
  @Schema(description = "External link (i.e. journal, data system, etc..)")
  public String getLink() {
    return link;
  }

  /**
   * <p>Setter for the field <code>link</code>.</p>
   *
   * @param link a {@link java.lang.String} object.
   */
  public void setLink(String link) {
    this.link = link;
  }

  /**
   * <p>fulltext.</p>
   *
   * @param fulltext a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source fulltext(String fulltext) {
    this.fulltext = fulltext;
    return this;
  }

  /**
   * Full text link (PDF)
   *
   * @return fulltext
   */
  @Schema(description = "Full text link (PDF)")
  public String getFulltext() {
    return fulltext;
  }

  /**
   * <p>Setter for the field <code>fulltext</code>.</p>
   *
   * @param fulltext a {@link java.lang.String} object.
   */
  public void setFulltext(String fulltext) {
    this.fulltext = fulltext;
  }

  /**
   * <p>doi.</p>
   *
   * @param doi a {@link java.lang.String} object.
   * @return a {@link org.irmng.aphia.v1_0.model.Source} object.
   */
  public Source doi(String doi) {
    this.doi = doi;
    return this;
  }

  /**
   * Digital Object Identifier
   *
   * @return doi
   */
  @Schema(description = "Digital Object Identifier")
  public String getDoi() {
    return doi;
  }

  /**
   * <p>Setter for the field <code>doi</code>.</p>
   *
   * @param doi a {@link java.lang.String} object.
   */
  public void setDoi(String doi) {
    this.doi = doi;
  }


  /** {@inheritDoc} */
  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Source source = (Source) o;
    return Objects.equals(this.sourceId, source.sourceId) &&
        Objects.equals(this.use, source.use) &&
        Objects.equals(this.reference, source.reference) &&
        Objects.equals(this.page, source.page) &&
        Objects.equals(this.url, source.url) &&
        Objects.equals(this.link, source.link) &&
        Objects.equals(this.fulltext, source.fulltext) &&
        Objects.equals(this.doi, source.doi);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(sourceId, use, reference, page, url, link, fulltext, doi);
  }


  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Source {\n");
    
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    use: ").append(toIndentedString(use)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
    sb.append("    fulltext: ").append(toIndentedString(fulltext)).append("\n");
    sb.append("    doi: ").append(toIndentedString(doi)).append("\n");
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
