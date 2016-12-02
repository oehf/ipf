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
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.message.ADT_A05;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.InteractionProfile;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti10.Iti10ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9.Iti9ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti9.Iti9ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message.QBP_Q21;
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions;

import java.util.Arrays;
import java.util.List;


/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PIX implements InteractionProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId {

        ITI_8_PIX("pix-iti8",
                "Patient Identity Feed",
                false,
                ITI8_CONFIGURATION,
                ITI8_NAK_FACTORY) {
            @Override
            public AuditStrategy<Iti8AuditDataset> getClientAuditStrategy() {
                return Iti8ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<Iti8AuditDataset> getServerAuditStrategy() {
                return Iti8ServerAuditStrategy.getInstance();
            }

            public Message request(String trigger) {
                Hl7v2TransactionConfiguration config = getHl7v2TransactionConfiguration();
                return request(
                        config.getAllowedRequestMessageTypes()[0],
                        trigger);
            }

        },
        ITI_8_XDS("xds-iti8",
                "Patient Identity Feed",
                false,
                ITI_8_PIX.hl7v2TransactionConfiguration,
                ITI_8_PIX.nakFactory) {
            @Override
            public AuditStrategy<Iti8AuditDataset> getClientAuditStrategy() {
                return ITI_8_PIX.getClientAuditStrategy();
            }

            @Override
            public AuditStrategy<Iti8AuditDataset> getServerAuditStrategy() {
                return ITI_8_PIX.getServerAuditStrategy();
            }


        },
        ITI_9("pix-iti9",
                "PIX Query",
                true,
                ITI9_CONFIGURATION,
                ITI9_NAK_FACTORY) {
            @Override
            public AuditStrategy<QueryAuditDataset> getClientAuditStrategy() {
                return Iti9ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<QueryAuditDataset> getServerAuditStrategy() {
                return Iti9ServerAuditStrategy.getInstance();
            }

        },
        ITI_10("pix-iti10",
                "PIX Update Notification",
                false,
                ITI10_CONFIGURATION,
                ITI10_NAK_FACTORY) {
            @Override
            public AuditStrategy<QueryAuditDataset> getClientAuditStrategy() {
                return Iti10ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<QueryAuditDataset> getServerAuditStrategy() {
                return Iti10ClientAuditStrategy.getInstance();
            }

        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter private NakFactory nakFactory;

        public Message request(String trigger) {
            Hl7v2TransactionConfiguration config = getHl7v2TransactionConfiguration();
            return request(
                    config.getAllowedRequestMessageTypes()[0],
                    trigger);
        }

    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    // Private static variables, simulating singletons

    private static final Hl7v2TransactionConfiguration ITI8_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
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

    private static final NakFactory ITI8_NAK_FACTORY = new NakFactory(ITI8_CONFIGURATION);

    private static final Hl7v2TransactionConfiguration ITI9_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
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

    private static final NakFactory ITI9_NAK_FACTORY = new QpdAwareNakFactory(ITI9_CONFIGURATION, "RSP", "K23");

    private static final Hl7v2TransactionConfiguration ITI10_CONFIGURATION =
            new Hl7v2TransactionConfiguration(
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

    private static final NakFactory ITI10_NAK_FACTORY = new NakFactory(ITI10_CONFIGURATION);
}
