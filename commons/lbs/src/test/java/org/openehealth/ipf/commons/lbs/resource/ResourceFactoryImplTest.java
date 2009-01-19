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
package org.openehealth.ipf.commons.lbs.resource;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.MemoryStore;
import org.openehealth.ipf.commons.lbs.store.StoreRegistration;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

/**
 * @author Jens Riemschneider
 */
public class ResourceFactoryImplTest {
    private static final String UNIT_OF_WORK = "UnitOfWorkId";
    private static final String CONTENT = "bla";
    private static final String NAME = "smurf";
    private static final String ID = "test";
    private static final String CONTENT_TYPE = "text/plain";
    
    private LargeBinaryStore store;
    private ResourceFactory factory;
    private InputStream inputStream;
    
    @Before
    public void setUp() {
        StoreRegistration.reset();
        store = new MemoryStore();
        factory = new ResourceFactory(store, "default");

        inputStream = new ByteArrayInputStream(CONTENT.getBytes());
    }
    
    @After
    public void tearDown() {
        store.deleteAll();
        StoreRegistration.reset();
    }

    @Test
    public void testCreateResourceReturnsGoodStuff() throws Exception {        
        ResourceDataSource resource = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, ID, inputStream);
        assertNotNull(resource);
        assertEquals(ID, resource.getId());
        
        assertEquals(NAME, resource.getName());
        assertStream(CONTENT, resource.getInputStream());
        assertEquals(CONTENT_TYPE, resource.getContentType());

        URI resourceUri = resource.getResourceUri();
        assertStream(CONTENT, store.getInputStream(resourceUri));
    }
    
    @Test
    public void testDeleteResource() throws Exception {
        ResourceDataSource resource = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, ID, inputStream);
        factory.deleteResource(UNIT_OF_WORK, resource);
        assertFalse(store.contains(resource.getResourceUri()));
    }
    
    @Test
    public void testDefaultIdOfResource() throws Exception {
        ResourceDataSource resource = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        assertEquals("default", resource.getId());
    }
    
    @Test
    public void testUnitOfWork() throws Exception {
        ResourceDataSource resource1 = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        ResourceDataSource resource2 = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);
        ResourceDataSource resource3 = factory.createResource("other", CONTENT_TYPE, NAME, null, inputStream);
        
        List<ResourceDataSource> unitOfWork1 = factory.getResources(UNIT_OF_WORK);
        assertEquals(2, unitOfWork1.size());
        assertTrue(unitOfWork1.contains(resource1));
        assertTrue(unitOfWork1.contains(resource2));

        List<ResourceDataSource> unitOfWork2 = factory.getResources("other");
        assertEquals(1, unitOfWork2.size());
        assertTrue(unitOfWork2.contains(resource3));
        
        factory.deleteResource(UNIT_OF_WORK, resource1);

        unitOfWork1 = factory.getResources(UNIT_OF_WORK);
        assertEquals(1, unitOfWork1.size());
        assertTrue(unitOfWork1.contains(resource2));
    }
    
    @Test
    public void testNiceClass() throws Exception {
        ResourceDataSource resource = factory.createResource(UNIT_OF_WORK, CONTENT_TYPE, NAME, null, inputStream);

        NiceClass.checkToString(factory, store, "default");
        NiceClass.checkNullSafety(factory, 
                asList(store, "default", inputStream, resource), 
                asList("createResource:3", "createResource:4"));
    }
    
    static void assertStream(String expected, InputStream stream) throws IOException {        
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        assertEquals(expected, bufferedReader.readLine());
    }
}
