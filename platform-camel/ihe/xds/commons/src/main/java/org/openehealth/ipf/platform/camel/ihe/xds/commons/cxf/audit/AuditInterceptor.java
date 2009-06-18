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

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;


/**
 * Base class for all ATNA audit-related CXF interceptors.
 * 
 * @author Dmytro Rud
 */
abstract public class AuditInterceptor extends AbstractPhaseInterceptor<Message> {
    
    private static final transient Log LOG = LogFactory.getLog(AuditInterceptor.class);
    
    
    /**
     * Key used to store audit datasets in CXF exchanges
     */
    public static final String CXF_EXCHANGE_KEY = "atna.audit.dataset";


    /**
     * Audit strategy associated with this interceptor.  
     */
    private AuditStrategy auditStrategy; 

    
    /**
     * Constructor which sets a strategy.
     * 
     * @param auditStrategy
     *      an audit strategy instance.  
     *      <p><code>null</code> values are explicitly prohibited. 
     */
    public AuditInterceptor(String phase, AuditStrategy auditStrategy) {
        super(phase);
        Validate.notNull(auditStrategy);
        this.auditStrategy = auditStrategy;
    }
    
    
    /**
     * Returns an audit dataset instance which corresponds to the given message.
     * <p>
     * When no such instance is currently associated with the message, a new one 
     * will be created by means of the corresponding {@link AuditStrategy} 
     * and registered in the message's exchange.
     * 
     * @param message
     *      CXF message currently handled by this interceptor
     * @return      
     *      an audit dataset instance, or <code>null</code> when this instance   
     *      could be neither obtained nor created from scratch
     */
    protected AuditDataset getAuditDataset(Message message) {
        AuditDataset auditDataset = (AuditDataset)message.getExchange().get(CXF_EXCHANGE_KEY);
        if(auditDataset == null) {
            auditDataset = getAuditStrategy().createAuditDataset();
            if(auditDataset == null) {
                LOG.warn("Cannot obtain audit dataset instance, NPE is pending");
                return null;
            }
            message.getExchange().put(CXF_EXCHANGE_KEY, auditDataset);
        }
        return auditDataset;
    }
    
    
    /**
     * Returns the audit strategy associated with this interceptor. 
     * 
     * @return
     *      an audit strategy instance or <code>null</code> when none configured
     */
    public AuditStrategy getAuditStrategy() {
        if(this.auditStrategy == null) {
            LOG.warn("Audit strategy not set, NPE is pending");
        }
        return this.auditStrategy;
    }
    
    
    /**
     * Performs the actual work, being called from {@link #handleMessage(Message)}.   
     * 
     * @param message CXF message to process 
     */
    public abstract void process(Message message) throws Exception;
    
    
    /**
     * Calls {@link #process(Message)} and "forwards" all exceptions
     * to the error log instead of letting them break message flow.
     * 
     * @param message CXF message to process 
     */
    @Override
    public final void handleMessage(Message message) {
        try {
            process(message);
        } catch(Exception e) {
            LOG.error("Fault in audit interceptor", e);
        }
    }
}
