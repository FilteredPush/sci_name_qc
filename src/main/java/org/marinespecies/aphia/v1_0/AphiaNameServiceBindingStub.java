/**
 * AphiaNameServiceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.marinespecies.aphia.v1_0;

public class AphiaNameServiceBindingStub extends org.apache.axis.client.Stub implements org.marinespecies.aphia.v1_0.AphiaNameServicePortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[15];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scientificname"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        oper.setReturnClass(int.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecords");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scientificname"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "like"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fuzzy"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaNameByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecordByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecord"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecordByExtID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "id"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "type"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecord"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getExtIDbyAphiaID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "type"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "externalidentifiers"));
        oper.setReturnClass(java.lang.String[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecordsByNames");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scientificnames"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://aphia/v1.0", "scientificnames"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "like"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "fuzzy"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaMatches"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[][].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecordsByVernacular");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "vernacular"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "like"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaRecordsByDate");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "startdate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "enddate"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaClassificationByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "Classification"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.Classification.class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getSourcesByAphiaID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "Sources"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.Source[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaSynonymsByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaVernacularsByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "Vernaculars"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.Vernacular[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getAphiaChildrenByID");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "AphiaID"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "offset"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("matchAphiaRecordsByNames");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "scientificnames"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://aphia/v1.0", "scientificnames"), java.lang.String[].class, false, false);
        oper.addParameter(param);
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("", "marine_only"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"), boolean.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaMatches"));
        oper.setReturnClass(org.marinespecies.aphia.v1_0.AphiaRecord[][].class);
        oper.setReturnQName(new javax.xml.namespace.QName("", "return"));
        oper.setStyle(org.apache.axis.constants.Style.RPC);
        oper.setUse(org.apache.axis.constants.Use.ENCODED);
        _operations[14] = oper;

    }

    public AphiaNameServiceBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public AphiaNameServiceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public AphiaNameServiceBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaMatches");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.AphiaRecord[][].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecord");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.AphiaRecord.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecords");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.AphiaRecord[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "AphiaRecord");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Classification");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.Classification.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "externalidentifiers");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "scientificnames");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Source");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.Source.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Sources");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.Source[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Source");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Vernacular");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.Vernacular.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Vernaculars");
            cachedSerQNames.add(qName);
            cls = org.marinespecies.aphia.v1_0.Vernacular[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://aphia/v1.0", "Vernacular");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
                    _call.setEncodingStyle(org.apache.axis.Constants.URI_SOAP11_ENC);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public int getAphiaID(java.lang.String scientificname, boolean marine_only) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaID", "getAphiaID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {scientificname, new java.lang.Boolean(marine_only)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return ((java.lang.Integer) _resp).intValue();
            } catch (java.lang.Exception _exception) {
                return ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_resp, int.class)).intValue();
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecords(java.lang.String scientificname, boolean like, boolean fuzzy, boolean marine_only, int offset) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecords");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecords", "getAphiaRecords"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {scientificname, new java.lang.Boolean(like), new java.lang.Boolean(fuzzy), new java.lang.Boolean(marine_only), new java.lang.Integer(offset)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String getAphiaNameByID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaNameByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaName", "getAphiaNameByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecordByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecord", "getAphiaRecordByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord getAphiaRecordByExtID(java.lang.String id, java.lang.String type) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecordByExtID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecord", "getAphiaRecordByExtID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {id, type});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String[] getExtIDbyAphiaID(int aphiaID, java.lang.String type) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getExtIDbyAphiaID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/externalidentifiers", "getExtIDbyAphiaID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID), type});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String[]) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[][] getAphiaRecordsByNames(java.lang.String[] scientificnames, boolean like, boolean fuzzy, boolean marine_only) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecordsByNames");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaMatches", "getAphiaRecordsByNames"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {scientificnames, new java.lang.Boolean(like), new java.lang.Boolean(fuzzy), new java.lang.Boolean(marine_only)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[][]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[][]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[][].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByVernacular(java.lang.String vernacular, boolean like, int offset) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecordsByVernacular");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecords", "getAphiaRecordsByVernacular"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {vernacular, new java.lang.Boolean(like), new java.lang.Integer(offset)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaRecordsByDate(java.lang.String startdate, java.lang.String enddate, boolean marine_only, int offset) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaRecordsByDate");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecords", "getAphiaRecordsByDate"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {startdate, enddate, new java.lang.Boolean(marine_only), new java.lang.Integer(offset)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.Classification getAphiaClassificationByID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaClassificationByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/Classification", "getAphiaClassificationByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.Classification) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.Classification) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.Classification.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.Source[] getSourcesByAphiaID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getSourcesByAphiaID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/Sources", "getSourcesByAphiaID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.Source[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.Source[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.Source[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaSynonymsByID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaSynonymsByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecords", "getAphiaSynonymsByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.Vernacular[] getAphiaVernacularsByID(int aphiaID) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaVernacularsByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaVernaculars", "getAphiaVernacularsByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.Vernacular[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.Vernacular[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.Vernacular[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[] getAphiaChildrenByID(int aphiaID, int offset, boolean marine_only) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("getAphiaChildrenByID");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaRecords", "getAphiaChildrenByID"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(aphiaID), new java.lang.Integer(offset), new java.lang.Boolean(marine_only)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public org.marinespecies.aphia.v1_0.AphiaRecord[][] matchAphiaRecordsByNames(java.lang.String[] scientificnames, boolean marine_only) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("matchAphiaRecordsByNames");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("http://aphia/v1.0/AphiaMatches", "matchAphiaRecordsByNames"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {scientificnames, new java.lang.Boolean(marine_only)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[][]) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.marinespecies.aphia.v1_0.AphiaRecord[][]) org.apache.axis.utils.JavaUtils.convert(_resp, org.marinespecies.aphia.v1_0.AphiaRecord[][].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
