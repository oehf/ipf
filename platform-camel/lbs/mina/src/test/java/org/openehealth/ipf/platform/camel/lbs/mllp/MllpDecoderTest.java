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
package org.openehealth.ipf.platform.camel.lbs.mllp;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.commons.lbs.resource.ResourceFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.MemoryStore;
import org.openehealth.ipf.commons.lbs.store.StoreRegistration;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;
import org.openehealth.ipf.platform.camel.lbs.mllp.MllpDecoder.SessionContent;

/**
 * @author Jens Riemschneider
 */
public class MllpDecoderTest {
    private MemoryStore store;
    private TestResourceFactory resourceFactory;
    private MllpDecoder decoder;
    private SessionContent sessionContent;
    private TestDecoderOutput out;
    private IoSession session;
    
    public static final String START = "\u000b";
    public static final String END = "\u001c";   
    public static final String CR = "\r";
    
    @Before
    public void setUp() throws Exception {
        store = new MemoryStore();
        resourceFactory = new TestResourceFactory(store, "defaultid");
        decoder = new MllpDecoder(resourceFactory);
        sessionContent = new SessionContent(resourceFactory);
        out = new TestDecoderOutput();
        session = EasyMock.createNiceMock(IoSession.class);
    }
    
    @After
    public void tearDown() throws Exception {
        store.deleteAll();
        StoreRegistration.reset();
    }

    @Test
    public void testDecodeSimpleShortMessage() throws Exception {
        String message = START + "Hello World" + END + CR;
        ByteBuffer in = toByteBuffer(message);
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Hello World", IOUtils.toString(out.getResource().getInputStream()));
        assertEquals(message.length(), in.position());
    }
    
    @Test
    public void testDecodeTwoMessages() throws Exception {
        String message = START + "Hello World" + END + CR + START + "Bye" + END + CR;
        ByteBuffer in = toByteBuffer(message);
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Hello World", IOUtils.toString(out.getResource().getInputStream()));
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Bye", IOUtils.toString(out.getResource().getInputStream()));
        assertEquals(message.length(), in.position());
    }
    
    @Test
    public void testDecodeMessageThatSpansMultipleBuffers() throws Exception {
        String messagePart1 = START + "This is only the beginning of the message. ";
        String messagePart2 = "It continues in the next buffer" + END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getResource());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is only the beginning of the message. It continues in the next buffer", 
                IOUtils.toString(out.getResource().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithCRInNextBuffer() throws Exception {
        String messagePart1 = START + "This is nearly the whole message" + END;
        String messagePart2 = CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getResource());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getResource().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithEndInNextBuffer() throws Exception {
        String messagePart1 = START + "This is nearly the whole message";
        String messagePart2 = END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getResource());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getResource().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithStartAsLastChar() throws Exception {
        String messagePart1 = START;
        String messagePart2 = "This is nearly the whole message" + END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getResource());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getResource().getInputStream()));
    }
    
    @Test
    public void testDecodeWriteThrowsException() throws Exception {
        String message = START + "Hello World" + END + CR;
        ByteBuffer in = toByteBuffer(message);
        out.throwException();
        try {
            decoder.doDecode(sessionContent, in, out);
        }
        catch (RuntimeException e) {
            assertEquals("test", e.getMessage());
        }

        ResourceDataSource resource = out.getResource();
        assertFalse("Resource must be removed when an exception occurs", 
                store.contains(resource.getResourceUri()));
    }
    
    @Test
    public void testDecodeStartsMessageButWeCloseSessionBeforeEnd() throws Exception {
        String message = START + "This is a ";
        ByteBuffer in = toByteBuffer(message);
        assertFalse(decoder.doDecode(sessionContent, in, out));
        decoder.finishDecode(sessionContent);
        
        // Resource was not written but it was created by the factory
        assertNull(out.getResource());
        ResourceDataSource resource = resourceFactory.getResource();
        assertFalse("Resource must be removed when an exception occurs", 
                store.contains(resource.getResourceUri()));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testStartMissingAtStart() throws Exception {
        ByteBuffer in = toByteBuffer("bla" + START + "Hello World" + END + CR);
        decoder.doDecode(sessionContent, in, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testCRMissing() throws Exception {
        ByteBuffer in = toByteBuffer(START + "Hello World" + END + "error");
        decoder.doDecode(sessionContent, in, out);
    }

    @Test
    public void testNiceClass() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1);

        NiceClass.checkToString(decoder, resourceFactory);
        NiceClass.checkNullSafety(decoder, 
                asList(buffer, out, resourceFactory, session), 
                asList());
    }    
    
    private ByteBuffer toByteBuffer(String message) {
        ByteBuffer in = ByteBuffer.allocate(1024);        
        in.put(message.getBytes());
        in.flip();
        return in;
    }

    private final class TestDecoderOutput implements ProtocolDecoderOutput {
        private ResourceDataSource resource;
        private boolean throwsException;

        @Override
        public void write(Object message) {
            assertNull("Multiple calls not expected", resource);
            resource = (ResourceDataSource) message;
            if (throwsException) {
                throw new RuntimeException("test");
            }
        }

        @Override
        public void flush() {}

        public ResourceDataSource getResource() {
            ResourceDataSource last = resource;
            resource = null;
            return last;
        }
        
        public void throwException() {
            this.throwsException = true;
        }
    }
    
    private final class TestResourceFactory extends ResourceFactory {
        private ResourceDataSource resource;

        public TestResourceFactory(LargeBinaryStore store, String defaultResourceId) {
            super(store, defaultResourceId);
        }
        
        public ResourceDataSource createResource(String unitOfWorkId) throws IOException {
            resource = super.createResource(unitOfWorkId);
            return resource;
        }
        
        public ResourceDataSource getResource() {
            return resource;
        }
    }
}
