/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.SuspendableService;
import org.apache.camel.impl.DefaultConsumer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.commons.ihe.fhir.atna.FhirAuditDataset;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

import java.util.Map;

/**
 * FHIR consumer
 *
 * @since 3.1
 */
public abstract class FhirConsumer<AuditDatasetType extends FhirAuditDataset>
        extends DefaultConsumer implements SuspendableService {

    public FhirConsumer(FhirEndpoint<AuditDatasetType> endpoint, Processor processor) {
        super(endpoint, processor);
    }

    protected void doStart() throws Exception {
        super.doStart();
        this.getEndpoint().connect(this);
    }

    protected void doStop() throws Exception {
        this.getEndpoint().disconnect(this);
        super.doStop();
    }

    @Override
    public FhirEndpoint<AuditDatasetType> getEndpoint() {
        return (FhirEndpoint<AuditDatasetType>)super.getEndpoint();
    }

    /**
     * This method can be called by {@link ca.uhn.fhir.rest.server.IResourceProvider} objects to send the received
     * (and potentially handled) request further down a Camel route.
     *
     * @param payload FHIR request content
     * @param headers headers
     * @param resultClass class of the result resource
     * @param <R> Resource type being returned
     * @return result of processing the FHIR request in Camel
     */
    final <R extends IBaseResource> R processInRoute(Object payload, Map<String, Object> headers, Class<R> resultClass) {
        Exchange exchange = getEndpoint().createExchange();
        exchange.getIn().setBody(payload);
        if (headers != null) {
            exchange.getIn().setHeaders(headers);
        }

        try {
            getProcessor().process(exchange);
        } catch (Throwable e) {
            getExceptionHandler().handleException(e);
        }

        // Handle exceptions!!

        Message resultMessage = Exchanges.resultMessage(exchange);
        return getEndpoint().getCamelContext().getTypeConverter()
                .convertTo(resultClass, exchange, resultMessage.getBody());
    }
}
