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

/**
 * Correlation Manager used when duplexing message over a single connection
 */
public class Hl7CorrelationManager extends TimeoutCorrelationManagerSupport {

    private PipeParser parser;

    public Hl7CorrelationManager(HapiContext hapiContext) {
        this.parser = hapiContext.getPipeParser();
    }

    @Override
    public String getRequestCorrelationId(Object request) {
        try {
            var message = request instanceof Message ? (Message) request : parser.parse(request.toString());
            return new Terser(message).get("/MSH-10");
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }

    @Override
    public String getResponseCorrelationId(Object response) {
        try {
            var message = response instanceof Message ? (Message) response : parser.parse(response.toString());
            return new Terser(message).get("/MSA-2");
        } catch (HL7Exception e) {
            throw new HL7v2Exception(e);
        }
    }
}
