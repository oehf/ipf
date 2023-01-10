package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.openehealth.ipf.commons.ihe.fhir.Constants.FHIR_REQUEST_DETAILS;

public class GenericFhirAuditStrategyTest {

    private final GenericFhirAuditStrategy classUnderTest = new GenericFhirAuditStrategy(false,
            EasyMock.mock(PatientIdExtractor.class));

    @Test
    public void enrichAuditDatasetFromRequestForNullRestOperationType() {
        GenericFhirAuditDataset auditDataset = classUnderTest.createAuditDataset();
        classUnderTest.enrichAuditDatasetFromRequest(auditDataset, classUnderTest,
                Collections.singletonMap(FHIR_REQUEST_DETAILS, new ServletRequestDetails()));
        assertNotNull(auditDataset);
    }

}
