/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.modules.hl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;

import java.util.Objects;

/**
 * HL7v2 segment finder.
 *
 * @author Dmytro Rud
 * @since 3.7
 */
public class SegmentFinder {

    private final String segmentName;
    private int repetition;

    private SegmentFinder(String segmentName, int repetition) {
        this.segmentName = Objects.requireNonNull(segmentName);
        if (repetition < 0) {
            throw new IllegalArgumentException("Repetition number must be non-negative");
        }
        this.repetition = repetition;
    }

    /**
     * Returns the Nth repetition (0-based) of the segment in the given message, or <code>null</code> if not found.
     */
    public static Segment find(Message message, String segmentName, int repetition) throws HL7Exception {
        Objects.requireNonNull(message);
        SegmentFinder segmentFinder = new SegmentFinder(segmentName, repetition);
        return segmentFinder.find(message);
    }

    private Segment find(Group group) throws HL7Exception {
        for (String name : group.getNames()) {
            Structure[] structures = group.getAll(name);
            if (structures.length > 0) {
                if ((structures[0] instanceof Segment) && segmentName.equals(structures[0].getName())) {
                    if (structures.length > repetition) {
                        return (Segment) structures[repetition];
                    } else {
                        repetition -= structures.length;
                    }
                } else if (structures[0] instanceof Group) {
                    for (Structure structure : structures) {
                        Segment segment = find((Group) structure);
                        if (segment != null) {
                            return segment;
                        }
                    }
                }
            }
        }
        return null;
    }

}
