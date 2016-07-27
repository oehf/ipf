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

import org.junit.Assert;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.dom.DOMResult;
import java.util.Arrays;
import java.util.List;

public class QueryListTest {
    @Test
    public void testXmlSerializationWithString() throws Exception {
        List<String> innerList1 = Arrays.asList("Lisa", "Jodi", "Terry", "Cassandra");
        List<String> innerList2 = Arrays.asList("Brock", "Thor", "Venus");

        QueryList<String> queryList = new QueryList<>();
        queryList.getOuterList().add(innerList1);
        queryList.getOuterList().add(innerList2);

        JAXBContext jaxbContext = JAXBContext.newInstance(QueryList.class);
        checkSerialization(jaxbContext, "queryList", queryList);
    }

    @Test
    public void testXmlSerializationWithCode() throws Exception {
        List<Code> innerList1 = Arrays.asList(
                new Code("Lisa", null, "Girlfriend scheme"),
                new Code("Jodi", null, "Girlfriend scheme"),
                new Code("Terry", null, "Girlfriend scheme"),
                new Code("Cassandra", null, "Girlfriend scheme"));
        List<Code> innerList2 = Arrays.asList(
                new Code("Brock", null, "Children scheme"),
                new Code("Thor", null, "Children scheme"),
                new Code("Venus", null, "Children scheme"));

        QueryList<Code> queryList = new QueryList<>();
        queryList.getOuterList().add(innerList1);
        queryList.getOuterList().add(innerList2);

        JAXBContext jaxbContext = JAXBContext.newInstance(QueryList.class, Code.class);
        checkSerialization(jaxbContext, "queryList", queryList);
    }

    @SuppressWarnings({"unchecked"})
    private <T> void checkSerialization(JAXBContext jaxbContext, String name, T object) throws Exception {
        QName qname = new QName("http://www.openehealth.org/ipf/xds", name);
        JAXBElement<T> jaxbElement = new JAXBElement<>(qname, (Class<T>) object.getClass(), object);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // marshaller.marshal(jaxbElement, System.out);

        DOMResult domResult = new DOMResult();
        marshaller.marshal(jaxbElement, domResult);
        Node marshalledNode = ((org.w3c.dom.Document) domResult.getNode()).getDocumentElement();

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        jaxbElement = (JAXBElement<T>) unmarshaller.unmarshal(marshalledNode, QueryList.class);
        T objectToo = jaxbElement.getValue();

        Assert.assertEquals(object, objectToo);
    }
}
