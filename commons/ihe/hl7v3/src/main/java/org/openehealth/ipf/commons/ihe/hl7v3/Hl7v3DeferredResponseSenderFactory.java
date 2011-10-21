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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditResponseInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.cxf.databinding.plainxml.PlainXmlDataBinding;


/**
 * Special factory for HL7 v3 Deferred Response senders.
 * @author Dmytro Rud
 */
public class Hl7v3DeferredResponseSenderFactory extends JaxWsClientFactory {

    public Hl7v3DeferredResponseSenderFactory(
            Hl7v3WsTransactionConfiguration wsTransactionConfiguration,
            String serviceUrl,
            WsAuditStrategy auditStrategy,
            InterceptorProvider customInterceptors)
    {
        super(wsTransactionConfiguration, serviceUrl, auditStrategy, customInterceptors);
    }


    @Override
    protected void configureInterceptors(Client client) {
        super.configureInterceptors(client);
        client.getEndpoint().getService().setDataBinding(new PlainXmlDataBinding());

        if (auditStrategy != null) {
            AuditResponseInterceptor auditInterceptor =
                new AuditResponseInterceptor(auditStrategy, true, null, false);
            client.getOutInterceptors().add(auditInterceptor);
            client.getOutFaultInterceptors().add(auditInterceptor);
        }
    }
}
