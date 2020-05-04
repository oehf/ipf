/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;

import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test for marshalling and unmarshalling of extra metadata in the simplified XDS data model.
 * @author Anurag Shrivastava
 */
public class ExtraMetadataMarshalingTest {

    private static final String KEY_1 = "urn:emc:em1";
    private static final List<String> VALUES_1 = Arrays.asList("One", "Two", "Three");

    private static final String KEY_2 = "urn:xyz";
    private static final List<String> VALUES_2 = Arrays.asList("12345", "67890");


    @Test
    public void testDocumentEntryUnMarshalling() throws Exception {
        Map<String, List<String>> extraMetaData = new HashMap<>();
        extraMetaData.put(KEY_1, VALUES_1);
        extraMetaData.put(KEY_2, VALUES_2);

        var original = new DocumentEntry();
        original.setExtraMetadata(extraMetaData);

        var context = JAXBContext.newInstance(DocumentEntry.class);
        var writer = new StringWriter();
        var marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output", true);
        marshaller.marshal(original, writer);
        var s = writer.toString();

        var result = (DocumentEntry) context.createUnmarshaller().unmarshal(new StringReader(s));

        assertEquals(2, result.getExtraMetadata().size());
        assertEquals(extraMetaData, result.getExtraMetadata());
        assertEquals(original, result);
        assertEquals(original.toString(), result.toString());
    }

}