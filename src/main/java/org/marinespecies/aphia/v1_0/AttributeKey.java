/**
 * AttributeKey.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class AttributeKey  implements java.io.Serializable {
    private int measurementTypeID;

    private java.lang.String measurementType;

    private int input_id;

    private int categoryID;

    private org.marinespecies.aphia.v1_0.AttributeKey[] children;

    public AttributeKey() {
    }

    public AttributeKey(
           int measurementTypeID,
           java.lang.String measurementType,
           int input_id,
           int categoryID,
           org.marinespecies.aphia.v1_0.AttributeKey[] children) {
           this.measurementTypeID = measurementTypeID;
           this.measurementType = measurementType;
           this.input_id = input_id;
           this.categoryID = categoryID;
           this.children = children;
    }


    /**
     * Gets the measurementTypeID value for this AttributeKey.
     * 
     * @return measurementTypeID
     */
    public int getMeasurementTypeID() {
        return measurementTypeID;
    }


    /**
     * Sets the measurementTypeID value for this AttributeKey.
     * 
     * @param measurementTypeID
     */
    public void setMeasurementTypeID(int measurementTypeID) {
        this.measurementTypeID = measurementTypeID;
    }


    /**
     * Gets the measurementType value for this AttributeKey.
     * 
     * @return measurementType
     */
    public java.lang.String getMeasurementType() {
        return measurementType;
    }


    /**
     * Sets the measurementType value for this AttributeKey.
     * 
     * @param measurementType
     */
    public void setMeasurementType(java.lang.String measurementType) {
        this.measurementType = measurementType;
    }


    /**
     * Gets the input_id value for this AttributeKey.
     * 
     * @return input_id
     */
    public int getInput_id() {
        return input_id;
    }


    /**
     * Sets the input_id value for this AttributeKey.
     * 
     * @param input_id
     */
    public void setInput_id(int input_id) {
        this.input_id = input_id;
    }


    /**
     * Gets the categoryID value for this AttributeKey.
     * 
     * @return categoryID
     */
    public int getCategoryID() {
        return categoryID;
    }


    /**
     * Sets the categoryID value for this AttributeKey.
     * 
     * @param categoryID
     */
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }


    /**
     * Gets the children value for this AttributeKey.
     * 
     * @return children
     */
    public org.marinespecies.aphia.v1_0.AttributeKey[] getChildren() {
        return children;
    }


    /**
     * Sets the children value for this AttributeKey.
     * 
     * @param children
     */
    public void setChildren(org.marinespecies.aphia.v1_0.AttributeKey[] children) {
        this.children = children;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributeKey)) return false;
        AttributeKey other = (AttributeKey) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.measurementTypeID == other.getMeasurementTypeID() &&
            ((this.measurementType==null && other.getMeasurementType()==null) || 
             (this.measurementType!=null &&
              this.measurementType.equals(other.getMeasurementType()))) &&
            this.input_id == other.getInput_id() &&
            this.categoryID == other.getCategoryID() &&
            ((this.children==null && other.getChildren()==null) || 
             (this.children!=null &&
              java.util.Arrays.equals(this.children, other.getChildren())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getMeasurementTypeID();
        if (getMeasurementType() != null) {
            _hashCode += getMeasurementType().hashCode();
        }
        _hashCode += getInput_id();
        _hashCode += getCategoryID();
        if (getChildren() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChildren());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChildren(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AttributeKey.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "AttributeKey"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measurementTypeID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementTypeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measurementType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("input_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "input_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CategoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("children");
        elemField.setXmlName(new javax.xml.namespace.QName("", "children"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "AttributeKey"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
