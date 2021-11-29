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

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditOutRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.util.List;
import java.util.Map;

/**
 * Factory for ITI Web Service stubs
 */
public class JaxWsRequestClientFactory<AuditDatasetType extends WsAuditDataset> extends JaxWsClientFactory<AuditDatasetType> {

    public JaxWsRequestClientFactory(
            WsTransactionConfiguration<AuditDatasetType> wsTransactionConfiguration,
            String serviceUrl,
            AuditStrategy<AuditDatasetType> auditStrategy,
            AuditContext auditContext,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features,
            Map<String, Object> properties,
            AsynchronyCorrelator<AuditDatasetType> correlator,
            WsSecurityInformation securityInformation,
            HTTPClientPolicy httpClientPolicy) {
        super(wsTransactionConfiguration, serviceUrl, auditStrategy, auditContext,
                customInterceptors, features, properties, correlator, securityInformation, httpClientPolicy);
    }

    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            if (wsTransactionConfiguration.isAuditRequestPayload()) {
                installPayloadInterceptors(client);
            }

            client.getOutInterceptors().add(new AuditOutRequestInterceptor<>(
                    auditStrategy, auditContext, correlator, getWsTransactionConfiguration()));

            var auditInterceptor =
                    new AuditResponseInterceptor<>(auditStrategy, auditContext, false, correlator, false);
            client.getInInterceptors().add(auditInterceptor);
            client.getInFaultInterceptors().add(auditInterceptor);
        }
    }
}
