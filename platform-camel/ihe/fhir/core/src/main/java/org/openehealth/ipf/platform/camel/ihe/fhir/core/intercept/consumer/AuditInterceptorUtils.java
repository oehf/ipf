/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.fhir.core.intercept.consumer;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.RequestIdHeaders;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDatasetEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ldap.LdapName;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Christian Ohr
 */
public abstract class AuditInterceptorUtils {

    private static final Logger log = LoggerFactory.getLogger(AuditInterceptorUtils.class);

    public static void extractClientCertificateCommonName(Exchange exchange, AuditDataset auditDataset) {
        var certificates = (X509Certificate[]) exchange.getIn().getHeader(Constants.HTTP_X509_CERTIFICATES);
        if (certificates != null && certificates.length > 0) {
            try {
                var certificate = certificates[0];
                var principal = certificate.getSubjectX500Principal();
                var dn = principal.getName();
                var ldapDN = new LdapName(dn);
                for (var rdn : ldapDN.getRdns()) {
                    if (rdn.getType().equalsIgnoreCase("CN")) {
                        auditDataset.setSourceUserName((String) rdn.getValue());
                        break;
                    }
                }
            } catch (Exception e) {
                log.info("Could not extract CN from client certificate", e);
            }
        }
    }

    public static Optional<String> extractAuthorizationHeader(Exchange exchange) {
        if (exchange.getIn().getHeader(Constants.HTTP_INCOMING_HEADERS) != null) {
            Map<String, List<String>> httpHeaders = exchange.getIn().getHeader(Constants.HTTP_INCOMING_HEADERS, Map.class);
            if (!httpHeaders.isEmpty()
                && httpHeaders.keySet().stream().anyMatch(Constants.HTTP_AUTHORIZATION::equalsIgnoreCase)) {

                var values = httpHeaders.entrySet().stream()
                    .filter(entry -> Constants.HTTP_AUTHORIZATION.equalsIgnoreCase(entry.getKey()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .orElse(new ArrayList<>());

                if (!values.isEmpty()) {
                    return Optional.of(values.get(0));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * The id correlating the audit records of the two ends of a transaction, together with the
     * participant object ID type it is to be recorded under.
     *
     * @param value the correlation id
     * @param type  how to record it, which says where the id came from
     */
    public record RequestId(String value, ParticipantObjectIdType type) {
    }

    /**
     * Extracts the id correlating the audit records of the two ends of a transaction from the first of
     * the configured headers the message carries. Incoming headers are searched before outgoing ones, so
     * that a server adopts the id its client sent rather than one the route happens to set as well.
     * <p>
     * The type follows the header the id was found in, unless the audit context forces one; see
     * {@link AuditContext#getRequestIdType()}.
     *
     * @param exchange     exchange being audited
     * @param auditContext audit context naming the headers to look for
     * @return the correlation id, if the message carries one
     */
    public static Optional<RequestId> extractRequestId(Exchange exchange, AuditContext auditContext) {
        for (var headerName : auditContext.getRequestIdHeaderNames()) {
            var value = httpHeaderValue(exchange, Constants.HTTP_INCOMING_HEADERS, headerName)
                .or(() -> httpHeaderValue(exchange, Constants.HTTP_OUTGOING_HEADERS, headerName));
            if (value.isPresent()) {
                var type = auditContext.getRequestIdType() != null ?
                    auditContext.getRequestIdType() :
                    RequestIdHeaders.participantObjectIdType(headerName);
                return value.map(id -> new RequestId(id, type));
            }
        }
        return Optional.empty();
    }

    private static Optional<String> httpHeaderValue(Exchange exchange, String headerMapName, String headerName) {
        Map<String, List<String>> httpHeaders = exchange.getIn().getHeader(headerMapName, Map.class);
        if (httpHeaders == null) {
            return Optional.empty();
        }
        return httpHeaders.entrySet().stream()
            .filter(entry -> headerName.equalsIgnoreCase(entry.getKey()))
            .map(Map.Entry::getValue)
            .filter(values -> values != null && !values.isEmpty())
            .map(values -> values.get(0))
            .flatMap(value -> RequestIdHeaders.extractRequestId(headerName, value).stream())
            .findFirst();
    }

    public static void enrichAuditDatasetFromRequest(AuditDataset auditDataset, AuditContext auditContext, Exchange exchange) {
        // an enricher configured afterwards may still override both the id and its type
        extractRequestId(exchange, auditContext).ifPresent(requestId ->
            auditDataset.setRequestId(requestId.value(), requestId.type()));
        if (auditContext.getFhirAuditDatasetEnricher() != null) {
            FhirAuditDatasetEnricher enricher = auditContext.getFhirAuditDatasetEnricher();
            enricher.enrichAuditDatasetFromRequest(auditDataset, exchange.getIn().getBody(), exchange.getIn().getHeaders());
        }
    }

    public static void enrichAuditDatasetFromResponse(AuditDataset auditDataset, AuditContext auditContext, Exchange exchange) {
        if (auditContext.getFhirAuditDatasetEnricher() != null) {
            FhirAuditDatasetEnricher enricher = auditContext.getFhirAuditDatasetEnricher();
            enricher.enrichAuditDatasetFromResponse(auditDataset, exchange.getIn().getBody(), exchange.getIn().getHeaders());
        }
    }

}
