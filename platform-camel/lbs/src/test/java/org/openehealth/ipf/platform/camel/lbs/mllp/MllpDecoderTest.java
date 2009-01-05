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
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentDataSource;
import org.openehealth.ipf.commons.lbs.attachment.AttachmentFactory;
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore;
import org.openehealth.ipf.commons.lbs.store.MemoryStore;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;
import org.openehealth.ipf.platform.camel.lbs.mllp.MllpDecoder.SessionContent;

/**
 * @author Jens Riemschneider
 */
public class MllpDecoderTest {
    private MemoryStore store;
    private TestAttachmentFactory attachmentFactory;
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
        attachmentFactory = new TestAttachmentFactory(store, "defaultid");
        decoder = new MllpDecoder(attachmentFactory);
        sessionContent = new SessionContent(attachmentFactory);
        out = new TestDecoderOutput();
        session = EasyMock.createNiceMock(IoSession.class);
    }

    @Test
    public void testDecodeSimpleShortMessage() throws Exception {
        String message = START + "Hello World" + END + CR;
        ByteBuffer in = toByteBuffer(message);
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Hello World", IOUtils.toString(out.getAttachment().getInputStream()));
        assertEquals(message.length(), in.position());
    }
    
    @Test
    public void testDecodeTwoMessages() throws Exception {
        String message = START + "Hello World" + END + CR + START + "Bye" + END + CR;
        ByteBuffer in = toByteBuffer(message);
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Hello World", IOUtils.toString(out.getAttachment().getInputStream()));
        assertTrue(decoder.doDecode(sessionContent, in, out));
        assertEquals("Bye", IOUtils.toString(out.getAttachment().getInputStream()));
        assertEquals(message.length(), in.position());
    }
    
    @Test
    public void testDecodeMessageThatSpansMultipleBuffers() throws Exception {
        String messagePart1 = START + "This is only the beginning of the message. ";
        String messagePart2 = "It continues in the next buffer" + END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getAttachment());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is only the beginning of the message. It continues in the next buffer", 
                IOUtils.toString(out.getAttachment().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithCRInNextBuffer() throws Exception {
        String messagePart1 = START + "This is nearly the whole message" + END;
        String messagePart2 = CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getAttachment());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getAttachment().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithEndInNextBuffer() throws Exception {
        String messagePart1 = START + "This is nearly the whole message";
        String messagePart2 = END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getAttachment());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getAttachment().getInputStream()));
    }
    
    @Test
    public void testDecodeMessageWithStartAsLastChar() throws Exception {
        String messagePart1 = START;
        String messagePart2 = "This is nearly the whole message" + END + CR;
        ByteBuffer in1 = toByteBuffer(messagePart1);
        ByteBuffer in2 = toByteBuffer(messagePart2);
        assertFalse(decoder.doDecode(sessionContent, in1, out));
        assertNull(out.getAttachment());
        assertTrue(decoder.doDecode(sessionContent, in2, out));
        assertEquals("This is nearly the whole message", 
                IOUtils.toString(out.getAttachment().getInputStream()));
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

        AttachmentDataSource attachment = out.getAttachment();
        assertFalse("Attachment must be removed when an exception occurs", 
                store.contains(attachment.getResourceUri()));
    }
    
    @Test
    public void testDecodeStartsMessageButWeCloseSessionBeforeEnd() throws Exception {
        String message = START + "This is a ";
        ByteBuffer in = toByteBuffer(message);
        assertFalse(decoder.doDecode(sessionContent, in, out));
        decoder.finishDecode(sessionContent);
        
        // Attachment was not written but it was created by the factory
        assertNull(out.getAttachment());
        AttachmentDataSource attachment = attachmentFactory.getAttachment();
        assertFalse("Attachment must be removed when an exception occurs", 
                store.contains(attachment.getResourceUri()));
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

        NiceClass.checkToString(decoder, attachmentFactory);
        NiceClass.checkNullSafety(decoder, 
                asList(buffer, out, attachmentFactory, session), 
                asList());
    }    
    
    private ByteBuffer toByteBuffer(String message) {
        ByteBuffer in = ByteBuffer.allocate(1024);        
        in.put(message.getBytes());
        in.flip();
        return in;
    }

    private final class TestDecoderOutput implements ProtocolDecoderOutput {
        private AttachmentDataSource attachment;
        private boolean throwsException;

        @Override
        public void write(Object message) {
            assertNull("Multiple calls not expected", attachment);
            attachment = (AttachmentDataSource) message;
            if (throwsException) {
                throw new RuntimeException("test");
            }
        }

        @Override
        public void flush() {}

        public AttachmentDataSource getAttachment() {
            AttachmentDataSource last = attachment;
            attachment = null;
            return last;
        }
        
        public void throwException() {
            this.throwsException = true;
        }
    }
    
    private final class TestAttachmentFactory extends AttachmentFactory {
        private AttachmentDataSource attachment;

        public TestAttachmentFactory(LargeBinaryStore store, String defaultAttachmentId) {
            super(store, defaultAttachmentId);
        }
        
        public AttachmentDataSource createAttachment(String unitOfWorkId) throws IOException {
            attachment = super.createAttachment(unitOfWorkId);
            return attachment;
        }
        
        public AttachmentDataSource getAttachment() {
            return attachment;
        }
    }
}
