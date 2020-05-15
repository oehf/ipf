/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.ws;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * Factory for Web Services serving asynchronous responses
 */
public class JaxWsAsyncResponseServiceFactory<AuditDatasetType extends WsAuditDataset> extends JaxWsServiceFactory<AuditDatasetType> {

    protected final AsynchronyCorrelator<AuditDatasetType> correlator;

    /**
     * Constructs the factory.
     * @param wsTransactionConfiguration
     *          the info about the service to produce.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     * @param auditStrategy
     *          server-side ATNA audit strategy.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     * @param correlator
     *          correlator for asynchronous interactions.
     */
    public JaxWsAsyncResponseServiceFactory(
            WsTransactionConfiguration<AuditDatasetType> wsTransactionConfiguration,
            String serviceAddress,
            AuditStrategy<AuditDatasetType> auditStrategy,
            AuditContext auditContext,
            InterceptorProvider customInterceptors,
            AsynchronyCorrelator<AuditDatasetType> correlator)
    {
        super(wsTransactionConfiguration, serviceAddress, auditStrategy, auditContext,
                customInterceptors, null);

        requireNonNull(correlator, "Correlator for asynchronous processing must be set.");
        this.correlator = correlator;
    }

    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            if (wsTransactionConfiguration.isAuditRequestPayload()) {
                svrFactory.getInInterceptors().add(new InPayloadExtractorInterceptor(SOAP_BODY));
            }

            var auditInterceptor =
                    new AuditResponseInterceptor<>(auditStrategy, auditContext, false, correlator, true);
            svrFactory.getInInterceptors().add(auditInterceptor);
            svrFactory.getInFaultInterceptors().add(auditInterceptor);
        }
    }
}
