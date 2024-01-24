package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import org.junit.jupiter.api.BeforeEach;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;

public class FhirRestMethanolTLSSenderIntegrationTest extends AbstractFhirRestTLSSenderIntegrationTest {

    @BeforeEach
    public void setup() {
        super.setup();
        auditContext.setAuditRepositoryTransport(AuditTransmissionChannel.FHIR_REST_METHANOL_TLS.getProtocolName());
    }
}
