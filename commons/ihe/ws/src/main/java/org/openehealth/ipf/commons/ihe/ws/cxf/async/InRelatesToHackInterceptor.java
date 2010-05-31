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
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.Names;
import org.apache.cxf.ws.addressing.VersionTransformer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * CXF interceptor which renames the RelatesTo WSA header in order to prevent
 * correlation failure in {@link org.apache.cxf.ws.addressing.soap.MAPCodec},
 * (line #779 for CXF 2.2.8).
 * 
 * @author Dmytro Rud
 */
public class InRelatesToHackInterceptor extends AbstractSoapInterceptor {

    /**
     * New local name of the WSA RelatesTo header.  Namespace URI will remain unchanged. 
     */
    public static final String CONCEAL_NAME = "_RelatesTo";
    
    private static final VersionTransformer WSA_VERSION_HELPER = new VersionTransformer(); 
    
    public InRelatesToHackInterceptor() {
        super(Phase.READ);
        addAfter(ReadHeadersInterceptor.class.getName());
    }

    
    @Override
    public void handleMessage(SoapMessage message) {
        List<Header> headers = message.getHeaders();
        for (int i = 0; i < headers.size(); ++i) {
            Header header = headers.get(i);
            QName name = header.getName();
            if (Names.WSA_RELATESTO_NAME.equals(name.getLocalPart())
                    && WSA_VERSION_HELPER.isSupported(name.getNamespaceURI())) 
            {
                Object o = header.getObject();
                if (o instanceof Element) {
                    Node child = ((Element) o).getFirstChild();
                    if (child instanceof Text) {
                        header.setName(new QName(name.getNamespaceURI(), CONCEAL_NAME));
                        header.setObject(((Text) child).getNodeValue());
                    }
                }
                break;
            }
        }
    }
    

    /**
     * Helper method &mdash; retrieve message ID from modified WS-Addressing header 
     * contained in the given headers list.
     * 
     * @param headers
     *      list of SOAP headers, may be <code>null</code>.
     */
    public static String retrieveMessageId(List<Header> headers) {
        if (headers != null) {
            for (Header header : headers) {
                QName qname = header.getName();
                if (CONCEAL_NAME.equals(qname.getLocalPart())
                        && WSA_VERSION_HELPER.isSupported(qname.getNamespaceURI())) 
                {
                    return (String) header.getObject();
                }
            }
        }
        return null;
    }

}

