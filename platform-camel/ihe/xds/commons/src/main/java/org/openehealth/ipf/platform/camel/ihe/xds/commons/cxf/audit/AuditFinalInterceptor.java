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

import java.util.List;

import org.apache.cxf.message.AbstractWrappedMessage;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * CXF interceptor that finalizes the gathering of auditing-related data
 * and performs the actual auditing.
 * <p>
 * Usable on both client and server sides.
 * 
 * @author Dmytro Rud
 */
public class AuditFinalInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     * @param isServerSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public AuditFinalInterceptor(ItiAuditStrategy auditStrategy, boolean isServerSide) {
        super(isServerSide ? Phase.PREPARE_SEND : Phase.PRE_LOGICAL, auditStrategy);
    }

    
    @Override
    public void process(Message message) throws Exception {
        ItiAuditDataset auditDataset = getAuditDataset(message);

        // try to extract response as POJO 
        List<?> list = message.getContent(List.class);
        Object pojo = ((list != null) && (list.size() > 0)) ? list.get(0) : null;
        
        // calculate outcome code
        // TODO: possibly use MINOR_FAILURE and SERIOUS_FAILURE as well, especially  
        //       on the client side where we could analyze HTTP response codes...
        Exchange exchange = message.getExchange();
        Message wrappedMessage = ((AbstractWrappedMessage)message).getMessage();
        RFC3881EventOutcomeCodes eventOutcome = 
            ((wrappedMessage == exchange.getInFaultMessage()) ||
             (wrappedMessage == exchange.getOutFaultMessage()) ||
             (pojo == null)) ? 
                    RFC3881EventOutcomeCodes.MAJOR_FAILURE : 
                    RFC3881EventOutcomeCodes.SUCCESS;

        // perform transaction-specific auditing
        getAuditStrategy().audit(eventOutcome, auditDataset);
    }
    
}
