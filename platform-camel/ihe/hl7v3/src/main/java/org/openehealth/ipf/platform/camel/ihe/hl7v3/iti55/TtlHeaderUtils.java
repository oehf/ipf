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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

import org.apache.camel.Message;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Helper functions for handling TTL SOAP header in XCPD.
 * @author Dmytro Rud
 */
public class TtlHeaderUtils {

    private static final QName TTL_HEADER_QNAME = new QName("urn:ihe:iti:xcpd:2009", "CorrelationTimeToLive"); 
    
    private TtlHeaderUtils() {
        throw new IllegalStateException("cannot instantiate utility class");
    }
    
    
    /**
     * Returns the XCPD TTL value stored in incoming SOAP headers associated   
     * with the given message, or <code>null</code>, when none found.
     */
    public static Duration getTtl(Message message) {
        Map<QName, Header> soapHeaders = CastUtils.cast(message.getHeader(
                AbstractWsEndpoint.INCOMING_SOAP_HEADERS,
                Map.class));
        if ((soapHeaders != null) && soapHeaders.containsKey(TTL_HEADER_QNAME)) {
            Object o = soapHeaders.get(TTL_HEADER_QNAME).getObject();
            if (o instanceof Element) {
                Node child = ((Element) o).getFirstChild();
                if (child instanceof Text) {
                    String value = child.getNodeValue();
                    try {
                        return DatatypeFactory.newInstance().newDuration(value);
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot parse TTL header " + value, e); 
                    }
                }
            }
        }
        return null;
    }
    
    
    /**
     * Stores the user-defined XCPD TTL value into outgoing SOAP headers    
     * associated with the given message.
     */
    public static void setTtl(Duration dura, Message message) {
        Object soapHeaders = message.getHeader(AbstractWsEndpoint.OUTGOING_SOAP_HEADERS);
        if (soapHeaders == null) {
            soapHeaders = new ArrayList<>();
            message.setHeader(AbstractWsEndpoint.OUTGOING_SOAP_HEADERS, soapHeaders);
        }
        
        Header ttlHeader = new Header(TTL_HEADER_QNAME, dura.toString(), getStringDataBinding());
        if (soapHeaders instanceof Collection) {
            ((Collection) soapHeaders).add(ttlHeader);
        } else if (soapHeaders instanceof Map) {
            ((Map) soapHeaders).put(TTL_HEADER_QNAME, ttlHeader);
        }
    }
    
    
    private static JAXBDataBinding getStringDataBinding() {
        try {
            return new JAXBDataBinding(String.class);
        } catch (JAXBException e) {
            // actually cannot occur
            throw new RuntimeException(e);
        }
    }
}
