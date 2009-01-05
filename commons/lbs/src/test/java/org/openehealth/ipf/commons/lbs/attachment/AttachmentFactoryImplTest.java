/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.lbs.attachment;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.MemoryStore;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

/**
 * @author Jens Riemschneider
 */
public class AttachmentFactoryImplTest {
    private static final String UNIT_OF_WORK = "UnitOfWorkId";
    private static final String CONTENT = "bla";
    private static final String NAME = "smurf";
    private static final String ID = "test";
    private static final String CONTENT_TYPE = "text/plain";
    
    private LargeBinaryStore store;
    private AttachmentFactory factory;
    private InputStream inputStream;
    
    @Before
    public void setUp() {
        store = new MemoryStore();
        factory = new AttachmentFactory(store, "default");

        inputStream = new ByteArrayInputStream(CONTENT.getBytes());
    }

    @Test
    public void testCreateAttachmentReturnsGoodStuff() throws Exception {        
        AttachmentDataSource attachment = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, ID, inputStream);
        assertNotNull(attachment);
        assertEquals(ID, attachment.getId());
        
        assertEquals(NAME, attachment.getName());
        assertStream(CONTENT, attachment.getInputStream());
        assertEquals(CONTENT_TYPE, attachment.getContentType());

        URI resourceUri = attachment.getResourceUri();
        assertStream(CONTENT, store.getInputStream(resourceUri));
    }
    
    @Test
    public void testDeleteAttachment() throws Exception {
        AttachmentDataSource attachment = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, ID, inputStream);
        factory.deleteAttachment(UNIT_OF_WORK, attachment);
        assertFalse(store.contains(attachment.getResourceUri()));
    }
    
    @Test
    public void testDefaultIdOfAttachment() throws Exception {
        AttachmentDataSource attachment = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        assertEquals("default", attachment.getId());
    }
    
    @Test
    public void testUnitOfWork() throws Exception {
        AttachmentDataSource attachment1 = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        AttachmentDataSource attachment2 = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        AttachmentDataSource attachment3 = factory.createAttachment("other", CONTENT_TYPE, NAME, null, inputStream);
        
        List<AttachmentDataSource> unitOfWorkAttachments1 = factory.getAttachments(UNIT_OF_WORK);
        assertEquals(2, unitOfWorkAttachments1.size());
        assertTrue(unitOfWorkAttachments1.contains(attachment1));
        assertTrue(unitOfWorkAttachments1.contains(attachment2));

        List<AttachmentDataSource> unitOfWorkAttachments2 = factory.getAttachments("other");
        assertEquals(1, unitOfWorkAttachments2.size());
        assertTrue(unitOfWorkAttachments2.contains(attachment3));
        
        factory.deleteAttachment(UNIT_OF_WORK, attachment1);

        unitOfWorkAttachments1 = factory.getAttachments(UNIT_OF_WORK);
        assertEquals(1, unitOfWorkAttachments1.size());
        assertTrue(unitOfWorkAttachments1.contains(attachment2));
    }
    
    @Test
    public void testNiceClass() throws Exception {
        AttachmentDataSource attachment = factory.createAttachment(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);

        NiceClass.checkToString(factory, store, "default");
        NiceClass.checkNullSafety(factory, 
                asList(store, "default", inputStream, attachment), 
                asList("createAttachment:3", "createAttachment:4"));
    }
    
    static void assertStream(String expected, InputStream stream) throws IOException {        
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        assertEquals(expected, bufferedReader.readLine());
    }
}
