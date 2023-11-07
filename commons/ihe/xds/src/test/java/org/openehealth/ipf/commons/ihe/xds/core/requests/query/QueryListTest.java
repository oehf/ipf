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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryListTest {
    @Test
    public void testXmlSerializationWithString() throws Exception {
        var innerList1 = Arrays.asList("Lisa", "Jodi", "Terry", "Cassandra");
        var innerList2 = Arrays.asList("Brock", "Thor", "Venus");

        var queryList = new QueryList<String>();
        queryList.getOuterList().add(innerList1);
        queryList.getOuterList().add(innerList2);

        var jaxbContext = JAXBContext.newInstance(QueryList.class);
        checkSerialization(jaxbContext, "queryList", queryList);
    }

    @Test
    public void testXmlSerializationWithCode() throws Exception {
        var innerList1 = Arrays.asList(
                new Code("Lisa", null, "Girlfriend scheme"),
                new Code("Jodi", null, "Girlfriend scheme"),
                new Code("Terry", null, "Girlfriend scheme"),
                new Code("Cassandra", null, "Girlfriend scheme"));
        var innerList2 = Arrays.asList(
                new Code("Brock", null, "Children scheme"),
                new Code("Thor", null, "Children scheme"),
                new Code("Venus", null, "Children scheme"));

        var queryList = new QueryList<Code>();
        queryList.getOuterList().add(innerList1);
        queryList.getOuterList().add(innerList2);

        var jaxbContext = JAXBContext.newInstance(QueryList.class, Code.class);
        checkSerialization(jaxbContext, "queryList", queryList);
    }

    @SuppressWarnings({"unchecked"})
    private <T> void checkSerialization(JAXBContext jaxbContext, String name, T object) throws Exception {
        var qname = new QName("http://www.openehealth.org/ipf/xds", name);
        var jaxbElement = new JAXBElement<>(qname, (Class<T>) object.getClass(), object);
        var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.marshal(jaxbElement, System.out);

        var domResult = new DOMResult();
        marshaller.marshal(jaxbElement, domResult);
        Node marshalledNode = ((org.w3c.dom.Document) domResult.getNode()).getDocumentElement();

        var unmarshaller = jaxbContext.createUnmarshaller();
        jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(marshalledNode, QueryList.class);
        var objectToo = jaxbElement.getValue();

        assertEquals(object, objectToo);
    }
}
