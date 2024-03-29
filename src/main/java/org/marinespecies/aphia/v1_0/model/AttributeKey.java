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
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.marinespecies.aphia.v1_0.model.AttributeKey;
/**
 * AttributeKey
 *
 * @author mole
 * @version $Id: $Id
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-04-27T19:29:57.442Z[GMT]")
public class AttributeKey {
  @SerializedName("measurementTypeID")
  private Integer measurementTypeID = null;

  @SerializedName("measurementType")
  private String measurementType = null;

  @SerializedName("input_id")
  private Integer inputId = null;

  @SerializedName("CategoryID")
  private Integer categoryID = null;

  @SerializedName("children")
  private List<AttributeKey> children = null;

  /**
   * <p>measurementTypeID.</p>
   *
   * @param measurementTypeID a {@link java.lang.Integer} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey measurementTypeID(Integer measurementTypeID) {
    this.measurementTypeID = measurementTypeID;
    return this;
  }

  /**
   * An internal identifier for the measurementType
   *
   * @return measurementTypeID
   */
  @Schema(example = "4", description = "An internal identifier for the measurementType")
  public Integer getMeasurementTypeID() {
    return measurementTypeID;
  }

  /**
   * <p>Setter for the field <code>measurementTypeID</code>.</p>
   *
   * @param measurementTypeID a {@link java.lang.Integer} object.
   */
  public void setMeasurementTypeID(Integer measurementTypeID) {
    this.measurementTypeID = measurementTypeID;
  }

  /**
   * <p>measurementType.</p>
   *
   * @param measurementType a {@link java.lang.String} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey measurementType(String measurementType) {
    this.measurementType = measurementType;
    return this;
  }

  /**
   * The nature of the measurement, fact, characteristic, or assertion &lt;a href&#x3D;&#x27;https://www.marinespecies.org/traits/wiki&#x27; target&#x3D;&#x27;_blank&#x27;&gt;https://www.marinespecies.org/traits/wiki&lt;/a&gt;
   *
   * @return measurementType
   */
  @Schema(example = "Functional group", description = "The nature of the measurement, fact, characteristic, or assertion <a href='https://www.marinespecies.org/traits/wiki' target='_blank'>https://www.marinespecies.org/traits/wiki</a>")
  public String getMeasurementType() {
    return measurementType;
  }

  /**
   * <p>Setter for the field <code>measurementType</code>.</p>
   *
   * @param measurementType a {@link java.lang.String} object.
   */
  public void setMeasurementType(String measurementType) {
    this.measurementType = measurementType;
  }

  /**
   * <p>inputId.</p>
   *
   * @param inputId a {@link java.lang.Integer} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey inputId(Integer inputId) {
    this.inputId = inputId;
    return this;
  }

  /**
   * The data type that is expected as value for this attribute definition
   *
   * @return inputId
   */
  @Schema(example = "1", description = "The data type that is expected as value for this attribute definition")
  public Integer getInputId() {
    return inputId;
  }

  /**
   * <p>Setter for the field <code>inputId</code>.</p>
   *
   * @param inputId a {@link java.lang.Integer} object.
   */
  public void setInputId(Integer inputId) {
    this.inputId = inputId;
  }

  /**
   * <p>categoryID.</p>
   *
   * @param categoryID a {@link java.lang.Integer} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey categoryID(Integer categoryID) {
    this.categoryID = categoryID;
    return this;
  }

  /**
   * The category identifier to list possible attribute values for this attribute definition
   *
   * @return categoryID
   */
  @Schema(example = "7", description = "The category identifier to list possible attribute values for this attribute definition")
  public Integer getCategoryID() {
    return categoryID;
  }

  /**
   * <p>Setter for the field <code>categoryID</code>.</p>
   *
   * @param categoryID a {@link java.lang.Integer} object.
   */
  public void setCategoryID(Integer categoryID) {
    this.categoryID = categoryID;
  }

  /**
   * <p>children.</p>
   *
   * @param children a {@link java.util.List} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey children(List<AttributeKey> children) {
    this.children = children;
    return this;
  }

  /**
   * <p>addChildrenItem.</p>
   *
   * @param childrenItem a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   * @return a {@link org.marinespecies.aphia.v1_0.model.AttributeKey} object.
   */
  public AttributeKey addChildrenItem(AttributeKey childrenItem) {
    if (this.children == null) {
      this.children = new ArrayList<AttributeKey>();
    }
    this.children.add(childrenItem);
    return this;
  }

  /**
   * The possible child attribute keys that help to describe to current attribute
   *
   * @return children
   */
  @Schema(description = "The possible child attribute keys that help to describe to current attribute")
  public List<AttributeKey> getChildren() {
    return children;
  }

  /**
   * <p>Setter for the field <code>children</code>.</p>
   *
   * @param children a {@link java.util.List} object.
   */
  public void setChildren(List<AttributeKey> children) {
    this.children = children;
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
    AttributeKey attributeKey = (AttributeKey) o;
    return Objects.equals(this.measurementTypeID, attributeKey.measurementTypeID) &&
        Objects.equals(this.measurementType, attributeKey.measurementType) &&
        Objects.equals(this.inputId, attributeKey.inputId) &&
        Objects.equals(this.categoryID, attributeKey.categoryID) &&
        Objects.equals(this.children, attributeKey.children);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(measurementTypeID, measurementType, inputId, categoryID, children);
  }


  /** {@inheritDoc} */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeKey {\n");
    
    sb.append("    measurementTypeID: ").append(toIndentedString(measurementTypeID)).append("\n");
    sb.append("    measurementType: ").append(toIndentedString(measurementType)).append("\n");
    sb.append("    inputId: ").append(toIndentedString(inputId)).append("\n");
    sb.append("    categoryID: ").append(toIndentedString(categoryID)).append("\n");
    sb.append("    children: ").append(toIndentedString(children)).append("\n");
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
