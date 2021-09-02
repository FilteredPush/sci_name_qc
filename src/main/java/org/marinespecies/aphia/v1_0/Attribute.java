/**
 * Attribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class Attribute  implements java.io.Serializable {
    private int aphiaID;

    private int measurementTypeID;

    private java.lang.String measurementType;

    private java.lang.String measurementValue;

    private int source_id;

    private java.lang.String reference;

    private java.lang.String qualitystatus;

    private int categoryID;

    private int aphiaID_Inherited;

    private org.marinespecies.aphia.v1_0.Attribute[] children;

    public Attribute() {
    }

    public Attribute(
           int aphiaID,
           int measurementTypeID,
           java.lang.String measurementType,
           java.lang.String measurementValue,
           int source_id,
           java.lang.String reference,
           java.lang.String qualitystatus,
           int categoryID,
           int aphiaID_Inherited,
           org.marinespecies.aphia.v1_0.Attribute[] children) {
           this.aphiaID = aphiaID;
           this.measurementTypeID = measurementTypeID;
           this.measurementType = measurementType;
           this.measurementValue = measurementValue;
           this.source_id = source_id;
           this.reference = reference;
           this.qualitystatus = qualitystatus;
           this.categoryID = categoryID;
           this.aphiaID_Inherited = aphiaID_Inherited;
           this.children = children;
    }


    /**
     * Gets the aphiaID value for this Attribute.
     * 
     * @return aphiaID
     */
    public int getAphiaID() {
        return aphiaID;
    }


    /**
     * Sets the aphiaID value for this Attribute.
     * 
     * @param aphiaID
     */
    public void setAphiaID(int aphiaID) {
        this.aphiaID = aphiaID;
    }


    /**
     * Gets the measurementTypeID value for this Attribute.
     * 
     * @return measurementTypeID
     */
    public int getMeasurementTypeID() {
        return measurementTypeID;
    }


    /**
     * Sets the measurementTypeID value for this Attribute.
     * 
     * @param measurementTypeID
     */
    public void setMeasurementTypeID(int measurementTypeID) {
        this.measurementTypeID = measurementTypeID;
    }


    /**
     * Gets the measurementType value for this Attribute.
     * 
     * @return measurementType
     */
    public java.lang.String getMeasurementType() {
        return measurementType;
    }


    /**
     * Sets the measurementType value for this Attribute.
     * 
     * @param measurementType
     */
    public void setMeasurementType(java.lang.String measurementType) {
        this.measurementType = measurementType;
    }


    /**
     * Gets the measurementValue value for this Attribute.
     * 
     * @return measurementValue
     */
    public java.lang.String getMeasurementValue() {
        return measurementValue;
    }


    /**
     * Sets the measurementValue value for this Attribute.
     * 
     * @param measurementValue
     */
    public void setMeasurementValue(java.lang.String measurementValue) {
        this.measurementValue = measurementValue;
    }


    /**
     * Gets the source_id value for this Attribute.
     * 
     * @return source_id
     */
    public int getSource_id() {
        return source_id;
    }


    /**
     * Sets the source_id value for this Attribute.
     * 
     * @param source_id
     */
    public void setSource_id(int source_id) {
        this.source_id = source_id;
    }


    /**
     * Gets the reference value for this Attribute.
     * 
     * @return reference
     */
    public java.lang.String getReference() {
        return reference;
    }


    /**
     * Sets the reference value for this Attribute.
     * 
     * @param reference
     */
    public void setReference(java.lang.String reference) {
        this.reference = reference;
    }


    /**
     * Gets the qualitystatus value for this Attribute.
     * 
     * @return qualitystatus
     */
    public java.lang.String getQualitystatus() {
        return qualitystatus;
    }


    /**
     * Sets the qualitystatus value for this Attribute.
     * 
     * @param qualitystatus
     */
    public void setQualitystatus(java.lang.String qualitystatus) {
        this.qualitystatus = qualitystatus;
    }


    /**
     * Gets the categoryID value for this Attribute.
     * 
     * @return categoryID
     */
    public int getCategoryID() {
        return categoryID;
    }


    /**
     * Sets the categoryID value for this Attribute.
     * 
     * @param categoryID
     */
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }


    /**
     * Gets the aphiaID_Inherited value for this Attribute.
     * 
     * @return aphiaID_Inherited
     */
    public int getAphiaID_Inherited() {
        return aphiaID_Inherited;
    }


    /**
     * Sets the aphiaID_Inherited value for this Attribute.
     * 
     * @param aphiaID_Inherited
     */
    public void setAphiaID_Inherited(int aphiaID_Inherited) {
        this.aphiaID_Inherited = aphiaID_Inherited;
    }


    /**
     * Gets the children value for this Attribute.
     * 
     * @return children
     */
    public org.marinespecies.aphia.v1_0.Attribute[] getChildren() {
        return children;
    }


    /**
     * Sets the children value for this Attribute.
     * 
     * @param children
     */
    public void setChildren(org.marinespecies.aphia.v1_0.Attribute[] children) {
        this.children = children;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Attribute)) return false;
        Attribute other = (Attribute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.aphiaID == other.getAphiaID() &&
            this.measurementTypeID == other.getMeasurementTypeID() &&
            ((this.measurementType==null && other.getMeasurementType()==null) || 
             (this.measurementType!=null &&
              this.measurementType.equals(other.getMeasurementType()))) &&
            ((this.measurementValue==null && other.getMeasurementValue()==null) || 
             (this.measurementValue!=null &&
              this.measurementValue.equals(other.getMeasurementValue()))) &&
            this.source_id == other.getSource_id() &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference()))) &&
            ((this.qualitystatus==null && other.getQualitystatus()==null) || 
             (this.qualitystatus!=null &&
              this.qualitystatus.equals(other.getQualitystatus()))) &&
            this.categoryID == other.getCategoryID() &&
            this.aphiaID_Inherited == other.getAphiaID_Inherited() &&
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
        _hashCode += getAphiaID();
        _hashCode += getMeasurementTypeID();
        if (getMeasurementType() != null) {
            _hashCode += getMeasurementType().hashCode();
        }
        if (getMeasurementValue() != null) {
            _hashCode += getMeasurementValue().hashCode();
        }
        _hashCode += getSource_id();
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        if (getQualitystatus() != null) {
            _hashCode += getQualitystatus().hashCode();
        }
        _hashCode += getCategoryID();
        _hashCode += getAphiaID_Inherited();
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
        new org.apache.axis.description.TypeDesc(Attribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "Attribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aphiaID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AphiaID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("measurementValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "measurementValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source_id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "source_id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qualitystatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qualitystatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CategoryID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aphiaID_Inherited");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AphiaID_Inherited"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("children");
        elemField.setXmlName(new javax.xml.namespace.QName("", "children"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "Attribute"));
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
