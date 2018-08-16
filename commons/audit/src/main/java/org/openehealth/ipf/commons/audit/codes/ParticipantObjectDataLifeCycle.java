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
 * Participant Object Data Life Cycle as originally specified in https://tools.ietf.org/html/rfc3881#section-5.5
 * and now maintained in http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5.1.2.
 * This value set is a literal part of the audit schema, ie.e. no other codes may be used.
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum ParticipantObjectDataLifeCycle implements EnumeratedValueSet<Short> {

    Origination(1),
    Import(2),
    Amendment(3),
    Verification(4),
    Translation(5),
    Access(6),
    Deidentification(7),
    Aggregation(8),
    Report(9),
    Export(10),
    Disclosure(11),
    ReceiptOfDisclosure(12),
    Arcchiving(13),
    LogicalDeletion(14),
    PermanentErasure(15);

    @Getter
    private Short value;

    ParticipantObjectDataLifeCycle(int value) {
        this.value = (short) value;
    }

    public static ParticipantObjectDataLifeCycle enumForCode(Short code) {
        return EnumeratedValueSet.enumForCode(ParticipantObjectDataLifeCycle.class, code);
    }
}
