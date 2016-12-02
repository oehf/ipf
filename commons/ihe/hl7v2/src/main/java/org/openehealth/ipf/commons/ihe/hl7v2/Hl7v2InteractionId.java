/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2;

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;

/**
 * HL7v2 Transaction Definition
 *
 * @author Christian Ohr
 * @since 3.2
 */
public interface Hl7v2InteractionId extends InteractionId {

    Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration();

    NakFactory getNakFactory();

    /**
     * Makes a valid request for this transaction. Note that the individual transaction types
     * may overload this method, e.g. using a concrete response type.
     *
     * @param messageType message type, e.g. ADT
     * @param trigger trigger event, e.g. A01
     * @return HAPI message created using the correct HapiContext
     * @throws HL7v2Exception if the message type or trigger event is not valid for this transaction
     */
    default Message request(String messageType, String trigger) {
        Message message = MessageUtils.makeMessage(
                getHl7v2TransactionConfiguration().getHapiContext(),
                messageType, trigger, getHl7v2TransactionConfiguration().getHl7Versions()[0].getVersion());
        try {
            getHl7v2TransactionConfiguration().checkRequestAcceptance(message);
            return message;
        } catch (Hl7v2AcceptanceException e) {
            throw new HL7v2Exception(e);
        }
    }

    /**
     * Optional initialization with dynamic TransactionOptions
     * @param options transaction options
     */
    default void init(TransactionOptions... options) {}
}
