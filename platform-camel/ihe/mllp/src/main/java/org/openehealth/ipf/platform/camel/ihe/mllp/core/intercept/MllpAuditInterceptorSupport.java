/*
 * Copyright 2017 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.camel.component.mina2.Mina2Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.ssl.SslFilter;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.AuditUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.atna.interceptor.AuditInterceptor;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.SSLSession;
import java.security.Principal;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

/**
 * Common audit interceptor support for both consumer and producer side of MLLP endpoints
 *
 * @author Christian Ohr
 */
public abstract class MllpAuditInterceptorSupport<AuditDatasetType extends MllpAuditDataset> extends InterceptorSupport<MllpTransactionEndpoint<AuditDatasetType>>
        implements AuditInterceptor<AuditDatasetType, MllpTransactionEndpoint<AuditDatasetType>> {

    private static final Logger LOG = LoggerFactory.getLogger(MllpAuditInterceptorSupport.class);
    private final AuditContext auditContext;

    public MllpAuditInterceptorSupport(AuditContext auditContext) {
        this.auditContext = requireNonNull(auditContext);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Message msg = exchange.getIn().getBody(Message.class);

        // skip auditing in case of non-auditable message types
        if (!isAuditable(msg)) {
            getWrappedProcessor().process(exchange);
            return;
        }

        AuditDatasetType auditDataset = createAndEnrichAuditDatasetFromRequest(exchange, msg);
        determineParticipantsAddresses(exchange, auditDataset);
        extractSslClientUser(exchange, auditDataset);

        boolean failed = false;
        try {
            getWrappedProcessor().process(exchange);
            Message result = resultMessage(exchange).getBody(Message.class);
            enrichAuditDatasetFromResponse(auditDataset, result);
            failed = !AuditUtils.isPositiveAck(result);
        } catch (Exception e) {
            failed = true;
            if (auditDataset != null) {
                auditDataset.setEventOutcomeDescription(e.getMessage());
            }
            throw e;
        } finally {
            if (auditDataset != null) {
                auditDataset.setEventOutcomeIndicator(failed ?
                        EventOutcomeIndicator.MajorFailure :
                        EventOutcomeIndicator.Success);
                getAuditStrategy().doAudit(auditContext, auditDataset);
            } else {
                LOG.warn("Audit dataset is not initialized, no auditing happens");
            }
        }
    }

    private void extractSslClientUser(Exchange exchange, AuditDatasetType auditDataset) {
        IoSession ioSession = exchange.getIn().getHeader(Mina2Constants.MINA_IOSESSION, IoSession.class);
        if (ioSession != null) {
            SSLSession sslSession = (SSLSession) ioSession.getAttribute(SslFilter.SSL_SESSION);
            if (sslSession != null) {
                try {
                    Principal principal = sslSession.getPeerPrincipal();
                    if (principal != null) {
                        String dn = principal.getName();
                        LdapName ldapDN = new LdapName(dn);
                        for (Rdn rdn : ldapDN.getRdns()) {
                            if (rdn.getType().equalsIgnoreCase("CN")) {
                                auditDataset.setSourceUserName((String) rdn.getValue());
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    LOG.info("Could not extract CN from client certificate", e);
                }
            }
        }
    }


    /**
     * Creates a new audit dataset and enriches it with data from the request
     * message.  All exception are ignored.
     *
     * @return newly created audit dataset or <code>null</code> when creation failed.
     */
    private AuditDatasetType createAndEnrichAuditDatasetFromRequest(Exchange exchange, Message msg) {
        AuditDatasetType auditDataset = getAuditStrategy().createAuditDataset();
        try {
            AuditUtils.enrichGenericAuditDatasetFromRequest(auditDataset, msg);
            return getAuditStrategy().enrichAuditDatasetFromRequest(auditDataset, msg, exchange.getIn().getHeaders());
        } catch (Exception e) {
            LOG.warn("Exception when enriching audit dataset from request", e);
            return auditDataset;
        }
    }

    /**
     * Enriches the given audit dataset with data from the response message.
     * All exception are ignored.
     */
    private void enrichAuditDatasetFromResponse(AuditDatasetType auditDataset, Message msg) {
        try {
            getAuditStrategy().enrichAuditDatasetFromResponse(auditDataset, msg, auditContext);
        } catch (Exception e) {
            LOG.warn("Exception when enriching audit dataset from response", e);
        }
    }

    /**
     * Checks whether the given message should be audited.
     * All exceptions are ignored.
     */
    private boolean isAuditable(Message message) {
        try {
            // no audit for fragments 2..n
            Terser terser = new Terser(message);
            return (!ArrayUtils.contains(message.getNames(), "DSC") ||
                    !StringUtils.isNotEmpty(terser.get("DSC-1"))) &&
                    getEndpoint().getHl7v2TransactionConfiguration().isAuditable(MessageUtils.eventType(message));
        } catch (Exception e) {
            LOG.warn("Exception when determining message auditability, no audit will be performed", e);
            return false;
        }
    }
}
