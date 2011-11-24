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

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.AttachmentInInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;

import java.util.Collection;
import java.util.Collections;

import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.HTTP;

/**
 * CXF interceptor that logs the whole payload (MIME, if so) of incoming messages.
 * Usable on both client and server sides.
 *
 * @author Dmytro Rud
 */
public class InPayloadLoggerInterceptor extends PayloadLogInterceptorBase {

    public InPayloadLoggerInterceptor(String fileNamePattern) {
        super(Phase.RECEIVE, fileNamePattern);
        addAfter(AttachmentInInterceptor.class.getName());
    }


    @Override
    protected String getHeadersPayload(SoapMessage message) {
        StringBuilder sb = new StringBuilder();

        if (Boolean.TRUE.equals(message.get(Message.REQUESTOR_ROLE))) {
            sb.append("HTTP response code: ").append(message.get(Message.RESPONSE_CODE)).append('\n');
        } else {
            sb.append("HTTP request: ")
                    .append(message.get(Message.HTTP_REQUEST_METHOD))
                    .append(' ')
                    .append(message.get(Message.REQUEST_URL))
                    .append('\n');
        }

        appendGenericHttpHeaders(message, sb);
        return sb.toString();
    }


    @Override
    protected String getBodyPayload(SoapMessage message) {
        StringPayloadHolder payloadHolder = message.getContent(StringPayloadHolder.class);
        return (payloadHolder != null) ? payloadHolder.get(HTTP) : "";
    }


    @Override
    public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
        return Collections.<PhaseInterceptor<? extends Message>> singletonList(new InPayloadExtractorInterceptor(HTTP));
    }
}

