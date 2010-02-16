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

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.utils.CorruptedInputStream;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

import javax.activation.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class ResourceDataSourceTest {
    private DataSource nonResourceDS;
    private ResourceCompatibleDataSource resourceCompatibleDS;
    private ResourceDataSource resourceDS;
    
    @Before
    public void setUp() throws Exception {
        nonResourceDS = createMock(DataSource.class);
        expect(nonResourceDS.getContentType()).andReturn("test/plain").anyTimes();
        expect(nonResourceDS.getInputStream()).andReturn(new ByteArrayInputStream("test".getBytes())).anyTimes();
        expect(nonResourceDS.getName()).andReturn("testname").anyTimes();
        expect(nonResourceDS.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
        replay(nonResourceDS);
        
        resourceCompatibleDS = createMock(ResourceCompatibleDataSource.class);        
        expect(resourceCompatibleDS.getInputStream()).andReturn(new ByteArrayInputStream("test".getBytes())).anyTimes();
        expect(resourceCompatibleDS.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
        expect(resourceCompatibleDS.getName()).andReturn("testname").anyTimes();
        expect(resourceCompatibleDS.getContentType()).andReturn("test/plain").anyTimes();
        expect(resourceCompatibleDS.getContentLength()).andReturn(17L).anyTimes();
        expect(resourceCompatibleDS.getResourceUri()).andReturn(URI.create("http://localhost/")).anyTimes();
        replay(resourceCompatibleDS);
        
        resourceDS = new ResourceDataSource("id", nonResourceDS);
    }

    @Test
    public void testNonResourceDataSource() throws Exception {
        ResourceDataSource dataSource = 
            new ResourceDataSource("id", nonResourceDS);
        
        assertSame(nonResourceDS.getInputStream(), dataSource.getInputStream());
        assertSame(nonResourceDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(nonResourceDS.getName(), dataSource.getName());
        assertEquals(nonResourceDS.getContentType(), dataSource.getContentType());
        dataSource.deleteAfterNextUsage();  // Does nothing -> Cannot test much here
        assertEquals(4, dataSource.getContentLength());
        assertNull(dataSource.getResourceUri());
        assertEquals("id", dataSource.getId());
    }

    @Test
    public void testResourceCompatibleDataSource() throws Exception {
        ResourceDataSource dataSource = 
            new ResourceDataSource("id", resourceCompatibleDS);
        
        assertSame(resourceCompatibleDS.getInputStream(), dataSource.getInputStream());
        assertSame(resourceCompatibleDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(resourceCompatibleDS.getName(), dataSource.getName());
        assertEquals(resourceCompatibleDS.getContentType(), dataSource.getContentType());
        assertEquals(resourceCompatibleDS.getContentLength(), dataSource.getContentLength());
        assertEquals(resourceCompatibleDS.getResourceUri(), dataSource.getResourceUri());
        assertEquals("id", dataSource.getId());
    }

    @Test
    public void testDeleteAfterNextUsageIsForwarded() {
        ResourceDataSource dataSource = 
            new ResourceDataSource("id", resourceCompatibleDS);

        reset(resourceCompatibleDS);        
        resourceCompatibleDS.deleteAfterNextUsage();
        expectLastCall().anyTimes();
        replay(resourceCompatibleDS);
        dataSource.deleteAfterNextUsage();
        verify(resourceCompatibleDS);
    }

    @Test
    public void testResourceDataSource() throws Exception {
        ResourceDataSource dataSource = 
            new ResourceDataSource("id2", resourceDS);
        
        assertSame(resourceDS.getInputStream(), dataSource.getInputStream());
        assertSame(resourceDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(resourceDS.getName(), dataSource.getName());
        assertEquals(resourceDS.getContentType(), dataSource.getContentType());
        assertEquals(resourceDS.getContentLength(), dataSource.getContentLength());
        assertEquals(resourceDS.getResourceUri(), dataSource.getResourceUri());
        assertEquals("id2", dataSource.getId());
    }
    
    @Test(expected = IOException.class)
    public void testDetermineLengthClosesStreamIfStreamIsCorrupt() throws Exception {
        CorruptedInputStream corruptedInputStream = new CorruptedInputStream();
        
        reset(nonResourceDS);
        expect(nonResourceDS.getInputStream()).andReturn(corruptedInputStream).anyTimes();
        replay(nonResourceDS);
        
        ResourceDataSource dataSource = 
            new ResourceDataSource("id", nonResourceDS);
        
        try {
            dataSource.getContentLength();
        }
        finally {
            assertTrue("stream was not closed", corruptedInputStream.isClosed());
        }
    }
    
    @Test
    public void testNiceClass() throws Exception {
        ResourceDataSource dataSource = 
            new ResourceDataSource("id", resourceCompatibleDS);
        
        NiceClass.checkToString(dataSource, "id", resourceCompatibleDS);
        NiceClass.checkNullSafety(dataSource, 
                asList("id", resourceCompatibleDS), 
                emptyList());
    }
}
