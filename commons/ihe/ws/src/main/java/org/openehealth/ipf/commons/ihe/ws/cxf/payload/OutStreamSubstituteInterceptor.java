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
package org.openehealth.ipf.commons.ihe.ws.cxf.payload;

import com.ctc.wstx.io.UTF8Writer;
import com.ctc.wstx.sw.BaseStreamWriter;
import com.ctc.wstx.sw.BufferingXmlWriter;
import org.apache.cxf.interceptor.StaxOutInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import javax.xml.stream.XMLStreamWriter;
import java.io.OutputStream;
import java.lang.reflect.Field;

/**
 * CXF interceptor that substitutes message output stream 
 * with a special wrapper that collects SOAP payload.
 *   
 * @author Dmytro Rud
 */
public class OutStreamSubstituteInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Field MWRITER_FIELD;
    private static final Field MOUT_WRITER_FIELD;
    private static final Field MOUT_STREAM_FIELD;
    static {
        try {
            MWRITER_FIELD = BaseStreamWriter.class.getDeclaredField("mWriter");
            MWRITER_FIELD.setAccessible(true);
            MOUT_WRITER_FIELD = BufferingXmlWriter.class.getDeclaredField("mOut");
            MOUT_WRITER_FIELD.setAccessible(true);
            MOUT_STREAM_FIELD = UTF8Writer.class.getDeclaredField("mOut");
            MOUT_STREAM_FIELD.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        }
    }

    public OutStreamSubstituteInterceptor() {
        super(Phase.PRE_STREAM);
        addAfter(StaxOutInterceptor.class.getName());
    }
    
    @Override
    public void handleMessage(Message message) {
        try {
            boolean success = false;
            Object x = message.getContent(XMLStreamWriter.class);
            if (x instanceof BaseStreamWriter) {
                x = MWRITER_FIELD.get(x);
                if (x instanceof BufferingXmlWriter) {
                    x = MOUT_WRITER_FIELD.get(x);
                    if (x instanceof UTF8Writer) {
                        UTF8Writer writer = (UTF8Writer) x;
                        x = MOUT_STREAM_FIELD.get(writer);
                        if (x instanceof OutputStream) {
                            OutputStream os = (OutputStream) x;
                            WrappedOutputStream wrapper = new WrappedOutputStream(os, (String) message.get(Message.ENCODING));
                            message.setContent(OutputStream.class, wrapper);
                            MOUT_STREAM_FIELD.set(writer, wrapper);
                            success = true;
                        }
                    }
                }
            }
            if (!success) {
                throw new IllegalStateException("Unable to wrap the output stream, check involved classes");
            }
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * Retrieves the instance of stream wrapper installed by this interceptor.
     * @param message
     *      CXF message which contains output stream as one of content types.
     * @return
     *      an instance of {@link WrappedOutputStream}.
     * @throws IllegalStateException
     *      when the stream wrapper instance could not be retrieved.
     */
    public static WrappedOutputStream getStreamWrapper(Message message) {
        OutputStream outputStream =  message.getContent(OutputStream.class);
        if (outputStream instanceof CacheAndWriteOutputStream) {
            // Extract what we need from the wrapper added by CXF. CXF sometimes adds the wrapper for diagnostics.
            outputStream = ((CacheAndWriteOutputStream) outputStream).getFlowThroughStream();
        }
        if (outputStream instanceof WrappedOutputStream) {
            return (WrappedOutputStream) outputStream;
        } else {
            throw new IllegalStateException("Message output stream is not of expected type");
        }
    }

}
