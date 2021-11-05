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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import ca.uhn.hl7v2.model.DataTypeException;
import net.ihe.gazelle.hl7v3.datatypes.*;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Device;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender;
import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess;
import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02DataSource;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02ParameterList;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02PatientIdentifier;
import net.ihe.gazelle.hl7v3.prpamt201307UV02.PRPAMT201307UV02QueryByParameter;
import net.ihe.gazelle.hl7v3.voc.*;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;

import java.util.List;
import java.util.Optional;

/**
 * Transformer between objects {@link PixV3QueryRequest} and {@link PRPAIN201309UV02Type}.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
public class PixV3QueryRequestTransformer {

    /**
     * Transforms a simplified query into the full PRPA model.
     *
     * @param simpleQuery The simplified query to transform.
     * @return the PRPA model query or {@code null} if the input was {@code null}.
     */
    public PRPAIN201309UV02Type toPrpa(final PixV3QueryRequest simpleQuery) {
        if (simpleQuery == null) {
            return null;
        }

        // Prepare query with fixed values
        final var query = new PRPAIN201309UV02Type();
        query.setITSVersion("XML_1.0");
        query.setInteractionId(new II("2.16.840.1.113883.1.18", "PRPA_IN201309UV02"));
        query.setProcessingCode(new CS("P", null, null));
        query.setProcessingModeCode(new CS("T", null, null));
        query.setAcceptAckCode(new CS("AL", null, null));

        final var controlActProcess =
                new PRPAIN201309UV02QUQIMT021001UV01ControlActProcess();
        controlActProcess.setClassCode(ActClassControlAct.CACT);
        controlActProcess.setMoodCode(XActMoodIntentEvent.EVN);
        controlActProcess.setCode(new CD("PRPA_TE201309UV02", null, null));
        query.setControlActProcess(controlActProcess);

        final var queryByParameter = new PRPAMT201307UV02QueryByParameter();
        queryByParameter.setStatusCode(new CS("new", null, null));
        queryByParameter.setResponsePriorityCode(new CS("I", null, null));
        queryByParameter.setParameterList(new PRPAMT201307UV02ParameterList());
        controlActProcess.setQueryByParameter(queryByParameter);

        // Fill query with given data
        query.setCreationTime(new TS());
        query.getCreationTime().setValue(HL7DTM.toSimpleString(simpleQuery.getCreationTime()));
        query.setId(simpleQuery.getMessageId());
        query.setReceiver(List.of(this.toReceiver(simpleQuery.getReceiver())));
        query.setSender(this.toSender(simpleQuery.getSender()));
        queryByParameter.setQueryId(simpleQuery.getQueryId());
        final var patientIdentifier = new PRPAMT201307UV02PatientIdentifier();
        patientIdentifier.setValue(List.of(simpleQuery.getQueryPatientId()));
        patientIdentifier.setSemanticsText(new ST());
        patientIdentifier.getSemanticsText().mixed = List.of("Patient.id");
        queryByParameter.getParameterList().setPatientIdentifier(List.of(patientIdentifier));
        for (final var dataSourceOid : simpleQuery.getDataSourceOids()) {
            final var dataSource = new PRPAMT201307UV02DataSource();
            dataSource.setValue(List.of(new II(dataSourceOid, null)));
            dataSource.setSemanticsText(new ST());
            dataSource.getSemanticsText().mixed = List.of("DataSource.id");
            queryByParameter.getParameterList().getDataSource().add(dataSource);
        }

        return query;
    }

    /**
     * Transforms a full PRPA query into the simplified model. Data loss can occur, as the simplified model is not
     * complete.
     *
     * @param query The full PRPA query to transform.
     * @return the simplified query or {@code null} if the input was {@code null}.
     */
    public PixV3QueryRequest fromPrpa(final PRPAIN201309UV02Type query) {
        if (query == null) {
            return null;
        }

        final var simpleQuery = new PixV3QueryRequest();
        simpleQuery.setMessageId(query.getId());
        simpleQuery.setSender(this.fromSender(query.getSender()));
        if (!query.getReceiver().isEmpty()) {
            simpleQuery.setReceiver(this.fromReceiver(query.getReceiver().get(0)));
        }
        simpleQuery.setCreationTime(Optional.ofNullable(query.getCreationTime())
                .map(TS::getValue)
                .map(value -> {
                    try {
                        return HL7DTM.toZonedDateTime(value);
                    } catch (final DataTypeException e) {
                        return null;
                    }
                })
                .orElse(null));

        if (query.getControlActProcess() == null) {
            return simpleQuery;
        }
        final var controlActProcess = query.getControlActProcess();

        if (controlActProcess.getQueryByParameter() == null) {
            return simpleQuery;
        }
        final var queryByParameter = controlActProcess.getQueryByParameter();
        simpleQuery.setQueryId(queryByParameter.getQueryId());

        if (queryByParameter.getParameterList() == null) {
            return simpleQuery;
        }
        final var parameterList = queryByParameter.getParameterList();
        if (!parameterList.getPatientIdentifier().isEmpty()) {
            final var patientIds = parameterList.getPatientIdentifier().get(0).getValue();
            if (!patientIds.isEmpty()) {
                simpleQuery.setQueryPatientId(patientIds.get(0));
            }
        }
        for (final var dataSource : parameterList.getDataSource()) {
            if (!dataSource.getValue().isEmpty()) {
                simpleQuery.getDataSourceOids().add(dataSource.getValue().get(0).getRoot());
            }
        }

        return simpleQuery;
    }

    private MCCIMT000100UV01Receiver toReceiver(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var receiver = new MCCIMT000100UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setTelecom(simpleDevice.getTelecom());
        final var device = new MCCIMT000100UV01Device();
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

    private MCCIMT000100UV01Sender toSender(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var sender = new MCCIMT000100UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setTelecom(simpleDevice.getTelecom());
        final var device = new MCCIMT000100UV01Device();
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

    public Device fromReceiver(final MCCIMT000100UV01Receiver receiver) {
        if (receiver == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(receiver.getTelecom());
        final var device = receiver.getDevice();
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

    public Device fromSender(final MCCIMT000100UV01Sender sender) {
        if (sender == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(sender.getTelecom());
        final var device = sender.getDevice();
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
