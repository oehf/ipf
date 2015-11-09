/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.atna.iti31;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public class Iti31AuditDataset extends MllpAuditDataset {

    private static final Set<String> CREATES = new HashSet<>(
            Arrays.asList(
                    "A01", "A04", "A05"));
    private static final Set<String> UPDATES = new HashSet<>(
            Arrays.asList(
                    "A02", "A03", "A06", "A07", "A08", "A09", "A10", "A12", "A13", "A14",
                    "A15", "A16", "A25", "A26", "A27", "A32", "A33", "A38", "A44", "A52",
                    "A53", "A54", "A55", "Z99"));
    private static final Set<String> MERGES = new HashSet<>(
            Arrays.asList(
                    "A40"));
    private static final Set<String> DELETES = new HashSet<>(
            Arrays.asList(
                    "A11"));

    /** Patient ID list from PID-3. */
    @Getter @Setter private String patientId;

    /** Old patient ID list from MRG-1 (for A40 only). */
    @Getter @Setter private String oldPatientId;


    public Iti31AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    boolean isCreate() {
        return CREATES.contains(getMessageType());
    }

    boolean isUpdate() {
        return UPDATES.contains(getMessageType());
    }

    boolean isMerge() {
        return MERGES.contains(getMessageType());
    }

    boolean isDelete() {
        return DELETES.contains(getMessageType());
    }
}
