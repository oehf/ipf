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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.utils;

import java.util.regex.Pattern;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.interceptor.Fault;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditFinalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditInterceptor;


/**
 * Base class of CXF interceptors used for ATNA auditing-related unit tests.
 * <p>
 * Usable both on the server side and on the client side. 
 * 
 * @author Dmytro Rud
 */
abstract public class AuditTestFinalInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final transient Log LOG = LogFactory.getLog(AuditTestFinalInterceptor.class);
    
    private static final String BYTE_REGEXP = "[12]?(\\d{1,2})";
    private static final String IP_REGEXP   = "(" + BYTE_REGEXP + "\\.){3}" + BYTE_REGEXP;
    private static Pattern IP_PATTERN = Pattern.compile(IP_REGEXP);
    
    /**
     * Whether WS-Addressing is used in the transaction.
     */
    private final boolean addressing;

    
    /**
     * Constructor.
     * 
     * @param isServerSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public AuditTestFinalInterceptor(boolean isServerSide, boolean addressing) {
        super(isServerSide ? Phase.PREPARE_SEND : Phase.PRE_LOGICAL);
        addAfter(AuditFinalInterceptor.class.getName());
        
        this.addressing = addressing;
    }

    
    @Override
    public void handleMessage(Message message) throws Fault {
        Exchange exchange = message.getExchange();
        AuditDataset auditDataset = (AuditDataset)exchange.get(AuditInterceptor.CXF_EXCHANGE_KEY);
        if(auditDataset == null) {
            LOG.warn("audit dataset not found");
            return;
        }   
        LOG.debug(auditDataset);

        /*
         * From technical reasons(*), this interceptor will be probably present
         * in all possible interceptor chains -- i.e. both client and server,
         * both inbound and outbound.  But it has only to act as outbound
         * interceptor on server-side and as inbound interceptor on client-side.
         * Therefore we have to check explicitly where we are.
         * 
         * ________________________
         *    *) I wonder whether a programmer's laziness can be considered 
         *       as a technical reason...
         *         
         */
        
        boolean isServerSide = auditDataset.isServerSide(); 

        boolean isOutbound = (message == exchange.getOutMessage()) || 
                             (message == exchange.getOutFaultMessage());

        if(isServerSide && isOutbound) {
            assertEquals(isAddressing(), (auditDataset.getUserId() != null));

            assertTrue(IP_PATTERN.matcher(auditDataset.getClientIpAddress()).matches());
            assertTrue(auditDataset.getServiceEndpointUrl().startsWith("http://") 
                    || auditDataset.getServiceEndpointUrl().startsWith("https://"));
            
            checkTransactionSpecificFields(auditDataset, true);
        }

        if(( ! isServerSide) && ( ! isOutbound)) { 
            assertNotNull(auditDataset.getServiceEndpointUrl());

            checkTransactionSpecificFields(auditDataset, false);
        }
    }
    

    /**
     * Performs transaction-specific checks.
     * 
     * @param auditDataset
     *      current audit dataset
     * @param isServerSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    abstract public void checkTransactionSpecificFields(AuditDataset auditDataset, boolean isServerSide);
    
    
    /**
     * Tests whether some payload is collected in the given dataset
     * and (naively) whether this payload contains SOAP data.
     *  
     * @param auditDataset
     */
    protected void testPayload(AuditDataset auditDataset) {
        String payload = auditDataset.getPayload();
        assertNotNull(payload);
        assertTrue(payload.startsWith("<"));
        assertTrue(payload.endsWith("Envelope>"));
    }


    public boolean isAddressing() {
        return addressing;
    }
}