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
import org.apache.cxf.interceptor.AttachmentOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.phase.PhaseInterceptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * CXF interceptor that logs the whole payload (MIME, if so) of outgoing messages.
 * Usable on both client and server sides.
 *
 * @author Dmytro Rud
 */
public class OutPayloadLoggerInterceptor extends PayloadLogInterceptorBase {

    public OutPayloadLoggerInterceptor(String fileNamePattern) {
        super(Phase.PRE_STREAM_ENDING, fileNamePattern);
        addAfter(AttachmentOutInterceptor.AttachmentOutEndingInterceptor.class.getName());
    }


    @Override
    protected String getHeadersPayload(SoapMessage message) {
        StringBuilder sb = new StringBuilder();

        Object endpointAddress = message.get(Message.ENDPOINT_ADDRESS);
        if (endpointAddress != null) {
            sb.append("Target endpoint: ").append(endpointAddress).append('\n');
        } else {
            Object responseCode = message.get(Message.RESPONSE_CODE);
            sb.append("HTTP response code: ")
                    .append((responseCode != null) ? responseCode : "200")
                    .append('\n');
        }

        appendGenericHttpHeaders(message, sb);
        return sb.toString();
    }


    @Override
    protected String getBodyPayload(SoapMessage message) {
        WrappedOutputStream wrapper = OutStreamSubstituteInterceptor.getStreamWrapper(message);
        wrapper.deactivate();
        return wrapper.getCollectedPayload();
    }


    @Override
    public Collection<PhaseInterceptor<? extends Message>> getAdditionalInterceptors() {
        Collection<PhaseInterceptor<? extends Message>> result = new ArrayList<PhaseInterceptor<? extends Message>>(2);
        result.add(new DisablePayloadCollectingDeactivationInterceptor());
        result.add(new OutStreamSubstituteInterceptor());
        return result;
    }


    /**
     * Interceptor which sets a context property to disable stopping collecting payload
     * in the {@link OutPayloadExtractorInterceptor}.
     */
    static class DisablePayloadCollectingDeactivationInterceptor extends AbstractPhaseInterceptor<Message> {
        public DisablePayloadCollectingDeactivationInterceptor() {
            super(Phase.WRITE_ENDING);
            addBefore(OutPayloadExtractorInterceptor.class.getName());
        }

        @Override
        public void handleMessage(Message message) throws Fault {
            message.setContextualProperty(
                    OutPayloadExtractorInterceptor.PAYLOAD_COLLECTING_DEACTIVATION_ENABLED,
                    Boolean.FALSE);
        }
    }
}

