/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xcpd.iti56;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdClientFactory;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdServiceFactory;

/**
 * Provides access to the service and client factories for ITI-56.
 * @author Dmytro Rud
 */
public class Iti56 {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static ItiServiceInfo ITI_56 = new ItiServiceInfo(
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti56PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti56/iti56-raw.wsdl",
            true,
            false);


    /**
     * Returns a factory for client stubs of the ITI-56 service.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceUrl
     *          the URL of the service to use.
     * @param correlator
     *          correlator for asynchronous messages.  
     * @return the client factory.
     */
    public static ItiClientFactory getClientFactory(
            boolean audit, 
            boolean allowIncompleteAudit, 
            String serviceUrl,
            AsynchronyCorrelator correlator) 
    {
        return new XcpdClientFactory(
                ITI_56,
                audit ? new Iti56ClientAuditStrategy(allowIncompleteAudit) : null,
                serviceUrl,
                correlator);
    }


    /**
     * Returns a factory for ITI-56 services.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.  
     * @return the service factory.
     */
    public static ItiServiceFactory getServiceFactory(
            boolean audit, 
            boolean allowIncompleteAudit, 
            String serviceAddress) 
    {
        return new XcpdServiceFactory(
                ITI_56,
                audit ? new Iti56ServerAuditStrategy(allowIncompleteAudit) : null,
                serviceAddress);
    }
}
