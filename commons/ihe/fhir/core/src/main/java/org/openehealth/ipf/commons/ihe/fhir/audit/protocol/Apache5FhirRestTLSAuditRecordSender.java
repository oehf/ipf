package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareAbstractRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareApacheRestfulClient5Factory;

public class Apache5FhirRestTLSAuditRecordSender extends AbstractFhirRestTLSAuditRecordSender {

    public Apache5FhirRestTLSAuditRecordSender(FhirContext context, String baseUrl) {
        super(context, baseUrl);
    }

    public Apache5FhirRestTLSAuditRecordSender(RestfulClientFactory restfulClientFactory, String baseUrl) {
        super(restfulClientFactory, baseUrl);
    }

    public Apache5FhirRestTLSAuditRecordSender(TlsParameters tlsParameters) {
        super(tlsParameters);
    }

    @Override
    protected SslAwareAbstractRestfulClientFactory<?> createSslAwareClientFactory(FhirContext fhirContext) {
        return new SslAwareApacheRestfulClient5Factory(fhirContext);
    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.FHIR_REST_APACHE5_TLS.getProtocolName();
    }
}
