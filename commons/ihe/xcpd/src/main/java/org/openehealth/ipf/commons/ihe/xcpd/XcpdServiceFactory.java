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
package org.openehealth.ipf.commons.ihe.xcpd;

import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.cxf.WsRejectionHandlingStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;

/**
 * Service factory for XCPD.
 * @author Dmytro Rud
 */
public class XcpdServiceFactory extends Hl7v3ServiceFactory {
    private final WsAuditStrategy auditStrategy;

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
    public XcpdServiceFactory(
            Hl7v3ServiceInfo serviceInfo,
            WsAuditStrategy auditStrategy,
            String serviceAddress,
            InterceptorProvider customInterceptors,
            WsRejectionHandlingStrategy rejectionHandlingStrategy)
    {
        super(serviceInfo, serviceAddress, customInterceptors, rejectionHandlingStrategy);
        this.auditStrategy = auditStrategy;
    }

    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            svrFactory.getInInterceptors().add(new AuditInRequestInterceptor(
                    auditStrategy, getServiceInfo()));
            
            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, true, null, false);
            svrFactory.getOutInterceptors().add(auditInterceptor);
            svrFactory.getOutFaultInterceptors().add(auditInterceptor);
        }
    }

}
