/**
 * Distribution.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class Distribution  implements java.io.Serializable {
    private java.lang.String locality;

    private java.lang.String locationID;

    private java.lang.String higherGeography;

    private java.lang.String higherGeographyID;

    private java.lang.String recordStatus;

    private java.lang.String typeStatus;

    private java.lang.String establishmentMeans;

    private float decimalLatitude;

    private float decimalLongitude;

    private java.lang.String qualityStatus;

    public Distribution() {
    }

    public Distribution(
           java.lang.String locality,
           java.lang.String locationID,
           java.lang.String higherGeography,
           java.lang.String higherGeographyID,
           java.lang.String recordStatus,
           java.lang.String typeStatus,
           java.lang.String establishmentMeans,
           float decimalLatitude,
           float decimalLongitude,
           java.lang.String qualityStatus) {
           this.locality = locality;
           this.locationID = locationID;
           this.higherGeography = higherGeography;
           this.higherGeographyID = higherGeographyID;
           this.recordStatus = recordStatus;
           this.typeStatus = typeStatus;
           this.establishmentMeans = establishmentMeans;
           this.decimalLatitude = decimalLatitude;
           this.decimalLongitude = decimalLongitude;
           this.qualityStatus = qualityStatus;
    }


    /**
     * Gets the locality value for this Distribution.
     * 
     * @return locality
     */
    public java.lang.String getLocality() {
        return locality;
    }


    /**
     * Sets the locality value for this Distribution.
     * 
     * @param locality
     */
    public void setLocality(java.lang.String locality) {
        this.locality = locality;
    }


    /**
     * Gets the locationID value for this Distribution.
     * 
     * @return locationID
     */
    public java.lang.String getLocationID() {
        return locationID;
    }


    /**
     * Sets the locationID value for this Distribution.
     * 
     * @param locationID
     */
    public void setLocationID(java.lang.String locationID) {
        this.locationID = locationID;
    }


    /**
     * Gets the higherGeography value for this Distribution.
     * 
     * @return higherGeography
     */
    public java.lang.String getHigherGeography() {
        return higherGeography;
    }


    /**
     * Sets the higherGeography value for this Distribution.
     * 
     * @param higherGeography
     */
    public void setHigherGeography(java.lang.String higherGeography) {
        this.higherGeography = higherGeography;
    }


    /**
     * Gets the higherGeographyID value for this Distribution.
     * 
     * @return higherGeographyID
     */
    public java.lang.String getHigherGeographyID() {
        return higherGeographyID;
    }


    /**
     * Sets the higherGeographyID value for this Distribution.
     * 
     * @param higherGeographyID
     */
    public void setHigherGeographyID(java.lang.String higherGeographyID) {
        this.higherGeographyID = higherGeographyID;
    }


    /**
     * Gets the recordStatus value for this Distribution.
     * 
     * @return recordStatus
     */
    public java.lang.String getRecordStatus() {
        return recordStatus;
    }


    /**
     * Sets the recordStatus value for this Distribution.
     * 
     * @param recordStatus
     */
    public void setRecordStatus(java.lang.String recordStatus) {
        this.recordStatus = recordStatus;
    }


    /**
     * Gets the typeStatus value for this Distribution.
     * 
     * @return typeStatus
     */
    public java.lang.String getTypeStatus() {
        return typeStatus;
    }


    /**
     * Sets the typeStatus value for this Distribution.
     * 
     * @param typeStatus
     */
    public void setTypeStatus(java.lang.String typeStatus) {
        this.typeStatus = typeStatus;
    }


    /**
     * Gets the establishmentMeans value for this Distribution.
     * 
     * @return establishmentMeans
     */
    public java.lang.String getEstablishmentMeans() {
        return establishmentMeans;
    }


    /**
     * Sets the establishmentMeans value for this Distribution.
     * 
     * @param establishmentMeans
     */
    public void setEstablishmentMeans(java.lang.String establishmentMeans) {
        this.establishmentMeans = establishmentMeans;
    }


    /**
     * Gets the decimalLatitude value for this Distribution.
     * 
     * @return decimalLatitude
     */
    public float getDecimalLatitude() {
        return decimalLatitude;
    }


    /**
     * Sets the decimalLatitude value for this Distribution.
     * 
     * @param decimalLatitude
     */
    public void setDecimalLatitude(float decimalLatitude) {
        this.decimalLatitude = decimalLatitude;
    }


    /**
     * Gets the decimalLongitude value for this Distribution.
     * 
     * @return decimalLongitude
     */
    public float getDecimalLongitude() {
        return decimalLongitude;
    }


    /**
     * Sets the decimalLongitude value for this Distribution.
     * 
     * @param decimalLongitude
     */
    public void setDecimalLongitude(float decimalLongitude) {
        this.decimalLongitude = decimalLongitude;
    }


    /**
     * Gets the qualityStatus value for this Distribution.
     * 
     * @return qualityStatus
     */
    public java.lang.String getQualityStatus() {
        return qualityStatus;
    }


    /**
     * Sets the qualityStatus value for this Distribution.
     * 
     * @param qualityStatus
     */
    public void setQualityStatus(java.lang.String qualityStatus) {
        this.qualityStatus = qualityStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Distribution)) return false;
        Distribution other = (Distribution) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.locality==null && other.getLocality()==null) || 
             (this.locality!=null &&
              this.locality.equals(other.getLocality()))) &&
            ((this.locationID==null && other.getLocationID()==null) || 
             (this.locationID!=null &&
              this.locationID.equals(other.getLocationID()))) &&
            ((this.higherGeography==null && other.getHigherGeography()==null) || 
             (this.higherGeography!=null &&
              this.higherGeography.equals(other.getHigherGeography()))) &&
            ((this.higherGeographyID==null && other.getHigherGeographyID()==null) || 
             (this.higherGeographyID!=null &&
              this.higherGeographyID.equals(other.getHigherGeographyID()))) &&
            ((this.recordStatus==null && other.getRecordStatus()==null) || 
             (this.recordStatus!=null &&
              this.recordStatus.equals(other.getRecordStatus()))) &&
            ((this.typeStatus==null && other.getTypeStatus()==null) || 
             (this.typeStatus!=null &&
              this.typeStatus.equals(other.getTypeStatus()))) &&
            ((this.establishmentMeans==null && other.getEstablishmentMeans()==null) || 
             (this.establishmentMeans!=null &&
              this.establishmentMeans.equals(other.getEstablishmentMeans()))) &&
            this.decimalLatitude == other.getDecimalLatitude() &&
            this.decimalLongitude == other.getDecimalLongitude() &&
            ((this.qualityStatus==null && other.getQualityStatus()==null) || 
             (this.qualityStatus!=null &&
              this.qualityStatus.equals(other.getQualityStatus())));
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
        if (getLocality() != null) {
            _hashCode += getLocality().hashCode();
        }
        if (getLocationID() != null) {
            _hashCode += getLocationID().hashCode();
        }
        if (getHigherGeography() != null) {
            _hashCode += getHigherGeography().hashCode();
        }
        if (getHigherGeographyID() != null) {
            _hashCode += getHigherGeographyID().hashCode();
        }
        if (getRecordStatus() != null) {
            _hashCode += getRecordStatus().hashCode();
        }
        if (getTypeStatus() != null) {
            _hashCode += getTypeStatus().hashCode();
        }
        if (getEstablishmentMeans() != null) {
            _hashCode += getEstablishmentMeans().hashCode();
        }
        _hashCode += new Float(getDecimalLatitude()).hashCode();
        _hashCode += new Float(getDecimalLongitude()).hashCode();
        if (getQualityStatus() != null) {
            _hashCode += getQualityStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Distribution.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://aphia/v1.0", "Distribution"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locality");
        elemField.setXmlName(new javax.xml.namespace.QName("", "locality"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "locationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("higherGeography");
        elemField.setXmlName(new javax.xml.namespace.QName("", "higherGeography"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("higherGeographyID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "higherGeographyID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recordStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "recordStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("typeStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "typeStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("establishmentMeans");
        elemField.setXmlName(new javax.xml.namespace.QName("", "establishmentMeans"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("decimalLatitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "decimalLatitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("decimalLongitude");
        elemField.setXmlName(new javax.xml.namespace.QName("", "decimalLongitude"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("qualityStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "qualityStatus"));
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
