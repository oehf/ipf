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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.InteractionProfile;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti30.Iti30ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti31.Iti31AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti31.Iti31ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti31.Iti31ServerAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pam.PamTransactions;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PAM implements InteractionProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements Hl7v2InteractionId {

        ITI_30("pam-iti30",
                "Patient Identity Management",
                false,
                null,
                null) {
            @Override
            public AuditStrategy<Iti30AuditDataset> getClientAuditStrategy() {
                return Iti30ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<Iti30AuditDataset> getServerAuditStrategy() {
                return Iti30ServerAuditStrategy.getInstance();
            }

            @Override
            public void init(TransactionOptions... options) {
                init(PamTransactions.ITI30, options);
            }



        },
        ITI_31("pam-iti31",
                "Patient Encounter Management",
                false,
                null,
                null) {
            @Override
            public AuditStrategy<Iti31AuditDataset> getClientAuditStrategy() {
                return Iti31ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<Iti31AuditDataset> getServerAuditStrategy() {
                return Iti31ServerAuditStrategy.getInstance();
            }

            @Override
            public void init(TransactionOptions... options) {
                init(PamTransactions.ITI31, options);
            }

        };

        @Getter
        private String name;
        @Getter
        private String description;
        @Getter
        private boolean query;
        @Getter
        @Setter
        private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter
        @Setter
        private NakFactory nakFactory;

        protected void init(PamTransactions pamTransactions, TransactionOptions... options) {
            setHl7v2TransactionConfiguration(new Hl7v2TransactionConfiguration(
                    new Version[]{Version.V25},
                    "PIM adapter",
                    "IPF",
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    ErrorCode.APPLICATION_INTERNAL_ERROR,
                    new String[]{"ADT"},
                    TransactionOptionUtils.concatAllToString(options),
                    new String[]{"ACK"},
                    new String[]{"*"},
                    new boolean[]{true},
                    new boolean[]{false},
                    HapiContextFactory.createHapiContext(PamTransactions.ITI31)));
            setNakFactory(new NakFactory(getHl7v2TransactionConfiguration()));
        }

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
}
