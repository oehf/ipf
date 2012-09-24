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
package org.openehealth.ipf.commons.ihe.hl7v2ws;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

/**
 * @author Dmytro Rud
 */
abstract public class Utils {

    /**
     * Converts the given HAPI Message to a String suitable for WS transport.
     * @param message
     *      message to convert.
     * @return String representation of the given HAPI message.
     */
    public static String render(Message message) {
        try {
            return message.encode().replaceAll("\r", "\r\n");
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }

}
