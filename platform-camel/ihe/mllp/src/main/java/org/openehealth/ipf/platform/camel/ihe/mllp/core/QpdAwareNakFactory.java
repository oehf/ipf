/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2AcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;

/**
 * NAK factory for PDQ, PDVQ and PIX Query.
 * @author Dmytro Rud
 */
public class QpdAwareNakFactory extends NakFactory {
    private final String messageType, triggerEvent;


    public QpdAwareNakFactory(Hl7v2TransactionConfiguration config, String messageType, String triggerEvent) {
        super(config);

        Validate.notEmpty(messageType);
        Validate.notEmpty(triggerEvent);

        this.messageType = messageType;
        this.triggerEvent = triggerEvent;
    }


    @Override
    public Message createNak(Message originalMessage, Throwable t, AckTypeCode ackTypeCode) {
        try {
            return (t instanceof Hl7v2AcceptanceException)
                ? super.createNak(originalMessage, t, ackTypeCode)
                : createNak0(originalMessage, t, ackTypeCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public Message createNak0(Message originalMessage, Throwable t, AckTypeCode ackTypeCode)
        throws HL7Exception, IllegalAccessException, NoSuchFieldException
    {
        AbstractMessage ack = (AbstractMessage) MessageUtils.response(
                getConfig().getParser().getFactory(),
                originalMessage,
                messageType,
                triggerEvent);

        getHl7Exception(t).populateMessage(ack, ackTypeCode);

        Segment ackQak = (Segment) ack.get("QAK");
        Terser.set(ackQak, 2, 0, 1, 1, "AE");

        // create a dummy QPD segment, it will be replaced with proper contents by
        // org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerSegmentEchoingInterceptor
        Segment ackQpd = (Segment) ack.get("QPD");
        Terser.set(ackQpd, 1, 0, 1, 1, "dummy");

        return ack;
    }


    @Override
    public Message createAck(Message originalMessage, AckTypeCode ackTypeCode) {
        throw new IllegalStateException("This transaction cannot return a simple ACK");
    }
}
