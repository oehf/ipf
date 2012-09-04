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

import static org.openehealth.ipf.commons.ihe.hl7v2ws.Utils.render;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;
import static org.openehealth.ipf.platform.camel.ihe.hl7v2.AcceptanceCheckUtils.checkRequestAcceptance;
import static org.openehealth.ipf.platform.camel.ihe.hl7v2.AcceptanceCheckUtils.checkResponseAcceptance;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * Generic implementation of an HL7v2-based Web Service.
 * 
 * @author Dmytro Rud
 * @author Mitko Kolev
 * @author Stefan Ivanov
 */
public abstract class AbstractHl7v2WebService extends AbstractWebService {
    private static final Log LOG = LogFactory.getLog(AbstractHl7v2WebService.class);

    private Hl7v2TransactionConfiguration config = null;
    private NakFactory nakFactory = null;


    /**
     * Configures HL7v2-related parameters of this Web Service.
     * This method is supposed to be called only once, directly after
     * an instance of this class has been created.  In other words,
     * this is a kind of "post-constructor".
     *
     * @param configurationHolder
     *      source of HL7v2-related configuration parameters.
     */
    void setHl7v2Configuration(Hl7v2ConfigurationHolder configurationHolder) {
        if ((this.config != null) || (this.nakFactory != null)) {
            throw new IllegalStateException("multiple calls to this method are not allowed");
        }

        this.config = configurationHolder.getHl7v2TransactionConfiguration();
        this.nakFactory = configurationHolder.getNakFactory();
    }


    protected String doProcess(String request) {
        // parse request
        MessageAdapter<?> msg;
        try {
            msg = MessageAdapters.make(config.getParser(), normalize(request));
            checkRequestAcceptance(msg, config);
        } catch (Exception e) {
            LOG.error(formatErrMsg("Request not acceptable"), e);
            return render(nakFactory.createDefaultNak(e));
        }

        MessageAdapter<?> originalRequest = msg.copy();

        // play the route, handle its outcomes and check response acceptance
        try {
            Exchange exchange = super.process(msg);
            Exception exception = Exchanges.extractException(exchange);
            if (exception != null) {
                throw exception;
            }

            // check response existence and acceptance
            msg = Hl7v2MarshalUtils.extractMessageAdapter(
                    resultMessage(exchange),
                    exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                    config.getParser());
            checkResponseAcceptance(msg, config);
            return render(msg.getHapiMessage());

        } catch (Exception e) {
            LOG.error(formatErrMsg("Message processing failed, response missing or not acceptable"), e);
            return render(nakFactory.createNak(originalRequest.getHapiMessage(), e));
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
    protected String formatErrMsg(String text) {
        return config.getSendingApplication() + ": " + text;
    }
    
    /**
     * Replaces the LF with CRLF. This give flexibility for the clients, because they must not escape 
     * the CR characters. Clients can simply send LFs.<br>
     * Without the normalization if the client sends no CR characters, all segments after MSH are parsed as MSH fields, so MSH is 
     * the only non-empty segment. 
     *    
     * @param request
     * @return the normalized request.
     */
    private String normalize(String request){
        if (request == null){
            return "";
        } else {
            return request.trim().replaceAll("\n", "\r\n");
        }
    }
}
