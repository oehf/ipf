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
package org.openehealth.ipf.commons.ihe.hl7v3.core.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01AcknowledgementDetail;
import org.openehealth.ipf.commons.ihe.hl7v3.core.Hl7v3Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simplified model of an accept acknowledgement (MCCI_IN000002UV01), the response of the
 * Patient Identity Feed HL7v3 (ITI-44) and PIX Update Notification HL7v3 (ITI-46) transactions.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AcceptAcknowledgement extends Hl7v3Message implements Hl7v3Response {

    private String acknowledgementTypeCode;

    private II targetMessageId;

    /**
     * The acknowledgement details. Shall be provided in case of an error. The list is never {@code null}.
     */
    private final List<MCCIMT000200UV01AcknowledgementDetail> acknowledgementDetails = new ArrayList<>();

    /**
     * Empty constructor. By default, it sets the acknowledgement type code to "Application Error".
     */
    public AcceptAcknowledgement() {
        super();
        setApplicationError();
    }

    /**
     * Creates and prepopulates an {@link AcceptAcknowledgement} for the given request. The receiver and sender are
     * inverted.
     *
     * @param request The acknowledged request.
     * @return the created acknowledgement.
     */
    public static AcceptAcknowledgement fromRequest(final Hl7v3Message request) {
        Objects.requireNonNull(request);
        final var acknowledgement = new AcceptAcknowledgement();
        acknowledgement.setSender(request.getReceiver());
        acknowledgement.setReceiver(request.getSender());
        acknowledgement.setTargetMessageId(request.getMessageId());
        return acknowledgement;
    }
}
