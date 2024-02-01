package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareAbstractRestfulClientFactory;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareApacheRestfulClientFactory;

public class ApacheFhirRestTLSAuditRecordSender extends AbstractFhirRestTLSAuditRecordSender {

    public ApacheFhirRestTLSAuditRecordSender(FhirContext context, String baseUrl) {
        super(context, baseUrl);
    }

    public ApacheFhirRestTLSAuditRecordSender(RestfulClientFactory restfulClientFactory, String baseUrl) {
        super(restfulClientFactory, baseUrl);
    }

    public ApacheFhirRestTLSAuditRecordSender(TlsParameters tlsParameters) {
        super(tlsParameters);
    }

    @Override
    protected SslAwareAbstractRestfulClientFactory<?> createSslAwareClientFactory(FhirContext fhirContext) {
        return new SslAwareApacheRestfulClientFactory(fhirContext);
    }
}
