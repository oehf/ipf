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
import static org.openehealth.ipf.platform.camel.ihe.hl7v2ws.util.MessageRenderer.render;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils.checkRequestAcceptance;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.AcceptanceCheckUtils.checkResponseAcceptance;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.createNak;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.extractMessageAdapter;

import org.apache.camel.Exchange;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

import ca.uhn.hl7v2.model.Message;

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
        Validate.notEmpty(request, "Empty request!");
        // preprocess
        MessageAdapter msgAdapter;
        try {
            msgAdapter = MessageAdapters.make(config.getParser(),
                    request.trim());
            checkRequestAcceptance(msgAdapter, config);
        } catch (Exception e) {
            return populatePreProcessingNak(e);
        }
        // process
        MessageAdapter originalRequest = msgAdapter.copy();
        Exchange exchange = super.process(msgAdapter);

        if (exchange.getException() != null) {
            return populateProcessingNak(originalRequest,
                    exchange.getException());
        }
        // postprocess
        try {
            msgAdapter = extractMessageAdapter(
                    resultMessage(exchange),
                    exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                    config.getParser());
            checkResponseAcceptance(msgAdapter, config);
            return render(msgAdapter);
        } catch (Exception e) {
            return populatePostProcessingNak(originalRequest, e);
        }
    }


    /**
     * 
     * @param cause
     * @return Encoded HAPI Message AR negative acknowledgment
     */
    protected String populatePreProcessingNak(Throwable cause) {
        LOG.error(formatErrMsg("Request rejected returns NAK"));
        return defaultNakString(cause);
    }

    /**
     * 
     * @param msgAdapter
     * @param cause
     * @return Encoded HAPI Message AE negative acknowledgment
     */
    protected String populateProcessingNak(MessageAdapter msgAdapter,
            Throwable cause) {
        LOG.error(formatErrMsg("Exchange failed returns NAK"));
        return nakString(msgAdapter, cause);
    }

    /**
     * 
     * @return Encoded HAPI Message AE negative acknowledgment
     */
    protected String populatePostProcessingNak(MessageAdapter msgAdapter,
            Throwable cause) {
        LOG.error(formatErrMsg("Response not accepted returns NAK"), cause);
        return defaultNakString(cause);
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
        return render(nak, config.getParser());
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
}
