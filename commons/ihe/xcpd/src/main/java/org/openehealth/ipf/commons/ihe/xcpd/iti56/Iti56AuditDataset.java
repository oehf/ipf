/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xcpd.iti56;

import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * ATNA Audit dataset for ITI-56.
 * @author Dmytro Rud
 */
public class Iti56AuditDataset extends WsAuditDataset {

    private String patientId;

    public Iti56AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
