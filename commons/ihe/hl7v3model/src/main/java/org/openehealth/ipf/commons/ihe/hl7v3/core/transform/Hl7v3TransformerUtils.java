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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform;

import ca.uhn.hl7v2.model.DataTypeException;
import net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity;
import net.ihe.gazelle.hl7v3.datatypes.ANY;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.datatypes.ST;
import net.ihe.gazelle.hl7v3.datatypes.TS;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Agent;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Device;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Organization;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver;
import net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Agent;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Device;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Organization;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Receiver;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Sender;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Agent;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Device;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Organization;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Receiver;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01Sender;
import net.ihe.gazelle.hl7v3.voc.CommunicationFunctionType;
import net.ihe.gazelle.hl7v3.voc.EntityClassDevice;
import net.ihe.gazelle.hl7v3.voc.EntityClassOrganization;
import net.ihe.gazelle.hl7v3.voc.EntityDeterminer;
import net.ihe.gazelle.hl7v3.voc.NullFlavor;
import net.ihe.gazelle.hl7v3.voc.RoleClassAgent;
import net.ihe.gazelle.hl7v3.voc.RoleClassAssignedEntity;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Helper methods shared by the transformers between the simplified HL7v3 models and the Gazelle JAXB model.
 *
 * @author Christian Ohr
 * @since 6.1
 */
public final class Hl7v3TransformerUtils {

    private Hl7v3TransformerUtils() {
    }

    /* --------------------- Devices (MCCI_MT000100UV01, used in requests) --------------------- */

    public static MCCIMT000100UV01Receiver toMcciMt000100Receiver(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var receiver = new MCCIMT000100UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setTelecom(simpleDevice.getTelecom());
        receiver.setDevice(toMcciMt000100Device(simpleDevice));
        return receiver;
    }

    public static MCCIMT000100UV01Sender toMcciMt000100Sender(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var sender = new MCCIMT000100UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setTelecom(simpleDevice.getTelecom());
        sender.setDevice(toMcciMt000100Device(simpleDevice));
        return sender;
    }

    public static Device fromMcciMt000100Receiver(final MCCIMT000100UV01Receiver receiver) {
        if (receiver == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(receiver.getTelecom());
        fromMcciMt000100Device(receiver.getDevice(), simpleDevice);
        return simpleDevice;
    }

    public static Device fromMcciMt000100Sender(final MCCIMT000100UV01Sender sender) {
        if (sender == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(sender.getTelecom());
        fromMcciMt000100Device(sender.getDevice(), simpleDevice);
        return simpleDevice;
    }

    private static MCCIMT000100UV01Device toMcciMt000100Device(final Device simpleDevice) {
        final var device = new MCCIMT000100UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminer.INSTANCE);
        device.getId().addAll(simpleDevice.getIds());
        device.getName().addAll(simpleDevice.getNames());
        device.setDesc(simpleDevice.getDesc());
        device.getTelecom().addAll(simpleDevice.getDeviceTelecom());
        device.setManufacturerModelName(simpleDevice.getManufacturerModelName());
        device.setSoftwareName(simpleDevice.getSoftwareName());
        if (!simpleDevice.getRepresentedOrganizationIds().isEmpty()) {
            final var organization = new MCCIMT000100UV01Organization();
            organization.setClassCode(EntityClassOrganization.ORG);
            organization.setDeterminerCode(EntityDeterminer.INSTANCE);
            organization.getId().addAll(simpleDevice.getRepresentedOrganizationIds());
            final var agent = new MCCIMT000100UV01Agent();
            agent.setClassCode(RoleClassAgent.AGNT);
            agent.setRepresentedOrganization(organization);
            device.setAsAgent(agent);
        }
        return device;
    }

    private static void fromMcciMt000100Device(final MCCIMT000100UV01Device device, final Device simpleDevice) {
        if (device == null) {
            return;
        }
        simpleDevice.getIds().addAll(device.getId());
        simpleDevice.getNames().addAll(device.getName());
        simpleDevice.setDesc(device.getDesc());
        simpleDevice.getDeviceTelecom().addAll(device.getTelecom());
        simpleDevice.setManufacturerModelName(device.getManufacturerModelName());
        simpleDevice.setSoftwareName(device.getSoftwareName());
        Optional.ofNullable(device.getAsAgent())
                .map(MCCIMT000100UV01Agent::getRepresentedOrganization)
                .ifPresent(organization -> simpleDevice.getRepresentedOrganizationIds().addAll(organization.getId()));
    }

    /* --------------------- Devices (MCCI_MT000300UV01, used in responses) --------------------- */

    public static MCCIMT000300UV01Receiver toMcciMt000300Receiver(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var receiver = new MCCIMT000300UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setTelecom(simpleDevice.getTelecom());
        receiver.setDevice(toMcciMt000300Device(simpleDevice));
        return receiver;
    }

    public static MCCIMT000300UV01Sender toMcciMt000300Sender(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var sender = new MCCIMT000300UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setTelecom(simpleDevice.getTelecom());
        sender.setDevice(toMcciMt000300Device(simpleDevice));
        return sender;
    }

    public static Device fromMcciMt000300Receiver(final MCCIMT000300UV01Receiver receiver) {
        if (receiver == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(receiver.getTelecom());
        fromMcciMt000300Device(receiver.getDevice(), simpleDevice);
        return simpleDevice;
    }

    public static Device fromMcciMt000300Sender(final MCCIMT000300UV01Sender sender) {
        if (sender == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(sender.getTelecom());
        fromMcciMt000300Device(sender.getDevice(), simpleDevice);
        return simpleDevice;
    }

    private static MCCIMT000300UV01Device toMcciMt000300Device(final Device simpleDevice) {
        final var device = new MCCIMT000300UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminer.INSTANCE);
        device.getId().addAll(simpleDevice.getIds());
        device.getName().addAll(simpleDevice.getNames());
        device.setDesc(simpleDevice.getDesc());
        device.getTelecom().addAll(simpleDevice.getDeviceTelecom());
        device.setManufacturerModelName(simpleDevice.getManufacturerModelName());
        device.setSoftwareName(simpleDevice.getSoftwareName());
        if (!simpleDevice.getRepresentedOrganizationIds().isEmpty()) {
            final var organization = new MCCIMT000300UV01Organization();
            organization.setClassCode(EntityClassOrganization.ORG);
            organization.setDeterminerCode(EntityDeterminer.INSTANCE);
            organization.getId().addAll(simpleDevice.getRepresentedOrganizationIds());
            final var agent = new MCCIMT000300UV01Agent();
            agent.setClassCode(RoleClassAgent.AGNT);
            agent.setRepresentedOrganization(organization);
            device.setAsAgent(agent);
        }
        return device;
    }

    private static void fromMcciMt000300Device(final MCCIMT000300UV01Device device, final Device simpleDevice) {
        if (device == null) {
            return;
        }
        simpleDevice.getIds().addAll(device.getId());
        simpleDevice.getNames().addAll(device.getName());
        simpleDevice.setDesc(device.getDesc());
        simpleDevice.getDeviceTelecom().addAll(device.getTelecom());
        simpleDevice.setManufacturerModelName(device.getManufacturerModelName());
        simpleDevice.setSoftwareName(device.getSoftwareName());
        Optional.ofNullable(device.getAsAgent())
                .map(MCCIMT000300UV01Agent::getRepresentedOrganization)
                .ifPresent(organization -> simpleDevice.getRepresentedOrganizationIds().addAll(organization.getId()));
    }

    /* --------------------- Devices (MCCI_MT000200UV01, used in acknowledgements) --------------------- */

    public static MCCIMT000200UV01Receiver toMcciMt000200Receiver(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var receiver = new MCCIMT000200UV01Receiver();
        receiver.setTypeCode(CommunicationFunctionType.RCV);
        receiver.setTelecom(simpleDevice.getTelecom());
        receiver.setDevice(toMcciMt000200Device(simpleDevice));
        return receiver;
    }

    public static MCCIMT000200UV01Sender toMcciMt000200Sender(final Device simpleDevice) {
        if (simpleDevice == null) {
            return null;
        }
        final var sender = new MCCIMT000200UV01Sender();
        sender.setTypeCode(CommunicationFunctionType.SND);
        sender.setTelecom(simpleDevice.getTelecom());
        sender.setDevice(toMcciMt000200Device(simpleDevice));
        return sender;
    }

    public static Device fromMcciMt000200Receiver(final MCCIMT000200UV01Receiver receiver) {
        if (receiver == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(receiver.getTelecom());
        fromMcciMt000200Device(receiver.getDevice(), simpleDevice);
        return simpleDevice;
    }

    public static Device fromMcciMt000200Sender(final MCCIMT000200UV01Sender sender) {
        if (sender == null) {
            return null;
        }
        final var simpleDevice = new Device();
        simpleDevice.setTelecom(sender.getTelecom());
        fromMcciMt000200Device(sender.getDevice(), simpleDevice);
        return simpleDevice;
    }

    private static MCCIMT000200UV01Device toMcciMt000200Device(final Device simpleDevice) {
        final var device = new MCCIMT000200UV01Device();
        device.setClassCode(EntityClassDevice.DEV);
        device.setDeterminerCode(EntityDeterminer.INSTANCE);
        device.getId().addAll(simpleDevice.getIds());
        device.getName().addAll(simpleDevice.getNames());
        device.setDesc(simpleDevice.getDesc());
        device.getTelecom().addAll(simpleDevice.getDeviceTelecom());
        device.setManufacturerModelName(simpleDevice.getManufacturerModelName());
        device.setSoftwareName(simpleDevice.getSoftwareName());
        if (!simpleDevice.getRepresentedOrganizationIds().isEmpty()) {
            final var organization = new MCCIMT000200UV01Organization();
            organization.setClassCode(EntityClassOrganization.ORG);
            organization.setDeterminerCode(EntityDeterminer.INSTANCE);
            organization.getId().addAll(simpleDevice.getRepresentedOrganizationIds());
            final var agent = new MCCIMT000200UV01Agent();
            agent.setClassCode(RoleClassAgent.AGNT);
            agent.setRepresentedOrganization(organization);
            device.setAsAgent(agent);
        }
        return device;
    }

    private static void fromMcciMt000200Device(final MCCIMT000200UV01Device device, final Device simpleDevice) {
        if (device == null) {
            return;
        }
        simpleDevice.getIds().addAll(device.getId());
        simpleDevice.getNames().addAll(device.getName());
        simpleDevice.setDesc(device.getDesc());
        simpleDevice.getDeviceTelecom().addAll(device.getTelecom());
        simpleDevice.setManufacturerModelName(device.getManufacturerModelName());
        simpleDevice.setSoftwareName(device.getSoftwareName());
        Optional.ofNullable(device.getAsAgent())
                .map(MCCIMT000200UV01Agent::getRepresentedOrganization)
                .ifPresent(organization -> simpleDevice.getRepresentedOrganizationIds().addAll(organization.getId()));
    }

    /* --------------------- Data types --------------------- */

    /**
     * Converts a {@link ZonedDateTime} into a {@link TS}.
     */
    public static TS toTs(final ZonedDateTime dateTime) {
        final var ts = new TS();
        ts.setValue(HL7DTM.toSimpleString(dateTime));
        return ts;
    }

    /**
     * Converts a {@link TS} into a {@link ZonedDateTime}, returning {@code null} if it is missing or unparseable.
     */
    public static ZonedDateTime fromTs(final TS ts) {
        return Optional.ofNullable(ts)
                .map(TS::getValue)
                .map(value -> {
                    try {
                        return HL7DTM.toZonedDateTime(value);
                    } catch (final DataTypeException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    /**
     * Creates an {@link ST} with the given text content.
     */
    public static ST toSt(final String text) {
        final var st = new ST();
        st.mixed = List.of(text);
        return st;
    }

    /**
     * Returns the text content of a data type with mixed content (e.g. {@link ST}), or {@code null}.
     */
    public static String fromMixed(final ANY any) {
        if (any == null || any.mixed == null || any.mixed.isEmpty()) {
            return null;
        }
        return any.mixed.stream()
                .filter(String.class::isInstance)
                .map(Serializable::toString)
                .collect(Collectors.joining());
    }

    /**
     * Returns the given name as {@link PN}, copying it if it is a plain {@link EN} (e.g. when unmarshalled without
     * {@code xsi:type}).
     */
    public static PN toPn(final EN name) {
        if (name == null || name instanceof PN) {
            return (PN) name;
        }
        final var pn = new PN();
        pn.setMixed(name.getMixed());
        pn.setUse(name.getUse());
        pn.setNullFlavor(name.getNullFlavor());
        return pn;
    }

    /**
     * Copies the attributes of an {@link II} into a (subclass) instance, e.g. one carrying an update mode.
     */
    public static <T extends II> T copyIi(final II source, final T target) {
        target.setRoot(source.getRoot());
        target.setExtension(source.getExtension());
        target.setAssigningAuthorityName(source.getAssigningAuthorityName());
        target.setDisplayable(source.getDisplayable());
        target.setNullFlavor(source.getNullFlavor());
        return target;
    }

    /**
     * Creates an {@link II} with a null flavor of "NA" (not applicable).
     */
    public static II notApplicableIi() {
        final var ii = new II();
        ii.setNullFlavor(NullFlavor.NA);
        return ii;
    }

    /**
     * Creates the custodian's assigned entity, or returns {@code null} if neither OID nor code is given.
     */
    public static COCTMT090003UV01AssignedEntity toCustodianAssignedEntity(final String oid, final CE code) {
        if (oid == null && code == null) {
            return null;
        }
        final var assignedEntity = new COCTMT090003UV01AssignedEntity();
        assignedEntity.setClassCode(RoleClassAssignedEntity.ASSIGNED);
        if (oid != null) {
            assignedEntity.getId().add(toIi(oid));
        }
        assignedEntity.setCode(code);
        return assignedEntity;
    }

    /**
     * Returns the OID of the custodian's assigned entity, or {@code null}.
     */
    public static String fromCustodianAssignedEntity(final COCTMT090003UV01AssignedEntity assignedEntity) {
        return assignedEntity == null || assignedEntity.getId().isEmpty() ? null : assignedEntity.getId().get(0).getRoot();
    }

    /**
     * Creates an {@link II} with only a root.
     */
    public static II toIi(final String root) {
        return new II(root, null);
    }
}
