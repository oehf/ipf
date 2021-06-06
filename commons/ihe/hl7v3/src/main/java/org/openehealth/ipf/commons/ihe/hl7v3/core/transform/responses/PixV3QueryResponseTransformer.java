/*
 * Copyright 2021 the original author or authors.
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

import ca.uhn.hl7v2.model.DataTypeException;
import net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity;
import net.ihe.gazelle.hl7v3.datatypes.*;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.*;
import net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian;
import net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01QueryAck;
import net.ihe.gazelle.hl7v3.prpain201310UV02.*;
import net.ihe.gazelle.hl7v3.prpamt201304UV02.PRPAMT201304UV02Patient;
import net.ihe.gazelle.hl7v3.prpamt201304UV02.PRPAMT201304UV02Person;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02DataSource;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02ParameterList;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02PatientIdentifier;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02QueryByParameter;
import net.ihe.gazelle.hl7v3.voc.*;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;

import java.util.List;
import java.util.Optional;

/**
 * Transformer between objects {@link PixV3QueryResponse} and {@link PRPAIN201310UV02Type}.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
public class PixV3QueryResponseTransformer {

    /**
     * Transforms a simplified response into the full PRPA model.
     *
     * @param simpleResponse The simplified response to transform.
     * @return the PRPA model response or {@code null} if the input was {@code null}.
     */
    public PRPAIN201310UV02Type toPrpa(final PixV3QueryResponse simpleResponse) {
        if (simpleResponse == null) {
            return null;
        }

        // Prepare response with fixed values
        final PRPAIN201310UV02Type response = new PRPAIN201310UV02Type();
        response.setITSVersion("XML_1.0");
        response.setInteractionId(new II("2.16.840.1.113883.1.18", "PRPA_IN201310UV02"));
        response.setProcessingCode(new CS("P", null, null));
        response.setProcessingModeCode(new CS("T", null, null));
        response.setAcceptAckCode(new CS("NE", null, null));

        final MCCIMT000300UV01Acknowledgement acknowledgement = new MCCIMT000300UV01Acknowledgement();
        response.getAcknowledgement().add(acknowledgement);

        final PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess =
                new PRPAIN201310UV02MFMIMT700711UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(new CD("PRPA_TE201310UV02", null, null));
        response.setControlActProcess(controlActProcess);

        final MFMIMT700711UV01QueryAck queryAck = new MFMIMT700711UV01QueryAck();
        controlActProcess.setQueryAck(queryAck);

        final PRPAMT201307UV02QueryByParameter queryByParameter = new PRPAMT201307UV02QueryByParameter();
        queryByParameter.setStatusCode(new CS("new", null, null));
        queryByParameter.setResponsePriorityCode(new CS("I", null, null));
        queryByParameter.setParameterList(new PRPAMT201307UV02ParameterList());
        controlActProcess.setQueryByParameter(queryByParameter);

        // Fill response with given data
        response.setCreationTime(new TS());
        response.getCreationTime().setValue(HL7DTM.toSimpleString(simpleResponse.getCreationTime()));
        response.setId(simpleResponse.getMessageId());
        response.setReceiver(List.of(toReceiver(simpleResponse.getReceiver())));
        response.setSender(toSender(simpleResponse.getSender()));
        queryByParameter.setQueryId(simpleResponse.getQueryId());
        final PRPAMT201307UV02PatientIdentifier patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.setValue(List.of(simpleResponse.getQueryPatientId()));
        patientIdentifier.setSemanticsText(new ST());
        patientIdentifier.getSemanticsText().mixed = List.of("Patient.id");
        queryByParameter.getParameterList().setPatientIdentifier(List.of(patientIdentifier));
        for (final String dataSourceOid : simpleResponse.getDataSourceOids()) {
            final PRPAMT201307UV02DataSource dataSource = new PRPAMT201307UV02DataSource();
            dataSource.setValue(List.of(new II(dataSourceOid, null)));
            dataSource.setSemanticsText(new ST());
            dataSource.getSemanticsText().mixed = List.of("DataSource.id");
            queryByParameter.getParameterList().getDataSource().add(dataSource);
        }
        acknowledgement.setTypeCode(new CS(simpleResponse.getAcknowledgementTypeCode(), null, null));
        acknowledgement.setTargetMessage(new MCCIMT000300UV01TargetMessage());
        acknowledgement.getTargetMessage().setId(simpleResponse.getTargetMessageId());
        acknowledgement.getAcknowledgementDetail().addAll(simpleResponse.getAcknowledgementDetails());
        queryAck.setQueryId(simpleResponse.getQueryId());
        queryAck.setQueryResponseCode(new CS(simpleResponse.getQueryResponseCode(), null, null));
        queryAck.setStatusCode(new CS("deliveredResponse", null, null));

        if (!"OK".equals(simpleResponse.getQueryResponseCode())) {
            // If the query response code is not OK, no RegistrationEvent shall be returned
            return response;
        }

        final PRPAIN201310UV02MFMIMT700711UV01Subject1 subject = new PRPAIN201310UV02MFMIMT700711UV01Subject1();
        subject.setTypeCode("SUBJ");
        controlActProcess.getSubject().add(subject);

        final PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationEvent =
                new PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent();
        registrationEvent.setStatusCode(new CS("ACTIVE", null, null));
        registrationEvent.setClassCode(ActClass.REG);
        registrationEvent.setMoodCode(ActMood.EVN);
        subject.setRegistrationEvent(registrationEvent);

        final PRPAIN201310UV02MFMIMT700711UV01Subject2 subject1 = new PRPAIN201310UV02MFMIMT700711UV01Subject2();
        subject1.setTypeCode(ParticipationTargetSubject.SBJ);
        registrationEvent.setSubject1(subject1);

        final PRPAMT201304UV02Patient patient = new PRPAMT201304UV02Patient();
        patient.setClassCode("PAT");
        patient.setStatusCode(new CS("ACTIVE", null, null));
        patient.setId(simpleResponse.getPatientIds());
        patient.setProviderOrganization(simpleResponse.getProviderOrganization());
        subject1.setPatient(patient);

        final PRPAMT201304UV02Person person = new PRPAMT201304UV02Person();
        person.setClassCode(EntityClass.PSN);
        person.setDeterminerCode(EntityDeterminer.INSTANCE);
        if (simpleResponse.getPersonName() != null) {
            person.getName().add(simpleResponse.getPersonName());
        }
        person.getId().addAll(simpleResponse.getPersonIds());
        person.getAsOtherIDs().addAll(simpleResponse.getAsOtherIDs());
        patient.setPatientPerson(person);

        final MFMIMT700711UV01Custodian custodian = new MFMIMT700711UV01Custodian();
        custodian.setTypeCode(ParticipationType.CST);
        registrationEvent.setCustodian(custodian);

        final COCTMT090003UV01AssignedEntity assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
        assignedEntity.getId().add(new II(simpleResponse.getCustodianOid(), null));
        custodian.setAssignedEntity(assignedEntity);

        return response;
    }

    /**
     * Transforms a full PRPA response into the simplified model. Data loss can occur, as the simplified model is not
     * complete.
     *
     * @param response The full PRPA response to transform.
     * @return the simplified response or {@code null} if the input was {@code null}.
     */
    public PixV3QueryResponse fromPrpa(final PRPAIN201310UV02Type response) {
        if (response == null) {
            return null;
        }

        final PixV3QueryResponse simpleResponse = new PixV3QueryResponse();
        simpleResponse.setMessageId(response.getId());
        simpleResponse.setSender(this.fromSender(response.getSender()));
        if (!response.getReceiver().isEmpty()) {
            simpleResponse.setReceiver(this.fromReceiver(response.getReceiver().get(0)));
        }
        simpleResponse.setCreationTime(Optional.ofNullable(response.getCreationTime())
                .map(TS::getValue)
                .map(value -> {
                    try {
                        return HL7DTM.toZonedDateTime(value);
                    } catch (final DataTypeException e) {
                        return null;
                    }
                })
                .orElse(null));

        if (!response.getAcknowledgement().isEmpty()) {
            final MCCIMT000300UV01Acknowledgement acknowledgement = response.getAcknowledgement().get(0);
            simpleResponse.setAcknowledgementTypeCode(acknowledgement.getTypeCode().getCode());
            if (acknowledgement.getTargetMessage() != null) {
                simpleResponse.setTargetMessageId(acknowledgement.getTargetMessage().getId());
            }
            simpleResponse.getAcknowledgementDetails().addAll(acknowledgement.getAcknowledgementDetail());
        }

        if (response.getControlActProcess() == null) {
            return simpleResponse;
        }
        final PRPAIN201310UV02MFMIMT700711UV01ControlActProcess controlActProcess = response.getControlActProcess();

        if (controlActProcess.getQueryByParameter() == null) {
            return simpleResponse;
        }
        final PRPAMT201307UV02QueryByParameter queryByParameter = controlActProcess.getQueryByParameter();
        simpleResponse.setQueryId(queryByParameter.getQueryId());

        if (queryByParameter.getParameterList() == null) {
            return simpleResponse;
        }
        final PRPAMT201307UV02ParameterList parameterList = queryByParameter.getParameterList();
        if (!parameterList.getPatientIdentifier().isEmpty()) {
            final List<II> patientIds = parameterList.getPatientIdentifier().get(0).getValue();
            if (!patientIds.isEmpty()) {
                simpleResponse.setQueryPatientId(patientIds.get(0));
            }
        }
        for (final PRPAMT201307UV02DataSource dataSource : parameterList.getDataSource()) {
            if (!dataSource.getValue().isEmpty()) {
                simpleResponse.getDataSourceOids().add(dataSource.getValue().get(0).getRoot());
            }
        }

        final MFMIMT700711UV01QueryAck queryAck = controlActProcess.getQueryAck();
        if (queryAck != null) {
            simpleResponse.setQueryResponseCode(queryAck.getQueryResponseCode().getCode());
        }

        if (controlActProcess.getSubject().isEmpty()) {
            return simpleResponse;
        }
        final PRPAIN201310UV02MFMIMT700711UV01RegistrationEvent registrationEvent =
                Optional.ofNullable(controlActProcess.getSubject().get(0).getRegistrationEvent()).orElse(null);
        if (registrationEvent == null) {
            return simpleResponse;
        }
        simpleResponse.setCustodianOid(Optional.ofNullable(registrationEvent.getCustodian())
                .map(MFMIMT700711UV01Custodian::getAssignedEntity)
                .map(COCTMT090003UV01AssignedEntity::getId)
                .map(list -> list.isEmpty() ? null : list.get(0))
                .map(II::getRoot).orElse(null));
        final PRPAMT201304UV02Patient patient =
                Optional.ofNullable(registrationEvent.getSubject1()).map(PRPAIN201310UV02MFMIMT700711UV01Subject2::getPatient).orElse(null);
        if (patient == null) {
            return simpleResponse;
        }
        simpleResponse.setProviderOrganization(Optional.ofNullable(patient.getProviderOrganization()).orElse(null));
        if (!patient.getId().isEmpty()) {
            simpleResponse.getPatientIds().addAll(patient.getId());
        }
        if (patient.getPatientPerson() != null) {
            if (!patient.getPatientPerson().getName().isEmpty()) {
                simpleResponse.setPersonName(patient.getPatientPerson().getName().get(0));
            }
            simpleResponse.getPersonIds().addAll(patient.getPatientPerson().getId());
            simpleResponse.getAsOtherIDs().addAll(patient.getPatientPerson().getAsOtherIDs());
        }
        return simpleResponse;
    }

    private MCCIMT000300UV01Receiver toReceiver(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final MCCIMT000300UV01Receiver receiver = new MCCIMT000300UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setTelecom(simpleDevice.getTelecom());
        final MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminer.INSTANCE);
        device.getId().addAll(simpleDevice.getIds());
        device.getName().addAll(simpleDevice.getNames());
        device.setDesc(simpleDevice.getDesc());
        device.getTelecom().addAll(simpleDevice.getDeviceTelecom());
        device.setManufacturerModelName(simpleDevice.getManufacturerModelName());
        device.setSoftwareName(simpleDevice.getSoftwareName());
        receiver.setDevice(device);
        return receiver;
    }

    private MCCIMT000300UV01Sender toSender(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setTelecom(simpleDevice.getTelecom());
        final MCCIMT000300UV01Device device = new MCCIMT000300UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminer.INSTANCE);
        device.getId().addAll(simpleDevice.getIds());
        device.getName().addAll(simpleDevice.getNames());
        device.setDesc(simpleDevice.getDesc());
        device.getTelecom().addAll(simpleDevice.getDeviceTelecom());
        device.setManufacturerModelName(simpleDevice.getManufacturerModelName());
        device.setSoftwareName(simpleDevice.getSoftwareName());
        sender.setDevice(device);
        return sender;
    }

    public Device fromReceiver(final MCCIMT000300UV01Receiver receiver) {
        if (receiver == null) {
            return null;
        }
        final Device simpleDevice = new Device();
        simpleDevice.setTelecom(receiver.getTelecom());
        final MCCIMT000300UV01Device device = receiver.getDevice();
        if (device != null) {
            simpleDevice.getIds().addAll(device.getId());
            simpleDevice.getNames().addAll(device.getName());
            simpleDevice.setDesc(device.getDesc());
            simpleDevice.getDeviceTelecom().addAll(device.getTelecom());
            simpleDevice.setManufacturerModelName(device.getManufacturerModelName());
            simpleDevice.setSoftwareName(device.getSoftwareName());
        }
        return simpleDevice;
    }

    public Device fromSender(final MCCIMT000300UV01Sender sender) {
        if (sender == null) {
            return null;
        }
        final Device simpleDevice = new Device();
        simpleDevice.setTelecom(sender.getTelecom());
        final MCCIMT000300UV01Device device = sender.getDevice();
        if (device != null) {
            simpleDevice.getIds().addAll(device.getId());
            simpleDevice.getNames().addAll(device.getName());
            simpleDevice.setDesc(device.getDesc());
            simpleDevice.getDeviceTelecom().addAll(device.getTelecom());
            simpleDevice.setManufacturerModelName(device.getManufacturerModelName());
            simpleDevice.setSoftwareName(device.getSoftwareName());
        }
        return simpleDevice;
    }
}
