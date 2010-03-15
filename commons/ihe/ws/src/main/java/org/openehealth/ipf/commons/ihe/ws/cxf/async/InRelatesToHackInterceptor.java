/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ws.cxf.async;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.interceptor.ReadHeadersInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.ContextUtils;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.apache.cxf.ws.addressing.soap.MAPCodec;
import org.w3c.dom.Element;

/**
 * CXF interceptor which removes the partial response flag from
 * incoming messages in order to let the CXF client unlock
 * the producer which is waiting for the response.
 * 
 * @author Dmytro Rud
 */
public class InRelatesToHackInterceptor extends AbstractSoapInterceptor {
    
    private static final QName RELATES_TO_QNAME = new QName(JAXWSAConstants.NS_WSA, "RelatesTo");
    
    public InRelatesToHackInterceptor() {
        super(Phase.READ);
        addAfter(ReadHeadersInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) {
        /* 
         * See line #779 in org.apache.cxf.ws.addressing.soap.MAPCodec.java (CXF 2.2.6)
         */
        List<Header> headers = message.getHeaders();
        for (Header header : headers) {
            if (RELATES_TO_QNAME.equals(header.getName())) { 
                message.setContent(Header.class, header);
                headers.remove(header);
                message.getMessage().getInterceptorChain().add(new InRelatesToHackEndingInterceptor());
                return;
            }
        }
    }
    

    // TODO: do we really need to restore the header?
    private class InRelatesToHackEndingInterceptor extends AbstractSoapInterceptor {
        public InRelatesToHackEndingInterceptor() {
            super(Phase.PRE_PROTOCOL);
            addAfter(MAPCodec.class.getName());
        }

        @Override
        public void handleMessage(SoapMessage message) throws Fault {
            Header header = message.getContent(Header.class);
            if (header == null) {
                return;
            }

            message.getHeaders().add(header);
            
            Element element = (Element) header.getObject();
            String messageId = element.getTextContent();
            RelatesToType relatesTo = new RelatesToType();
            relatesTo.setValue(messageId);
            AddressingPropertiesImpl maps = ContextUtils.retrieveMAPs(message, false, false, false);
            maps.setRelatesTo(relatesTo);

            message.removeContent(Header.class);
        }
    }
    
}

