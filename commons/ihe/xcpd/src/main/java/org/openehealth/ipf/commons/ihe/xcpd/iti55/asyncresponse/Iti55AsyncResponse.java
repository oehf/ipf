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
package org.openehealth.ipf.commons.ihe.xcpd.iti55.asyncresponse;

import javax.xml.namespace.QName;

import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.xcpd.Iti55ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.xcpd.XcpdAsyncResponseServiceFactory;

/**
 * Provides access to the service factory for ITI-55 async response.
 * @author Dmytro Rud
 */
public class Iti55AsyncResponse {
    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static ItiServiceInfo ITI_55_ASYNC_RESPONSE = new ItiServiceInfo(
            new QName(NS_URI, "RespondingGateway_Response_Service", "xcpd"),
            Iti55AsyncResponsePortType.class,
            new QName(NS_URI, "RespondingGateway_Response_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti55/iti55-asyncresponse-raw.wsdl",
            true,
            false);
           
    /**
     * Returns a factory for ITI-55 async response receivers.
     * @param audit
     *          <code>true</code> to enable auditing.
     * @param allowIncompleteAudit
     *          <code>true</code> if audit entries are logged even if not all 
     *          necessary data is available. 
     * @param serviceAddress
     *          the address used to publish the service in the current context.
     * @param correlator
     *          correlator for asynchronous messages.  
     * @return the service factory.
     */
    public static ItiServiceFactory getServiceFactory(
            boolean audit, 
            boolean allowIncompleteAudit, 
            String serviceAddress,
            AsynchronyCorrelator correlator) 
    {
        return new XcpdAsyncResponseServiceFactory(
                ITI_55_ASYNC_RESPONSE,
                audit ? new Iti55ClientAuditStrategy(allowIncompleteAudit) : null,
                serviceAddress,
                correlator);
    }
}
