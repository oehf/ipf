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

package org.openehealth.ipf.commons.ihe.xds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsSubmitAuditDataset;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XDM implements XdsInteractionProfile {

    private static final XDM Instance = new XDM();

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {

        ITI_41("xds-iti41",
                "Provide and Register Document Set-b",
                false,
                XDS_B.Interactions.ITI_41.getWsTransactionConfiguration()) {
            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getClientAuditStrategy() {
                return XDS_B.Interactions.ITI_41.getClientAuditStrategy();
            }

            @Override
            public AuditStrategy<XdsSubmitAuditDataset> getServerAuditStrategy() {
                return XDS_B.Interactions.ITI_41.getServerAuditStrategy();
            }
        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter private WsTransactionConfiguration wsTransactionConfiguration;

        @Override
        public XdsInteractionProfile getInteractionProfile() {
            return Instance;
        }

    }

    @Override
    public boolean isEbXml30Based() {
        return true;
    }

    @Override
    public boolean requiresHomeCommunityId() {
        return false;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }
}
