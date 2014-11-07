/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.preparser.PreParser;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.StartupListener;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpDispatchEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.splitPreserveAllTokens;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

/**
 * Interceptor which dispatches an incoming request message to another MLLP route.
 * @author Dmytro Rud
 */
public class ConsumerDispatchingInterceptor extends AbstractMllpInterceptor<MllpDispatchEndpoint> implements StartupListener {
    private static final transient Logger LOG = LoggerFactory.getLogger(ConsumerDispatchingInterceptor.class);

    private final String[] routeIds;
    private Map<String, Hl7v2Interceptor> map;


    /**
     * Constructs a dispatching interceptor.
     * @param camelContext
     *      Camel context.
     * @param routeIds
     *      IDs of routes containing target IPF MLLP endpoints.
     */
    public ConsumerDispatchingInterceptor(CamelContext camelContext, String[] routeIds) {
        this.routeIds = routeIds;

        try {
            camelContext.addStartupListener(this);
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }


    @Override
    public void onCamelContextStarted(CamelContext camelContext, boolean alreadyStarted) throws Exception {
        map = new HashMap<>(routeIds.length);
        for (String routeId : routeIds) {
            try {
                Mina2Consumer consumer = (Mina2Consumer) camelContext.getRoute(routeId).getConsumer();
                Hl7v2Interceptor interceptor = (Hl7v2Interceptor) consumer.getProcessor();
                while (! (interceptor instanceof ConsumerStringProcessingInterceptor)) {
                    interceptor = (Hl7v2Interceptor) interceptor.getWrappedProcessor();
                }

                map.put(routeId, (Hl7v2Interceptor) interceptor.getWrappedProcessor());
            } catch (NullPointerException e) {
                throw new Exception("Route with ID='" + routeId + "' not found or is not an IPF MLLP route", e);
            } catch (ClassCastException e) {
                throw new Exception("Route with ID='" + routeId + "' is not an IPF MLLP route", e);
            }
        }
    }


    @Override
    public void process(Exchange exchange) throws Exception {

        // determine attributes of the message
        String message = exchange.getIn().getBody(String.class);
        String[] fields = PreParser.getFields(message, "MSH-9-1", "MSH-9-2", "MSH-9-3", "MSH-12");
        String messageType = fields[0];
        String triggerEvent = fields[1];
        String messageStructure = fields[2];
        String version = fields[3];

        // check who can accept the message
        boolean found = false;
        for (String routeId : routeIds) {
            Hl7v2Interceptor interceptor = map.get(routeId);
            Hl7v2TransactionConfiguration config = interceptor.getConfigurationHolder().getHl7v2TransactionConfiguration();
            try {
                config.checkMessageAcceptance(messageType, triggerEvent, messageStructure, version, true);

                LOG.debug("Dispatch message with MSH-9-1='{}', MSH-9-2='{}', MSH-9-3='{}', MSH-12='{}' to route '{}'",
                        messageType, triggerEvent, messageStructure, version, routeId);
                found = true;
                interceptor.process(exchange);
                break;
            } catch (Hl7v2AcceptanceException e) {
                // no problem
            }
        }

        if (! found) {
            LOG.debug("Nobody can process message with MSH-9-1='{}', MSH-9-2='{}', MSH-9-3='{}', MSH-12='{}'",
                    messageType, triggerEvent, messageStructure, version);
            HL7v2Exception exception = new HL7v2Exception(new HL7Exception(
                    "Unsupported message type and/or version", ErrorCode.APPLICATION_INTERNAL_ERROR));
            resultMessage(exchange).setBody(getNakFactory().createDefaultNak(exception).encode());
        }
    }

}
