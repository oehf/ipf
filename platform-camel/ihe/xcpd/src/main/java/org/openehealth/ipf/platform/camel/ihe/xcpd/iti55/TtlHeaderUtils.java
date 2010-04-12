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
package org.openehealth.ipf.platform.camel.ihe.xcpd.iti55;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;

import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
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
     * Retrieves the XCPD TTL header from the given message context.
     */
    @SuppressWarnings("unchecked")
    public static Duration retrieveTtlHeader(Map<String, Object> context) {
        List<Header> soapHeaders = (List<Header>) context.get(Header.HEADER_LIST);
        if (soapHeaders != null) {
            for (Header soapHeader : soapHeaders) {
                if (TTL_HEADER_QNAME.equals(soapHeader.getName())) {
                    Object o = soapHeader.getObject();
                    if (o instanceof Element) {
                        Node child = ((Element) o).getFirstChild();
                        if (child instanceof Text) {
                            try {
                                String value = ((Text) child).getNodeValue();
                                return DatatypeFactory.newInstance().newDuration(value);
                            } catch (DatatypeConfigurationException e) {
                                throw new RuntimeException(e); 
                            }
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }


    /**
     * Retrieves the incoming XCPD TTL header from the given message context
     * and returns it as a map suitable for Camel exchange headers
     * (or <code>null</code>, when the header could not be retrieved).
     */
    public static Map<String, Object> retrieveTtlHeaderAsMap(Map<String, Object> context) {
        Duration dura = retrieveTtlHeader(context);
        if (dura != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(Iti55Component.XCPD_INPUT_TTL_HEADER_NAME, dura);
            return map;
        }
        return null;
    }
    
    
    /**
     * Adds an XCPD TTL header containing the given duration to the given message context.
     */
    @SuppressWarnings("unchecked")
    public static void addTtlHeader(Duration dura, Map<String, Object> context) {
        try {
            if (dura != null) {
                Header soapHeader = new Header(TTL_HEADER_QNAME, dura.toString(), new JAXBDataBinding(String.class));
                List<Header> soapHeaders = (List<Header>) context.get(Header.HEADER_LIST);
                if (soapHeaders == null) {
                    soapHeaders = new ArrayList<Header>();
                }
                soapHeaders.add(soapHeader);
                context.put(Header.HEADER_LIST, soapHeaders);
            }
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    
    /**
     * Removes XCPD TTL header from the given message context.
     */
    @SuppressWarnings("unchecked")
    public static void removeTtlHeader(Map<String, Object> context) {
        List<Header> soapHeaders = (List<Header>) context.get(Header.HEADER_LIST);
        if (soapHeaders != null) {
            for (Header header : soapHeaders) {
                if (TTL_HEADER_QNAME.equals(header.getName())) {
                    soapHeaders.remove(header);
                    return;
                }
            }
        }
    }
}
