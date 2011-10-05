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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.apache.commons.lang3.Validate;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;

/**
 * Service factory for receivers of asynchronous XCPD responses.
 * @author Dmytro Rud
 */
public class Hl7v3AsyncResponseServiceFactory extends Hl7v3ServiceFactory {
    private final AsynchronyCorrelator correlator;

    /**
     * Constructs the factory.
     * @param wsTransactionConfiguration
     *          the info about the service to produce.
     * @param auditStrategy
     *          the auditing strategy to use.
     * @param serviceAddress
     *          the address of the service that it should be published with.
     * @param correlator
     *          correlator for asynchronous interactions.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     */
    public Hl7v3AsyncResponseServiceFactory(
            Hl7v3WsTransactionConfiguration wsTransactionConfiguration,
            String serviceAddress,
            Hl7v3AuditStrategy auditStrategy,
            AsynchronyCorrelator correlator,
            InterceptorProvider customInterceptors)
    {
        super(wsTransactionConfiguration, serviceAddress, auditStrategy, customInterceptors, null);
        
        Validate.notNull(correlator);
        this.correlator = correlator;
    }

    
    @Override
    protected void configureInterceptors(ServerFactoryBean svrFactory) {
        super.configureInterceptors(svrFactory);

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, false, correlator, true);
            svrFactory.getInInterceptors().add(auditInterceptor);
            svrFactory.getInFaultInterceptors().add(auditInterceptor);
        }
    }

}
