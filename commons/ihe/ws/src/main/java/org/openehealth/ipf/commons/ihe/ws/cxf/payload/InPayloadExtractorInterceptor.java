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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;


/**
 * CXF interceptor that saves XML payload of the incoming SOAP message 
 * body into String content of the CXF message.
 * <p>
 * Usable on both client and server sides. 
 * 
 * @author Dmytro Rud
 */
public class InPayloadExtractorInterceptor extends AbstractPhaseInterceptor<Message> {

    private final boolean needExtractSoapBody;

    /**
     * Constructor.
     * @param needExtractSoapBody
     *      whether the proper SOAP body should be extracted from
     *      the payload (<code>true</code>), or the payload should
     *      be saved as-is (<code>false</code>).
     */
    public InPayloadExtractorInterceptor(boolean needExtractSoapBody) {
        super(Phase.PRE_STREAM);
        addAfter(AttachmentInInterceptor.class.getName());
        addBefore(StaxInInterceptor.class.getName());
        this.needExtractSoapBody = needExtractSoapBody;
    }

    @Override
    public void handleMessage(Message message) {
        try {
            InputStream stream = message.getContent(InputStream.class);
            byte[] streamBytes = IOUtils.readBytesFromStream(stream);
            String payload = new String(streamBytes);
            if (needExtractSoapBody) {
                payload = SoapUtils.extractSoapBody(payload);
            }
            message.setContent(String.class, payload);
            message.setContent(InputStream.class, new ByteArrayInputStream(streamBytes));
        } catch (IOException e) {
            throw new RuntimeException("Error when extracting SOAP payload", e);
        }
    }
}

