/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.iti59;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ClassUtils.getShortCanonicalName;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle.*;

/**
 * Audit strategy for the ITI-59 transaction.
 *
 * @author Dmytro Rud
 */
@Slf4j
abstract class Iti59AuditStrategy extends AuditStrategySupport<Iti59AuditDataset> {

    private static final Map<String, ParticipantObjectTypeCode> PARTICIPANT_OBJECT_CODE_MAP;
    static {
        PARTICIPANT_OBJECT_CODE_MAP = new HashMap<>();
        PARTICIPANT_OBJECT_CODE_MAP.put("HCProfessional".toLowerCase(), ParticipantObjectTypeCode.Person);
        PARTICIPANT_OBJECT_CODE_MAP.put("HCRegulatedOrganization".toLowerCase(), ParticipantObjectTypeCode.Organization);
    }

    protected Iti59AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti59AuditDataset createAuditDataset() {
        return new Iti59AuditDataset(isServerSide());
    }

    /**
     * Enriches the given audit item with UID and participant object type code which correspond to the given DN.
     * @param item      audit item to be enriched
     * @param dn        current (old) LDAP DN value
     */
    private static void enrichAuditItem(Iti59AuditDataset.RequestItem item, String dn) {
        try {
            for (var rdn : new LdapName(dn).getRdns()) {
                var value = (String) rdn.getValue();
                switch (rdn.getType().toLowerCase()) {
                    case "uid":
                        item.setUid(value);
                        break;
                    case "ou":
                        item.setParticipantObjectTypeCode(PARTICIPANT_OBJECT_CODE_MAP.get(value.toLowerCase()));
                        break;
                }
            }
        } catch (InvalidNameException e) {
            log.debug("Cannot parse DN", e);
        }
    }

    @Override
    public Iti59AuditDataset enrichAuditDatasetFromRequest(Iti59AuditDataset auditDataset, Object requestObject, Map<String, Object> parameters) {
        var batchRequest = (BatchRequest) requestObject;
        if ((batchRequest == null) ||
                (batchRequest.getBatchRequests() == null) ||
                batchRequest.getBatchRequests().isEmpty()) {
            log.debug("Empty batch request");
            return auditDataset;
        }

        var requestItems = new Iti59AuditDataset.RequestItem[batchRequest.getBatchRequests().size()];

        for (var i = 0; i < batchRequest.getBatchRequests().size(); ++i) {
            var dsmlMessage = batchRequest.getBatchRequests().get(i);

            if (dsmlMessage instanceof AddRequest addRequest) {
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(addRequest.getRequestID()), EventActionCode.Create);
                requestItems[i].setParticipantObjectDataLifeCycle(Origination);
                enrichAuditItem(requestItems[i], addRequest.getDn());

            } else if (dsmlMessage instanceof ModifyRequest modifyRequest) {
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(modifyRequest.getRequestID()), EventActionCode.Update);
                requestItems[i].setParticipantObjectDataLifeCycle(Amendment);
                enrichAuditItem(requestItems[i], modifyRequest.getDn());

            } else if (dsmlMessage instanceof ModifyDNRequest modifyDNRequest) {
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(modifyDNRequest.getRequestID()), EventActionCode.Execute);
                requestItems[i].setParticipantObjectDataLifeCycle(Translation);
                enrichAuditItem(requestItems[i], modifyDNRequest.getDn());
                try {
                    requestItems[i].setNewUid(new LdapName(modifyDNRequest.getNewrdn()).getRdns().stream()
                            .filter(rdn -> "uid".equalsIgnoreCase(rdn.getType()))
                            .map(rdn -> (String) rdn.getValue())
                            .findAny()
                            .orElse(null));
                } catch (Exception e) {
                    log.debug("Cannot parse new Rdn", e);
                }

            } else if (dsmlMessage instanceof DelRequest delRequest) {
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(delRequest.getRequestID()), EventActionCode.Delete);
                requestItems[i].setParticipantObjectDataLifeCycle(PermanentErasure);
                enrichAuditItem(requestItems[i], delRequest.getDn());

            } else {
                log.debug("Cannot handle ITI-59 request of type {}", getShortCanonicalName(dsmlMessage, "<null>"));
            }
        }

        auditDataset.setRequestItems(requestItems);
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti59AuditDataset auditDataset, Object responseObject, AuditContext auditContext) {
        // check whether there is any need to analyse the response object
        if (auditDataset.getRequestItems() == null) {
            log.debug("The request was empty, nothing to audit");
            return true;
        }

        var batchResponse = (BatchResponse) responseObject;

        // if there are no response fragments at all -- set outcome codes of all requests to failure
        if ((batchResponse == null) || (batchResponse.getBatchResponses() == null)) {
            for (var requestItem : auditDataset.getRequestItems()) {
                if (requestItem != null) {
                    requestItem.setOutcomeCode(EventOutcomeIndicator.SeriousFailure);
                }
            }
            return false;
        }

        // prepare to pairing
        Map<String, Object> byRequestId = new HashMap<>();
        var byNumber = new Object[batchResponse.getBatchResponses().size()];

        for (var i = 0; i < batchResponse.getBatchResponses().size(); ++i) {
            var value = batchResponse.getBatchResponses().get(i).getValue();
            if (value instanceof LDAPResult ldapResult) {
                if (isEmpty(ldapResult.getRequestID())) {
                    byNumber[i] = ldapResult;
                } else {
                    byRequestId.put(ldapResult.getRequestID(), ldapResult);
                }
            } else if (value instanceof ErrorResponse errorResponse) {
                if (isEmpty(errorResponse.getRequestID())) {
                    byNumber[i] = errorResponse;
                } else {
                    byRequestId.put(errorResponse.getRequestID(), errorResponse);
                }
            }
        }

        // try to pair requests with responses
        for (var i = 0; i < auditDataset.getRequestItems().length; ++i) {
            var requestItem = auditDataset.getRequestItems()[i];

            if (requestItem != null) {
                if (isEmpty(requestItem.getRequestId())) {
                    setOutcomeCode(
                            requestItem,
                            (i < byNumber.length) ? byNumber[i] : null,
                            "Could not find response for the ID-less ITI-59 request number {}: either too few responses, or wrong type, or has a request ID",
                            i);
                } else {
                    setOutcomeCode(
                            requestItem,
                            byRequestId.get(requestItem.getRequestId()),
                            "Could not find response for the ITI-59 sub-request with ID '{}': either no ID match, or wrong type",
                            requestItem.getRequestId());
                }
            }
        }

        return true;
    }

    private static void setOutcomeCode(Iti59AuditDataset.RequestItem requestItem, Object value, String failureLogMessage, Object... failureLogArgs) {
        if (value instanceof LDAPResult ldapResult) {
            requestItem.setOutcomeCode((ldapResult.getResultCode() != null) && (ldapResult.getResultCode().getCode() == 0)
                    ? EventOutcomeIndicator.Success
                    : EventOutcomeIndicator.SeriousFailure);
            requestItem.setOutcomeDescription(ldapResult.getErrorMessage());
        } else if (value instanceof ErrorResponse) {
            requestItem.setOutcomeCode(EventOutcomeIndicator.SeriousFailure);
            requestItem.setOutcomeDescription(((ErrorResponse)value).getMessage());
        } else {
            requestItem.setOutcomeCode(EventOutcomeIndicator.MajorFailure);
            log.debug(failureLogMessage, failureLogArgs);
        }
    }

    @Override
    public AuditMessage[] makeAuditMessage(AuditContext auditContext, Iti59AuditDataset auditDataset) {
        // TODO: consider grouping multiple items per ATNA message based on a combination of action code and outcome code
        return Stream.of(auditDataset.getRequestItems())
                .filter(requestItem -> StringUtils.isNotBlank(requestItem.getUid()))
                .map(requestItem -> makeAuditMessage(auditContext, auditDataset, requestItem))
                .toArray(AuditMessage[]::new);
    }

    protected abstract AuditMessage makeAuditMessage(AuditContext auditContext, Iti59AuditDataset auditDataset, Iti59AuditDataset.RequestItem requestItem);

}