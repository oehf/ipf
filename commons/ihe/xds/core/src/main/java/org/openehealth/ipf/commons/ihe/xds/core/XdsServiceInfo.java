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
package org.openehealth.ipf.commons.ihe.xds.core;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;

/**
 * Contains information about a web-service (e.g. the service class). 
 * This is the static information about the web-service, i.e. all data that does
 * not depend on the dynamic endpoint configuration (e.g. if SOAP 1.1 or 1.2 is
 * used).
 */
public class XdsServiceInfo extends ItiServiceInfo {
    private final boolean auditPayload;

    /**
     * Constructs the service info.
     * @param serviceName
     *          the qualified name of the service.
     * @param serviceClass
     *          the class of the service interface.
     * @param bindingName
     *          the qualified name of the binding to use.
     * @param portName11
     *          the qualified port name for SOAP 1.1.
     * @param portName12
     *          the qualified port name for SOAP 1.2
     * @param mtom
     *          {@code true} if this service requires MTOM.
     * @param wsdlLocation
     *          the location of the WSDL of this webservice.
     * @param addressing
     *          {@code true} if this service requires WS-Addressing.
     * @param swaOutSupport
     *          <code>true</code> if this service requires SwA for its output.
     * @param auditPayload
     *          <code>true</code> if this service must save payload in audit record.
     */
    public XdsServiceInfo(QName serviceName, 
                          Class<?> serviceClass,
                          QName bindingName,
                          QName portName11,
                          QName portName12,
                          boolean mtom, 
                          String wsdlLocation, 
                          boolean addressing, 
                          boolean swaOutSupport,
                          boolean auditPayload) 
    {
        super(serviceName, serviceClass, bindingName, portName11, portName12,
                mtom, wsdlLocation, addressing, swaOutSupport);
        this.auditPayload = auditPayload;
    }

    /**
     * @return <code>true</code> if this service must save payload in audit record.
     */
    public boolean isAuditPayload() {
        return auditPayload;
    }
}
