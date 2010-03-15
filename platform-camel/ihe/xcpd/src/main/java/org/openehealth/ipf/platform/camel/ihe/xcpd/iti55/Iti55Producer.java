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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import java.util.Map;

import javax.xml.datatype.Duration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3CorrelationUtils;
import org.openehealth.ipf.commons.ihe.ws.ItiClientFactory;
import org.openehealth.ipf.commons.ihe.xcpd.iti55.Iti55PortType;
import org.openehealth.ipf.platform.camel.ihe.ws.async.AsynchronousItiProducer;

/**
 * The producer implementation for the ITI-45 component.
 * @author Dmytro Rud
 */
public class Iti55Producer extends AsynchronousItiProducer {
    
    /**
     * Constructs the producer.
     * @param endpoint
     *          the endpoint creating this producer.
     * @param clientFactory
     *          the factory for clients to produce messages for the service.              
     */
    public Iti55Producer(Iti55Endpoint endpoint, ItiClientFactory clientFactory) {
        super(endpoint, clientFactory);
    }
    

    @Override
    protected void enrichRequestExchange(Exchange exchange, Map<String, Object> requestContext) {
        // add TTL header
        Duration dura = exchange.getIn().getHeader(
                Iti55Component.XCPD_OUTPUT_TTL_HEADER_NAME, Duration.class);
        TtlHeaderUtils.addTtlHeader(dura, requestContext);
    }

    
    @Override
    protected String call(Object client, String body) {
        return ((Iti55PortType) client).respondingGatewayPRPAIN201305UV02(body);
    }


    @Override
    protected void enrichResponseMessage(Message message, Map<String, Object> responseContext) {
        Duration dura = TtlHeaderUtils.retrieveTtlHeader(responseContext);
        if (dura != null) {
            message.setHeader(Iti55Component.XCPD_INPUT_TTL_HEADER_NAME, dura);
        }
    }


    @Override
    protected String getMessageIdForCorrelation(String body) {
        return Hl7v3CorrelationUtils.getHl7v3MessageId(body, true);
    }
    
    
    @Override
    protected void cleanRequestContext(Map<String, Object> requestContext) {
        super.cleanRequestContext(requestContext);
        TtlHeaderUtils.removeTtlHeader(requestContext);
    }
    

    /*
    @Override
    protected void callService(Exchange exchange) {
        // prepare
        String body = exchange.getIn().getBody(String.class);
        Iti55PortType client = (Iti55PortType) getClient();
        configureClient(client);
        BindingProvider bindingProvider = (BindingProvider) client;
        Map<String, Object> requestContext = bindingProvider.getRequestContext();
        cleanRequestContext(requestContext);

        
        // get and analyse WS-Addressing asynchrony configuration
        String replyToUri = exchange.getIn().getHeader(
                Iti55Component.XCPD_WSA_REPLYTO_HEADER_NAME, String.class);
        boolean async = (replyToUri != null) && (! replyToUri.isEmpty());

        // for asynchronous interaction: prepare WSA headers
        if (async) {
            setWSAReplyToHeader(replyToUri, requestContext);
            
            AsynchronousItiEndpoint endpoint = (AsynchronousItiEndpoint) getEndpoint();
            String correlationKey = exchange.getIn().getHeader(
                    Iti55Component.XCPD_CORRELATION_KEY_HEADER_NAME, String.class);
            endpoint.getCorrelator().put(
                    Hl7v3CorrelationUtils.getHl7v3MessageId(body, true), 
                    endpoint.getEndpointUri(),
                    correlationKey);
        }
        
        // invoke
        exchange.setPattern(async ? ExchangePattern.InOnly : ExchangePattern.InOut);
        String result = client.respondingGatewayPRPAIN201305UV02(body);
        
        // for synchronous interaction: handle response
        if (! async) {
            Message resultMessage = Exchanges.resultMessage(exchange);
            dura = TtlHeaderUtils.retrieveTtlHeader(bindingProvider.getResponseContext());
            if (dura != null) {
                resultMessage.setHeader(Iti55Component.XCPD_INPUT_TTL_HEADER_NAME, dura);
            }
            resultMessage.setBody(result);
        } 
    }
*/

}
