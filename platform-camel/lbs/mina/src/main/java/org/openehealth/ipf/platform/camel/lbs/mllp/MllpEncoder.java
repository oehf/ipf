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

import static org.apache.commons.lang.Validate.notNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.camel.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * Encoder implementation that reads an input stream and writes the content in blocks.
 * <p>
 * This encoder converts the message to an {@link InputStream} (if needed) via the
 * type conversion provided by Camel. 
 * @author Jens Riemschneider
 */
public class MllpEncoder extends ProtocolEncoderAdapter {

    static final int BLOCK_SIZE = 1024;
    
    private TypeConverter typeConverter;

    private static final Log log = LogFactory.getLog(MllpEncoder.class);

    /**
     * Constructs the encoder
     * @param typeConverter
     *          the type converter to use when interpreting messages as {@code InputStream}
     */
    public MllpEncoder(TypeConverter typeConverter) {
        notNull(typeConverter, "typeConverter cannot be null");
        this.typeConverter = typeConverter;
    }

    /* (non-Javadoc)
     * @see org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina.common.IoSession, java.lang.Object, org.apache.mina.filter.codec.ProtocolEncoderOutput)
     */
    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        notNull(message, "message cannot be null");
        notNull(out, "out cannot be null");
        
        InputStream inputStream = typeConverter.convertTo(InputStream.class, message);        
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        
        try {
            AutoCreateByteBuffer buffer = new AutoCreateByteBuffer();
        
            putAndSendIfFull(out, buffer, (byte) MllpStoreCodec.START_MESSAGE);
            int data = 0;
            while (data != -1) {
                data = reader.read();
                if (data != -1) {
                    putAndSendIfFull(out, buffer, data);
                }                
            }
            
            putAndSendIfFull(out, buffer, (byte) MllpStoreCodec.END_MESSAGE);
            putAndSendIfFull(out, buffer, (byte) MllpStoreCodec.LAST_CHARACTER);
        
            out.write(buffer.getAndDrop());
            log.debug("encoded message: " + message);
        }
        finally {
            reader.close();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("{%1$s: typeConverter=%2$s}", getClass().getSimpleName(), typeConverter);
    }

    private void putAndSendIfFull(ProtocolEncoderOutput out, AutoCreateByteBuffer buffer, int data) {
        buffer.put((byte)data);
        if (!buffer.hasRemaining()) {
            out.write(buffer.getAndDrop());                    
        }
    }

    private static final class AutoCreateByteBuffer {
        private ByteBuffer buf;
        
        public void put(byte data) {
            if (buf == null) {
                buf = ByteBuffer.allocate(BLOCK_SIZE);
            }
            
            buf.put(data);
        }

        public boolean hasRemaining() {
            return buf.hasRemaining();
        }
        
        public ByteBuffer getAndDrop() {
            ByteBuffer bufForWrite = buf;
            buf = null;

            bufForWrite.flip();
            return bufForWrite;
        }        
    }
}
