/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses;

import net.ihe.gazelle.hl7v3.datatypes.CS;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mcciin000002UV01.MCCIIN000002UV01Type;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01Acknowledgement;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01TargetMessage;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;

import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils.*;

/**
 * Transformer between objects {@link AcceptAcknowledgement} and {@link MCCIIN000002UV01Type}
 * (response of ITI-44 and ITI-46).
 *
 * @author Christian Ohr
 * @since 6.1
 */
public class AcceptAcknowledgementTransformer {

    /**
     * Transforms a simplified acknowledgement into the full MCCI model.
     *
     * @param simpleAcknowledgement The simplified acknowledgement to transform.
     * @return the MCCI model acknowledgement or {@code null} if the input was {@code null}.
     */
    public MCCIIN000002UV01Type toMcci(final AcceptAcknowledgement simpleAcknowledgement) {
        if (simpleAcknowledgement == null) {
            return null;
        }

        final var message = new MCCIIN000002UV01Type();
        message.setITSVersion("XML_1.0");
        message.setId(simpleAcknowledgement.getMessageId());
        message.setCreationTime(toTs(simpleAcknowledgement.getCreationTime()));
        message.setInteractionId(new II("2.16.840.1.113883.1.6", "MCCI_IN000002UV01"));
        message.setProcessingCode(new CS("P", null, null));
        message.setProcessingModeCode(new CS("T", null, null));
        message.setAcceptAckCode(new CS("NE", null, null));
        if (simpleAcknowledgement.getReceiver() != null) {
            message.setReceiver(List.of(toMcciMt000200Receiver(simpleAcknowledgement.getReceiver())));
        }
        message.setSender(toMcciMt000200Sender(simpleAcknowledgement.getSender()));

        final var acknowledgement = new MCCIMT000200UV01Acknowledgement();
        acknowledgement.setTypeCode(new CS(simpleAcknowledgement.getAcknowledgementTypeCode(), null, null));
        acknowledgement.setTargetMessage(new MCCIMT000200UV01TargetMessage());
        acknowledgement.getTargetMessage().setId(simpleAcknowledgement.getTargetMessageId());
        acknowledgement.getAcknowledgementDetail().addAll(simpleAcknowledgement.getAcknowledgementDetails());
        message.getAcknowledgement().add(acknowledgement);
        return message;
    }

    /**
     * Transforms a full MCCI acknowledgement into the simplified model.
     *
     * @param message The full MCCI acknowledgement to transform.
     * @return the simplified acknowledgement or {@code null} if the input was {@code null}.
     */
    public AcceptAcknowledgement fromMcci(final MCCIIN000002UV01Type message) {
        if (message == null) {
            return null;
        }

        final var simpleAcknowledgement = new AcceptAcknowledgement();
        simpleAcknowledgement.setMessageId(message.getId());
        simpleAcknowledgement.setCreationTime(fromTs(message.getCreationTime()));
        simpleAcknowledgement.setSender(fromMcciMt000200Sender(message.getSender()));
        if (!message.getReceiver().isEmpty()) {
            simpleAcknowledgement.setReceiver(fromMcciMt000200Receiver(message.getReceiver().get(0)));
        }
        if (!message.getAcknowledgement().isEmpty()) {
            final var acknowledgement = message.getAcknowledgement().get(0);
            simpleAcknowledgement.setAcknowledgementTypeCode(
                    acknowledgement.getTypeCode() != null ? acknowledgement.getTypeCode().getCode() : null);
            if (acknowledgement.getTargetMessage() != null) {
                simpleAcknowledgement.setTargetMessageId(acknowledgement.getTargetMessage().getId());
            }
            simpleAcknowledgement.getAcknowledgementDetails().addAll(acknowledgement.getAcknowledgementDetail());
        }
        return simpleAcknowledgement;
    }
}
