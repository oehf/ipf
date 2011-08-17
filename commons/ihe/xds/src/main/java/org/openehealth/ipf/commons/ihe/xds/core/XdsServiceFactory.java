/*
 * Copyright 2011 the original author or authors.
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

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * Service factory for XDS and XCA transactions.
 * @author Jens Riemschneide
 * @author Dmytro Rud
 */
public class XdsServiceFactory extends ItiServiceFactory {

    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the service to produce.
     * @param auditStrategy
     *          the auditing strategy to use.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     * @param rejectionHandlingStrategy
     *          user-defined rejection handling strategy.
     */
    public XdsServiceFactory(
            ItiServiceInfo serviceInfo,
            XdsAuditStrategy auditStrategy,
            String serviceAddress,
            InterceptorProvider customInterceptors,
            WsRejectionHandlingStrategy rejectionHandlingStrategy)
    {
        super(serviceInfo, serviceAddress, auditStrategy,
                customInterceptors, rejectionHandlingStrategy);
    }

    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            if (serviceInfo.isAuditRequestPayload()) {
                svrFactory.getInInterceptors().add(new InPayloadExtractorInterceptor(SOAP_BODY));
            }

            svrFactory.getInInterceptors().add(new AuditInRequestInterceptor(
                    auditStrategy, getServiceInfo()));

            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, true, null, false);
            svrFactory.getOutInterceptors().add(auditInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditInterceptor);
        }
    }
}
