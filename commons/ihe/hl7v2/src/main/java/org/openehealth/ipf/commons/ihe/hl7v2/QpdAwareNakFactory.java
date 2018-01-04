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

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * NAK factory for PDQ, PDVQ and PIX Query.
 * 
 * @author Dmytro Rud
 */
public class QpdAwareNakFactory extends NakFactory<QueryAuditDataset> {

    private static final Logger LOG = LoggerFactory.getLogger(QpdAwareNakFactory.class);
    private final String messageType, triggerEvent;


    public QpdAwareNakFactory(Hl7v2TransactionConfiguration<QueryAuditDataset> config, String messageType, String triggerEvent) {
        super(config);

        Validate.notEmpty(messageType);
        Validate.notEmpty(triggerEvent);

        this.messageType = messageType;
        this.triggerEvent = triggerEvent;
    }


    @Override
    public Message createNak(Message originalMessage, HL7Exception t, AcknowledgmentCode ackTypeCode) {
        try {
            return (t instanceof Hl7v2AcceptanceException)
                ? super.createNak(originalMessage, t, ackTypeCode)
                : createNak0(originalMessage, t, ackTypeCode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Message createNak0(Message originalMessage, HL7Exception e, AcknowledgmentCode ackTypeCode)
        throws HL7Exception {
        AbstractMessage ack = (AbstractMessage) MessageUtils.response(
                originalMessage,
                messageType,
                triggerEvent);

        LOG.info("Creating NAK response event of type {}", ack.getClass().getName());

        e.populateResponse(ack, ackTypeCode, 0);

        Segment msa = (Segment) ack.get("MSA");
        Terser.set(msa, 1, 0, 1, 1, ackTypeCode.name());

        Segment ackQak = (Segment) ack.get("QAK");
        Segment origQpd = (Segment) originalMessage.get("QPD");
        if (origQpd != null) {
            String queryTag = Terser.get(origQpd, 2, 0, 1, 1);
            Terser.set(ackQak, 1, 0, 1, 1, queryTag);
            LOG.debug("Set QAK-1 to {}", queryTag);
        }
        Terser.set(ackQak, 2, 0, 1, 1, "AE");

        // create a dummy QPD segment, it will be replaced with proper contents by
        // org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerSegmentEchoingInterceptor
        Segment ackQpd = (Segment) ack.get("QPD");
        Terser.set(ackQpd, 1, 0, 1, 1, "dummy");

        return ack;
    }


    @Override
    public Message createAck(Message originalMessage) {
        throw new IllegalStateException("This transaction cannot return a simple ACK");
    }
}
