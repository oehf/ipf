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
import ca.uhn.hl7v2.parser.Parser;

/**
 * Generic implementation of an HL7v2-based Web Service.
 * 
 * @author Dmytro Rud
 * @author Mitko Kolev
 * @author Stefan Ivanov
 */
public abstract class AbstractHl7v2WebService extends DefaultItiWebService {
    private static final Log LOG = LogFactory.getLog(AbstractHl7v2WebService.class);

    private final MllpTransactionConfiguration config;

    public AbstractHl7v2WebService(MllpTransactionConfiguration config) {
        super();
        this.config = config;
    }

    protected String doProcess(String request) {
        // preprocess
        MessageAdapter msgAdapter;
        try {
            msgAdapter = MessageAdapters.make(config.getParser(), request.trim());
            checkRequestAcceptance(msgAdapter, config);
        } catch (Exception e) {
            LOG.error(formatErrMsg("Request rejected, returning NAK"));
            return defaultNakString(e);
        }
        // process
        MessageAdapter originalRequest = msgAdapter.copy();
        Exchange exchange = super.process(msgAdapter);

        if (exchange.getException() != null) {
            LOG.error(formatErrMsg("Exchange failed, returning NAK"));
            return nakString(originalRequest, exchange.getException());
        }
        // postprocess
        try {
            msgAdapter = extractMessageAdapter(
                    resultMessage(exchange),
                    exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                    config.getParser());
            checkResponseAcceptance(msgAdapter, config);
            return render(msgAdapter);
        } catch (Exception noMessageAdapterInOutBody) {
            LOG.error(formatErrMsg("Response not accepted, returning NAK"), noMessageAdapterInOutBody);
            return defaultNakString(noMessageAdapterInOutBody);
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
    protected String nakString(MessageAdapter msgAdapter, Throwable cause) {
        MessageAdapter nak = createNak(cause,
                (Message) msgAdapter.getTarget(), config);
        return render(nak);
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
        return render(nak);
    }
    
    /**
     * Creates a message from the input <code>text</code >with prefix the sending
     * application in the transaction configuration.
     * 
     * @param text
     *            input text used for logging
     * @return a String with prefix the sending application
     */
    protected String formatErrMsg(String text) {
        return config.getSendingApplication() + ": " + text;
    }
    
    /**
     * Converts the given HAPI Message to a String suitable for WS transport.
     * 
     * @param message
     *            a {@link Message} to convert.
     * @param parser
     *            a {@link Parser} to convert.
     * @return a String representation of the given HAPI message.
     */
    protected String render(Message message) {
        try {
            return message.getParser().encode(message).replaceAll("\r", "\r\n");
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected String render(MessageAdapter msgAdapter) {
        return render((Message) msgAdapter.getTarget());
    }
}
