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

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Message;
import org.openehealth.ipf.commons.ihe.ws.cxf.AbstractSafeInterceptor;


/**
 * Base class for all ATNA audit-related CXF interceptors.
 * @author Dmytro Rud
 */
abstract public class AuditInterceptor extends AbstractSafeInterceptor {
    private static final transient Log LOG = LogFactory.getLog(AuditInterceptor.class);

    
    /**
     * Key used to store audit datasets in CXF exchanges
     */
    public static final String CXF_EXCHANGE_KEY = "atna.audit.dataset";


    /**
     * Audit strategy associated with this interceptor.  
     */
    private final WsAuditStrategy auditStrategy;

    
    /**
     * Constructor which sets a strategy.
     * 
     * @param phase
     *          the phase in which to use this interceptor.
     * @param auditStrategy
     *          an audit strategy instance. <p><code>null</code> values are
     *          explicitly prohibited. 
     */
    protected AuditInterceptor(String phase, WsAuditStrategy auditStrategy) {
        super(phase);
        Validate.notNull(auditStrategy);
        this.auditStrategy = auditStrategy;
    }
    
    
    /**
     * Returns an audit dataset instance which corresponds to the given message.
     * <p>
     * When no such instance is currently associated with the message, a new one 
     * will be created by means of the corresponding {@link WsAuditStrategy} 
     * and registered in the message's exchange.
     * 
     * @param message
     *      CXF message currently handled by this interceptor
     * @return      
     *      an audit dataset instance, or <code>null</code> when this instance   
     *      could be neither obtained nor created from scratch
     */
    protected WsAuditDataset getAuditDataset(Message message) {
        WsAuditDataset auditDataset = (WsAuditDataset)message.getExchange().get(CXF_EXCHANGE_KEY);
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
    public WsAuditStrategy getAuditStrategy() {
        if (auditStrategy == null) {
            LOG.warn("Audit strategy not set, NPE is pending");
        }
        return auditStrategy;
    }
}
