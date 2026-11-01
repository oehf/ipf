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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses;

import net.ihe.gazelle.hl7v3.datatypes.CD;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.INT;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Acknowledgement;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01TargetMessage;
import net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian;
import net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01QueryAck;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject1;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject2;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02QueryMatchObservation;
import net.ihe.gazelle.hl7v3.prpamt201310UV02.PRPAMT201310UV02Subject;
import net.ihe.gazelle.hl7v3.voc.ActClass;
import net.ihe.gazelle.hl7v3.voc.ActClassControlAct;
import net.ihe.gazelle.hl7v3.voc.ActClassObservation;
import net.ihe.gazelle.hl7v3.voc.ActMood;
import net.ihe.gazelle.hl7v3.voc.ParticipationTargetSubject;
import net.ihe.gazelle.hl7v3.voc.ParticipationType;
import net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDemographicsQueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PatientDemographicsQueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.PatientTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.AbstractPatientDemographicsQueryRequestTransformer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.*;

/**
 * Base transformer between the simplified patient demographics query response models and
 * {@link PRPAIN201306UV02Type}. Subclasses handle the transaction-specific parts (PDQv3 ITI-47, XCPD ITI-55).
 *
 * @param <R> type of the simplified response model
 * @author Christian Ohr
 * @since 6.1
 */
public abstract class AbstractPatientDemographicsQueryResponseTransformer<
        R extends PatientDemographicsQueryRequest & PatientDemographicsQueryResponse> {

    private final AbstractPatientDemographicsQueryRequestTransformer<? super R> queryTransformer;

    /**
     * @param queryTransformer transformer used for the {@code queryByParameter} element echoed in the response.
     */
    protected AbstractPatientDemographicsQueryResponseTransformer(
            final AbstractPatientDemographicsQueryRequestTransformer<? super R> queryTransformer) {
        this.queryTransformer = Objects.requireNonNull(queryTransformer);
    }

    /**
     * Transforms a simplified response into the full PRPA model.
     *
     * @param simpleResponse The simplified response to transform.
     * @return the PRPA model response or {@code null} if the input was {@code null}.
     */
    public PRPAIN201306UV02Type toPrpa(final R simpleResponse) {
        if (simpleResponse == null) {
            return null;
        }

        final var response = new PRPAIN201306UV02Type();
        response.setITSVersion("XML_1.0");
        response.setId(simpleResponse.getMessageId());
        response.setCreationTime(toTs(simpleResponse.getCreationTime()));
        response.setInteractionId(new II("2.16.840.1.113883.1.6", "PRPA_IN201306UV02"));
        response.setProcessingCode(new CS("P", null, null));
        response.setProcessingModeCode(new CS("T", null, null));
        response.setAcceptAckCode(new CS("NE", null, null));
        if (simpleResponse.getReceiver() != null) {
            response.setReceiver(List.of(toMcciMt000300Receiver(simpleResponse.getReceiver())));
        }
        response.setSender(toMcciMt000300Sender(simpleResponse.getSender()));

        final var acknowledgement = new MCCIMT000300UV01Acknowledgement();
        acknowledgement.setTypeCode(new CS(simpleResponse.getAcknowledgementTypeCode(), null, null));
        acknowledgement.setTargetMessage(new MCCIMT000300UV01TargetMessage());
        acknowledgement.getTargetMessage().setId(simpleResponse.getTargetMessageId());
        acknowledgement.getAcknowledgementDetail().addAll(simpleResponse.getAcknowledgementDetails());
        response.getAcknowledgement().add(acknowledgement);

        final var controlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(new CD("PRPA_TE201306UV02", null, "2.16.840.1.113883.1.6"));
        response.setControlActProcess(controlActProcess);

        // If the query response code is not OK, no RegistrationEvent shall be returned
        if ("OK".equals(simpleResponse.getQueryResponseCode())) {
            for (final var patient : simpleResponse.getPatients()) {
                controlActProcess.getSubject().add(toSubject(patient));
            }
        }

        final var queryAck = new MFMIMT700711UV01QueryAck();
        queryAck.setQueryId(simpleResponse.getQueryId());
        queryAck.setStatusCode(new CS("deliveredResponse", null, null));
        queryAck.setQueryResponseCode(new CS(simpleResponse.getQueryResponseCode(), null, null));
        controlActProcess.setQueryAck(queryAck);
        controlActProcess.setQueryByParameter(queryTransformer.toQueryByParameter(simpleResponse));

        toPrpaSpecific(simpleResponse, response);
        return response;
    }

    /**
     * Transforms a full PRPA response into the simplified model. Data loss can occur, as the simplified model is not
     * complete.
     *
     * @param response The full PRPA response to transform.
     * @return the simplified response or {@code null} if the input was {@code null}.
     */
    public R fromPrpa(final PRPAIN201306UV02Type response) {
        if (response == null) {
            return null;
        }

        final var simpleResponse = newSimpleResponse();
        simpleResponse.setMessageId(response.getId());
        simpleResponse.setCreationTime(fromTs(response.getCreationTime()));
        simpleResponse.setSender(fromMcciMt000300Sender(response.getSender()));
        if (!response.getReceiver().isEmpty()) {
            simpleResponse.setReceiver(fromMcciMt000300Receiver(response.getReceiver().get(0)));
        }

        if (!response.getAcknowledgement().isEmpty()) {
            final var acknowledgement = response.getAcknowledgement().get(0);
            if (acknowledgement.getTypeCode() != null) {
                simpleResponse.setAcknowledgementTypeCode(acknowledgement.getTypeCode().getCode());
            }
            if (acknowledgement.getTargetMessage() != null) {
                simpleResponse.setTargetMessageId(acknowledgement.getTargetMessage().getId());
            }
            simpleResponse.getAcknowledgementDetails().addAll(acknowledgement.getAcknowledgementDetail());
        }

        final var controlActProcess = response.getControlActProcess();
        if (controlActProcess != null) {
            queryTransformer.fromQueryByParameter(controlActProcess.getQueryByParameter(), simpleResponse);
            final var queryAck = controlActProcess.getQueryAck();
            if (queryAck != null && queryAck.getQueryResponseCode() != null) {
                simpleResponse.setQueryResponseCode(queryAck.getQueryResponseCode().getCode());
            }
            for (final var subject : controlActProcess.getSubject()) {
                fromSubject(subject).ifPresent(simpleResponse.getPatients()::add);
            }
        }

        fromPrpaSpecific(response, simpleResponse);
        return simpleResponse;
    }

    /**
     * @return a new instance of the simplified response model.
     */
    protected abstract R newSimpleResponse();

    /**
     * Hook for transaction-specific parts of the message.
     */
    protected void toPrpaSpecific(final R simpleResponse, final PRPAIN201306UV02Type response) {
    }

    /**
     * Hook for transaction-specific parts of the message.
     */
    protected void fromPrpaSpecific(final PRPAIN201306UV02Type response, final R simpleResponse) {
    }

    /**
     * @return the custodian code to emit for the given patient. By default, the one set in the patient.
     */
    protected CE custodianCode(final MatchedPatient patient) {
        return patient.getCustodianCode();
    }

    private PRPAIN201306UV02MFMIMT700711UV01Subject1 toSubject(final MatchedPatient simplePatient) {
        final var patient = PatientTransformer.toPrpaMt201310(simplePatient);
        if (simplePatient.getMatchDegree() != null) {
            final var observation = new PRPAMT201310UV02QueryMatchObservation();
            observation.setClassCode(ActClassObservation.COND);
            observation.setMoodCode(ActMood.EVN);
            observation.setCode(new CD("IHE_PDQ", null, null));
            observation.setValue(new INT(simplePatient.getMatchDegree()));
            final var subjectOf1 = new PRPAMT201310UV02Subject();
            subjectOf1.setTypeCode(ParticipationTargetSubject.SBJ);
            subjectOf1.setQueryMatchObservation(observation);
            patient.getSubjectOf1().add(subjectOf1);
        }

        final var subject1 = new PRPAIN201306UV02MFMIMT700711UV01Subject2();
        subject1.setTypeCode(ParticipationTargetSubject.SBJ);
        subject1.setPatient(patient);

        final var registrationEvent = new PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent();
        registrationEvent.setClassCode(ActClass.REG);
        registrationEvent.setMoodCode(ActMood.EVN);
        registrationEvent.getId().add(notApplicableIi());
        registrationEvent.setStatusCode(new CS("active", null, null));
        registrationEvent.setSubject1(subject1);

        final var assignedEntity = toCustodianAssignedEntity(simplePatient.getCustodianOid(), custodianCode(simplePatient));
        if (assignedEntity != null) {
            final var custodian = new MFMIMT700711UV01Custodian();
            custodian.setTypeCode(ParticipationType.CST);
            custodian.setAssignedEntity(assignedEntity);
            registrationEvent.setCustodian(custodian);
        }

        final var subject = new PRPAIN201306UV02MFMIMT700711UV01Subject1();
        subject.setTypeCode("SUBJ");
        subject.setRegistrationEvent(registrationEvent);
        return subject;
    }

    private Optional<MatchedPatient> fromSubject(final PRPAIN201306UV02MFMIMT700711UV01Subject1 subject) {
        final var registrationEvent = subject.getRegistrationEvent();
        if (registrationEvent == null) {
            return Optional.empty();
        }
        final var patient = Optional.ofNullable(registrationEvent.getSubject1())
                .map(PRPAIN201306UV02MFMIMT700711UV01Subject2::getPatient)
                .orElse(null);
        final var simplePatient = PatientTransformer.fromPrpaMt201310(patient, new MatchedPatient());
        Optional.ofNullable(registrationEvent.getCustodian())
                .map(MFMIMT700711UV01Custodian::getAssignedEntity)
                .ifPresent(assignedEntity -> {
                    simplePatient.setCustodianOid(fromCustodianAssignedEntity(assignedEntity));
                    simplePatient.setCustodianCode(assignedEntity.getCode());
                });
        if (patient != null) {
            patient.getSubjectOf1().stream()
                    .map(PRPAMT201310UV02Subject::getQueryMatchObservation)
                    .filter(observation -> observation != null && observation.getValue() instanceof INT)
                    .findFirst()
                    .ifPresent(observation -> simplePatient.setMatchDegree(((INT) observation.getValue()).getValue()));
        }
        return Optional.of(simplePatient);
    }
}
