package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.SuspendableService;
import org.apache.camel.component.http.HttpConsumer;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.util.ExchangeHelper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

import java.util.Map;

/**
 *
 */
public class FhirConsumer extends HttpConsumer implements SuspendableService {

    public FhirConsumer(HttpEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
    }

    /**
     * This method can be called by {@link ca.uhn.fhir.rest.server.IResourceProvider} objects to send the received
     * (and potentially handled) request further down a Camel route.
     *
     * @param payload
     * @param headers
     * @param resultClass
     * @param <T>
     * @return
     */
    final <T extends IBaseResource> T process(Object payload, Map<String, Object> headers, Class<T> resultClass) {
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
        return getEndpoint().getCamelContext().getTypeConverter().convertTo(resultClass, exchange, resultMessage.getBody());
    }
}
