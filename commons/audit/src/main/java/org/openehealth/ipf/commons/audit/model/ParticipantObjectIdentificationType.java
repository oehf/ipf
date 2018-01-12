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
package org.openehealth.ipf.commons.audit.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectDataLifeCycle;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCode;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Christian Ohr
 */
@EqualsAndHashCode
public class ParticipantObjectIdentificationType implements Serializable, Validateable {

    /**
     * Identifies a specific instance of the participant object.
     */
    @Getter
    private final String participantObjectID;

    /**
     * Describes the identifier that is contained in Participant Object ID.
     */
    @Getter
    private final ParticipantObjectIdType participantObjectIDTypeCode;

    /**
     * An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
     */
    @Getter
    @Setter
    private String participantObjectName;

    /**
     * The actual query for a query-type participant object.
     */
    @Getter
    @Setter
    private byte[] participantObjectQuery;

    /**
     * Code for the participant object type being audited. This value is distinct from the user's role
     * or any user relationship to the participant object.
     */
    @Getter
    @Setter
    private ParticipantObjectTypeCode participantObjectTypeCode;

    /**
     * Code representing the functional application role of Participant Object being audited.
     * The ParticipantObjectTypeCodeRole identifies the role that the object played in the event that is being reported.
     * Most events involve multiple participating objects. ParticipantObjectTypeCodeRole identifies which object took which role in the event.
     * It also covers agents, multi-purpose entities, and multi-role entities. For the purpose of the event one primary role is chosen.
     */
    @Getter
    @Setter
    private ParticipantObjectTypeCodeRole participantObjectTypeCodeRole;

    /**
     * Identifier for the data life-cycle stage for the participant object.
     * This can be used to provide an audit trail for data, over time, as it passes through the system.
     */
    @Getter
    @Setter
    private ParticipantObjectDataLifeCycle participantObjectDataLifeCycle;

    /**
     * Denotes policy-defined sensitivity for the Participant Object ID such as VIP, HIV status,
     * mental health status, or similar topics.
     */
    @Getter
    @Setter
    private String participantObjectSensitivity;

    private List<TypeValuePairType> participantObjectDetails;

    private List<DicomObjectDescriptionType> participantObjectDescriptions;


    /**
     * @param participantObjectID         Identifies a specific instance of the participant object.
     * @param participantObjectIDTypeCode Describes the identifier that is contained in Participant Object ID.
     */
    public ParticipantObjectIdentificationType(String participantObjectID, ParticipantObjectIdType participantObjectIDTypeCode) {
        this.participantObjectID = requireNonNull(participantObjectID, "participantObjectID must be not null");
        this.participantObjectIDTypeCode = requireNonNull(participantObjectIDTypeCode, "participantObjectIDTypeCode must be not null");
    }

    /**
     * Implementation-defined data about specific details of the object accessed or used. This element is a Type-value pair.
     * The "type" attribute is an implementation-defined text string. The "value" attribute is base 64 encoded data.
     * The value is suitable for conveying binary data.
     *
     * @return Implementation-defined data about specific details of the object accessed or used
     */
    public List<TypeValuePairType> getParticipantObjectDetails() {
        if (participantObjectDetails == null) {
            participantObjectDetails = new ArrayList<>();
        }
        return this.participantObjectDetails;
    }

    public List<DicomObjectDescriptionType> getParticipantObjectDescriptions() {
        if (participantObjectDescriptions == null) {
            participantObjectDescriptions = new ArrayList<>();
        }
        return this.participantObjectDescriptions;
    }

    /**
     * SOPClass is Required if ParticipantObjectIDTypeCode is (110180, DCM, "Study Instance UID") and any of the optional fields
     * (AccessionNumber, ContainsMPPS, NumberOfInstances, ContainsSOPInstances,Encrypted,Anonymized) are present in this
     * Participant Object. May be present if ParticipantObjectIDTypeCode is (110180, DCM, "Study Instance UID") even though none
     * of the optional fields are present.
     */
    @Override
    public void validate() {
        if (participantObjectIDTypeCode == ParticipantObjectIdTypeCode.StudyInstanceUID &&
                participantObjectDescriptions.isEmpty())
            throw new AuditException("DICOM Object Descriptions must be present for StudyInstanceUID participant object ID types");
    }
}
