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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssigningAuthority;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.spring.core.config.SpringTypeConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToClinicalDocumentConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToDomConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.ByteArrayToStringConverter;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters.DataHandlerToByteArrayConverter;
import org.springframework.core.convert.support.GenericConversionService;

import jakarta.activation.DataHandler;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests conversion services
 * 
 * @author Stefan Ivanov
 */
public class DocumentTest {
    private DocumentEntry docEntry;
    private DataHandler someData;
    
    @BeforeEach
    public void setUp() throws Exception {
        var somePatientID = new Identifiable("id1", new AssigningAuthority("1.3"));
        someData = SampleData.createDataHandler();
        docEntry = SampleData.createDocumentEntry(somePatientID);

        var conversionService = new GenericConversionService();
        conversionService.addConverter(new DataHandlerToByteArrayConverter());
        conversionService.addConverter(new ByteArrayToStringConverter());
        conversionService.addConverter(new ByteArrayToClinicalDocumentConverter());
        conversionService.addConverter(new ByteArrayToDomConverter());
        Document.setConversionService(new SpringTypeConverter(conversionService));
    }

    @Test
    public final void constructor() {
        var doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void getContentsClassOfDataHandler() {
        var doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void getNullContents() {
        var doc = new Document(docEntry, null);
        assertNull(doc.getContent(DataHandler.class));
        assertNull(doc.getContent(String.class));
    }
    
    @Test
    public final void addContents() {
        var doc = new Document(docEntry, null);
        assertNull(doc.setContent(DataHandler.class, someData));
        assertTrue(someData.equals(doc.getContent(DataHandler.class)));
    }
    
    @Test
    public final void removeContents() {
        var doc = new Document(docEntry, null);
        doc.setContent(DataHandler.class, someData);
        doc.getContent(byte[].class);
        doc.getContent(String.class);
        doc.removeContent(String.class);
        assertNotNull(doc.getContent(String.class));
    }

    @Test
    public final void getContentsClassOfT() {
        var testContent = "data";
        var doc = new Document(docEntry, someData);
        doc.setContent(String.class, testContent);
        var modContent = testContent.replace('a', 'c');
        doc.setContent(String.class, modContent);
        assertEquals(modContent, doc.getContent(String.class), "String content shoud be '" + modContent + "'");
    }
    
    @Test
    public final void getContentsSize() {
        var doc = new Document(docEntry, someData);
        doc.setContent(String.class, "data1");
        doc.setContent(Integer.class, 2);
        doc.setContent(Integer.class, 4);
        assertEquals(3, doc.getContentsCount(), "Size of the contents should be 3!");
    }

    @Test
    public final void getContentsKeySet() {
        var doc = new Document(docEntry, someData);
        doc.setContent(String.class, "data1");
        doc.setContent(Integer.class, 2);
        doc.setContent(Integer.class, 4);
        var classArray = new Class<?>[] {String.class, Integer.class, DataHandler.class};
        for (var clazz : classArray) {
            assertTrue(doc.hasContent(clazz));
        }
        assertEquals(classArray.length, doc.getContentsCount());
    }
    
    @Test
    public final void getContentsMissing() {
        var doc = new Document(docEntry, someData);
        doc.getContent(byte[].class);
        doc.getContent(String.class);
        assertTrue(doc.hasContent(String.class), "DataHandler should be converted to String");
    }

    @Test
    public final void equalsPositive() {
        var doc1 = new Document(docEntry, someData);
        var doc2 = new Document(docEntry, null);
        doc2.setContent(DataHandler.class, someData);
        assertTrue(doc1.equals(doc2));
    }
}
