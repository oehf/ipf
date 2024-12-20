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

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v2.Constants;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;


/**
 * Consumer-side Camel interceptor which creates a {@link Message}
 * from various possible response types.
 *
 * @author Dmytro Rud
 */
public class ConsumerAdaptingInterceptor extends InterceptorSupport {
    private static final Logger log = LoggerFactory.getLogger(ConsumerAdaptingInterceptor.class);
    public static final String ACK_TYPE_CODE_HEADER = "ipf.hl7v2.AckTypeCode";

    private final String charsetName;


    /**
     * Constructor which enforces the use of a particular character set.
     * The given value will be propagated to the Camel exchange property
     * {@link Exchange#CHARSET_NAME}, rewriting its old content.
     *
     * @param charsetName
     *      character set to use in all data transformations.
     */
    public ConsumerAdaptingInterceptor(String charsetName) {
        super();
        this.charsetName = charsetName;
    }


    /**
     * Constructor which does not enforce the use of a particular character set.
     * When the Camel exchange does not contain property {@link Exchange#CHARSET_NAME},
     * the default system character set will be used.
     */
    public ConsumerAdaptingInterceptor() {
        this(null);
    }

    
    /**
     * Converts response to a {@link Message}, throws
     * a {@link org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException} on failure.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        var originalMessage = exchange.getIn().getHeader(Constants.ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, Message.class);

        // run the route
        try {
            getWrappedProcessor().process(exchange);
            var exception = Exchanges.extractException(exchange);
            if (exception != null) {
                throw exception;
            }
        } catch (Exception e) {
            log.warn("Message processing failed ({}: {}). Creating NAK message.", e.getClass().getSimpleName(), e.getMessage());
            log.debug("Exception details: ", e);
            exchange.getMessage().setBody(getEndpoint(HL7v2Endpoint.class).getNakFactory().createNak(originalMessage, e));
        }

        // try to convert route response from a known type
        if (charsetName != null) {
            exchange.setProperty(Exchange.CHARSET_NAME, charsetName);
        }

        var message = exchange.getMessage();
        var body = exchange.getMessage().getBody();
        try {
            message.setBody(Hl7v2MarshalUtils.convertBodyToMessage(
                    message,
                    characterSet(exchange),
                    getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration().getParser()));
        } catch (Hl7v2AdaptingException e) {
            if (body instanceof Throwable) {
                message.setBody(getEndpoint(HL7v2Endpoint.class).getNakFactory().createNak(originalMessage, (Throwable) body));
            } else {
                analyseMagicHeader(message, originalMessage).ifPresentOrElse(message::setBody, () -> {
                    throw e;
                });
            }
        }
    }


    /**
     * Considers a specific header to determine whether the route author want us to generate
     * an automatic acknowledgment, and generates the latter when the author really does.   
     */
    private Optional<Message> analyseMagicHeader(org.apache.camel.Message m, Message originalMessage) throws HL7Exception, IOException {
        var header = m.getHeader(ACK_TYPE_CODE_HEADER);
        if (!(header instanceof AcknowledgmentCode)) {
            return Optional.empty();
        }

        Message ack;
        if ((header == AcknowledgmentCode.AA) || (header == AcknowledgmentCode.CA)) {
            ack = getEndpoint(HL7v2Endpoint.class).getNakFactory().createAck(
                    originalMessage);
        } else {
            var exception = new HL7Exception(
                    "HL7v2 processing failed",
                    getEndpoint(HL7v2Endpoint.class).getHl7v2TransactionConfiguration().getResponseErrorDefaultErrorCode());
            ack = getEndpoint(HL7v2Endpoint.class).getNakFactory().createNak(
                    originalMessage,
                    exception, 
                    (AcknowledgmentCode) header);
        }
        return Optional.ofNullable(ack);
    }

}
