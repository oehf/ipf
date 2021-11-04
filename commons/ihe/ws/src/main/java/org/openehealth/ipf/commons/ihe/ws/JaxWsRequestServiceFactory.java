/*
 * Copyright 2017 the original author or authors.
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

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * Factory for Web Services serving requests
 */
public class JaxWsRequestServiceFactory<AuditDatasetType extends WsAuditDataset> extends JaxWsServiceFactory<AuditDatasetType> {

    public JaxWsRequestServiceFactory(
            WsTransactionConfiguration<AuditDatasetType> wsTransactionConfiguration,
            String serviceAddress,
            AuditStrategy<AuditDatasetType> auditStrategy,
            AuditContext auditContext,
            InterceptorProvider customInterceptors,
            WsRejectionHandlingStrategy rejectionHandlingStrategy)
    {
        super(wsTransactionConfiguration, serviceAddress, auditStrategy, auditContext, customInterceptors, rejectionHandlingStrategy);
    }

    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            if (wsTransactionConfiguration.isAuditRequestPayload()) {
                svrFactory.getInInterceptors().add(new InPayloadExtractorInterceptor(SOAP_BODY));
            }

            svrFactory.getInInterceptors().add(new AuditInRequestInterceptor<>(
                    auditStrategy, auditContext, wsTransactionConfiguration));

            var auditInterceptor =
                    new AuditResponseInterceptor<>(auditStrategy, auditContext, true, null, false);
            svrFactory.getOutInterceptors().add(auditInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditInterceptor);
        }
    }

}
