/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.iti8;

import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditDataset;

/**
 * Audit dataset for ITI-8 (PIX Feed).
 * 
 * @author Dmytro Rud
 */
public class Iti8AuditDataset extends MllpAuditDataset {

    /** Patient ID list from PID-3. */
    private String patientId;
    
    /** Old patient ID list from MRG-1 (for A40 only). */
    private String oldPatientId;       

    
    public Iti8AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    
    // ----- getters and setters -----

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setOldPatientId(String oldPatientId) {
        this.oldPatientId = oldPatientId;
    }

    public String getOldPatientId() {
        return oldPatientId;
    }
}
 
