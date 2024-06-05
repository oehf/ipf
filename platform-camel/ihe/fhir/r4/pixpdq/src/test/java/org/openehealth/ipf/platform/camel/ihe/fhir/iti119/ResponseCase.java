package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.valueset.BundleEntrySearchModeEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

public enum ResponseCase {

    OK;

    public List<Patient> populateResponse(Parameters request) {
        var patient = new Patient()
            .addName(new HumanName().setFamily("Test"));
        ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.put(patient, BundleEntrySearchModeEnum.MATCH);
        return List.of(patient);
    }
}
