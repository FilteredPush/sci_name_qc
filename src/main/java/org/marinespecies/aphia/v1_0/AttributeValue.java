/**
 * AttributeValue.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class AttributeValue  implements java.io.Serializable {
    private int measurementValueID;

    private java.lang.String measurementValue;

    private java.lang.String measurementValueCode;

    private org.marinespecies.aphia.v1_0.AttributeValue[] children;

    public AttributeValue() {
    }

    public AttributeValue(
           int measurementValueID,
           java.lang.String measurementValue,
           java.lang.String measurementValueCode,
           org.marinespecies.aphia.v1_0.AttributeValue[] children) {
           this.measurementValueID = measurementValueID;
           this.measurementValue = measurementValue;
           this.measurementValueCode = measurementValueCode;
           this.children = children;
    }


    /**
     * Gets the measurementValueID value for this AttributeValue.
     * 
     * @return measurementValueID
     */
    public int getMeasurementValueID() {
        return measurementValueID;
    }


    /**
     * Sets the measurementValueID value for this AttributeValue.
     * 
     * @param measurementValueID
     */
    public void setMeasurementValueID(int measurementValueID) {
        this.measurementValueID = measurementValueID;
    }


    /**
     * Gets the measurementValue value for this AttributeValue.
     * 
     * @return measurementValue
     */
    public java.lang.String getMeasurementValue() {
        return measurementValue;
    }


    /**
     * Sets the measurementValue value for this AttributeValue.
     * 
     * @param measurementValue
     */
    public void setMeasurementValue(java.lang.String measurementValue) {
        this.measurementValue = measurementValue;
    }


    /**
     * Gets the measurementValueCode value for this AttributeValue.
     * 
     * @return measurementValueCode
     */
    public java.lang.String getMeasurementValueCode() {
        return measurementValueCode;
    }


    /**
     * Sets the measurementValueCode value for this AttributeValue.
     * 
     * @param measurementValueCode
     */
    public void setMeasurementValueCode(java.lang.String measurementValueCode) {
        this.measurementValueCode = measurementValueCode;
    }


    /**
     * Gets the children value for this AttributeValue.
     * 
     * @return children
     */
    public org.marinespecies.aphia.v1_0.AttributeValue[] getChildren() {
        return children;
    }


    /**
     * Sets the children value for this AttributeValue.
     * 
     * @param children
     */
    public void setChildren(org.marinespecies.aphia.v1_0.AttributeValue[] children) {
        this.children = children;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributeValue)) return false;
        AttributeValue other = (AttributeValue) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.measurementValueID == other.getMeasurementValueID() &&
            ((this.measurementValue==null && other.getMeasurementValue()==null) || 
             (this.measurementValue!=null &&
              this.measurementValue.equals(other.getMeasurementValue()))) &&
            ((this.measurementValueCode==null && other.getMeasurementValueCode()==null) || 
             (this.measurementValueCode!=null &&
              this.measurementValueCode.equals(other.getMeasurementValueCode()))) &&
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
        _hashCode += getMeasurementValueID();
        if (getMeasurementValue() != null) {
            _hashCode += getMeasurementValue().hashCode();
        }
        if (getMeasurementValueCode() != null) {
            _hashCode += getMeasurementValueCode().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(AttributeValue.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "AttributeValue"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measurementValueID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementValueID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measurementValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("measurementValueCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementValueCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("children");
        elemField.setXmlName(new javax.xml.namespace.QName("", "children"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "AttributeValue"));
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
