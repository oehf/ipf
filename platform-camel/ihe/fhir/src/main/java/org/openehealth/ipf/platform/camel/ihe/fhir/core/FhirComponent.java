package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.UriEndpointComponent;

import java.util.Map;

/**
 *
 */
public abstract class FhirComponent<ConfigType extends FhirEndpointConfiguration> extends UriEndpointComponent {

    public FhirComponent() {
        super(FhirEndpoint.class);
    }

    public FhirComponent(CamelContext context) {
        super(context, FhirEndpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return null;
    }
}
