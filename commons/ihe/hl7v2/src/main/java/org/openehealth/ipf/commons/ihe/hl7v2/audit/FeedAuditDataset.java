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
package org.openehealth.ipf.commons.ihe.hl7v2.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditContext;

/**
 *
 * @author Dmytro Rud
 */
@SuppressWarnings("serial")
public class FeedAuditDataset extends MllpAuditDataset {

    /**
     * Patient ID list from PID-3 in HL7 CX format
     */
    @Getter @Setter
    private String patientId;

    /**
     * Old patient ID list from MRG-1 in HL7 CX format (for A40 only).
     */
    @Getter @Setter
    private String oldPatientId;

    public FeedAuditDataset(boolean serverSide) {
        super(serverSide);
    }

}
