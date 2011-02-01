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
package org.openehealth.ipf.commons.ihe.xds.core.cxf;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.AuditInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;


/**
 * CXF interceptor that finalizes the gathering of auditing-related data
 * and performs the actual auditing.
 * <p>
 * Usable on both client and server sides.
 * 
 * @author Dmytro Rud
 */
public class XdsAuditFinalInterceptor extends AuditInterceptor {

    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     * @param serverSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public XdsAuditFinalInterceptor(WsAuditStrategy auditStrategy, boolean serverSide) {
        super(serverSide ? Phase.PREPARE_SEND : Phase.PRE_LOGICAL, auditStrategy);
    }

    
    @Override
    protected void process(SoapMessage message) throws Exception {
        WsAuditDataset auditDataset = getAuditDataset(message);
        Object response = extractPojo(message);
        
        // determine event outcome code
        Exchange exchange = message.getExchange();
        Message wrappedMessage = message.getMessage();

        boolean fault =
           (message == exchange.getInFaultMessage()) ||
           (message == exchange.getOutFaultMessage()) ||
           (wrappedMessage == exchange.getInFaultMessage()) ||
           (wrappedMessage == exchange.getOutFaultMessage()) ||
           (response == null);

        auditDataset.setEventOutcomeCode(fault ?
                RFC3881EventOutcomeCodes.SERIOUS_FAILURE :
                getAuditStrategy().getEventOutcomeCode(response));

        // perform transaction-specific auditing
        getAuditStrategy().audit(auditDataset);
    }
    
}
