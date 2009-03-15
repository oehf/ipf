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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.activation.DataSource;

import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.common.WriteFuture;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.lbs.utils.CorruptedInputStream;
import org.openehealth.ipf.commons.lbs.utils.NiceClass;

/**
 * @author Jens Riemschneider
 */
public class MllpEncoderTest {
    private MllpEncoder encoder;
    private TestEncoderOutput out;
    private TypeConverter typeConverter;
    private IoSession session;
    
    public static final String START = "\u000b";
    public static final String END = "\u001c";   
    public static final String CR = "\r";
    
    @Before
    public void setUp() throws Exception {
        typeConverter = new DefaultCamelContext().getTypeConverter();
        encoder = new MllpEncoder(typeConverter);
        out = new TestEncoderOutput();
        session = EasyMock.createNiceMock(IoSession.class);
    }

    @Test
    public void testSimpleEncodeWithString() throws Exception {
        checkSimpleEncode("Hello World");
    }

    @Test
    public void testSimpleEncodeWithByteArray() throws Exception {
        checkSimpleEncode("Hello World".getBytes());
    }

    @Test
    public void testSimpleEncodeWithInputStream() throws Exception {
        checkSimpleEncode(IOUtils.toInputStream("Hello World"));
    }

    @Test
    public void testSimpleEncodeWithDataSource() throws Exception {
        DataSource dataSource = new DataSource() {
            @Override
            public InputStream getInputStream() throws IOException { 
                return IOUtils.toInputStream("Hello World");
            }
            @Override
            public String getContentType() { return null; }
            @Override
            public String getName() { return null; }
            @Override
            public OutputStream getOutputStream() throws IOException { return null; }            
        };
        checkSimpleEncode(dataSource);
    }

    private void checkSimpleEncode(Object message) throws Exception {
        encoder.encode(session, message, out);
        assertEquals(1, out.getWrittenBuffers().size());
        ByteBuffer buffer = out.getWrittenBuffers().get(0);
        assertMessageInBuffer("Hello World", buffer);
    }
    
    @Test
    public void testSimpleEncodeWithEmptyString() throws Exception {
        encoder.encode(session, "", out);
        assertEquals(1, out.getWrittenBuffers().size());
        ByteBuffer buffer = out.getWrittenBuffers().get(0);
        assertMessageInBuffer("", buffer);
    }

    @Test(expected = IOException.class)
    public void testEncodeStreamThrowsException() throws Exception {
        CorruptedInputStream inputStream = new CorruptedInputStream();
        try {
            encoder.encode(session, inputStream, out);
        }
        finally {
            assertTrue("Input stream not closed", inputStream.isClosed());
        }
    }
    
    @Test
    public void testEncodeLongerMessage() throws Exception {
        checkLongMessage(MllpEncoder.BLOCK_SIZE);
    }

    @Test
    public void testEncodeMessageWhereEndAndCRIsInNextBuffer() throws Exception {
        checkLongMessage(MllpEncoder.BLOCK_SIZE - 1);
    }
    
    @Test
    public void testEncodeMessageWhereCRIsInNextBuffer() throws Exception {
        checkLongMessage(MllpEncoder.BLOCK_SIZE - 2);
    }
    
    @Test
    public void testNiceClass() throws Exception {
        NiceClass.checkToString(encoder, typeConverter);
        NiceClass.checkNullSafety(encoder, asList("bla", out, session, typeConverter), asList(session));
    }
    
    private void checkLongMessage(int length) throws Exception {
        String message = StringUtils.repeat("A", length);        
        encoder.encode(session, message, out);
        assertEquals(2, out.getWrittenBuffers().size());
        ByteBuffer buffer = merge(out.getWrittenBuffers());
        assertMessageInBuffer(message, buffer);
    }
    
    private ByteBuffer merge(Collection<ByteBuffer> buffers) {
        int totalLimit = 0;
        for (ByteBuffer buffer : buffers) {
            totalLimit += buffer.limit();
        }

        ByteBuffer merged = ByteBuffer.allocate(totalLimit);
        for (ByteBuffer buffer : buffers) {
            merged.put(buffer);
        }
        merged.flip();
        return merged;
    }

    private void assertMessageInBuffer(String message, ByteBuffer buffer) {
        assertEquals(START, String.valueOf((char)buffer.get()));
        byte[] data = new byte[message.length()];
        buffer.get(data);
        assertTrue(Arrays.equals(message.getBytes(), data));
        assertEquals(END, String.valueOf((char)buffer.get()));
        assertEquals(CR, String.valueOf((char)buffer.get()));
        assertEquals(0, buffer.remaining());
    }

    private final class TestEncoderOutput implements ProtocolEncoderOutput {
        private List<ByteBuffer> writtenBuffers = new ArrayList<ByteBuffer>();
        private boolean throwsException;

        public List<ByteBuffer> getWrittenBuffers() {
            return writtenBuffers;
        }
        
        public void throwException() {
            this.throwsException = true;
        }

        @Override
        public void mergeAll() {}

        @Override
        public void write(ByteBuffer buf) {
            writtenBuffers.add(buf);
            if (throwsException) {
                throw new RuntimeException("test");
            }
        }

        @Override
        public WriteFuture flush() {
            return null;
        }
    }
}
