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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditOutRequestInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.databinding.plainxml.PlainXmlDataBinding;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InNamespaceMergeInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadInjectorInterceptor;
import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * Factory for HL7 v3 Web Service clients.
 * @author Dmytro Rud
 */
public class Hl7v3ClientFactory extends JaxWsClientFactory {
    private final AsynchronyCorrelator correlator;

    /**
     * Constructs the factory.
     * @param wsTransactionConfiguration
     *          the info about the Web Service.
     * @param serviceUrl
     *          the URL of the Web Service.
     * @param auditStrategy
     *          the audit strategy to use.
     * @param correlator
     *          optional asynchrony correlator.
     * @param customInterceptors
     *          user-defined custom CXF interceptors.
     */
    public Hl7v3ClientFactory(
            Hl7v3WsTransactionConfiguration wsTransactionConfiguration,
            String serviceUrl,
            WsAuditStrategy auditStrategy,
            AsynchronyCorrelator correlator,
            InterceptorProvider customInterceptors)
    {
        super(wsTransactionConfiguration, serviceUrl, auditStrategy, customInterceptors);
        this.correlator = correlator;
    }

    
    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        client.getInInterceptors().add(new InPayloadExtractorInterceptor(SOAP_BODY));
        client.getInInterceptors().add(new InNamespaceMergeInterceptor());
        client.getInInterceptors().add(new InPayloadInjectorInterceptor(0));
        client.getEndpoint().getService().setDataBinding(new PlainXmlDataBinding());

        // install auditing-related interceptors if the user has not switched auditing off
        if (auditStrategy != null) {
            client.getOutInterceptors().add(new AuditOutRequestInterceptor(
                    auditStrategy, correlator, getWsTransactionConfiguration()));

            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, false, correlator, false);
            client.getInInterceptors().add(auditInterceptor);
            client.getInFaultInterceptors().add(auditInterceptor);
        }
    }
}
