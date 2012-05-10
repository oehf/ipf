/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.ws;

import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.handler.MessageContext;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;

/**
 * Base class for receivers of asynchronous responses for Web Service-based IHE transactions.
 * @author Dmytro Rud
 */
abstract public class AbstractAsyncResponseWebService extends AbstractWebService {
    private static final transient Log LOG = LogFactory.getLog(AbstractAsyncResponseWebService.class);

    /**
     * Determines whether correlation items related to the given message can be dropped.
     * <p>
     * Per default, always returns <code>true</code>.
     * @param response
     *      response message.
     * @return
     *      <code>true</code> when correlation items related
     *      to the given message can be dropped.
     */
    protected boolean canDropCorrelation(Object response) {
        return true;
    }


    /**
     * Before calling the base method, determines correlation key  
     * and stores it into message headers. 
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Exchange process(
            Object body, 
            Map<String, Object> headers,
            ExchangePattern exchangePattern) 
    {
        final AsynchronyCorrelator correlator = ((AbstractWsEndpoint) getConsumer().getEndpoint()).getCorrelator();

        MessageContext messageContext = new WebServiceContextImpl().getMessageContext();
        AddressingPropertiesImpl apropos = (AddressingPropertiesImpl) messageContext.get(JAXWSAConstants.SERVER_ADDRESSING_PROPERTIES_INBOUND);
        String messageId = ((apropos != null) && (apropos.getRelatesTo() != null))
                ? apropos.getRelatesTo().getValue()
                : null;

        // when no ReplyTo header found -- try alternative keys
        if (messageId == null) {
            String[] alternativeKeys = getAlternativeResponseKeys(body);
            if (alternativeKeys != null) {
                for (String key : alternativeKeys) {
                    messageId = correlator.getMessageId(key);
                    if (messageId != null) {
                        break;
                    }
                }
            }
        }

        if (messageId != null) {
            // expose user-defined correlation key as message header
            String correlationKey = correlator.getCorrelationKey(messageId);
            if (correlationKey != null) {
                if (headers == null) {
                    // NB: it shouldn't be a non-modifiable singleton map...
                    headers = new HashMap<String, Object>();
                }
                headers.put(AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME, correlationKey);
            }

            // drop correlation data when appropriate
            if (canDropCorrelation(body)) {
                correlator.delete(messageId);
            }
        } else {
            LOG.error("Cannot retrieve WSA RelatesTo header, message correlation not possible");
        }

        return super.process(body, headers, exchangePattern);
    }


    /**
     * Determines the set of correlation keys for the given response message,
     * which are alternative to the WS-Addressing message ID referenced in the
     * &lt;ReplyTo&gt; header.
     * An example of alternative key is the query ID in HL7v3-based transactions.
     * <p>
     * Per default, this method returns <code>null</code>.
     *
     * @param response
     *      response message.
     * @return
     *      A non-empty collection of non-<code>null</code> alternative keys,
     *      or <code>null</code>, when no keys could have been extracted.
     */
    protected String[] getAlternativeResponseKeys(Object response) {
        return null;
    }
}

