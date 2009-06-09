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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti41.audit;

import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.cxf.audit.AuditDataset;


/**
 * ITI-41 specific Audit Dataset. 
 * 
 * @author Dmytro Rud
 */
public class Iti41AuditDataset extends AuditDataset {

    private String submissionSetUuid;

    public Iti41AuditDataset(boolean isServerSide) {
        super(isServerSide);
    }

    public void setSubmissionSetUuid(String submissionSetUuid) {
        this.submissionSetUuid = submissionSetUuid;
    }
    
    public String getSubmissionSetUuid() {
        return submissionSetUuid;
    }
}
