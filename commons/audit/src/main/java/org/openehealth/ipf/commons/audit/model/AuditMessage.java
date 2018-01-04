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
import org.openehealth.ipf.commons.audit.event.BaseAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.marshal.dicom.Current;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * DICOM AuditMessage basis type. AuditMessage instances should always be built using
 * {@link BaseAuditMessageBuilder} subclasses to
 * ensure compliance with the specification.
 *
 * @author Christian Ohr
 */
@EqualsAndHashCode
public class AuditMessage implements Serializable, Validateable {

    @Getter @Setter
    private EventIdentificationType eventIdentification;

    private List<ActiveParticipantType> activeParticipant;

    @Getter @Setter
    private AuditSourceIdentificationType auditSourceIdentification;
    private List<ParticipantObjectIdentificationType> participantObjectIdentification;

    public List<ActiveParticipantType> getActiveParticipants() {
        if (activeParticipant == null) {
            activeParticipant = new ArrayList<>();
        }
        return this.activeParticipant;
    }

    public List<ActiveParticipantType> findActiveParticipants(Predicate<ActiveParticipantType> selector) {
        return getActiveParticipants().stream()
                .filter(selector)
                .collect(Collectors.toList());
    }

    public List<ParticipantObjectIdentificationType> getParticipantObjectIdentifications() {
        if (participantObjectIdentification == null) {
            participantObjectIdentification = new ArrayList<>();
        }
        return this.participantObjectIdentification;
    }

    public List<ParticipantObjectIdentificationType> findParticipantObjectIdentifications(Predicate<ParticipantObjectIdentificationType> selector) {
        return getParticipantObjectIdentifications().stream()
                .filter(selector)
                .collect(Collectors.toList());
    }

    /**
     * Validates the constructed audit message against the specification, because API does not completely
     * prevent constructing incomplete or inconsistent messages.
     *
     * @throws org.openehealth.ipf.commons.audit.AuditException AuditException in case validation fails
     */
    @Override
    public void validate() {
        if (eventIdentification == null) {
            throw new AuditException("The event must be identified");
        }
        if (auditSourceIdentification == null) {
            throw new AuditException("The event must be have an audit source");
        }
        if (getActiveParticipants().isEmpty()) {
            throw new AuditException("The event must have one or more active participants");
        }

        eventIdentification.validate();
        auditSourceIdentification.validate();
        activeParticipant.forEach(ActiveParticipantType::validate);
        participantObjectIdentification.forEach(ParticipantObjectIdentificationType::validate);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return Current.toString(this, true);
    }
}
