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

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.async.InPartialResponseHackInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.xcpd.cxf.XcpdAuditInterceptor;
import org.openehealth.ipf.commons.ihe.xcpd.cxf.XcpdProducerAuditInterceptor;

/**
 * Client factory for XCPD transactions.
 * @author Dmytro Rud
 */
public class XcpdClientFactory extends Hl7v3ClientFactory {
    private final WsAuditStrategy auditStrategy;
    private final AsynchronyCorrelator correlator;
    
    /**
     * Constructs the factory.
     * @param serviceInfo
     *          the info about the web-service.
     * @param auditStrategy
     *          the audit strategy to use.
     * @param serviceAddress
     *          the URL of the web-service.
     * @param correlator
     *          asynchrony correlator.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     */
    public XcpdClientFactory(
            Hl7v3ServiceInfo serviceInfo,
            WsAuditStrategy auditStrategy, 
            String serviceAddress,
            AsynchronyCorrelator correlator,
            InterceptorProvider customInterceptors) 
    {
        super(serviceInfo, serviceAddress, customInterceptors);
        this.auditStrategy = auditStrategy;
        this.correlator = correlator;
    }

    
    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        client.getInInterceptors().add(new InPartialResponseHackInterceptor());

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            client.getOutInterceptors().add(new XcpdProducerAuditInterceptor(auditStrategy, correlator));
            
            XcpdAuditInterceptor auditInterceptor = 
                new XcpdAuditInterceptor(auditStrategy, false, correlator, false);
            client.getInInterceptors().add(auditInterceptor);
            client.getInFaultInterceptors().add(auditInterceptor);
        }
    }
}
