/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggerBase;
import org.openehealth.ipf.commons.ihe.core.payload.PayloadLoggingSpelContext;

/**
 * Base class for MLLP interceptors which store incoming and outgoing payload
 * into files with user-defined name patterns.
 *
 * @author Dmytro Rud
 */
public class MllpPayloadLoggerBase extends PayloadLoggerBase<PayloadLoggingSpelContext> {

    public void logPayload(Exchange exchange, boolean useOutMessage) throws Exception {
        Long sequenceId = exchange.getProperty(SEQUENCE_ID_PROPERTY_NAME, Long.class);
        if (sequenceId == null) {
            sequenceId = getNextSequenceId();
            exchange.setProperty(SEQUENCE_ID_PROPERTY_NAME, sequenceId);
        }

        PayloadLoggingSpelContext spelContext = new PayloadLoggingSpelContext(sequenceId);

        Message message = useOutMessage ? exchange.getOut() : exchange.getIn();
        doLogPayload(
                spelContext,
                exchange.getProperty(Exchange.CHARSET_NAME, String.class),
                message.getBody(String.class));
    }

}
