/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.core.atna.custom;

import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.ihe.IHETransactionEventTypeCodes;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.GenericIHEAuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.events.ihe.PatientRecordEvent;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.ParticipantObjectIdentificationType;
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.TypeValuePairType;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

import java.io.UnsupportedEncodingException;

import static org.openehealth.ipf.commons.ihe.core.atna.custom.CustomAuditorUtils.configureEvent;

/**
 * Implementation of HL7v3 Auditors to send audit messages for
 * <ul>
 *     <li>ITI-44 (PIX v3)</li>
 *     <li>ITI-45 (PIX v3)</li>
 *     <li>ITI-46 (PIX v3)</li>
 *     <li>ITI-47 (PDQ v3)</li>
 *     <li>ITI-55 (XCPD)</li>
 *     <li>ITI-56 (XCPD)</li>
 *     <li>PCC-1 (QED)</li>
 * </ul>
 *
 * @author Dmytro Rud
 */
public class Hl7v3Auditor extends IHEAuditor {

    public static Hl7v3Auditor getAuditor() {
        AuditorModuleContext ctx = AuditorModuleContext.getContext();
        return (Hl7v3Auditor) ctx.getAuditor(Hl7v3Auditor.class);
    }


    private void auditIti44(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventActionCodes eventActionCode,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String[] patientIds,
            String messageId)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        PatientRecordEvent event = new PatientRecordEvent(
                true,
                eventOutcome,
                eventActionCode,
                new IHETransactionEventTypeCodes.PatientIdentityFeedV3());

        configureEvent(this, serverSide, event, replyToUri, userName, pixManagerUri, pixManagerUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, messageId);
        audit(event);
    }


    public void auditIti44Add(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String[] patientIds,
            String messageId)
    {
        auditIti44(serverSide, RFC3881EventCodes.RFC3881EventActionCodes.CREATE,
                eventOutcome, replyToUri, userName, pixManagerUri, clientIpAddress,
                patientIds, messageId);
    }


    public void auditIti44Revise(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String[] patientIds,
            String messageId)
    {
        auditIti44(serverSide, RFC3881EventCodes.RFC3881EventActionCodes.UPDATE,
                eventOutcome, replyToUri, userName, pixManagerUri, clientIpAddress,
                patientIds, messageId);
    }


    public void auditIti44Delete(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String oldPatientId,
            String messageId)
    {
        String[] patientIds = new String[] { oldPatientId };
        auditIti44(serverSide, RFC3881EventCodes.RFC3881EventActionCodes.DELETE,
                eventOutcome, replyToUri, userName, pixManagerUri, clientIpAddress,
                patientIds, messageId);
    }


    public void auditIti45(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String queryPayload,
            String[] patientIds)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new IHETransactionEventTypeCodes.PIXQueryV3());

        configureEvent(this, serverSide, event, replyToUri, userName, pixManagerUri, pixManagerUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, null);
        event.addQueryParticipantObject(null, null, payloadBytes(queryPayload), null,
                new IHETransactionEventTypeCodes.PIXQueryV3());
        audit(event);
    }


    public void auditIti46(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pixManagerUri,
            String clientIpAddress,

            String[] patientIds,
            String messageId)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        PatientRecordEvent event = new PatientRecordEvent(
                true,
                eventOutcome,
                RFC3881EventCodes.RFC3881EventActionCodes.READ,
                new IHETransactionEventTypeCodes.PIXUpdateNotificationV3());

        configureEvent(this, serverSide, event, replyToUri, userName, pixManagerUri, pixManagerUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, messageId);
        audit(event);
    }


    public void auditIti47(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String pdSupplierUri,
            String clientIpAddress,

            String queryPayload,
            String[] patientIds)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        // Create and configure query event
        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new IHETransactionEventTypeCodes.PatientDemographicsQueryV3());

        configureEvent(this, serverSide, event, replyToUri, userName, pdSupplierUri, pdSupplierUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, null);
        event.addQueryParticipantObject(null, null, payloadBytes(queryPayload), null,
                new IHETransactionEventTypeCodes.PatientDemographicsQueryV3());
        audit(event);
    }


    public void auditIti55(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String respondingGatewayUri,
            String clientIpAddress,

            String queryPayload,
            String queryId,
            String homeCommunityId,
            String[] patientIds)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new IHETransactionEventTypeCodes.CrossGatewayPatientDiscovery());

        configureEvent(this, serverSide, event, replyToUri, userName, respondingGatewayUri, respondingGatewayUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, null);
        event.addXCPDParticipantObject(queryId, homeCommunityId, payloadBytes(queryPayload));
        audit(event);
    }


    public void auditIti56(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String respondingGatewayUri,
            String clientIpAddress,

            String queryPayload,
            String patientId)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        Iti56QueryEvent event = new Iti56QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.PatientLocationQuery());

        configureEvent(this, serverSide, event, replyToUri, userName, respondingGatewayUri, respondingGatewayUri, clientIpAddress);
        event.addPatientParticipantObject(patientId);
        event.addQueryParametersObject(queryPayload);
        audit(event);
    }


    public void auditPcc1(
            boolean serverSide,
            RFC3881EventCodes.RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String userName,
            String clinicalDataSourceUri,
            String clientIpAddress,

            String queryPayload,
            String queryId,
            String[] patientIds)
    {
        if (! isAuditorEnabled()) {
            return;
        }

        QueryEvent event = new QueryEvent(
                true,
                eventOutcome,
                new CustomIHETransactionEventTypeCodes.QueryExistingData());

        configureEvent(this, serverSide, event, replyToUri, userName, clinicalDataSourceUri, clinicalDataSourceUri, clientIpAddress);
        addPatientParticipantObjects(event, patientIds, null);
        event.addQedParticipantObject(queryId, payloadBytes(queryPayload));
        audit(event);
    }


    protected static byte[] payloadBytes(String payload) {
        if (payload == null) {
            return null;
        }
        try {
            return payload.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return payload.getBytes();
        }
    }


    protected static void addPatientParticipantObjects(
            GenericIHEAuditEventMessage event,
            String[] patientIds,
            String messageId)
    {
        if (! EventUtils.isEmptyOrNull(patientIds)) {
            for (String patientId : patientIds) {
                event.addPatientParticipantObject(patientId);
            }

            if (messageId != null) {
                TypeValuePairType tvp = new TypeValuePairType();
                tvp.setType("II");
                tvp.setValue(payloadBytes(messageId));
                for (ParticipantObjectIdentificationType type : event.getAuditMessage().getParticipantObjectIdentification()) {
                    type.getParticipantObjectDetail().add(tvp);
                }
            }
        }
    }


}
