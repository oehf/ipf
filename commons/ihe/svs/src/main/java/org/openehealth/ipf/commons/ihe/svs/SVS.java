/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48AuditStrategy;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48PortType;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the Sharing Value Sets (SVS) integration profile of the IHE TF.
 *
 * @author Quentin Ligier
 * @since 5.0
 */
public class SVS implements IntegrationProfile {

    private static final SVS Instance = new SVS();

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<SvsAuditDataset>> {
        ITI_48(ITI_48_WS_CONFIG);

        @Getter
        private final WsTransactionConfiguration<SvsAuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final WsTransactionConfiguration<SvsAuditDataset> ITI_48_WS_CONFIG = new WsTransactionConfiguration<>(
        "svs-iti48",
        "Retrieve Value Set",
        true,
        new Iti48AuditStrategy(false),
        new Iti48AuditStrategy(true),
        new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Service", "ihe"),
        Iti48PortType.class,
        new QName("urn:ihe:iti:svs:2008", "ValueSetRepository_Binding_Soap12", "ihe"),
        false,
        "wsdl/iti48/iti48.wsdl",
        true,
        false,
        false,
        false
    );
}