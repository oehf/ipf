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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit;

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Basis for Strategy pattern implementation for ATNA Auditing.
 * 
 * @author Dmytro Rud
 */
public interface AuditStrategy {
    
    // TODO: externalize constant
    public static final String HOME_COMMUNITY_ID = "";
    

    /**
     * Creates a new instance of transaction-specific audit dataset. 
     * 
     * @return
     *      newly created audit dataset
     */
    public AuditDataset createAuditDataset();

    
    /**
     * Returns true when audit item must include message payload dump. 
     * 
     * @return
     *      <code>true</code> when payload should be saved; <code>false</code> otherwise        
     */
    public boolean needSavePayload();
    
    
    /**
     * Enriches the dataset with transaction-specific information from the given POJO.
     *   
     * @param pojo
     *      POJO extracted from the message
     * @param auditDataset
     *      audit dataset to be enriched
     */
    public void enrichDataset(Object pojo, AuditDataset auditDataset);
    
    
    /**
     * Performs transaction-specific auditing using 
     * information containing in the dataset.
     *   
     * @param eventOutcome
     *      event outcome code as defined in RFC 3881
     * @param auditDataset
     *      audit dataset with all the information needed 
     */
    public void doAudit(RFC3881EventOutcomeCodes eventOutcome, AuditDataset auditDataset);
}
