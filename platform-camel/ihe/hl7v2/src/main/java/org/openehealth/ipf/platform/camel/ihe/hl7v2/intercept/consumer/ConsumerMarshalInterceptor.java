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

import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
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

    public ConsumerMarshalInterceptor(
            Hl7v2ConfigurationHolder configurationHolder,
            Processor wrappedProcessor)
    {
        super(configurationHolder, wrappedProcessor);
    }


    /**
     * Unmarshals the request, passes it to the processing route, 
     * and marshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        MessageAdapter originalAdapter = null;
        Parser parser = getTransactionConfiguration().getParser();
        
        // unmarshal
        boolean unmarshallingFailed = false;
        try {
            org.apache.camel.Message in = exchange.getIn();
            String originalString = in.getBody(String.class);
            originalAdapter = MessageAdapters.make(parser, originalString);
            in.setBody(originalAdapter);
            
            // Store different representations of the original request into Camel headers.
            // Call to .copy() will throw an NPE when the message structure (MSH-9-3)
            // is wrong.
            in.setHeader(ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME,
                    (originalAdapter.getTarget() instanceof GenericMessage) 
                        ? originalAdapter 
                        : originalAdapter.copy());
            in.setHeader(ORIGINAL_MESSAGE_STRING_HEADER_NAME, originalString);
        } catch (Exception e) {
            unmarshallingFailed = true;
            LOG.error("Unmarshalling failed, message processing not possible", e);
            Message nak = getNakFactory().createDefaultNak(e);
            resultMessage(exchange).setBody(nak);
        }
        
        // run the route
        if( ! unmarshallingFailed) {
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
