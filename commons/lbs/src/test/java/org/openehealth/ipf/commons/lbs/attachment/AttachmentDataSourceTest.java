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
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import javax.activation.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.utils.CorruptedInputStream;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

/**
 * @author Jens Riemschneider
 */
public class AttachmentDataSourceTest {
    private DataSource nonAttachmentDS;
    private AttachmentCompatibleDataSource attachmentCompatibleDS;
    private AttachmentDataSource attachmentDS;
    
    @Before
    public void setUp() throws Exception {
        nonAttachmentDS = createMock(DataSource.class);
        expect(nonAttachmentDS.getContentType()).andReturn("test/plain").anyTimes();
        expect(nonAttachmentDS.getInputStream()).andReturn(new ByteArrayInputStream("test".getBytes())).anyTimes();
        expect(nonAttachmentDS.getName()).andReturn("testname").anyTimes();
        expect(nonAttachmentDS.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
        replay(nonAttachmentDS);
        
        attachmentCompatibleDS = createMock(AttachmentCompatibleDataSource.class);        
        expect(attachmentCompatibleDS.getInputStream()).andReturn(new ByteArrayInputStream("test".getBytes())).anyTimes();
        expect(attachmentCompatibleDS.getOutputStream()).andReturn(new ByteArrayOutputStream()).anyTimes();
        expect(attachmentCompatibleDS.getName()).andReturn("testname").anyTimes();
        expect(attachmentCompatibleDS.getContentType()).andReturn("test/plain").anyTimes();
        expect(attachmentCompatibleDS.getContentLength()).andReturn(17L).anyTimes();
        expect(attachmentCompatibleDS.getResourceUri()).andReturn(URI.create("http://localhost/")).anyTimes();
        replay(attachmentCompatibleDS);
        
        attachmentDS = new AttachmentDataSource("id", nonAttachmentDS);
    }

    @Test
    public void testNonAttachmentDataSource() throws Exception {
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id", nonAttachmentDS);
        
        assertSame(nonAttachmentDS.getInputStream(), dataSource.getInputStream());
        assertSame(nonAttachmentDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(nonAttachmentDS.getName(), dataSource.getName());
        assertEquals(nonAttachmentDS.getContentType(), dataSource.getContentType());
        dataSource.deleteAfterNextUsage();  // Does nothing -> Cannot test much here
        assertEquals(4, dataSource.getContentLength());
        assertNull(dataSource.getResourceUri());
        assertEquals("id", dataSource.getId());
    }

    @Test
    public void testAttachmentCompatibleDataSource() throws Exception {
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id", attachmentCompatibleDS);
        
        assertSame(attachmentCompatibleDS.getInputStream(), dataSource.getInputStream());
        assertSame(attachmentCompatibleDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(attachmentCompatibleDS.getName(), dataSource.getName());
        assertEquals(attachmentCompatibleDS.getContentType(), dataSource.getContentType());
        assertEquals(attachmentCompatibleDS.getContentLength(), dataSource.getContentLength());
        assertEquals(attachmentCompatibleDS.getResourceUri(), dataSource.getResourceUri());
        assertEquals("id", dataSource.getId());
    }

    @Test
    public void testDeleteAfterNextUsageIsForwarded() {
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id", attachmentCompatibleDS);

        reset(attachmentCompatibleDS);        
        attachmentCompatibleDS.deleteAfterNextUsage();
        expectLastCall().anyTimes();
        replay(attachmentCompatibleDS);
        dataSource.deleteAfterNextUsage();
        verify(attachmentCompatibleDS);
    }

    @Test
    public void testAttachmentDataSource() throws Exception {
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id2", attachmentDS);
        
        assertSame(attachmentDS.getInputStream(), dataSource.getInputStream());
        assertSame(attachmentDS.getOutputStream(), dataSource.getOutputStream());
        assertEquals(attachmentDS.getName(), dataSource.getName());
        assertEquals(attachmentDS.getContentType(), dataSource.getContentType());
        assertEquals(attachmentDS.getContentLength(), dataSource.getContentLength());
        assertEquals(attachmentDS.getResourceUri(), dataSource.getResourceUri());
        assertEquals("id2", dataSource.getId());
    }
    
    @Test(expected = IOException.class)
    public void testDetermineLengthClosesStreamIfStreamIsCorrupt() throws Exception {
        CorruptedInputStream corruptedInputStream = new CorruptedInputStream();
        
        reset(nonAttachmentDS);
        expect(nonAttachmentDS.getInputStream()).andReturn(corruptedInputStream).anyTimes();
        replay(nonAttachmentDS);
        
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id", nonAttachmentDS);
        
        try {
            dataSource.getContentLength();
        }
        finally {
            assertTrue("stream was not closed", corruptedInputStream.isClosed());
        }
    }
    
    @Test
    public void testNiceClass() throws Exception {
        AttachmentDataSource dataSource = 
            new AttachmentDataSource("id", attachmentCompatibleDS);
        
        NiceClass.checkToString(dataSource, "id", attachmentCompatibleDS);
        NiceClass.checkNullSafety(dataSource, 
                asList("id", attachmentCompatibleDS), 
                asList());
    }
}
