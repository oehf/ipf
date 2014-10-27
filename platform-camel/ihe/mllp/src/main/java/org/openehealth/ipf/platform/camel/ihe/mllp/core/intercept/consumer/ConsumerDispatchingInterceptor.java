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

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.StartupListener;
import org.apache.camel.component.mina2.Mina2Consumer;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IdentityHashMap;

import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.splitPreserveAllTokens;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

/**
 * Interceptor which dispatches an incoming request message to another MLLP route.
 * @author Dmytro Rud
 */
public class ConsumerDispatchingInterceptor extends AbstractMllpInterceptor implements StartupListener {
    private static final transient Logger LOG = LoggerFactory.getLogger(ConsumerDispatchingInterceptor.class);

    private final String[] routeIds;
    private IdentityHashMap<Hl7v2Interceptor, String> map;


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
        map = new IdentityHashMap<Hl7v2Interceptor, String>(routeIds.length);
        for (String routeId : routeIds) {
            Mina2Consumer consumer = (Mina2Consumer) camelContext.getRoute(routeId).getConsumer();
            Hl7v2Interceptor interceptor = (Hl7v2Interceptor) consumer.getProcessor();
            while (! ConsumerMarshalInterceptor.class.getName().equals(interceptor.getId())) {
                interceptor = (Hl7v2Interceptor) interceptor.getWrappedProcessor();
            }

            map.put(interceptor, routeId);
        }
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        String messageType = null;
        String triggerEvent = null;
        String messageStructure = null;
        String version = null;

        // determine attributes of the message
        String message = exchange.getIn().getBody(String.class);
        String[] segments = split(message, '\r');
        if (segments.length > 0) {
            String msh = segments[0];
            if (msh.length() > 10) {
                String[] mshFields = splitPreserveAllTokens(msh, msh.charAt(3));
                if (mshFields.length > 11) {
                    String[] msh9Components = split(mshFields[8], msh.charAt(4));
                    if (msh9Components.length > 1) {
                        messageType = msh9Components[0];
                        triggerEvent = msh9Components[1];
                    }
                    if (msh9Components.length > 2) {
                        messageStructure = msh9Components[2];
                    }
                    version = mshFields[11];
                }
            }
        }

        // check who can accept the message
        boolean found = false;
        for (Hl7v2Interceptor interceptor : map.keySet()) {
            Hl7v2TransactionConfiguration config = interceptor.getConfigurationHolder().getHl7v2TransactionConfiguration();
            try {
                config.checkMessageAcceptance(messageType, triggerEvent, messageStructure, version, true);

                LOG.debug("Dispatch message with MSH-9-1='{}', MSH-9-2='{}', MSH-9-3='{}', MSH-12='{}' to route '{}'",
                        messageType, triggerEvent, messageStructure, version, map.get(interceptor));
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
            Hl7v2AcceptanceException exception = new Hl7v2AcceptanceException("Unsupported message type and/or version", 207);
            resultMessage(exchange).setBody(getNakFactory().createDefaultNak(exception).encode());
        }
    }

}
