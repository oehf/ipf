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
package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.telemetry.Span;
import org.openehealth.ipf.commons.ihe.fhir.FhirTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.IpfSpanDecorator;

/**
 * Base class for the span decorators of the FHIR based transactions.
 * <p>
 * On top of the transaction metadata contributed by the superclass, the FHIR version is added: the
 * same transaction may be offered for more than one FHIR version, under the same endpoint scheme, so the
 * version is what tells the spans of the two apart.
 * <p>
 * Trace context propagation is not implemented here: it is performed by the HTTP client and server
 * the FHIR endpoints are built on, whose instrumentation establishes the context before the exchange
 * reaches the Camel route and injects it into the outgoing HTTP headers.
 *
 * @author Christian Ohr
 * @since 6.0
 */
public abstract class FhirSpanDecorator extends IpfSpanDecorator {

    /** FHIR version of the transaction, e.g. {@code R4}. */
    public static final String TAG_FHIR_VERSION = "ihe.fhir.version";

    @Override
    public void beforeTracingEvent(Span span, Exchange exchange, Endpoint endpoint) {
        super.beforeTracingEvent(span, exchange, endpoint);
        if (transactionConfiguration(endpoint) instanceof FhirTransactionConfiguration configuration
                && configuration.getFhirVersion() != null) {
            span.setTag(TAG_FHIR_VERSION, configuration.getFhirVersion().name());
        }
    }
}
