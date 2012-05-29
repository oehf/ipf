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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.core.atna.AuditorManager;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * ATNA audit strategy base for IHE PIX/PDQ transactions.
 * 
 * @author Dmytro Rud
 */
abstract public class MllpAuditStrategy<T extends MllpAuditDataset> {
    // whether we audit on server (true) or on client (false)
    @Getter(AccessLevel.PROTECTED) private final boolean serverSide;


    /**
     * Constructor.
     * @param serverSide
     *      <code>true</code> when this strategy is a server-side one;
     *      <code>false</code> otherwise.
     */
    protected MllpAuditStrategy(boolean serverSide) {
        this.serverSide = serverSide;
    }


    /**
     * Creates a new audit dataset instance.
     */
    abstract public T createAuditDataset();


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
     */
    abstract public String[] getNecessaryFields(String eventTrigger);
    
    
    /**
     * Enriches the given audit dataset with transaction-specific 
     * contents of the request message and Camel exchange.
     * @param auditDataset
     *      audit dataset to be enriched.
     * @param msg
     *      {@link MessageAdapter} representing the message.
     * @param exchange
     *      Camel exchange
     */
    abstract public void enrichAuditDatasetFromRequest(
            T auditDataset,
            MessageAdapter<?> msg,
            Exchange exchange);
    
    
    /**
     * Enriches the given audit dataset with transaction-specific 
     * contents of the response message.
     * @param auditDataset
     *      audit dataset to be enriched.
     * @param msg
     *      {@link MessageAdapter} representing the message.
     */
    public void enrichAuditDatasetFromResponse(
            T auditDataset,
            MessageAdapter<?> msg)
    {
        // does nothing per default
    }
    
    
    /**
     * Performs the actual ATNA audit.
     * @param eventOutcome
     *          Transaction completion status.
     * @param auditDataset
     *          Collected audit dataset. 
     */
    abstract public void doAudit(
            RFC3881EventOutcomeCodes eventOutcome, 
            T auditDataset);


    /**
     * Audits an authentication node failure.
     * @param hostAddress
     *          the address of the node that is responsible for the failure.
     */
    public void auditAuthenticationNodeFailure(String hostAddress) {
        AuditorManager.getPIXManagerAuditor().auditNodeAuthenticationFailure(
            true, null, getClass().getName(), null, hostAddress, null);
    }
}
