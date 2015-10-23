package org.openehealth.ipf.commons.ihe.fhir.translation

import ca.uhn.fhir.model.api.IResource
import ca.uhn.hl7v2.model.Message
import org.hl7.fhir.instance.model.api.IBaseResource

/**
 *
 */
interface TranslatorFhirToHL7v2<M extends Message> {

    M translateFhirToHL7v2(Map<String, IResource> resources);

}
