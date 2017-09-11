package org.openehealth.ipf.commons.ihe.fhir.translation;

import java.util.Map;

/**
 * @author Christian Ohr
 */
public interface FhirTranslator<T> {

    /**
     * Translates a FhirObject into something else
     *
     * @param fhir FhirObject
     * @return HL7v2 message
     */
    T translateFhir(Object fhir, Map<String, Object> parameters);
}
