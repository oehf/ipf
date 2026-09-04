/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.telemetry;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TraceContext;

/**
 * A PDQ ITI-21 consumer that both answers and reports what arrived on the wire, so that a test can see
 * whether the trace context made it into the message.
 *
 * @author Christian Ohr
 */
public class Iti21TelemetryRouteBuilder extends RouteBuilder {

    public static final int PORT = 8899;

    /** What the consumer received, before it was parsed. */
    public static volatile String receivedMessage;

    /**
     * Left empty on purpose: the route is added by the test once the tracer is installed, because the
     * route policy that produces the consumer span only attaches when a route is created.
     */
    @Override
    public void configure() {
    }

    /** The consumer route, to be added once the tracer is in place. */
    public static RouteBuilder consumerRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                from("pdq-iti21://0.0.0.0:" + PORT + "?audit=false")
                .process(exchange -> {
                    receivedMessage = exchange.getIn().getHeader(
                            org.openehealth.ipf.commons.ihe.hl7v2.Constants.ORIGINAL_MESSAGE_STRING_HEADER_NAME,
                            String.class);
                    var request = exchange.getIn().getBody(ca.uhn.hl7v2.model.Message.class);
                    exchange.getMessage().setBody(
                            org.openehealth.ipf.modules.hl7.message.MessageUtils.response(request, "RSP", "K22"));
                });
            }
        };
    }

    /** @return the trace context of the message the consumer received. */
    public static java.util.Map<String, String> receivedTraceContext() {
        return Hl7v2TraceContext.read(receivedMessage);
    }
}
