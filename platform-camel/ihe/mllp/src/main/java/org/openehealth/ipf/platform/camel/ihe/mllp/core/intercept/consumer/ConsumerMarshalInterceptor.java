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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAdaptingException;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import ca.uhn.hl7v2.model.GenericMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;


/**
 * Consumer-side HL7 marshaling/unmarshaling interceptor.
 * @author Dmytro Rud
 */
public class ConsumerMarshalInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerMarshalInterceptor.class);

    public ConsumerMarshalInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
    }


    /**
     * Unmarshals the request, passes it to the processing route, 
     * and marshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        String charset = getMllpEndpoint().getConfiguration().getCharsetName();
        MessageAdapter originalAdapter = null;
        Parser parser = getMllpEndpoint().getTransactionConfiguration().getParser();
        
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
            Message nak = getMllpEndpoint().getNakFactory().createDefaultNak(e);
            resultMessage(exchange).setBody(nak);
        }
        
        // run the route
        if( ! unmarshallingFailed) {
            try {
                exchange.setProperty(Exchange.CHARSET_NAME, charset);
                getWrappedProcessor().process(exchange);
            } catch (MllpAdaptingException mae) {
                throw mae;
            } catch (Exception e) {
                LOG.error("Message processing failed", e);
                resultMessage(exchange).setBody(getMllpEndpoint().getNakFactory().
                        createNak(originalAdapter.getHapiMessage(), e));
            }
        }
        
        // marshal if necessary
        String s = MllpMarshalUtils.marshalStandardTypes(
            resultMessage(exchange), 
            charset, 
            parser);
        resultMessage(exchange).setBody(s);
    }

}
