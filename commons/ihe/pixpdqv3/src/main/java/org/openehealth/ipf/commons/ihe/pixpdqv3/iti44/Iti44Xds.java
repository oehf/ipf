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
package org.openehealth.ipf.commons.ihe.pixpdqv3.iti44;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.pixpdqv3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.pixpdqv3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;

/**
 * Provides access to the service and client factories for ITI-44 (XDS-b).
 * @author Dmytro Rud
 */
public class Iti44Xds {
    private static final String NS_URI = "urn:ihe:iti:xds-b:2007";
    private final static ItiServiceInfo ITI_44 = new ItiServiceInfo(
            new QName(NS_URI, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-xds-raw.wsdl",
            false,
            false);
           
    /**
     * Returns a factory for client stubs of the ITI-44 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @return the client factory.
     */
    public static ItiClientFactory getClientFactory(boolean audit, boolean allowIncompleteAudit, String serviceUrl) {
        return new Hl7v3ClientFactory(ITI_44, false, serviceUrl);
    }

    /**
     * Returns a factory for ITI-44 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static ItiServiceFactory getServiceFactory(boolean audit, boolean allowIncompleteAudit, String serviceAddress) {
        return new Hl7v3ServiceFactory(ITI_44, serviceAddress);
    }
}
