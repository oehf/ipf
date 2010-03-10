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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.AbstractWrappedMessage;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * CXF interceptor that fills the audit dataset with values from 
 * the input SOAP message (XML and/or POJO).
 * <p>
 * Usable on both client and server sides.
 * 
 * @author Dmytro Rud
 */
public class AuditDatasetEnrichmentInterceptor extends AuditInterceptor {
    private static final transient Log LOG = LogFactory.getLog(AuditDatasetEnrichmentInterceptor.class);
    
    /**
     * Constructor.
     * 
     * @param auditStrategy
     *      an audit strategy instance
     * @param serverSide
     *      whether this interceptor is being used on the server side 
     *      (<code>true</code>) or on the client side (<code>false</code>)  
     */
    public AuditDatasetEnrichmentInterceptor(WsAuditStrategy auditStrategy, boolean serverSide) {
        super(serverSide ? Phase.PRE_INVOKE : Phase.WRITE_ENDING, auditStrategy);
        if( ! serverSide) {
            addAfter(OutPayloadExtractorInterceptor.class.getName());
        }
    }

    
    @Override
    protected void process(Message message) throws Exception {
        WsAuditDataset auditDataset = getAuditDataset(message);

        // determine what direction do we handle
        Exchange exchange = message.getExchange();
        Message wrappedMessage = ((AbstractWrappedMessage)message).getMessage();
        boolean isInboundMessage = 
            wrappedMessage == exchange.getInMessage() ||
            wrappedMessage == exchange.getInFaultMessage() ||
            message == exchange.getInMessage() ||
            message == exchange.getInFaultMessage();
        
        // retrieve WS-Addressing headers
        AddressingPropertiesImpl wsaProperties = 
            (AddressingPropertiesImpl)message.get(isInboundMessage ? 
                    JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_INBOUND :
                    JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES_OUTBOUND);
        
        if(wsaProperties != null) {
            // extract client User ID from WS-Addressing <wsa:ReplyTo> element
            EndpointReferenceType replyTo = wsaProperties.getReplyTo();
            if(replyTo != null) {
                AttributedURIType address = replyTo.getAddress();
                if(address != null) {
                    auditDataset.setUserId(address.getValue());
                }
            }
        } else {
            LOG.error("Missing WS-Addressing headers");
        }

        // get <soapenv:Header> element
        Document rootDocument = (Document)message.getContent(org.w3c.dom.Node.class);
        // TODO: WSS interceptor?
        if(rootDocument != null) {
            Element soapHeader = SoapUtils.getElementNS(
                rootDocument.getDocumentElement(), 
                SoapUtils.SOAP_NS_URIS, 
                "Header");
    
            // extract client User Name (as contents of <WSS:Usename>)
            Element elem = SoapUtils.getDeepElementNS(
                soapHeader,
                SoapUtils.WS_SECURITY_NS_URIS, 
                new String[] {"Security", "UsernameToken", "Username"}); 
            if(elem != null) {
                auditDataset.setUserName(elem.getTextContent());
            }
        }
        
        // determine client IP address and service endpoint URL
        if(isInboundMessage) {
            HttpServletRequest request = (HttpServletRequest)message.get(AbstractHTTPDestination.HTTP_REQUEST);
            if(request != null) {
                auditDataset.setClientIpAddress(request.getRemoteAddr());
                auditDataset.setServiceEndpointUrl(request.getRequestURL().toString());
            }
        } else {
            auditDataset.setServiceEndpointUrl((String)message.get(Message.ENDPOINT_ADDRESS));
        }

        // extract value prepared by (Client|Server)PayloadExctactorInterceptor
        auditDataset.setPayload(message.getContent(String.class));
        
        // perform transaction-specific audit dataset enrichment
        Object pojo = message.getContent(List.class).get(0);
        getAuditStrategy().enrichDataset(pojo, auditDataset);
    }

}
