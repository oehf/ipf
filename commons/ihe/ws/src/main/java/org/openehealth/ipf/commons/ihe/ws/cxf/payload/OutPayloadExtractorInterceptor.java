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

import java.io.OutputStream;

import org.apache.cxf.binding.soap.interceptor.SoapOutInterceptor.SoapOutEndingInterceptor;
import org.apache.cxf.io.CacheAndWriteOutputStream;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;

/**
 * CXF interceptor that reads outgoing payload collected by the output 
 * stream proxy installed in {@link OutStreamSubstituteInterceptor} 
 * and stores it in the message as String content type.
 * 
 * @author Dmytro Rud
 */
public class OutPayloadExtractorInterceptor extends AbstractPhaseInterceptor<Message> {

    public OutPayloadExtractorInterceptor() {
        super(Phase.WRITE_ENDING);
        addAfter(SoapOutEndingInterceptor.class.getName());
    }
    
    @Override
    public void handleMessage(Message message) {
        WrappedOutputStream wrapper;
        OutputStream outputStream =  message.getContent(OutputStream.class);
        if (outputStream instanceof CacheAndWriteOutputStream) {
            // Extract what we need from the wrapper added by CXF. CXF sometimes adds the wrapper for diagnostics.
            outputStream = ((CacheAndWriteOutputStream)outputStream).getFlowThroughStream();
        }
        if (outputStream instanceof WrappedOutputStream) {
            wrapper = (WrappedOutputStream) outputStream;
        } else {
            throw new IllegalStateException("Message output stream is not of expected type");
        }
        String soapEnvelope = wrapper.getCollectedPayloadAndDeactivate();
        String payload = SoapUtils.extractSoapBody(soapEnvelope);
        message.setContent(String.class, payload);
    }
}
