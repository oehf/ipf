package org.openehealth.ipf.commons.ihe.fhir.iti81;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirQueryAuditDataset;

import java.util.ArrayList;
import java.util.List;

public class FhirAuditEventQueryAuditDataset extends FhirQueryAuditDataset {

    @Getter
    private final List<String> auditEventUris = new ArrayList<>();

    public FhirAuditEventQueryAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
