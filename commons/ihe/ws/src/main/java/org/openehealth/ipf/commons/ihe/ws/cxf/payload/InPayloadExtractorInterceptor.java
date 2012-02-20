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
import java.io.UnsupportedEncodingException;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.interceptor.DocLiteralInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.StaxInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType;


/**
 * CXF interceptor that saves String payload of the incoming SOAP message
 * body into the CXF message.  Usable on both client and server sides.
 *
 * @author Dmytro Rud
 */
public class InPayloadExtractorInterceptor extends AbstractPhaseInterceptor<Message> {
    private final PayloadType payloadType;

    public InPayloadExtractorInterceptor(PayloadType payloadType) {
        super(InPayloadExtractorInterceptor.class.getName() + '-' + payloadType, getPhase(payloadType));
        this.payloadType = payloadType;
        switch (payloadType) {
            case HTTP:
                addBefore(AttachmentInInterceptor.class.getName());
                break;
            case SOAP_BODY:
                addAfter(AttachmentInInterceptor.class.getName());
                addBefore(StaxInInterceptor.class.getName());
                break;
        }
    }


    private static String getPhase(PayloadType payloadType) {
        switch (payloadType) {
            case HTTP:
                return Phase.RECEIVE;
            case SOAP_BODY:
                return Phase.PRE_STREAM;
        }
        throw new IllegalArgumentException("Unknown payload type " + payloadType);
    }


    @Override
    public void handleMessage(Message message) {
        if (isGET(message)) {
            return;
        }

        // extract current message contents from the stream,
        // substitute the used stream by an again-usable one.
        byte[] bytes;
        try {
            InputStream stream = message.getContent(InputStream.class);
            bytes = IOUtils.readBytesFromStream(stream);
            message.setContent(InputStream.class, new ByteArrayInputStream(bytes));
        } catch (IOException e) {
            throw new RuntimeException("Error when extracting payload", e);
        }

        // optionally extract SOAP Body from the SOAP Envelope
        String payload;
        try {
            String charsetName = (String) message.get(Message.ENCODING);
            payload = (charsetName != null) ? new String(bytes, charsetName) : new String(bytes);
        } catch (UnsupportedEncodingException e) {
            // actually cannot occur, because non-supported encodings
            // will cause exceptions a lot earlier
            throw new RuntimeException(e);
        }

        if (payloadType == PayloadType.SOAP_BODY) {
            payload = SoapUtils.extractSoapBody(payload);
        }

        // save the String payload into the message's content map
        StringPayloadHolder payloadHolder = message.getContent(StringPayloadHolder.class);
        if (payloadHolder == null) {
            payloadHolder = new StringPayloadHolder();
            message.setContent(StringPayloadHolder.class, payloadHolder);
        }
        payloadHolder.put(payloadType, payload);

        // optionally take care of dropping HTTP payload and
        // input stream after the SOAP Body has been successfully parsed
        if (payloadType == PayloadType.HTTP) {
            message.getInterceptorChain().add(new DropHttpPayloadInterceptor());
        }
    }


    /**
     * Interceptor which deletes saved "whole-HTTP" payload and the byte
     * array-based input stream, which are not necessary any more after
     * the SOAP Body has been successfully processed by the
     * {@link org.apache.cxf.interceptor.DocLiteralInInterceptor}.
     */
    private static class DropHttpPayloadInterceptor extends AbstractPhaseInterceptor<Message> {

        private DropHttpPayloadInterceptor() {
            super(Phase.UNMARSHAL);
            addAfter(DocLiteralInInterceptor.class.getName());
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            StringPayloadHolder payloadHolder = message.getContent(StringPayloadHolder.class);
            if (payloadHolder != null) {
                payloadHolder.remove(PayloadType.HTTP);
            }
            message.removeContent(InputStream.class);
        }
    }
}

