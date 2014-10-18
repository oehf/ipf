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

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final transient Logger LOG = LoggerFactory.getLogger(WsAuditStrategy.class);

    /**
     * Whether this is a server-side or a client-side strategy. 
     * Server side is where the response is generated, i.e. asynchronous
     * response receivers must use <code>false</code> here.
     */
    @Getter @Setter private boolean serverSide;


    /**
     * Constructs an audit strategy.
     *   
     * @param serverSide
     *      whether this is a server-side or a client-side strategy.
     *      Server side is where the response is generated, i.e. asynchronous
     *      response receivers must use <code>false</code> here.
     */
    public WsAuditStrategy(boolean serverSide) {
        setServerSide(serverSide);
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
     * Simply calls {@link #doAudit}.
     * @param auditDataset
     *      audit dataset  
     */
    public void audit(T auditDataset) throws Exception {
        doAudit(auditDataset);
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
     * Determines which RFC 3881 event outcome code corresponds to the
     * given response POJO.  
     * @param response
     *      response ebXML POJO.
     * @return
     *      RFC 3881 event outcome code.
     */
    public abstract RFC3881EventOutcomeCodes getEventOutcomeCode(Object response);
    
}
