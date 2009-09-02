/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds;

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti14PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti15PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti16PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti18PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti41PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti42PortType;
import org.openehealth.ipf.commons.ihe.xds.ports.Iti43PortType;

import javax.xml.namespace.QName;

/**
 * Contains information about a web-service (e.g. the service class). 
 * This is the static information about the web-service, i.e. all data that does
 * not depend on the dynamic endpoint configuration (e.g. if SOAP 1.1 or 1.2 is
 * used).
 * @param <T>
 *          type of the service interface.
 */
public enum ItiServiceInfo {
    /** Service info for ITI-14 */
    ITI_14(new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti14PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti14.wsdl",
            false,
            false),

    /** Service info for ITI-15 */
    ITI_15(new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Service", "ihe"),
            Iti15PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRepository_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti15.wsdl",
            false,
            true),

    /** Service info for ITI-16 */
    ITI_16(new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti16PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti16.wsdl",
            false,
            false),
    
    /** Service info for ITI-18 */
    ITI_18(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti18PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap12", "ihe"),
            false,
            "wsdl/iti18.wsdl",
            true,
            false),
    
    /** Service info for ITI-41 */
    ITI_41(new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti41PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap12", "ihe"),
            true,
            "wsdl/iti41.wsdl",
            true,
            false),
    
    /** Service info for ITI-42 */
    ITI_42(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Service", "ihe"),
            Iti42PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_Port_Soap12", "ihe"),
            false,
            "wsdl/iti42.wsdl",
            true,
            false),
    
    /** Service info for ITI-43 */
    ITI_43(new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Service", "ihe"),
            Iti43PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Binding_Soap12", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds-b:2007", "DocumentRepository_Port_Soap12", "ihe"),
            true,
            "wsdl/iti43.wsdl",
            true,
            false);

    private final QName bindingName;
    private final Class<?> serviceClass;
    private final QName serviceName;
    private final String wsdlLocation;
    private final boolean mtom;
    private final boolean addressing;
    private final QName portName11;
    private final QName portName12;
    private final boolean swaOutSupport;

    private ItiServiceInfo(QName serviceName, 
                          Class<?> serviceClass,
                          QName bindingName,
                          QName portName11,
                          QName portName12,
                          boolean mtom, 
                          String wsdlLocation, 
                          boolean addressing, 
                          boolean swaOutSupport) 
    {
        Validate.notNull(serviceName, "serviceName");
        Validate.notNull(serviceClass, "serviceClass");
        Validate.notNull(bindingName, "bindingName");
        Validate.notNull(portName11, "portName11");
        Validate.notNull(wsdlLocation, "wsdlLocation");
        
        this.serviceClass = serviceClass;
        this.serviceName = serviceName;
        this.bindingName = bindingName;
        this.portName11 = portName11;
        this.portName12 = portName12;
        this.mtom = mtom;
        this.wsdlLocation = wsdlLocation;
        this.addressing = addressing;
        this.swaOutSupport = swaOutSupport;
    }

    /**
     * @return the qualified name of the binding to use.
     */
    public QName getBindingName() {
        return bindingName;
    }

    /**
     * @return the qualified port name for SOAP 1.1.
     */
    public QName getPortName11() {
        return portName11;
    }

    /**
     * @return the qualified port name for SOAP 1.2 
     * (can be <code>null<code> for XDS.a transactions).  
     */
    public QName getPortName12() {
        return portName12;
    }

    /**
     * @return the qualified name of the service.
     */
    public QName getServiceName() {
        return serviceName;
    }

    /**
     * @return the class of the service interface.
     */
    public Class<?> getServiceClass() {
        return serviceClass;
    }

    /**
     * @return {@code true} if this service requires MTOM.
     */
    public boolean isMtom() {
        return mtom;
    }

    /**
     * @return the location of the WSDL of this webservice.
     */
    public String getWsdlLocation() {
        return wsdlLocation;
    }

    /**
     * Whether WS-Addressing should be supported.  
     * Currently affects only the client side (i.e. the Camel producer). 
     * 
     * @return {@code true} if this service requires WS-Addressing.
     */
    public boolean isAddressing() {
        return addressing;
    }

    /**
     * @return <code>true</code> if this service requires SwA for its output.
     */
    public boolean isSwaOutSupport() {
        return swaOutSupport;
    }
}
