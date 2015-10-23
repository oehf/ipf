package org.openehealth.ipf.platform.camel.ihe.fhir.core;

import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.impl.DefaultEndpoint;

/**
 *
 */
public abstract class FhirEndpoint<
        ConfigType extends FhirEndpointConfiguration,
        ComponentType extends FhirComponent<ConfigType>> extends HttpEndpoint {



}
