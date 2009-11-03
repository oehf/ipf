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

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.xds.core.XdsClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsServiceFactory;
import org.openehealth.ipf.commons.ihe.xds.core.XdsServiceInfo;

/**
 * Provides access to the service and client factories for ITI-16.
 * @author Jens Riemschneider
 */
public class Iti16 {
    private final static XdsServiceInfo ITI_16 = new XdsServiceInfo(
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Service", "ihe"),
            Iti16PortType.class,
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Binding_Soap11", "ihe"),
            new QName("urn:ihe:iti:xds:2007", "DocumentRegistry_Port_Soap11", "ihe"),
            null,
            false,
            "wsdl/iti16.wsdl",
            false,
            false,
            true);
           
    /**
     * Returns a factory for client stubs of the ITI-16 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @return the client factory.
     */
    public static XdsClientFactory getClientFactory(boolean audit, boolean allowIncompleteAudit, String serviceUrl) {        
        return new XdsClientFactory(ITI_16, false, audit ? new Iti16ClientAuditStrategy(allowIncompleteAudit) : null, serviceUrl);
    }

    /**
     * Returns a factory for ITI-16 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static XdsServiceFactory getServiceFactory(boolean audit, boolean allowIncompleteAudit, String serviceAddress) {
        return new XdsServiceFactory(ITI_16, audit ? new Iti16ServerAuditStrategy(allowIncompleteAudit) : null, serviceAddress);
    }
}
