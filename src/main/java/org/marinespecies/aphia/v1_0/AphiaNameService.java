/**
 * AphiaNameService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public interface AphiaNameService extends javax.xml.rpc.Service {

/**
 * The data is licensed under a Creative Commons 'BY' 4.0 License,
 * see http://creativecommons.org/licenses/by/4.0/deed.en. For more information,
 * please visit http://marinespecies.org/aphia.php?p=webservice.
 */
    public java.lang.String getAphiaNameServicePortAddress();

    public org.marinespecies.aphia.v1_0.AphiaNameServicePortType getAphiaNameServicePort() throws javax.xml.rpc.ServiceException;

    public org.marinespecies.aphia.v1_0.AphiaNameServicePortType getAphiaNameServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
