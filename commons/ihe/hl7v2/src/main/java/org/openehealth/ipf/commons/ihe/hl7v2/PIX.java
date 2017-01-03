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
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti10.Iti10ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti10.Iti10ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9.Iti9ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9.Iti9ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;

import java.util.Arrays;
import java.util.List;


/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PIX implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId {
        ITI_8_PIX( ITI_8_CONFIGURATION,  ITI_8_NAK_FACTORY),
        ITI_8_XDS( ITI_8_CONFIGURATION,  ITI_8_NAK_FACTORY),
        ITI_9    ( ITI_9_CONFIGURATION,  ITI_9_NAK_FACTORY),
        ITI_10   (ITI_10_CONFIGURATION, ITI_10_NAK_FACTORY);

        @Getter private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter private NakFactory nakFactory;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    // Private static variables, simulating singletons

    private static final Hl7v2TransactionConfiguration ITI_8_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
                    "pix-iti8",
                    "Patient Identity Feed",
                    false,
                    new Iti8ClientAuditStrategy(),
                    new Iti8ServerAuditStrategy(),
                    Version.V231,
                    "PIX adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    "ADT", "A01 A04 A05 A08 A40",
                    "ACK", "*",
                    true,
                    false,
                    HapiContextFactory.createHapiContext(
                            CustomModelClassUtils.createFactory("pix", "2.3.1"),
                            PixPdqTransactions.ITI8));

    private static final NakFactory ITI_8_NAK_FACTORY = new NakFactory(ITI_8_CONFIGURATION);

    private static final Hl7v2TransactionConfiguration ITI_9_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
                    "pix-iti9",
                    "PIX Query",
                    true,
                    new Iti9ClientAuditStrategy(),
                    new Iti9ServerAuditStrategy(),
                    Version.V25,
                    "PIX adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    "QBP", "Q23",
                    "RSP", "K23",
                    true,
                    false,
                    HapiContextFactory.createHapiContext(
                            CustomModelClassUtils.createFactory("pix", "2.5"),
                            PixPdqTransactions.ITI9));

    private static final NakFactory ITI_9_NAK_FACTORY = new QpdAwareNakFactory(ITI_9_CONFIGURATION, "RSP", "K23");

    private static final Hl7v2TransactionConfiguration ITI_10_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
                    "pix-iti10",
                    "PIX Update Notification",
                    false,
                    new Iti10ClientAuditStrategy(),
                    new Iti10ServerAuditStrategy(),
                    Version.V25,
                    "PIX adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    "ADT", "A31",
                    "ACK", "*",
                    true,
                    false,
                    HapiContextFactory.createHapiContext(PixPdqTransactions.ITI10));

    private static final NakFactory ITI_10_NAK_FACTORY = new NakFactory(ITI_10_CONFIGURATION);
}
