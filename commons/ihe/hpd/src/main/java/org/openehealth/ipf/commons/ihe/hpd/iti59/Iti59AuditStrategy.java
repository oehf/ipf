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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ClassUtils.getShortCanonicalName;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Audit strategy for the ITI-59 transaction.
 * @author Dmytro Rud
 */
@Slf4j
public class Iti59AuditStrategy extends AuditStrategySupport<Iti59AuditDataset> {

    private static final String ATTR_NAME = "hcIdentifier";

    public Iti59AuditStrategy(boolean serverSide) {
        super(serverSide);
    }

    @Override
    public Iti59AuditDataset createAuditDataset() {
        return new Iti59AuditDataset(isServerSide());
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
                requestItems[i] = new Iti59AuditDataset.RequestItem(
                        trimToNull(addRequest.getRequestID()),
                        RFC3881EventCodes.RFC3881EventActionCodes.CREATE,
                        addRequest.getAttr().stream()
                                .filter(x -> ATTR_NAME.equalsIgnoreCase(x.getName()))
                                .flatMap(x -> x.getValue().stream())
                                .collect(Collectors.toSet()));

            } else if (dsmlMessage instanceof ModifyRequest) {
                ModifyRequest modifyRequest = (ModifyRequest) dsmlMessage;
                requestItems[i] = new Iti59AuditDataset.RequestItem(
                        trimToNull(modifyRequest.getRequestID()),
                        RFC3881EventCodes.RFC3881EventActionCodes.UPDATE,
                        modifyRequest.getModification().stream()
                                .filter(x -> ATTR_NAME.equals(x.getName()))
                                .flatMap(x -> x.getValue().stream())
                                .collect(Collectors.toSet()));

            } else if (dsmlMessage instanceof ModifyDNRequest) {
                // TODO ?
            } else if (dsmlMessage instanceof DelRequest) {
                // TODO ?
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
                requestItem.setOutcomeCode(RFC3881EventCodes.RFC3881EventOutcomeCodes.SERIOUS_FAILURE);
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
            AuditorManager.getHpdAuditor().auditIti59(
                    isServerSide(),
                    requestItem.getActionCode(),
                    requestItem.getOutcomeCode(),
                    auditDataset.getUserId(),
                    auditDataset.getUserName(),
                    auditDataset.getServiceEndpointUrl(),
                    auditDataset.getClientIpAddress(),
                    requestItem.getProviderIds(),
                    auditDataset.getPurposesOfUse());
        }
    }

}