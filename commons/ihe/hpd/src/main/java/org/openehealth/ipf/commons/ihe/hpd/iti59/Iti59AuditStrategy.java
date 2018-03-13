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
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.*;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881ParticipantObjectCodes.RFC3881ParticipantObjectTypeCodes;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.ClassUtils.getShortCanonicalName;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * Audit strategy for the ITI-59 transaction.
 * @author Dmytro Rud
 */
@Slf4j
public class Iti59AuditStrategy extends AuditStrategySupport<Iti59AuditDataset> {

    private static final Map<String, RFC3881ParticipantObjectTypeCodes> PARTICIPANT_OBJECT_CODE_MAP;
    static {
        PARTICIPANT_OBJECT_CODE_MAP = new HashMap<>();
        PARTICIPANT_OBJECT_CODE_MAP.put("HCProfessional", RFC3881ParticipantObjectTypeCodes.PERSON);
        PARTICIPANT_OBJECT_CODE_MAP.put("HCRegulatedOrganization", RFC3881ParticipantObjectTypeCodes.ORGANIZATION);
    }

    public Iti59AuditStrategy(boolean serverSide) {
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
        if (isEmpty(dn)) return;
        try {
            for (Rdn rdn : new LdapName(dn).getRdns()) {
                String value = (String) rdn.getValue();
                switch (rdn.getType().toLowerCase()) {
                    case "uid":
                        item.setUid(value);
                        break;
                    case "ou":
                        item.setParticipantObjectTypeCode(PARTICIPANT_OBJECT_CODE_MAP.get(value));
                        break;
                }
            }
        } catch (InvalidNameException e) {
            log.debug("Cannot parse DN", e);
        }
    }

    @Override
    public Iti59AuditDataset enrichAuditDatasetFromRequest(Iti59AuditDataset auditDataset, Object requestObject, Map<String, Object> parameters) {
        BatchRequest batchRequest = (BatchRequest) requestObject;
        if ((batchRequest == null) || (batchRequest.getBatchRequests() == null) || batchRequest.getBatchRequests().isEmpty()) {
            log.debug("Empty batch request");
            return auditDataset;
        }

        Iti59AuditDataset.RequestItem[] requestItems = new Iti59AuditDataset.RequestItem[batchRequest.getBatchRequests().size()];

        for (int i = 0; i < batchRequest.getBatchRequests().size(); ++i) {
            DsmlMessage dsmlMessage = batchRequest.getBatchRequests().get(i);

            if (dsmlMessage instanceof AddRequest) {
                AddRequest addRequest = (AddRequest) dsmlMessage;
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(addRequest.getRequestID()),
                                                                    RFC3881EventCodes.RFC3881EventActionCodes.CREATE);
                enrichAuditItem(requestItems[i], addRequest.getDn());

            } else if (dsmlMessage instanceof ModifyRequest) {
                ModifyRequest modifyRequest = (ModifyRequest) dsmlMessage;
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(modifyRequest.getRequestID()),
                                                                    RFC3881EventCodes.RFC3881EventActionCodes.UPDATE);
                enrichAuditItem(requestItems[i], modifyRequest.getDn());

            } else if (dsmlMessage instanceof ModifyDNRequest) {
                ModifyDNRequest modifyDNRequest = (ModifyDNRequest) dsmlMessage;
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(modifyDNRequest.getRequestID()),
                                                                    RFC3881EventCodes.RFC3881EventActionCodes.EXECUTE);
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
            } else if (dsmlMessage instanceof DelRequest) {
                DelRequest delRequest = (DelRequest) dsmlMessage;
                requestItems[i] = new Iti59AuditDataset.RequestItem(trimToNull(delRequest.getRequestID()),
                                                                    RFC3881EventCodes.RFC3881EventActionCodes.DELETE);
                enrichAuditItem(requestItems[i], delRequest.getDn());
            } else {
                log.debug("Cannot handle ITI-59 request of type {}", getShortCanonicalName(dsmlMessage, "<null>"));
            }
        }

        auditDataset.setRequestItems(requestItems);
        return auditDataset;
    }

    @Override
    public boolean enrichAuditDatasetFromResponse(Iti59AuditDataset auditDataset, Object responseObject) {
        // check whether there is any need to analyse the response object
        if (auditDataset.getRequestItems() == null) {
            log.debug("The request was empty, nothing to audit");
            return true;
        }

        BatchResponse batchResponse = (BatchResponse) responseObject;

        // if there are no response fragments at all -- set outcome codes of all requests to failure
        if ((batchResponse == null) || (batchResponse.getBatchResponses() == null)) {
            for (Iti59AuditDataset.RequestItem requestItem : auditDataset.getRequestItems()) {
                if (requestItem != null) {
                    requestItem.setOutcomeCode(RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE);
                }
            }
            return false;
        }

        // prepare to pairing
        Map<String, Object> byRequestId = new HashMap<>();
        Object[] byNumber = new Object[batchResponse.getBatchResponses().size()];

        for (int i = 0; i < batchResponse.getBatchResponses().size(); ++i) {
            Object value = batchResponse.getBatchResponses().get(i).getValue();
            if (value instanceof LDAPResult) {
                LDAPResult ldapResult = (LDAPResult) value;
                if (isEmpty(ldapResult.getRequestID())) {
                    byNumber[i] = ldapResult;
                } else {
                    byRequestId.put(ldapResult.getRequestID(), ldapResult);
                }
            }
            else if (value instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) value;
                if (isEmpty(errorResponse.getRequestID())) {
                    byNumber[i] = errorResponse;
                } else {
                    byRequestId.put(errorResponse.getRequestID(), errorResponse);
                }
            }
        }

        // try to pair requests with responses
        for (int i = 0; i < auditDataset.getRequestItems().length; ++i) {
            Iti59AuditDataset.RequestItem requestItem = auditDataset.getRequestItems()[i];

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
        if (value instanceof LDAPResult) {
            LDAPResult ldapResult = (LDAPResult) value;
            requestItem.setOutcomeCode((ldapResult.getResultCode() != null) && (ldapResult.getResultCode().getCode() == 0)
                    ? RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS
                    : RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE);
        }
        else if (value instanceof ErrorResponse) {
            requestItem.setOutcomeCode(RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE);
        }
        else {
            requestItem.setOutcomeCode(RFC3881EventCodes.RFC3881EventOutcomeCodes.MAJOR_FAILURE);
            log.debug(failureLogMessage, failureLogArgs);
        }
    }

    @Override
    public RFC3881EventCodes.RFC3881EventOutcomeCodes getEventOutcomeCode(Object responseObject) {
        // is not used because individual outcome codes are determined for each sub-request
        return null;
    }

    @Override
    public void doAudit(Iti59AuditDataset auditDataset) {
        for (Iti59AuditDataset.RequestItem requestItem : auditDataset.getRequestItems()) {
            if (requestItem != null) {
                AuditorManager.getHpdAuditor().auditIti59(
                        isServerSide(),
                        requestItem.getActionCode(),
                        requestItem.getOutcomeCode(),
                        auditDataset.getUserId(),
                        auditDataset.getUserName(),
                        auditDataset.getServiceEndpointUrl(),
                        auditDataset.getClientIpAddress(),
                        requestItem.getParticipantObjectTypeCode(),
                        requestItem.getUid(),
                        requestItem.getNewUid(),
                        auditDataset.getPurposesOfUse(),
                        auditDataset.getUserRoles());
            }
        }
    }

}