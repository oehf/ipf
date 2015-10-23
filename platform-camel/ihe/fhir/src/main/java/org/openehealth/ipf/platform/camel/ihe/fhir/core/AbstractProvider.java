package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Map;

/**
 *
 */
public abstract class AbstractProvider {

    private transient FhirConsumer consumer;

    protected final <T extends IBaseResource> T process(Object payload, Map<String, Object> headers, Class<T> resultType) {
        if (consumer == null) {
            throw new IllegalStateException("Consumer is not initialized");
        }
        return consumer.process(payload, headers, resultType);
    }

    // Ensure this is only used once!
    void setConsumer(FhirConsumer consumer) {
        this.consumer = consumer;
    }
}
