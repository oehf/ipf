package org.openehealth.ipf.platform.camel.ihe.fhir.iti83;

import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openehealth.ipf.platform.camel.ihe.fhir.core.AbstractResourceProvider;

import java.util.HashMap;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.Constants.*;

/**
 *
 */
public class Iti83ResourceProvider extends AbstractResourceProvider {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Parameters.class;
    }

    @Operation(name=PIXM_OPERATION_NAME)
    public Parameters pixQuery(
            @OperationParam(name=SOURCE_IDENTIFIER_NAME) TokenParam sourceIdentifier,
            @OperationParam(name=TARGET_SYSTEM_NAME) UriParam targetSystem) {

        // Populate Parameters with matching resources
        Map<String, Object> searchParameters = new HashMap<>();
        searchParameters.put(SOURCE_IDENTIFIER_SYSTEM_NAME, sourceIdentifier.getSystem());
        searchParameters.put(SOURCE_IDENTIFIER_EXTENSION_NAME, sourceIdentifier.getValue());
        searchParameters.put(TARGET_SYSTEM_NAME, targetSystem.getValue());

        // Run down the route, if any
        return process(searchParameters, null, Parameters.class);
    }

}
