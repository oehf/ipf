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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.activation.DataHandler;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;

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
    }

    @Test
    public final void constructor() {
        Document doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContents(DataHandler.class)));
    }
    
    @Test
    public final void getContentsClassOfDataHandler() {
        Document doc = new Document(docEntry, someData);
        assertTrue(someData.equals(doc.getContents(DataHandler.class)));
    }
    
    @Test
    public final void getNullContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.getContents(DataHandler.class));
        assertNull(doc.getContents(String.class));
    }
    
    @Test
    public final void addContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.addContents(DataHandler.class, someData));
        assertTrue(someData.equals(doc.getContents(DataHandler.class)));
    }
    
    @Test
    public final void removeContents() {
        Document doc = new Document(docEntry, null);
        doc.addContents(DataHandler.class, someData);
        doc.getContents(String.class);
        doc.getContents().remove(String.class);
        assertNotNull(doc.getContents(String.class));
    }
    
    @Test
    public final void addNullContents() {
        Document doc = new Document(docEntry, null);
        assertNull(doc.addContents(DataHandler.class, null));
    }

    @Test
    public final void getContentsClassOfT() {
        String testContent = "data";
        Document doc = new Document(docEntry, someData);
        doc.addContents(String.class, testContent);
        String modContent = testContent.replace('a', 'c');
        doc.addContents(String.class, modContent);
        assertEquals("String content shoud be \'" + modContent + "\'", modContent,
            doc.getContents(String.class));
    }
    
    @Test
    public final void getContentsSize() {
        Document doc = new Document(docEntry, someData);
        doc.addContents(String.class, new String("data1"));
        doc.addContents(Integer.class, new Integer(2));
        doc.addContents(Integer.class, new Integer(4));
        assertEquals("Size of the contents should be 3!", 3, doc.getContents().size());
    }

    @Test
    public final void getContentsKeySet() {
        Document doc = new Document(docEntry, someData);
        doc.addContents(String.class, new String("data1"));
        doc.addContents(Integer.class, new Integer(2));
        doc.addContents(Integer.class, new Integer(4));
        Set<Class<?>> expectedKeySet = new HashSet<Class<?>>(3);
        Class<?>[] classArray = new Class<?>[] {String.class, Integer.class, DataHandler.class};
        expectedKeySet.addAll(Arrays.asList(classArray));
        assertEquals(expectedKeySet, doc.getContents().keySet());
    }
    
    @Test
    public final void getContentsMissing() {
        Document doc = new Document(docEntry, someData);
        doc.getContents(String.class);
        assertTrue("DataHanlder should be converted to String",
            doc.getContents().containsKey(String.class));
    }

    @Test
    public final void equalsPositive() {
        Document doc1 = new Document(docEntry, someData);
        Document doc2 = new Document(docEntry, null);
        doc2.addContents(DataHandler.class, someData);
        assertTrue(doc1.equals(doc2));
    }
}
