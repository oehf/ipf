/*
 * Copyright 2009 the original author or authors.
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
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet;

/**
 * Tests that all requests/responses and metadata classes can be serialized.
 * @author Jens Riemschneider
 */
public class SerializationTest {
    @Test
    public void testProvideAndRegisterDocumentSet() throws Exception {
        ProvideAndRegisterDocumentSet request = SampleData.createProvideAndRegisterDocumentSet();
        checkSerialization(request);
    }

    @Test
    public void testQueryRegistry() throws Exception {
        checkSerialization(SampleData.createFindDocumentsQuery());
        checkSerialization(SampleData.createFindDocumentsForMultiplePatientsQuery());
        checkSerialization(SampleData.createFindFoldersQuery());
        checkSerialization(SampleData.createFindFoldersForMultiplePatientsQuery());
        checkSerialization(SampleData.createFindSubmissionSetsQuery());
        checkSerialization(SampleData.createGetAllQuery());
        checkSerialization(SampleData.createGetAssociationsQuery());
        checkSerialization(SampleData.createGetDocumentsAndAssociationsQuery());
        checkSerialization(SampleData.createGetDocumentsQuery());
        checkSerialization(SampleData.createGetFolderAndContentsQuery());
        checkSerialization(SampleData.createGetFoldersForDocumentQuery());
        checkSerialization(SampleData.createGetFoldersQuery());
        checkSerialization(SampleData.createGetRelatedDocumentsQuery());
        checkSerialization(SampleData.createGetSubmissionSetAndContentsQuery());
        checkSerialization(SampleData.createGetSubmissionSetsQuery());        
        checkSerialization(SampleData.createSqlQuery());        
    }
    
    @Test
    public void testRegisterDocumentSet() throws Exception {
        checkSerialization(SampleData.createRegisterDocumentSet());
    }
    
    @Test
    public void testRetrieveDocumentSet() throws Exception {
        checkSerialization(SampleData.createRetrieveDocumentSet());
    }

    @Test
    public void testQueryResponse() throws Exception {
        checkSerialization(SampleData.createQueryResponseWithLeafClass());
        checkSerialization(SampleData.createQueryResponseWithObjRef());
    }

    @Test
    public void testRetrievedDocumentSet() throws Exception {
        RetrievedDocumentSet response = SampleData.createRetrievedDocumentSet();
        for (RetrievedDocument doc : response.getDocuments()) {
            doc.setDataHandler(null);
        }
        checkSerialization(response);
    }
    
    private void checkSerialization(Object original) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        ObjectInputStream ois = null;
        try {
            oos.writeObject(original);
            InputStream in = new ByteArrayInputStream(out.toByteArray());
            ois = new ObjectInputStream(in);   
            Object copy = ois.readObject();
            assertSame(original.getClass(), copy.getClass());
            assertEquals(original, copy);
        }
        finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(ois);
        }
    }
}
