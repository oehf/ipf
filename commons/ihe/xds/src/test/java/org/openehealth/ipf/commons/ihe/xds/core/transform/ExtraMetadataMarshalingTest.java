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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLClassification;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLClassification30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ClassificationType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.RegisterDocumentSetTransformer;
import org.openehealth.ipf.commons.xml.XmlUtils;

import javax.xml.bind.JAXBContext;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        var extraMetaData = new HashMap<String, List<String>>();
        extraMetaData.put(KEY_1, VALUES_1);
        extraMetaData.put(KEY_2, VALUES_2);

        var original = new DocumentEntry();
        original.setExtraMetadata(extraMetaData);

        var context = JAXBContext.newInstance(DocumentEntry.class);
        var s = XmlUtils.renderJaxb(context, original, true);

        var result = (DocumentEntry) context.createUnmarshaller().unmarshal(new StringReader(s));

        assertEquals(2, result.getExtraMetadata().size());
        assertEquals(extraMetaData, result.getExtraMetadata());
        assertEquals(original, result);
        assertEquals(original.toString(), result.toString());
    }

    @Test
    public void testExtraClassifications() throws Exception {
        var originalSimplified = SampleData.createRegisterDocumentSet();
        originalSimplified.getSubmissionSet().setExtraClassifications(List.of(
                extraClassification("urn:uuid:1234-1", "representation 1"),
                extraClassification("urn:uuid:1234-2", "representation 2")));
        originalSimplified.getDocumentEntries().get(0).setExtraClassifications(List.of(
                extraClassification("urn:uuid:1234-3", "representation 3"),
                extraClassification("urn:uuid:1234-4", "representation 4")));
        originalSimplified.getFolders().get(0).setExtraClassifications(List.of(
                extraClassification("urn:uuid:1234-5", "representation 5"),
                extraClassification("urn:uuid:1234-6", "representation 6")));

        var jaxbContext = JAXBContext.newInstance(SubmitObjectsRequest.class, RegisterDocumentSet.class);

        RegisterDocumentSetTransformer transformer = new RegisterDocumentSetTransformer(new EbXMLFactory30());
        var originalEbxml = transformer.toEbXML(originalSimplified).getInternal();
        var originalEbxmlRendered = XmlUtils.renderJaxb(jaxbContext, originalEbxml, true);
        var originalSimplifiedRendered = XmlUtils.renderJaxb(jaxbContext, originalSimplified, true);

        var resultEbxml = (SubmitObjectsRequest) jaxbContext.createUnmarshaller().unmarshal(new StringReader(originalEbxmlRendered));
        var resultSimplified = transformer.fromEbXML(new EbXMLSubmitObjectsRequest30(resultEbxml));
        var resultSimplifiedRendered = XmlUtils.renderJaxb(jaxbContext, resultSimplified, true);

        assertEquals(2, resultSimplified.getSubmissionSet().getExtraClassifications().size());
        assertEquals(2, resultSimplified.getDocumentEntries().get(0).getExtraClassifications().size());
        assertEquals(2, resultSimplified.getFolders().get(0).getExtraClassifications().size());
        assertEquals(originalSimplifiedRendered, resultSimplifiedRendered);
        for (int i = 1; i <= 6; ++i) {
            assertTrue(originalEbxmlRendered.contains("classificationScheme=\"urn:uuid:1234-" + i + '"'));
            assertTrue(originalEbxmlRendered.contains("nodeRepresentation=\"representation " + i + '"'));
        }
    }

    private static EbXMLClassification extraClassification(
            String classificationScheme,
            String nodeRepresentation)
    {
        var classification = new EbXMLClassification30(new ClassificationType());
        classification.setClassificationScheme(classificationScheme);
        classification.setNodeRepresentation(nodeRepresentation);
        return classification;
    }
}