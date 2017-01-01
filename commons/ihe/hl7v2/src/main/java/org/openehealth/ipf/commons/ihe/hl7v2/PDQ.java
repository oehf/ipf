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

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti21.Iti21ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti21.Iti21ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti22.Iti22ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti22.Iti22ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PDQ implements IntegrationProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId {
        ITI_21(ITI_21_CONFIGURATION, ITI_21_NAK_FACTORY),
        ITI_22(ITI_22_CONFIGURATION, ITI_22_NAK_FACTORY);

        @Getter private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter private NakFactory nakFactory;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Hl7v2TransactionConfiguration ITI_21_CONFIGURATION =
            new PdqTransactionConfiguration(
                    "pdq-iti21",
                    "Patient Demographics Query",
                    true,
                    new Iti21ClientAuditStrategy(),
                    new Iti21ServerAuditStrategy(),
                    new Version[] {Version.V25},
                    "PDQ adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[]{"QBP", "QCN"},
                    new String[]{"Q22", "J01"},
                    new String[]{"RSP", "ACK"},
                    new String[]{"K22", "*"},
                    new boolean[]{true, false},
                    new boolean[]{true, false},
                    HapiContextFactory.createHapiContext(
                            CustomModelClassUtils.createFactory("pdq", "2.5"),
                            PixPdqTransactions.ITI21));

    private static final NakFactory ITI_21_NAK_FACTORY =
            new QpdAwareNakFactory(ITI_21_CONFIGURATION, "RSP", "K22");

    private static final Hl7v2TransactionConfiguration ITI_22_CONFIGURATION =
            new PdqTransactionConfiguration(
                    "pdq-iti22",
                    "Patient Demographics And Visit Query",
                    true,
                    new Iti22ClientAuditStrategy(),
                    new Iti22ServerAuditStrategy(),
                    new Version[] {Version.V25},
                    "PDQ adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[] {"QBP", "QCN"},
                    new String[] {"ZV1", "J01"},
                    new String[] {"RSP", "ACK"},
                    new String[] {"ZV2", "*"},
                    new boolean[] {true, false},
                    new boolean[] {true, false},
                    HapiContextFactory.createHapiContext(
                            CustomModelClassUtils.createFactory("pdq", "2.5"),
                            PixPdqTransactions.ITI22));

    private static final NakFactory ITI_22_NAK_FACTORY =
            new QpdAwareNakFactory(ITI_22_CONFIGURATION, "RSP", "ZV2");
}
