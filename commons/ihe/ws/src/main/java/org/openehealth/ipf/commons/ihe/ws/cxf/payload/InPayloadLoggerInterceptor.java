/*
 * Copyright 2011 the original author or authors.
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

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * CXF interceptor which pretty-prints incoming HTTP headers
 * and XML payload into the log.
 * @author Dmytro Rud
 */
public class InPayloadLoggerInterceptor extends AbstractPayloadLoggerInterceptor {

    public InPayloadLoggerInterceptor(String type) {
        super(Phase.PRE_STREAM, type);
        addAfter(AttachmentInInterceptor.class.getName());
        addBefore(StaxInInterceptor.class.getName());
    }

    @Override
    public void handleMessage(Message message) {
        try {
            InputStream stream = message.getContent(InputStream.class);
            byte[] streamBytes = IOUtils.readBytesFromStream(stream);
            Map httpHeaders = (Map) message.get(Message.PROTOCOL_HEADERS);
            doLog(new String(streamBytes), httpHeaders);
            message.setContent(InputStream.class, new ByteArrayInputStream(streamBytes));
        } catch (IOException e) {
            throw new RuntimeException("Error when logging incoming SOAP payload", e);
        }
    }
}

