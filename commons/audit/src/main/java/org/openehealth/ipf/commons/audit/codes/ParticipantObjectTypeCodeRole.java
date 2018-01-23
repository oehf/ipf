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

package org.openehealth.ipf.commons.audit.codes;


import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedValueSet;

/**
 * Participant Object Type Role codes as originally specified in https://tools.ietf.org/html/rfc3881#section-5.5
 * and now maintained in http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.1.2.
 * This value set is a literal part of the audit schema, ie.e. no other codes may be used.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum ParticipantObjectTypeCodeRole implements EnumeratedValueSet<Short> {

    /**
     * This object is the patient that is the subject of care related to this event.
     * It is identifiable by patient ID or equivalent. The patient may be either human or animal.
     */
    Patient(1),
    /**
     * This is a location identified as related to the event. This is usually the location where the event took place.
     * Note that for shipping, the usual events are arrival at a location or departure from a location.
     */
    Location(2),

    Report(3),
    Resource(4),
    MasterFile(5),
    User(6),
    List(7),
    Doctor(8),
    Subscriber(9),
    Guarantor(10),
    SecurityUserEntity(11),
    SecurityUserGroup(12),
    SecurityResource(13),
    SecurityGranularityDefinition(14),
    Provider(15),
    DataDestination(16),
    DataRepository(17),
    Schedule(18),
    Customer(19),
    Job(20),
    JobStream(21),
    Table(22),
    RoutingCriteria(23),
    Query(24),

    // Added recently

    DataSource(25),
    ProcessingElement(26);

    @Getter
    private Short value;

    ParticipantObjectTypeCodeRole(int value) {
        this.value = (short) value;
    }

}
