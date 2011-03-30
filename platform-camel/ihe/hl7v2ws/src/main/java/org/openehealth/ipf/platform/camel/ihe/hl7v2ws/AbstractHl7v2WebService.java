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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils.checkRequestAcceptance;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils.checkResponseAcceptance;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.createNak;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.extractMessageAdapter;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

/**
 * Generic implementation of an HL7v2-based Web Service.
 * 
 * @author Dmytro Rud
 * @author Stefan Ivanov
 */
public abstract class AbstractHl7v2WebService extends DefaultItiWebService {
    private static final Log LOG = LogFactory.getLog(AbstractHl7v2WebService.class);

    private final MllpTransactionConfiguration config;

    public AbstractHl7v2WebService(MllpTransactionConfiguration config) {
        super();
        this.config = config;
    }


    protected String doProcess(String hl7RequestString) {
        hl7RequestString = hl7RequestString.trim();
        
        // message acceptance and validation
        MessageAdapter msg;
        try {
            msg = MessageAdapters.make(config.getParser(), hl7RequestString);
            checkRequestAcceptance(msg, config);
        } catch (Exception e) {
            LOG.error(msg("unacceptable request, return NAK"));
            return defaultNakString(e);
        }
       
        // delegate message processing to the route
        MessageAdapter originalRequest = msg.copy();
        Exchange exchange = super.process(msg);
        Exception exchangeException = exchange.getException();

        // check processing results after the route
        if (exchangeException != null) {
            LOG.error(msg("exchange failed, return NAK"));
            return nakString(originalRequest, exchangeException);
        }

        // create result from the outbound message
        try {
            MessageAdapter responseMsg = extractMessageAdapter(
                    resultMessage(exchange),
                    exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                    config.getParser());

            checkResponseAcceptance(responseMsg, config);
            return renderForWs(responseMsg);
        } catch (Exception formatException) {
            LOG.error(msg("unacceptable response, return NAK"),
                    formatException);
            return nakString(originalRequest, formatException);
        }
    }


    /**
     * Creates a message from the input <code>text</code >with prefix the sending
     * application in the transaction configuration.
     * 
     * @param text
     *            input text used for logging
     * @return a String with prefix the sending application
     */
    protected String msg(String text) {
        return config.getSendingApplication() + ": " + text;
    }


    protected String renderForWs(MessageAdapter adapter) {
        return renderForWs((Message) adapter.getTarget());
    }


    /**
     * Converts the given HAPI Message to a String suitable for WS transport.
     * @param message
     *      a {@link Message} to convert.
     * @return a String representation of the given HAPI message.
     */
    protected String renderForWs(Message message) {
        try {
            return config.getParser().encode(message).replaceAll("\r", "\r\n");
        } catch (HL7Exception e) {
            // actually cannot occur
            throw new RuntimeException(e);
        }
    }


    /**
     * Creates an HL7v2 NAK message related to the given one.
     * @param originalRequest
     *      original request message.
     * @param exception
     *      exception occurred.
     * @return
     *      NAK as a String ready for WS transport.
     */
    protected String nakString(MessageAdapter originalRequest, Exception exception) {
        MessageAdapter nak = createNak(exception, (Message) originalRequest.getTarget(), config);
        return renderForWs((Message) nak.getTarget());
    }


    /**
     * Creates a "default" HL7v2 NAK message.
     * @param exception
     *      exception occurred.
     * @return
     *      NAK as a String ready for WS transport.
     */
    protected String defaultNakString(Throwable exception) {
        Message nak = config.getNakFactory().createDefaultNak(config, exception);
        return renderForWs(nak);
    }
    
}
