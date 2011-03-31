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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.util;

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

/**
 * Helper to convert HAPI message content to SOAP payload
 * 
 * @author Stefan Ivanov
 * @author Mitko Kolev
 * 
 */
public class MessageRenderer {

    /**
     * Converts the given HAPI Message to a String suitable for WS transport.
     * 
     * @param message
     *            a {@link Message} to convert.
     * @param parser
     *            a {@link Parser} to convert.
     * @return a String representation of the given HAPI message.
     */
    public static String render(Message message, Parser parser) {
        try {
            return parser.encode(message).replaceAll("\r", "\r\n");
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 
     * @param msgAdapter
     *            a {@link MessageAdapter} to convert.
     * @return a String representation of the given HAPI message.
     */
    public static String render(MessageAdapter msgAdapter) {
        return render((Message) msgAdapter.getTarget(), msgAdapter.getParser());
    }

}
