/*
 * Copyright 2026 the original author or authors.
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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.w3c.dom.Document;

import javax.xml.transform.dom.DOMResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Christian Ohr
 * @since 5.3
 */
public class FindDocumentsExcludeQueryTest {

    /**
     * A JAXB context can only be created if every mapped element is listed in the
     * {@code propOrder} of the type -- this fails outright otherwise.
     */
    @Test
    public void testXmlSerialization() throws Exception {
        var query = (FindDocumentsExcludeQuery) SampleData.createFindDocumentsExcludeQuery().getQuery();

        var jaxbContext = JAXBContext.newInstance(FindDocumentsExcludeQuery.class);
        var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        var domResult = new DOMResult();
        marshaller.marshal(query, domResult);
        var marshalledNode = ((Document) domResult.getNode()).getDocumentElement();
        assertNotNull(marshalledNode);

        var unmarshaller = jaxbContext.createUnmarshaller();
        var result = (FindDocumentsExcludeQuery) unmarshaller.unmarshal(marshalledNode);

        assertEquals(query, result);
    }
}
