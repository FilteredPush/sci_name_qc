package org.marinespecies.aphia.v1_0;

public class AphiaNameServicePortTypeProxy implements org.marinespecies.aphia.v1_0.AphiaNameServicePortType {
  private String _endpoint = null;
  private org.marinespecies.aphia.v1_0.AphiaNameServicePortType aphiaNameServicePortType = null;
  
  public AphiaNameServicePortTypeProxy() {
    _initAphiaNameServicePortTypeProxy();
  }
  
  public AphiaNameServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initAphiaNameServicePortTypeProxy();
  }
  
  private void _initAphiaNameServicePortTypeProxy() {
    try {
      aphiaNameServicePortType = (new org.marinespecies.aphia.v1_0.AphiaNameServiceLocator()).getAphiaNameServicePort();
      if (aphiaNameServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)aphiaNameServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)aphiaNameServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (aphiaNameServicePortType != null)
      ((javax.xml.rpc.Stub)aphiaNameServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.marinespecies.aphia.v1_0.AphiaNameServicePortType getAphiaNameServicePortType() {
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType;
  }
  
  public int getAphiaID(java.lang.String scientificname, boolean marine_only) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaID(scientificname, marine_only);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecords(java.lang.String scientificname, boolean like, boolean fuzzy, boolean marine_only, int offset) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecords(scientificname, like, fuzzy, marine_only, offset);
  }
  
  public java.lang.String getAphiaNameByID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaNameByID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecordByID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByExtID(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecordByExtID(id, type);
  }
  
  public java.lang.String[] getExtIDbyAphiaID(int aphiaID, java.lang.String type) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getExtIDbyAphiaID(aphiaID, type);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[][] getAphiaRecordsByNames(java.lang.String[] scientificnames, boolean like, boolean fuzzy, boolean marine_only) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecordsByNames(scientificnames, like, fuzzy, marine_only);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByVernacular(java.lang.String vernacular, boolean like, int offset) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecordsByVernacular(vernacular, like, offset);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByDate(java.lang.String startdate, java.lang.String enddate, boolean marine_only, int offset) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaRecordsByDate(startdate, enddate, marine_only, offset);
  }
  
  public org.marinespecies.aphia.v1_0.Classification getAphiaClassificationByID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaClassificationByID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.Source[] getSourcesByAphiaID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getSourcesByAphiaID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaSynonymsByID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaSynonymsByID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.Vernacular[] getAphiaVernacularsByID(int aphiaID) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaVernacularsByID(aphiaID);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaChildrenByID(int aphiaID, int offset, boolean marine_only) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.getAphiaChildrenByID(aphiaID, offset, marine_only);
  }
  
  public org.marinespecies.aphia.v1_0.AphiaRecord[][] matchAphiaRecordsByNames(java.lang.String[] scientificnames, boolean marine_only) throws java.rmi.RemoteException{
    if (aphiaNameServicePortType == null)
      _initAphiaNameServicePortTypeProxy();
    return aphiaNameServicePortType.matchAphiaRecordsByNames(scientificnames, marine_only);
  }
  
  
}