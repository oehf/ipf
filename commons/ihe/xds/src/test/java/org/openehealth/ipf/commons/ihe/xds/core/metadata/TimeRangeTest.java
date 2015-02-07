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

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;

public class TimeRangeTest {
    @Test
    public void testXmlDateSerialization() throws Exception {
        TimeRange timeRange = new TimeRange();

        timeRange.setFrom("2011030512");
        timeRange.setTo("20120301080642.190");
        checkSerialization("timeRange", timeRange);

        timeRange.setFrom("20110305");
        timeRange.setTo("2012");
        checkSerialization("timeRange", timeRange);
    }

    @SuppressWarnings({"unchecked"})
    private <T> void checkSerialization(String name, T object) throws Exception {
        QName qname = new QName("http://www.openehealth.org/ipf/xds", name);
        JAXBElement<T> jaxbElement = new JAXBElement<>(qname, (Class<T>) object.getClass(), object);
        JAXBContext jaxbContext = JAXBContext.newInstance(TimeRange.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(jaxbElement, System.out);

        DOMResult domResult = new DOMResult();
        marshaller.marshal(jaxbElement, domResult);
        Node marshalledNode = ((org.w3c.dom.Document) domResult.getNode()).getDocumentElement();

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(marshalledNode, TimeRange.class);
        T objectToo = jaxbElement.getValue();

        // .toString() is used because org.joda.DateTime.equals() compares not only
        // the actual timestamps, but also chronologies, and even if they are almost
        // identical, like ISO and Gregorian, equals() will return false
        Assert.assertEquals(object.toString(), objectToo.toString());
    }
}
