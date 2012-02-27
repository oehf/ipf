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

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.AbstractHl7v2Interceptor;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Consumer-side HL7 marshaling/unmarshaling interceptor.
 * <p>
 * When the Camel exchange does not contain property {@link Exchange#CHARSET_NAME},
 * the default system character set will be used.
 *
 * @author Dmytro Rud
 */
public class ConsumerMarshalInterceptor extends AbstractHl7v2Interceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerMarshalInterceptor.class);


    /**
     * Unmarshals the request, passes it to the processing route, 
     * and marshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        MessageAdapter<?> originalAdapter = null;
        Parser parser = getHl7v2TransactionConfiguration().getParser();
        
        org.apache.camel.Message inMessage = exchange.getIn();
        String originalString = inMessage.getBody(String.class);

        // unmarshal
        boolean unmarshallingFailed = false;
        try {
            originalAdapter = MessageAdapters.make(parser, originalString);
        } catch (Exception e) {
            unmarshallingFailed = true;
            LOG.error("Unmarshalling failed, message processing not possible", e);
            Message nak = getNakFactory().createDefaultNak(e);
            resultMessage(exchange).setBody(nak);
        }

        if( ! unmarshallingFailed) {
            // prepare exchange
            MessageAdapter<?> copyAdapter;
            try {
                copyAdapter = originalAdapter.copy();
            } catch (Exception e) {
                // this exception will occur when the message structure (MSH-9-3) of
                // the original adapter is wrong or when unknown segments are present
                copyAdapter = originalAdapter;
            }
            inMessage.setBody(originalAdapter);
            inMessage.setHeader(ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, copyAdapter);
            inMessage.setHeader(ORIGINAL_MESSAGE_STRING_HEADER_NAME, originalString);

            // run the route
            try {
                getWrappedProcessor().process(exchange);
            } catch (Hl7v2AdaptingException mae) {
                throw mae;
            } catch (Exception e) {
                LOG.error("Message processing failed", e);
                resultMessage(exchange).setBody(
                        getNakFactory().createNak(originalAdapter.getHapiMessage(), e));
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
