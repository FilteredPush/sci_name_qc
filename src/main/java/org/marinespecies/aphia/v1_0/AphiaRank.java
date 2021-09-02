/**
 * AphiaRank.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class AphiaRank  implements java.io.Serializable {
    private int taxonRankID;

    private java.lang.String taxonRank;

    private int aphiaID;

    private java.lang.String kingdom;

    public AphiaRank() {
    }

    public AphiaRank(
           int taxonRankID,
           java.lang.String taxonRank,
           int aphiaID,
           java.lang.String kingdom) {
           this.taxonRankID = taxonRankID;
           this.taxonRank = taxonRank;
           this.aphiaID = aphiaID;
           this.kingdom = kingdom;
    }


    /**
     * Gets the taxonRankID value for this AphiaRank.
     * 
     * @return taxonRankID
     */
    public int getTaxonRankID() {
        return taxonRankID;
    }


    /**
     * Sets the taxonRankID value for this AphiaRank.
     * 
     * @param taxonRankID
     */
    public void setTaxonRankID(int taxonRankID) {
        this.taxonRankID = taxonRankID;
    }


    /**
     * Gets the taxonRank value for this AphiaRank.
     * 
     * @return taxonRank
     */
    public java.lang.String getTaxonRank() {
        return taxonRank;
    }


    /**
     * Sets the taxonRank value for this AphiaRank.
     * 
     * @param taxonRank
     */
    public void setTaxonRank(java.lang.String taxonRank) {
        this.taxonRank = taxonRank;
    }


    /**
     * Gets the aphiaID value for this AphiaRank.
     * 
     * @return aphiaID
     */
    public int getAphiaID() {
        return aphiaID;
    }


    /**
     * Sets the aphiaID value for this AphiaRank.
     * 
     * @param aphiaID
     */
    public void setAphiaID(int aphiaID) {
        this.aphiaID = aphiaID;
    }


    /**
     * Gets the kingdom value for this AphiaRank.
     * 
     * @return kingdom
     */
    public java.lang.String getKingdom() {
        return kingdom;
    }


    /**
     * Sets the kingdom value for this AphiaRank.
     * 
     * @param kingdom
     */
    public void setKingdom(java.lang.String kingdom) {
        this.kingdom = kingdom;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AphiaRank)) return false;
        AphiaRank other = (AphiaRank) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.taxonRankID == other.getTaxonRankID() &&
            ((this.taxonRank==null && other.getTaxonRank()==null) || 
             (this.taxonRank!=null &&
              this.taxonRank.equals(other.getTaxonRank()))) &&
            this.aphiaID == other.getAphiaID() &&
            ((this.kingdom==null && other.getKingdom()==null) || 
             (this.kingdom!=null &&
              this.kingdom.equals(other.getKingdom())));
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
        _hashCode += getTaxonRankID();
        if (getTaxonRank() != null) {
            _hashCode += getTaxonRank().hashCode();
        }
        _hashCode += getAphiaID();
        if (getKingdom() != null) {
            _hashCode += getKingdom().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AphiaRank.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRank"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxonRankID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taxonRankID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxonRank");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taxonRank"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("aphiaID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AphiaID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kingdom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "kingdom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
