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
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.XdsAuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.cxf.XdsAuditFinalInterceptor;

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
     * @param auditStrategy
     *          the audit strategy to use.
     * @param serviceUrl
     *          the URL of the web-service.
     */
    public XdsClientFactory(XdsServiceInfo serviceInfo, XdsAuditStrategy auditStrategy, String serviceUrl) {
        super(serviceInfo, serviceUrl);
        this.auditStrategy = auditStrategy;
    }


    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        
        // install auditing-related interceptors if the user has not switched
        // auditing off
        if (auditStrategy != null) {
            client.getOutInterceptors().add(new XdsAuditDatasetEnrichmentInterceptor(auditStrategy, false));
            XdsAuditFinalInterceptor finalInterceptor = new XdsAuditFinalInterceptor(auditStrategy, false);
            client.getInInterceptors().add(finalInterceptor);
            client.getInFaultInterceptors().add(finalInterceptor);

            // install payload collecting interceptors  
            if(((XdsServiceInfo) serviceInfo).isAuditPayload()) {
                installPayloadInterceptors(client);
            }
        }
    }
}
