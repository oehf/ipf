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
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v2.Constants;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Consumer-side HL7 marshaling/unmarshaling interceptor.
 * <p>
 * When the Camel exchange does not contain property {@link Exchange#CHARSET_NAME},
 * the default system character set will be used.
 *
 * @author Dmytro Rud
 */
public class ConsumerMarshalInterceptor extends InterceptorSupport {

    private static final Logger log = LoggerFactory.getLogger(ConsumerMarshalInterceptor.class);

    private final boolean copyOriginalMessage;

    public ConsumerMarshalInterceptor(boolean copyOriginalMessage) {
        this.copyOriginalMessage = copyOriginalMessage;
    }

    /**
     * Unmarshals the request, passes it to the processing route and marshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Message originalMessage;
        var parser = getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration().getParser();

        // This should already be a string
        var inMessage = exchange.getIn();
        var originalString = inMessage.getBody(String.class);

        try {
            originalMessage = parser.parse(originalString);
        } catch (HL7Exception e) {
            log.error("Unmarshalling failed, message processing not possible ({}). Creating a default NAK response", e.getMessage());
            log.debug("Exception details: ", e);
            var nak = getEndpoint(HL7v2Endpoint.class).getNakFactory().createDefaultNak(e);
            exchange.getMessage().setBody(parser.encode(nak));
            return;
        }

        inMessage.setBody(originalMessage);
        inMessage.setHeader(Constants.ORIGINAL_MESSAGE_STRING_HEADER_NAME, originalString);

        // Put the original message into the headers. Make a copy if requested
        try {
            inMessage.setHeader(Constants.ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, copyOriginalMessage ? MessageUtils.copyMessage(originalMessage) : originalMessage);
        } catch (Exception e) {
            // this exception will occur when the message structure (MSH-9-3) of
            // the original adapter is wrong or when unknown segments are present
            inMessage.setHeader(Constants.ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, originalMessage);
        }

        // run the route
        try {
            getWrappedProcessor().process(exchange);
            var s = Hl7v2MarshalUtils.convertBodyToString(
                    exchange.getMessage(),
                    characterSet(exchange),
                    false);
            exchange.getMessage().setBody(s);
        } catch (Exception e) {
            // With Netty, we treat unhandlable response types like other errors and return a NAK
            log.warn("Message processing failed ({}). Creating NAK message.", e.getMessage());
            log.debug("Exception details: ", e);
            var nak = getEndpoint(HL7v2Endpoint.class).getNakFactory().createNak(originalMessage, e);
            exchange.getMessage().setBody(parser.encode(nak));
        }

    }

}
