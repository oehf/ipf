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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToClinicalDocumentConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToDomConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToStringConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.DataHandlerToByteArrayConverter;
import org.springframework.core.convert.support.GenericConversionService;

/**
 * @author Stefan Ivanov
 * 
 */
public class DocumentTest {
    private DocumentEntry docEntry;
    private Identifiable somePatientID;
    private DataHandler someData;
    
    @Before
    public void setUp() throws Exception {
        somePatientID = new Identifiable("id1", new AssigningAuthority("1.3"));
        someData = SampleData.createDataHandler();
        docEntry = SampleData.createDocumentEntry(somePatientID);

        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new DataHandlerToByteArrayConverter());
        conversionService.addConverter(new ByteArrayToStringConverter());
        conversionService.addConverter(new ByteArrayToClinicalDocumentConverter());
        conversionService.addConverter(new ByteArrayToDomConverter());
        Document.setConversionService(conversionService);
    }

    @Test
    public final void constructor() {
        Document doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void getContentsClassOfDataHandler() {
        Document doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void getNullContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.getContent(DataHandler.class));
        assertNull(doc.getContent(String.class));
    }
    
    @Test
    public final void addContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.setContent(DataHandler.class, someData));
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void removeContents() {
        Document doc = new Document(docEntry, null);
        doc.setContent(DataHandler.class, someData);
        doc.getContent(byte[].class);
        doc.getContent(String.class);
        doc.removeContent(String.class);
        assertNotNull(doc.getContent(String.class));
    }

    /*
    @Test
    public final void addNullContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.setContent(DataHandler.class, null));
    }
    */

    @Test
    public final void getContentsClassOfT() {
        String testContent = "data";
        Document doc = new Document(docEntry, someData);
        doc.setContent(String.class, testContent);
        String modContent = testContent.replace('a', 'c');
        doc.setContent(String.class, modContent);
        assertEquals("String content shoud be \'" + modContent + "\'", modContent,
            doc.getContent(String.class));
    }
    
    @Test
    public final void getContentsSize() {
        Document doc = new Document(docEntry, someData);
        doc.setContent(String.class, new String("data1"));
        doc.setContent(Integer.class, new Integer(2));
        doc.setContent(Integer.class, new Integer(4));
        assertEquals("Size of the contents should be 3!", 3, doc.getContentsCount());
    }

    @Test
    public final void getContentsKeySet() {
        Document doc = new Document(docEntry, someData);
        doc.setContent(String.class, new String("data1"));
        doc.setContent(Integer.class, new Integer(2));
        doc.setContent(Integer.class, new Integer(4));
        Set<Class<?>> expectedKeySet = new HashSet<Class<?>>(3);
        Class<?>[] classArray = new Class<?>[] {String.class, Integer.class, DataHandler.class};
        for (Class<?> clazz : classArray) {
            assertTrue(doc.hasContent(clazz));
        }
        assertEquals(classArray.length, doc.getContentsCount());
    }
    
    @Test
    public final void getContentsMissing() {
        Document doc = new Document(docEntry, someData);
        doc.getContent(byte[].class);
        doc.getContent(String.class);
        assertTrue("DataHandler should be converted to String",
                doc.hasContent(String.class));
    }

    @Test
    public final void equalsPositive() {
        Document doc1 = new Document(docEntry, someData);
        Document doc2 = new Document(docEntry, null);
        doc2.setContent(DataHandler.class, someData);
        assertTrue(doc1.equals(doc2));
    }
}
