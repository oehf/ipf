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

import org.apache.cxf.binding.soap.interceptor.SoapOutInterceptor.SoapOutEndingInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.Phase;

import java.io.OutputStream;

/**
 * CXF interceptor which pretty-prints outgoing XML payload into the log.
 * <p>
 * A prerequisite is {@link OutStreamSubstituteInterceptor} must be installed as well.
 *
 * @author Dmytro Rud
 */
public class OutPayloadLoggerInterceptor extends AbstractPayloadLoggerInterceptor {

    public OutPayloadLoggerInterceptor(String title) {
        super(Phase.WRITE_ENDING, title);
        addAfter(SoapOutEndingInterceptor.class.getName());
    }
    
    @Override
    public void handleMessage(Message message) {
        WrappedOutputStream wrapper = (WrappedOutputStream) message.getContent(OutputStream.class);
        String soapEnvelope = wrapper.getCollectedPayloadAndDeactivate();
        doLog(soapEnvelope, null);
    }
}
