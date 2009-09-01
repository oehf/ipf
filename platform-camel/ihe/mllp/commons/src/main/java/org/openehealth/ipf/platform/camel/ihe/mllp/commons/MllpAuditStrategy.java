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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons;

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Auditing strategy interface for IHE PIX/PDQ transactions.
 * 
 * @author Dmytro Rud
 */
public interface MllpAuditStrategy {
    
    /**
     * Creates a new audit dataset instance.
     */
    public MllpAuditDataset createAuditDataset();
    
    
    /**
     * Returns an array containing names of transaction-specific fields
     * that must be present in a complete audit dataset.
     * <p>
     * These field names must have first letters capitalized,
     * e.g. "PatientId". 
     *
     * @param eventTrigger
     *          Event trigger of the current HL7 message, e.g. "A01". 
     * @return
     *          A String array.
     *          
     */
    public String[] getNecessaryFields(String eventTrigger);
    
    
    /**
     * Enriches the given audit dataset with transaction-specific 
     * contents of the request message.
     * @param auditDataset
     *          Audit dataset to be enriched.
     * @param msg
     *          {@link MessageAdapter} representing the message.
     */
    public void enrichAuditDatasetFromRequest(MllpAuditDataset auditDataset, MessageAdapter msg);
    
    
    /**
     * Enriches the given audit dataset with transaction-specific 
     * contents of the response message.
     * @param auditDataset
     *          Audit dataset to be enriched.
     * @param msg
     *          {@link MessageAdapter} representing the message.
     */
    public void enrichAuditDatasetFromResponse(MllpAuditDataset auditDataset, MessageAdapter msg);
    
    
    /**
     * Performs the actual ATNA audit.
     * @param eventOutcome
     *          Transaction completion status.
     * @param auditDataset
     *          Collected audit dataset. 
     */
    public void doAudit(
            RFC3881EventOutcomeCodes eventOutcome, 
            MllpAuditDataset auditDataset);

}
