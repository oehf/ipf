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
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;
import org.openehealth.ipf.commons.ihe.fhir.Constants;
import org.openehealth.ipf.commons.ihe.fhir.audit.FhirAuditDatasetEnricher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ldap.LdapName;
import javax.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Christian Ohr
 */
public abstract class AuditInterceptorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(AuditInterceptorUtils.class);

    public static void extractClientCertificateCommonName(Exchange exchange, AuditDataset auditDataset) {
        var certificates = (X509Certificate[]) exchange.getIn().getHeader(Constants.HTTP_X509_CERTIFICATES);
        if (certificates != null && certificates.length > 0) {
            try {
                var certificate = certificates[0];
                var principal = certificate.getSubjectDN();
                var dn = principal.getName();
                var ldapDN = new LdapName(dn);
                for (var rdn : ldapDN.getRdns()) {
                    if (rdn.getType().equalsIgnoreCase("CN")) {
                        auditDataset.setSourceUserName((String) rdn.getValue());
                        break;
                    }
                }
            } catch (Exception e) {
                LOG.info("Could not extract CN from client certificate", e);
            }
        }
    }

    public static Optional<String> extractAuthorizationHeader(Exchange exchange) {
        if (exchange.getIn().getHeader(Constants.HTTP_INCOMING_HEADERS) != null) {
            Map<String, List<String>> httpHeaders = exchange.getIn().getHeader(Constants.HTTP_INCOMING_HEADERS, Map.class);
            if (!httpHeaders.isEmpty()
                && httpHeaders.keySet().stream().anyMatch(Constants.HTTP_AUTHORIZATION::equalsIgnoreCase)) {

                List<String> values = httpHeaders.entrySet().stream()
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

    public static void enrichAuditDataset(AuditDataset auditDataset, AuditContext auditContext, Exchange exchange) {
        if (auditContext.getFhirAuditDatasetEnricher() != null) {
            FhirAuditDatasetEnricher enricher = auditContext.getFhirAuditDatasetEnricher();
            enricher.enrichAuditDataset(auditDataset, exchange.getIn().getBody(), exchange.getIn().getHeaders());
        }
    }

}
