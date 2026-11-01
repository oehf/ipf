/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import net.ihe.gazelle.hl7v3.datatypes.CD;
import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mfmimt700701UV01.MFMIMT700701UV01Custodian;
import net.ihe.gazelle.hl7v3.mfmimt700701UV01.MFMIMT700701UV01PriorRegisteredRole;
import net.ihe.gazelle.hl7v3.mfmimt700701UV01.MFMIMT700701UV01PriorRegistration;
import net.ihe.gazelle.hl7v3.mfmimt700701UV01.MFMIMT700701UV01ReplacementOf;
import net.ihe.gazelle.hl7v3.mfmimt700701UV01.MFMIMT700701UV01Subject3;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MFMIMT700701UV01ControlActProcess;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MFMIMT700701UV01RegistrationEvent;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MFMIMT700701UV01Subject1;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MFMIMT700701UV01Subject2;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import net.ihe.gazelle.hl7v3.voc.ActClass;
import net.ihe.gazelle.hl7v3.voc.ActClassControlAct;
import net.ihe.gazelle.hl7v3.voc.ActMood;
import net.ihe.gazelle.hl7v3.voc.ParticipationTargetSubject;
import net.ihe.gazelle.hl7v3.voc.ParticipationType;
import net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.PatientTransformer;

import java.util.List;
import java.util.Objects;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.*;

/**
 * Transformer between objects {@link PatientDuplicatesResolvedRequest} and {@link PRPAIN201304UV02Type}
 * (Patient Registry Duplicates Resolved, ITI-44).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class PatientDuplicatesResolvedTransformer {

    /**
     * Transforms a simplified message into the full PRPA model.
     *
     * @param simpleRequest The simplified message to transform.
     * @return the PRPA model message or {@code null} if the input was {@code null}.
     */
    public PRPAIN201304UV02Type toPrpa(final PatientDuplicatesResolvedRequest simpleRequest) {
        if (simpleRequest == null) {
            return null;
        }

        final var request = new PRPAIN201304UV02Type();
        request.setITSVersion("XML_1.0");
        request.setId(simpleRequest.getMessageId());
        request.setCreationTime(toTs(simpleRequest.getCreationTime()));
        request.setInteractionId(new II("2.16.840.1.113883.1.6", "PRPA_IN201304UV02"));
        request.setProcessingCode(new CS("P", null, null));
        request.setProcessingModeCode(new CS("T", null, null));
        request.setAcceptAckCode(new CS("AL", null, null));
        if (simpleRequest.getReceiver() != null) {
            request.setReceiver(List.of(toMcciMt000100Receiver(simpleRequest.getReceiver())));
        }
        request.setSender(toMcciMt000100Sender(simpleRequest.getSender()));

        final var simplePatient = simpleRequest.getPatient();
        final var subject1 = new PRPAIN201304UV02MFMIMT700701UV01Subject2();
        subject1.setTypeCode(ParticipationTargetSubject.SBJ);
        subject1.setPatient(PatientTransformer.toPrpaMt201303(simplePatient));

        final var registrationEvent = new PRPAIN201304UV02MFMIMT700701UV01RegistrationEvent();
        registrationEvent.setClassCode(ActClass.REG);
        registrationEvent.setMoodCode(ActMood.EVN);
        registrationEvent.getId().add(notApplicableIi());
        registrationEvent.setStatusCode(new CS("active", null, null));
        registrationEvent.setSubject1(subject1);
        final var assignedEntity = toCustodianAssignedEntity(simplePatient.getCustodianOid(), null);
        if (assignedEntity != null) {
            final var custodian = new MFMIMT700701UV01Custodian();
            custodian.setTypeCode(ParticipationType.CST);
            custodian.setAssignedEntity(assignedEntity);
            registrationEvent.setCustodian(custodian);
        }

        if (!simpleRequest.getObsoletePatientIds().isEmpty()) {
            final var priorRegisteredRole = new MFMIMT700701UV01PriorRegisteredRole();
            priorRegisteredRole.setClassCode("PAT");
            priorRegisteredRole.getId().addAll(simpleRequest.getObsoletePatientIds());
            final var priorSubject = new MFMIMT700701UV01Subject3();
            priorSubject.setTypeCode(ParticipationTargetSubject.SBJ);
            priorSubject.setPriorRegisteredRole(priorRegisteredRole);
            final var priorRegistration = new MFMIMT700701UV01PriorRegistration();
            priorRegistration.setClassCode(ActClass.REG);
            priorRegistration.setMoodCode(ActMood.EVN);
            priorRegistration.setStatusCode(new CS("obsolete", null, null));
            priorRegistration.setSubject1(priorSubject);
            final var replacementOf = new MFMIMT700701UV01ReplacementOf();
            replacementOf.setTypeCode("RPLC");
            replacementOf.setPriorRegistration(priorRegistration);
            registrationEvent.getReplacementOf().add(replacementOf);
        }

        final var subject = new PRPAIN201304UV02MFMIMT700701UV01Subject1();
        subject.setTypeCode("SUBJ");
        subject.setRegistrationEvent(registrationEvent);

        final var controlActProcess = new PRPAIN201304UV02MFMIMT700701UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(new CD("PRPA_TE201304UV02", null, "2.16.840.1.113883.1.6"));
        controlActProcess.getSubject().add(subject);
        request.setControlActProcess(controlActProcess);
        return request;
    }

    /**
     * Transforms a full PRPA message into the simplified model. Data loss can occur, as the simplified model is not
     * complete. Only the first subject is considered.
     *
     * @param request The full PRPA message to transform.
     * @return the simplified message or {@code null} if the input was {@code null}.
     */
    public PatientDuplicatesResolvedRequest fromPrpa(final PRPAIN201304UV02Type request) {
        if (request == null) {
            return null;
        }

        final var simpleRequest = new PatientDuplicatesResolvedRequest();
        simpleRequest.setMessageId(request.getId());
        simpleRequest.setCreationTime(fromTs(request.getCreationTime()));
        simpleRequest.setSender(fromMcciMt000100Sender(request.getSender()));
        if (!request.getReceiver().isEmpty()) {
            simpleRequest.setReceiver(fromMcciMt000100Receiver(request.getReceiver().get(0)));
        }

        if (request.getControlActProcess() == null || request.getControlActProcess().getSubject().isEmpty()) {
            return simpleRequest;
        }
        final var registrationEvent = request.getControlActProcess().getSubject().get(0).getRegistrationEvent();
        if (registrationEvent == null) {
            return simpleRequest;
        }
        if (registrationEvent.getSubject1() != null) {
            PatientTransformer.fromPrpaMt201303(registrationEvent.getSubject1().getPatient(), simpleRequest.getPatient());
        }
        if (registrationEvent.getCustodian() != null) {
            simpleRequest.getPatient().setCustodianOid(
                    fromCustodianAssignedEntity(registrationEvent.getCustodian().getAssignedEntity()));
        }

        registrationEvent.getReplacementOf().stream()
                .map(MFMIMT700701UV01ReplacementOf::getPriorRegistration)
                .filter(Objects::nonNull)
                .map(MFMIMT700701UV01PriorRegistration::getSubject1)
                .filter(subject -> subject != null && subject.getPriorRegisteredRole() != null)
                .forEach(subject -> simpleRequest.getObsoletePatientIds().addAll(subject.getPriorRegisteredRole().getId()));
        return simpleRequest;
    }
}
