/*
 * Copyright 2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.audit.protocol;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.impl.RestfulClientFactory;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.AuditMetadataProvider;
import org.openehealth.ipf.commons.audit.BalpAuditContext;
import org.openehealth.ipf.commons.audit.FhirContextHolder;
import org.openehealth.ipf.commons.audit.TlsParameters;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionChannel;
import org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol;
import org.openehealth.ipf.commons.ihe.fhir.SslAwareAbstractRestfulClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static ca.uhn.fhir.context.FhirContext.forR4;

/**
 * FHIR REST client for sending FHIR Audit Events to an Audit Record Repository.
 *
 * @author Boris Stanojevic
 * @since 4.8
 */
public abstract class AbstractFhirRestTLSAuditRecordSender implements AuditTransmissionProtocol {

    private IGenericClient client;
    private static final String BASE_URL_FORMAT = "https://%s:%s/%s";
    private FhirContext context;
    private TlsParameters tlsParameters;
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFhirRestTLSAuditRecordSender.class);

    public AbstractFhirRestTLSAuditRecordSender(final FhirContext context, String baseUrl) {
        this.context = Objects.requireNonNull(context, "FhirContext must not be null");
        createClient(baseUrl);
    }

    public AbstractFhirRestTLSAuditRecordSender(RestfulClientFactory restfulClientFactory, String baseUrl) {
        this.context = Objects
            .requireNonNull(restfulClientFactory, "RestfulClientFactory must not be null")
            .getFhirContext();
        createClient(baseUrl);
    }

    public AbstractFhirRestTLSAuditRecordSender(TlsParameters tlsParameters) {
        this.tlsParameters = tlsParameters;
    }

    @Override
    public void send(AuditContext auditContext,
                     AuditMetadataProvider auditMetadataProvider,
                     String auditEvent) throws Exception {
        if (client == null) {
            context = FhirContextHolder.get();
            if (context == null) {
                context = forR4();
            }
            new TlsParametersAwareRestfulClientFactory(
                this.context,
                this.tlsParameters);
            String baseUrl = String.format(BASE_URL_FORMAT,
                auditContext.getAuditRepositoryHostName(),
                auditContext.getAuditRepositoryPort(),
                (auditContext instanceof BalpAuditContext)?
                    ((BalpAuditContext)auditContext).getAuditRepositoryContextPath() : "");
            createClient(baseUrl);
        }
        MethodOutcome outcome = client
            .create()
            .resource(auditEvent)
            .execute();

        LOG.debug("Audit Repository Response: " + outcome.getResponseStatusCode());
    }

    private synchronized void createClient(String baseUrl) {
        if (client == null) {
            client = context.getRestfulClientFactory().newGenericClient(baseUrl);
        }
    }

    @Override
    public void shutdown() {

    }

    @Override
    public String getTransportName() {
        return AuditTransmissionChannel.FHIR_REST_TLS.getProtocolName();
    }

    private final class TlsParametersAwareRestfulClientFactory {

        private final FhirContext fhirContext;
        private final RestfulClientFactory restfulClientFactory;

        public TlsParametersAwareRestfulClientFactory(FhirContext fhirContext, TlsParameters tlsParameters) {
            this.fhirContext = fhirContext;
            this.restfulClientFactory = createRestfulFactory(tlsParameters);
        }

        private RestfulClientFactory createRestfulFactory(TlsParameters tlsParameters) {
            SslAwareAbstractRestfulClientFactory<?> factory = createSslAwareClientFactory(fhirContext);

            factory.initializeSecurityInformation(true,
                tlsParameters.getSSLContext(false), null, "", "");
            fhirContext.setRestfulClientFactory(factory);
            return factory;
        }

        public RestfulClientFactory getRestfulClientFactory() {
            return this.restfulClientFactory;
        }
    }

    protected abstract SslAwareAbstractRestfulClientFactory<?> createSslAwareClientFactory(FhirContext fhirContext);
}
