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
package org.openehealth.ipf.commons.ihe.ws;

import org.apache.commons.lang3.Validate;
import javax.xml.namespace.QName;

/**
 * Contains information about a Web Service-based transaction.
 * All parameters are static, i. e. do not depend on the endpoint configuration.
 */
public class WsTransactionConfiguration {
    private final QName bindingName;
    private final Class<?> sei;
    private final QName serviceName;
    private final String wsdlLocation;
    private final boolean mtom;
    private final boolean addressing;
    private final boolean swaOutSupport;
    private final boolean auditRequestPayload;
    private final boolean supportAsynchrony;

    /**
     * Constructs the service info.
     * @param serviceName
     *          the qualified name of the service.
     * @param sei
     *          service endpoint interface.
     * @param bindingName
     *          the qualified name of the binding to use.
     * @param mtom
     *          {@code true} if this service requires MTOM.
     * @param wsdlLocation
     *          the location of the WSDL of this webservice.
     * @param addressing
     *          {@code true} if this service requires WS-Addressing.
     * @param swaOutSupport
     *          <code>true</code> if this service requires SwA for its output.
     * @param auditRequestPayload
     *          <code>true</code> if this service must save payload in audit record.
     * @param supportAsynchrony
     *      <code>true</code> if service producers should be allowed to request
     *      asynchronous responses via WS-Addressing &lt;ReplyTo&gt; header.
     *      (obviously does not make any sense when <code>addressing==false</code>).
     */
    public WsTransactionConfiguration(QName serviceName,
                                      Class<?> sei,
                                      QName bindingName,
                                      boolean mtom,
                                      String wsdlLocation,
                                      boolean addressing,
                                      boolean swaOutSupport,
                                      boolean auditRequestPayload,
                                      boolean supportAsynchrony)
    {
        Validate.notNull(serviceName, "serviceName");
        Validate.notNull(sei, "service endpoint interface");
        Validate.notNull(bindingName, "bindingName");
        Validate.notNull(wsdlLocation, "wsdlLocation");

        this.sei = sei;
        this.serviceName = serviceName;
        this.bindingName = bindingName;
        this.mtom = mtom;
        this.wsdlLocation = wsdlLocation;
        this.addressing = addressing;
        this.swaOutSupport = swaOutSupport;
        this.auditRequestPayload = auditRequestPayload;
        this.supportAsynchrony = supportAsynchrony;
    }

    /**
     * @return the qualified name of the WSDL binding to use.
     */
    public QName getBindingName() {
        return bindingName;
    }

    /**
     * @return the qualified name of the WSDL service.
     */
    public QName getServiceName() {
        return serviceName;
    }

    /**
     * @return the class of the service interface.
     */
    public Class<?> getSei() {
        return sei;
    }

    /**
     * @return {@code true} if this service requires MTOM.
     */
    public boolean isMtom() {
        return mtom;
    }

    /**
     * @return location of the WSDL document.
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

    /**
     * @return <code>true</code> if this service must save payload in audit record.
     */
    public boolean isAuditRequestPayload() {
        return auditRequestPayload;
    }

    /**
     * @return
     *      <code>true</code> if service producers sre allowed to request
     *      asynchronous responses via WS-Addressing &lt;ReplyTo&gt; header.
     */
    public boolean isSupportAsynchrony() {
        return supportAsynchrony;
    }
}
