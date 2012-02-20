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

import org.apache.cxf.binding.soap.interceptor.SoapOutInterceptor.SoapOutEndingInterceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.openehealth.ipf.commons.ihe.ws.utils.SoapUtils;
import static org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder.PayloadType.SOAP_BODY;

/**
 * CXF interceptor that reads outgoing payload collected by the output 
 * stream proxy installed in {@link OutStreamSubstituteInterceptor} 
 * and stores it in the message as String content type.
 * 
 * @author Dmytro Rud
 */
public class OutPayloadExtractorInterceptor extends AbstractPhaseInterceptor<Message> {

    /**
     * When the CXF message contains <code>Boolean.FALSE</code> in the property
     * with this name, collecting message payload will not be deactivated
     * after the SOAP part has been written.
     */
    public static final String PAYLOAD_COLLECTING_DEACTIVATION_ENABLED =
            OutPayloadExtractorInterceptor.class.getName() + ".collecting.deactivation.enabled";


    public OutPayloadExtractorInterceptor() {
        super(Phase.WRITE_ENDING);
        addAfter(SoapOutEndingInterceptor.class.getName());
    }


    @Override
    public void handleMessage(Message message) {
        if (isGET(message)) {
            return;
        }

        WrappedOutputStream wrapper = OutStreamSubstituteInterceptor.getStreamWrapper(message);
        if (! Boolean.FALSE.equals(message.getContextualProperty(PAYLOAD_COLLECTING_DEACTIVATION_ENABLED))) {
            wrapper.deactivate();
        }

        String soapEnvelope = wrapper.getCollectedPayload();
        String payload = SoapUtils.extractSoapBody(soapEnvelope);
        StringPayloadHolder payloadHolder = new StringPayloadHolder();
        payloadHolder.put(SOAP_BODY, payload);
        message.setContent(StringPayloadHolder.class, payloadHolder);
    }
}
