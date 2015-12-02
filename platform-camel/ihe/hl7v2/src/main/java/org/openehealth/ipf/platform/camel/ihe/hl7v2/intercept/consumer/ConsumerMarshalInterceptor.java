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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v2.Constants;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Consumer-side HL7 marshaling/unmarshaling interceptor.
 * <p>
 * When the Camel exchange does not contain property {@link Exchange#CHARSET_NAME},
 * the default system character set will be used.
 *
 * @author Dmytro Rud
 */
public class ConsumerMarshalInterceptor extends InterceptorSupport<HL7v2Endpoint> {
    private static final transient Logger LOG = LoggerFactory.getLogger(ConsumerMarshalInterceptor.class);


    /**
     * Unmarshals the request, passes it to the processing route, 
     * and marshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message originalMessage = null;
        Parser parser = getEndpoint().getHl7v2TransactionConfiguration().getParser();
        
        org.apache.camel.Message inMessage = exchange.getIn();
        String originalString = inMessage.getBody(String.class);

        // unmarshal
        boolean unmarshallingFailed = false;
        try {
            originalMessage = parser.parse(originalString);
        } catch (HL7Exception e) {
            unmarshallingFailed = true;
            LOG.error("Unmarshalling failed, message processing not possible", e);
            Message nak = getEndpoint().getNakFactory().createDefaultNak(e);
            resultMessage(exchange).setBody(nak);
        }

        if( ! unmarshallingFailed) {
            // prepare exchange
            Message copy;
            try {

                copy = MessageUtils.copy(originalMessage);
            } catch (Exception e) {
                // this exception will occur when the message structure (MSH-9-3) of
                // the original adapter is wrong or when unknown segments are present
                copy = originalMessage;
            }
            inMessage.setBody(originalMessage);
            inMessage.setHeader(Constants.ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, copy);
            inMessage.setHeader(Constants.ORIGINAL_MESSAGE_STRING_HEADER_NAME, originalString);

            // run the route
            try {
                getWrappedProcessor().process(exchange);
            } catch (Hl7v2AdaptingException mae) {
                throw mae;
            } catch (Exception e) {
                LOG.error("Message processing failed", e);
                resultMessage(exchange).setBody(
                        getEndpoint().getNakFactory().createNak(originalMessage, e));
            }
        }
        
        // marshal if necessary
        String s = Hl7v2MarshalUtils.marshalStandardTypes(
                resultMessage(exchange),
                characterSet(exchange),
                parser);
        resultMessage(exchange).setBody(s);
    }

}
