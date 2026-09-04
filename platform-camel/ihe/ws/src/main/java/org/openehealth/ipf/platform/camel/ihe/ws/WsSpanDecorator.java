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
package org.openehealth.ipf.platform.camel.ihe.ws;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.telemetry.Span;
import org.openehealth.ipf.platform.camel.ihe.core.IpfSpanDecorator;

/**
 * Base class for the span decorators of the SOAP based eHealth transactions.
 * <p>
 * Trace context propagation is not implemented here: it is performed by CXF, whose instrumentation
 * establishes the context before the exchange reaches the Camel route on the server side and injects
 * it into the outgoing HTTP headers on the client side.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class WsSpanDecorator extends IpfSpanDecorator {

    /**
     * Home community the endpoint belongs to, e.g. {@code urn:oid:1.2.3.4.5}. An organizational
     * identifier, not a patient related one.
     */
    public static final String TAG_HOME_COMMUNITY_ID = "ihe.homeCommunityId";

    @Override
    public void beforeTracingEvent(Span span, Exchange exchange, Endpoint endpoint) {
        super.beforeTracingEvent(span, exchange, endpoint);
        if (endpoint instanceof AbstractWsEndpoint<?> wsEndpoint && wsEndpoint.getHomeCommunityId() != null) {
            span.setTag(TAG_HOME_COMMUNITY_ID, wsEndpoint.getHomeCommunityId());
        }
    }
}
