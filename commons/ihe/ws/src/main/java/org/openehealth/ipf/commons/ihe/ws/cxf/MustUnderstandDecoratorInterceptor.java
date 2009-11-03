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
package org.openehealth.ipf.commons.ihe.ws.cxf;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.addressing.soap.MAPCodec;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * CXF Interceptor to tag SOAP headers with an {@code mustUnderstand} flag. 
 * @author Jens Riemschneider
 */
public class MustUnderstandDecoratorInterceptor extends AbstractSoapInterceptor {
    private final List<QName> mustUnderstandHeaders = new ArrayList<QName>();

    /**
     * Constructs the interceptor.
     */
    public MustUnderstandDecoratorInterceptor() {
        super(Phase.WRITE);
        addAfter(MAPCodec.class.getName());
        addBefore(LoggingOutInterceptor.class.getName());
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        List<Header> headers = message.getHeaders();
        for (QName name : mustUnderstandHeaders) {
            mustUnderstand(headers, name);
        }
    }

    /**
     * Adds a set of SOAP headers which are to be flagged 
     * with {@code mustUnderstand}.
     *  
     * @param headers
     *          the headers to flag, represented as Strings 
     */
    public void addHeaders(List<String> headers) {
        for (String header : headers) {
            int namespaceEnd = header.indexOf('}');
            String namespace = header.substring(1, namespaceEnd);
            String name = header.substring(namespaceEnd + 1);
            mustUnderstandHeaders.add(new QName(namespace, name));
        }
    }

    /**
     * Adds an item to the list of SOAP headers which  
     * are to be flagged with {@code mustUnderstand}.
     *  
     * @param header
     *          the header to flag.
     */
    public void addHeader(QName header) {
        this.mustUnderstandHeaders.add(header);
    }
    
    
    private Header getHeader(List<Header> headers, QName name) {
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                return header;
            }
        }
        return null;
    }

    private void mustUnderstand(List<Header> headers, QName name) {
        Header header = getHeader(headers, name);
        if (header == null) {
            return;
        }

        if (header instanceof SoapHeader) {
            SoapHeader soapHeader = (SoapHeader) header;
            soapHeader.setMustUnderstand(true);
            return;
        }

        headers.remove(header);
        SoapHeader newHeader = new SoapHeader(name, header.getObject());
        newHeader.setMustUnderstand(true);
        headers.add(newHeader);
    }
}
