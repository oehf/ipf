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
package org.openehealth.ipf.commons.ihe.ws.cxf.audit;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * Basis for Strategy pattern implementation for ATNA Auditing
 * in Web Service-based IHE transactions.
 *
 * @param <T>
 *     type of audit dataset.
 *
 * @author Dmytro Rud
 */
public abstract class WsAuditStrategy<T extends WsAuditDataset> {
    private static final transient Log LOG = LogFactory.getLog(WsAuditStrategy.class);

    /**
     * Whether this is a server-side or a client-side strategy. 
     * Server side is where the response is generated, i.e. asynchronous
     * response receivers must use <code>false</code> here.
     */
    private boolean serverSide;

    /**
     * Whether this strategy should allow incomplete audit records.
     */
    private boolean allowIncompleteAudit;
    

    /**
     * Constructs an audit strategy.
     *   
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     *      Server side is where the response is generated, i.e. asynchronous
     *      response receivers must use <code>false</code> here.
     * @param allowIncompleteAudit
     *      whether this strategy should allow incomplete audit records
     *      (parameter initially configurable via endpoint URL).
     */
    public WsAuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        setServerSide(serverSide);
        setAllowIncompleteAudit(allowIncompleteAudit);
    }
    

    /**
     * Creates a new audit dataset audit instance. 
     * 
     * @return
     *      newly created audit dataset
     */
    public abstract T createAuditDataset();

    
    /**
     * Enriches the dataset with transaction-specific information from the given request POJO.
     *
     * @param request
     *      request as POJO extracted from the message
     * @param auditDataset
     *      audit dataset to be enriched
     * @throws Exception
     *      any exception that occurred during this operation
     */
    public abstract void enrichDatasetFromRequest(Object request, T auditDataset)
        throws Exception;


    /**
     * Enriches the dataset with transaction-specific information from the given response POJO.
     *
     * @param response
     *      response as POJO extracted from the message
     * @param auditDataset
     *      audit dataset to be enriched
     * @throws Exception
     *      any exception that occurred during this operation
     */
    public abstract void enrichDatasetFromResponse(Object response, T auditDataset)
        throws Exception;


    /**
     * Performs transaction-specific auditing using 
     * information containing in the dataset.
     *   
     * @param auditDataset
     *      audit dataset with all the information needed
     * @throws Exception
     *      any exception that occurred during this operation
     */
    public abstract void doAudit(T auditDataset)
        throws Exception;

    
    /**
     * Checks whether the audit can be performed and calls {@link #doAudit}  
     * if the answer is positive. 
     * <p>
     * Audit can be performed when all necessary data is present or
     * when the user allows us to audit with incomplete data,
     * @see #allowIncompleteAudit
     * 
     * @param auditDataset
     *      audit dataset  
     * @throws Exception
     *      any exception that occurred during auditing
     */
    public void audit(T auditDataset) throws Exception {
        Set<String> missing = auditDataset.checkFields(getNecessaryAuditFieldNames(), true);
        if(! missing.isEmpty()) {
            StringBuilder sb = new StringBuilder("Missing audit fields: ");
            for(String fieldName : missing) {
                sb.append(fieldName).append(", ");
            }
            sb.append(isAllowIncompleteAudit() ? 
                "but incomplete audit is allowed, so we'll perform it." :
                "auditing not possible.");
            LOG.error(sb.toString());
        }
        if(missing.isEmpty() || isAllowIncompleteAudit()) {
            doAudit(auditDataset);
        }
    }


    /**
     * Determines whether the given response finalizes the interaction
     * and thus the ATNA auditing should be finalized as well.
     * <p>
     * Per default always returns <code>true</code>.
     *
     * @param response
     *      response in transaction-specific format (POJO, XML string, etc.).
     * @return
     *      <code>true</code> when this response finalizes the interaction.
     */
    public boolean isAuditableResponse(Object response) {
        return true;
    }

    
    /**
     * Returns a transaction-specific list of names of fields 
     * a "complete" audit dataset must contain. 
     *  
     * @return
     *      list of field names as a string array
     */
    public abstract String[] getNecessaryAuditFieldNames();
    
    
    /**
     * Determines which RFC 3881 event outcome code corresponds to the
     * given response POJO.  
     * @param response
     *      response ebXML POJO.
     * @return
     *      RFC 3881 event outcome code.
     */
    public abstract RFC3881EventOutcomeCodes getEventOutcomeCode(Object response);
    

    /* ----- automatically generated getters and setters ----- */

    /**
     * Defines whether this is a server-side or a client-side strategy.
     * @param serverSide
     *          whether this is a server-side or a client-side strategy.
     */
    public void setServerSide(boolean serverSide) {
        this.serverSide = serverSide;
    }

    /**
     * @return whether this is a server-side or a client-side strategy.
     */
    public boolean isServerSide() {
        return serverSide;
    }

    /**
     * Defines whether this strategy should allow incomplete audit records.
     * @param allowIncompleteAudit
     *          whether this strategy should allow incomplete audit records.
     */
    public void setAllowIncompleteAudit(boolean allowIncompleteAudit) {
        this.allowIncompleteAudit = allowIncompleteAudit;
    }

    /**
     * @return whether this strategy should allow incomplete audit records.
     */
    public boolean isAllowIncompleteAudit() {
        return allowIncompleteAudit;
    }
}
