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
import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.StartupListener;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interceptor which dispatches an incoming request message to another MLLP route.
 * <p>
 *
 * @author Dmytro Rud
 */
public final class ConsumerDispatchingInterceptor extends InterceptorSupport
        implements StartupListener {
    private static final Logger log = LoggerFactory.getLogger(ConsumerDispatchingInterceptor.class);

    private final List<String> routeIds = new ArrayList<>();
    private final Map<String, Interceptor> map = new HashMap<>();


    /**
     * Constructs a dispatching interceptor.
     *
     * @param camelContext Camel context.
     * @param routeIds     IDs of routes containing target IPF MLLP endpoints.
     */
    public ConsumerDispatchingInterceptor(CamelContext camelContext, String... routeIds) {
        this(camelContext);
        if (routeIds != null && routeIds.length > 0) {
            addTransactionRoutes(routeIds);
        }
    }

    /**
     * Constructs a dispatching interceptor
     *
     * @param camelContext Camel context
     */
    public ConsumerDispatchingInterceptor(CamelContext camelContext) {
        super();
        try {
            camelContext.addStartupListener(this);
        } catch (Exception e) {
            throw new RuntimeCamelException(e);
        }
    }

    public boolean addTransactionRoutes(String... routeIds) {
        return this.routeIds.addAll(Arrays.asList(routeIds));
    }

    @Override
    public void onCamelContextStarted(CamelContext camelContext, boolean alreadyStarted) throws Exception {
        collectTransactionTargets(camelContext);
        if (!addTargets(camelContext)) {
            log.info("Mllp Dispatcher endpoint exposed without transaction targets.");
        }
    }

    /**
     * Dynamically find all {@link MllpTransactionEndpoint} containing a reference
     * to this instance and append it to the routeId list
     *
     * @param camelContext camel context
     */
    private void collectTransactionTargets(CamelContext camelContext) {
        camelContext.getRoutes().stream()
                .filter(route -> route.getEndpoint() instanceof MllpTransactionEndpoint)
                .forEach(route -> {
                    var endpoint = (MllpTransactionEndpoint<?>) route.getEndpoint();
                    if (endpoint.getDispatcher() == this) {
                        addTransactionRoutes(route.getId());
                    }
                });
    }

    private boolean addTargets(CamelContext camelContext) throws CamelException {
        for (var routeId : routeIds) {
            try {
                var route = camelContext.getRoute(routeId);
                if (route != null) {
                    var consumer = route.getConsumer();
                    var interceptor = (Interceptor) consumer.getProcessor();
                    while (!(interceptor instanceof ConsumerStringProcessingInterceptor)) {
                        interceptor = (Interceptor) interceptor.getWrappedProcessor();
                    }
                    log.debug("Adding MLLP transaction route {} to dispatcher", routeId);
                    map.put(routeId, (Interceptor) interceptor.getWrappedProcessor());
                } else {
                    throw new CamelException("Route with ID='" + routeId + "' not found or is not an IPF MLLP route");
                }
            } catch (ClassCastException e) {
                throw new CamelException("Route with ID='" + routeId + "' is not an IPF MLLP route", e);
            }
        }
        return !map.isEmpty();
    }

    @Override
    public void process(Exchange exchange) throws Exception {

        // determine attributes of the message
        var message = exchange.getIn().getBody(String.class);
        var fields = PreParser.getFields(message, "MSH-9-1", "MSH-9-2", "MSH-9-3", "MSH-12");
        var messageType = fields[0];
        var triggerEvent = fields[1];
        var messageStructure = fields[2];
        var version = fields[3];

        // check who can accept the message
        var found = false;
        for (var routeId : routeIds) {
            var interceptor = map.get(routeId);
            var config = interceptor.getEndpoint(MllpEndpoint.class).getHl7v2TransactionConfiguration();
            try {
                config.checkMessageAcceptance(messageType, triggerEvent, messageStructure, version, true);

                log.debug("Dispatch message with MSH-9-1='{}', MSH-9-2='{}', MSH-9-3='{}', MSH-12='{}' to route '{}'",
                        messageType, triggerEvent, messageStructure, version, routeId);
                found = true;
                interceptor.process(exchange);
                break;
            } catch (Hl7v2AcceptanceException e) {
                // no problem
            }
        }

        if (!found) {
            log.debug("Nobody can process message with MSH-9-1='{}', MSH-9-2='{}', MSH-9-3='{}', MSH-12='{}'",
                    messageType, triggerEvent, messageStructure, version);
            var exception = new HL7Exception(
                    "Unsupported message type and/or version", ErrorCode.APPLICATION_INTERNAL_ERROR);
            exchange.getMessage().setBody(getEndpoint(HL7v2Endpoint.class).getNakFactory().createDefaultNak(exception).encode());
        }
    }


}
