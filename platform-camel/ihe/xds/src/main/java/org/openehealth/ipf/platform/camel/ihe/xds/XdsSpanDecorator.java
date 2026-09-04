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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.telemetry.Span;
import org.openehealth.ipf.commons.ihe.xds.XdsInteractionId;
import org.openehealth.ipf.platform.camel.ihe.core.InteractionAwareComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.WsSpanDecorator;

/**
 * Base class for the span decorators of the document sharing transactions.
 * <p>
 * On top of the transaction metadata contributed by the superclasses, the IHE profile the
 * transaction belongs to is added, so that spans can be grouped by profile rather than only by
 * transaction: several profiles share transactions, and a transaction alone does not tell which
 * profile an actor was playing.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class XdsSpanDecorator extends WsSpanDecorator {

    /** Acronym of the IHE profile the transaction belongs to, e.g. {@code XDS} or {@code XCA}. */
    public static final String TAG_PROFILE = "ihe.profile";

    @Override
    public void beforeTracingEvent(Span span, Exchange exchange, Endpoint endpoint) {
        super.beforeTracingEvent(span, exchange, endpoint);
        var profile = interactionProfile(endpoint);
        if (profile != null) {
            span.setTag(TAG_PROFILE, profile);
        }
    }

    private static String interactionProfile(Endpoint endpoint) {
        if (endpoint instanceof DefaultEndpoint defaultEndpoint
                && defaultEndpoint.getComponent() instanceof InteractionAwareComponent component
                && component.getInteractionId() instanceof XdsInteractionId interactionId
                && interactionId.getInteractionProfile() != null) {
            return interactionId.getInteractionProfile().getClass().getSimpleName();
        }
        return null;
    }
}
