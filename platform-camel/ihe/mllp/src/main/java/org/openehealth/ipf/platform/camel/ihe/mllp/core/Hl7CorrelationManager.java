/*
 * Copyright 20021 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.component.netty.TimeoutCorrelationManagerSupport;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Correlation Manager used when duplexing message over a single Netty connection.
 * Requests and responses are correlated based on MSH-10 and MSA-2 message identifiers
 */
public class Hl7CorrelationManager extends TimeoutCorrelationManagerSupport {

    private static final Logger log = LoggerFactory.getLogger(Hl7CorrelationManager.class);

    private final PipeParser parser;

    public Hl7CorrelationManager(HapiContext hapiContext) {
        this.parser = hapiContext.getPipeParser();
    }

    @Override
    public String getRequestCorrelationId(Object request) {
        try {
            Message message = getMessage(request);
            var msgId = new Terser(message).get("/MSH-10");
            log.debug("Recorded request with msg id {}", msgId);
            return msgId;
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }

    @Override
    public String getResponseCorrelationId(Object response) {
        try {
            Message message = getMessage(response);
            var msgId = new Terser(message).get("/MSA-2");
            log.debug("Recorded response with msg id {}", msgId);
            return msgId;
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }

    private Message getMessage(Object request) throws HL7Exception {
        Message message;
        if (request instanceof Message) {
            message = (Message) request;
        } else if (request instanceof byte[]) {
            message = parser.parse(new String((byte[]) request));
        } else {
            message = parser.parse(request.toString());
        } return message;
    }
}
