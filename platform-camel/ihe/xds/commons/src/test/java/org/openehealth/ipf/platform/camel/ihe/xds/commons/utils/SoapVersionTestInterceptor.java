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

import static junit.framework.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDatasetEnrichmentInterceptor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditInterceptor;


/**
 * A CXF interceptor that checks the version of SOAP messages.
 * <p>
 * Intended to be used for outgoing messages on client side. 
 * 
 * @author Dmytro Rud
 */
public class SoapVersionTestInterceptor extends AbstractSoapInterceptor {
    
    private static final transient Log LOG = LogFactory.getLog(SoapVersionTestInterceptor.class);

    /**
     * Whether the interceptor must check the usage of SOAP version 1.1
     * in the outgoing request of the client.
     * <p>
     * <code>null</code> means "do not check".
     * <p>
     * Setting of this static field between Web Service invocations   
     * seems to be a little bit dirty, I know...    
     */
    private static Double soapVersion = null;
    
    /**
     * Sets the SOAP version to check.
     * @param d 
     *      May be <code>1.1</code>, <code>1.2</code> or <code>null</code>.
     */
    public static void setSoapVersion(Double d) {
        soapVersion = d; 
    }
    
    /**
     * Constructor.
     * 
     * @param isServerSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public SoapVersionTestInterceptor() {
        super(Phase.WRITE);
        addAfter(AuditDatasetEnrichmentInterceptor.class.getName());
    }

    
    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        Exchange exchange = message.getExchange();
        ItiAuditDataset auditDataset = (ItiAuditDataset)exchange.get(AuditInterceptor.CXF_EXCHANGE_KEY);
        if(auditDataset == null) {
            // This will be the case either on server side
            // or on the client side, when auditing is disabled. 
            // Simply ignore.
            return;
        }
        
        boolean isOutbound = (message == exchange.getOutMessage()) || 
                             (message == exchange.getOutFaultMessage());

        if(( ! auditDataset.isServerSide()) && isOutbound && (soapVersion != null)) {
            LOG.debug("Expect SOAP version " + soapVersion);
            assertEquals(soapVersion, message.getVersion().getVersion());
        }
    }
}
