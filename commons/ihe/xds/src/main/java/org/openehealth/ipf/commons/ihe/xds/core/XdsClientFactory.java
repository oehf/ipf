/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditOutRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;

/**
 * Client factory for XDS and XCA transactions.
 * @author Jens Riemschneide
 * @author Dmytro Rud
 */
public class XdsClientFactory extends JaxWsClientFactory {
    private final AsynchronyCorrelator correlator;
    
    /**
     * Constructs the factory.
     * @param wsTransactionConfiguration
     *          the info about the web-service.
     * @param serviceAddress
     *          the URL of the web-service.
     * @param auditStrategy
     *          the audit strategy to use.
     * @param correlator
     *          asynchrony correlator.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     */
    public XdsClientFactory(
            WsTransactionConfiguration wsTransactionConfiguration,
            String serviceAddress,
            WsAuditStrategy auditStrategy,
            AsynchronyCorrelator correlator,
            InterceptorProvider customInterceptors) 
    {
        super(wsTransactionConfiguration, serviceAddress, auditStrategy, customInterceptors);
        this.correlator = correlator;
    }

    
    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            if (wsTransactionConfiguration.isAuditRequestPayload()) {
                installPayloadInterceptors(client);
            }

            client.getOutInterceptors().add(new AuditOutRequestInterceptor(
                    auditStrategy, correlator, getWsTransactionConfiguration()));

            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, false, correlator, false);
            client.getInInterceptors().add(auditInterceptor);
            client.getInFaultInterceptors().add(auditInterceptor);
        }
    }
}
