/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeRangeTest {
    @Test
    public void testXmlDateSerialization() throws Exception {
        var timeRange = new TimeRange();

        timeRange.setFrom("2011030512");
        timeRange.setTo("20120301080642.190");
        checkSerialization("timeRange", timeRange);

        timeRange.setFrom("20110305");
        timeRange.setTo("2012");
        checkSerialization("timeRange", timeRange);
    }

    @SuppressWarnings({"unchecked"})
    private <T> void checkSerialization(String name, T object) throws Exception {
        var qname = new QName("http://www.openehealth.org/ipf/xds", name);
        var jaxbElement = new JAXBElement<>(qname, (Class<T>) object.getClass(), object);
        var jaxbContext = JAXBContext.newInstance(TimeRange.class);
        var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.marshal(jaxbElement, System.out);

        var domResult = new DOMResult();
        marshaller.marshal(jaxbElement, domResult);
        Node marshalledNode = ((org.w3c.dom.Document) domResult.getNode()).getDocumentElement();

        var unmarshaller = jaxbContext.createUnmarshaller();
        jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(marshalledNode, TimeRange.class);
        var objectToo = jaxbElement.getValue();

        assertEquals(object, objectToo);
    }
}
