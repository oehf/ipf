package org.openehealth.ipf.commons.ihe.fhir.translation;

import java.util.Map;

/**
 * @author Christian Ohr
 */
public interface ToFhirTranslator<T> {

    /**
     * Translates something into a FHIR object
     *
     * @param input input data
     * @return FHIR data
     */
    Object translateToFhir(T input, Map<String, Object> parameters);
}
