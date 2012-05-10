/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55.deferredresponse;

import org.apache.camel.Exchange;
import org.apache.cxf.jaxws.context.WrappedMessageContext;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55DeferredResponsePortType;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AbstractAuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES;

/**
 * @author Dmytro Rud
 */
public class Iti55DeferredResponseProducer extends AbstractWsProducer<String, String> {

    public Iti55DeferredResponseProducer(AbstractWsEndpoint endpoint, JaxWsClientFactory clientFactory) {
        super(endpoint, clientFactory, String.class, String.class);
    }


    @Override
    protected void enrichRequestContext(Exchange exchange, WrappedMessageContext requestContext) {
        // NB: Camel message headers used here are set in Iti55Service's intern Callable.

        // propagate WS-Addressing request message ID
        String requestMessageId = exchange.getIn().getHeader("iti55.deferred.requestMessageId", String.class);
        if (requestMessageId != null) {
            RelatesToType relatesToHolder = new RelatesToType();
            relatesToHolder.setValue(requestMessageId);
            AddressingPropertiesImpl apropos = new AddressingPropertiesImpl();
            apropos.setRelatesTo(relatesToHolder);
            requestContext.put(CLIENT_ADDRESSING_PROPERTIES, apropos);
        }

        // inject audit dataset
        WsAuditDataset auditDataset = exchange.getIn().getHeader("iti55.deferred.auditDataset", WsAuditDataset.class);
        if (auditDataset != null) {
            requestContext.put(AbstractAuditInterceptor.DATASET_CONTEXT_KEY, auditDataset);
        }
    }


    @Override
    protected String callService(Object client, String responseString) throws Exception {
        return ((Iti55DeferredResponsePortType) client).receiveDeferredResponse(responseString);
    }
}
