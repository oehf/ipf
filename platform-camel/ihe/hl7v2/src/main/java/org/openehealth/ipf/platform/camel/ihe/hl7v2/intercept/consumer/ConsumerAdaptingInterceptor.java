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
import org.apache.camel.Exchange;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.AbstractHl7v2Interceptor;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;


/**
 * Consumer-side Camel interceptor which creates a {@link MessageAdapter} 
 * from various possible response types.
 *
 * @author Dmytro Rud
 */
public class ConsumerAdaptingInterceptor extends AbstractHl7v2Interceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerAdaptingInterceptor.class);
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
     * Converts response to a {@link MessageAdapter}, throws 
     * a {@link org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AdaptingException} on failure.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        MessageAdapter<?> originalAdapter = exchange.getIn().getHeader(ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME, MessageAdapter.class); 
        Message originalMessage = originalAdapter.getHapiMessage();

        // run the route
        try {
            getWrappedProcessor().process(exchange);
            Exception exception = Exchanges.extractException(exchange);
            if (exception != null) {
                throw exception;
            }
        } catch (Exception e) {
            LOG.error("Message processing failed", e);
            resultMessage(exchange).setBody(getNakFactory().createNak(originalMessage, e));
        }

        org.apache.camel.Message m = Exchanges.resultMessage(exchange);
        Object body = m.getBody();

        // try to convert route response from a known type
        if (charsetName != null) {
            exchange.setProperty(Exchange.CHARSET_NAME, charsetName);
        }
        MessageAdapter<?> msg = Hl7v2MarshalUtils.extractMessageAdapter(
                m,
                characterSet(exchange),
                getHl7v2TransactionConfiguration().getParser());
        
        // additionally: an Exception in the body?
        if((msg == null) && (body instanceof Throwable)) {
            Message message = getNakFactory().createNak(originalMessage, (Throwable) body);
            msg = new MessageAdapter(message);
        }
        
        // no known data type --> determine user's intention on the basis of a header 
        if(msg == null) {
            msg = analyseMagicHeader(m, originalMessage);
        }

        // unable to create a MessageAdaper :-(
        if(msg == null) {
            throw new Hl7v2AdaptingException("Cannot create HL7v2 message from " +
                    ClassUtils.getSimpleName(body, "<null>") +
                    " returned from the route");
        }
        
        m.setBody(msg);
    }


    /**
     * Considers a specific header to determine whether the route author want us to generate
     * an automatic acknowledgment, and generates the latter when the author really does.   
     */
    @SuppressWarnings("rawtypes")
    private MessageAdapter analyseMagicHeader(org.apache.camel.Message m, Message originalMessage) {
        Object header = m.getHeader(ACK_TYPE_CODE_HEADER);
        if ((header == null) || ! (header instanceof AckTypeCode)) {
            return null;
        }

        Message ack;
        if ((header == AckTypeCode.AA) || (header == AckTypeCode.CA)) {
            ack = getNakFactory().createAck(
                    originalMessage,
                    (AckTypeCode) header);
        } else {
            HL7v2Exception exception = new HL7v2Exception(
                    "HL7v2 processing failed",
                    getHl7v2TransactionConfiguration().getResponseErrorDefaultErrorCode());
            ack = getNakFactory().createNak(
                    originalMessage,
                    exception, 
                    (AckTypeCode) header); 
        }
        return new MessageAdapter(ack);
    }

}
