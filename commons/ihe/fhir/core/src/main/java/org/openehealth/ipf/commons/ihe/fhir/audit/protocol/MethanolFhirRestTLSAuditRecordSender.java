package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareAbstractRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareMethanolRestfulClientFactory;

public class MethanolFhirRestTLSAuditRecordSender extends AbstractFhirRestTLSAuditRecordSender {

    public MethanolFhirRestTLSAuditRecordSender(FhirContext context, String baseUrl) {
        super(context, baseUrl);
    }

    public MethanolFhirRestTLSAuditRecordSender(RestfulClientFactory restfulClientFactory, String baseUrl) {
        super(restfulClientFactory, baseUrl);
    }

    public MethanolFhirRestTLSAuditRecordSender(TlsParameters tlsParameters) {
        super(tlsParameters);
    }

    @Override
    protected SslAwareAbstractRestfulClientFactory<?> createSslAwareClientFactory(FhirContext fhirContext) {
        return new SslAwareMethanolRestfulClientFactory(fhirContext);
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.FHIR_REST_METHANOL_TLS.getProtocolName();
    }
}
