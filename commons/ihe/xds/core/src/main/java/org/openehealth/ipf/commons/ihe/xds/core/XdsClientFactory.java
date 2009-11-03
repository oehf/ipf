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

import org.apache.cxf.endpoint.Client;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.xds.core.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.AuditFinalInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;

/**
 * Factory for XDS web-service stubs.
 * @author Jens Riemschneider
 */
public class XdsClientFactory extends ItiClientFactory {
    private final XdsAuditStrategy auditStrategy;

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the web-service.
     * @param soap11
     *          whether SOAP 1.1 should be used instead of SOAP 1.2 for XDS.b 
     *          transactions. Does not have any meaning for XDS.a transactions.
     * @param auditStrategy
     *          the audit strategy to use.
     * @param serviceUrl
     *          the URL of the web-service.
     */
    public XdsClientFactory(XdsServiceInfo serviceInfo, boolean soap11, XdsAuditStrategy auditStrategy, String serviceUrl) {
        super(serviceInfo, soap11, serviceUrl);
        this.auditStrategy = auditStrategy;
    }


    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        
        // install auditing-related interceptors if the user has not switched
        // auditing off
        if (auditStrategy != null) {
            client.getOutInterceptors().add(new AuditDatasetEnrichmentInterceptor(auditStrategy, false));
            AuditFinalInterceptor finalInterceptor = new AuditFinalInterceptor(auditStrategy, false);
            client.getInInterceptors().add(finalInterceptor);
            client.getInFaultInterceptors().add(finalInterceptor);

            // install payload collecting interceptors  
            if(((XdsServiceInfo) serviceInfo).isAuditPayload()) {
                installPayloadInterceptors(client);
            }
        }
    }
}
