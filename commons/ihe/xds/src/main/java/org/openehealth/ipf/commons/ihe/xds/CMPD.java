/*
 * Copyright 2020 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.pharm1.Pharm1AuditStrategy;
import org.openehealth.ipf.commons.ihe.xds.pharm1.Pharm1PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * Definitions for the Community Medication Prescription and Dispense (CMPD) integration profile of the PHARM TF.
 *
 * @author Quentin Ligier
 * @since 3.7
 */
public class CMPD implements XdsIntegrationProfile {

    private static final CMPD Instance = new CMPD();

    @AllArgsConstructor
    public enum Interactions implements XdsInteractionId {
        PHARM_1(ITI_PHARM_1_WS_CONFIG);

        @Getter private final WsTransactionConfiguration<? extends XdsAuditDataset> wsTransactionConfiguration;

        @Override
        public XdsIntegrationProfile getInteractionProfile() {
            return Instance;
        }
    }

    @Override
    public boolean isEbXml30Based() {
        return true;
    }

    @Override
    public HomeCommunityIdOptionality getHomeCommunityIdOptionality() {
        return HomeCommunityIdOptionality.NEVER;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<XdsQueryAuditDataset> ITI_PHARM_1_WS_CONFIG = new WsTransactionConfiguration<>(
            "cmpd-pharm1",
            "Query Pharmacy Documents",
            true,
            new Pharm1AuditStrategy(false),
            new Pharm1AuditStrategy(true),
            new QName("urn:ihe:iti:xds-b:2007", "CommunityPharmacyManager_Service", "ihe"),
            Pharm1PortType.class,
            new QName("urn:ihe:iti:xds-b:2007", "CommunityPharmacyManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/pharm1.wsdl",
            true,
            false,
            true,
            false);

}
