/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.ech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ech.ech0213.Ech0213PortType;
import org.openehealth.ipf.commons.ihe.ech.ech0214.Ech0214PortType;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

public class ECH implements IntegrationProfile {

    private static final ECH Instance = new ECH();

    @AllArgsConstructor
    public enum Interactions implements WsInteractionId<WsTransactionConfiguration<WsAuditDataset>> {
        ECH_0213(ECH_0213_CONFIG),
        ECH_0214(ECH_0214_CONFIG);

        @Getter
        private final WsTransactionConfiguration<WsAuditDataset> wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private final static WsTransactionConfiguration<WsAuditDataset> ECH_0213_CONFIG = new WsTransactionConfiguration<>(
        "ech-ech0213",
        "EPR-SPID Management Service",
        false,
        null,
        null,
        new QName("http://www.zas.admin.ch/wupispid/ws/managementService/1", "SpidManagementService"),
        Ech0213PortType.class,
        new QName("http://www.zas.admin.ch/wupispid/ws/managementService/1", "SpidManagementServiceBindingV1"),
        false,
        "wsdl/ech0213.wsdl",
        true,
        false,
        false,
        false);

    private final static WsTransactionConfiguration<WsAuditDataset> ECH_0214_CONFIG = new WsTransactionConfiguration<>(
        "ech-ech0214",
        "EPR-SPID Query Service",
        false,
        null,
        null,
        new QName("http://www.zas.admin.ch/wupispid/ws/queryService/2", "SpidQueryService"),
        Ech0214PortType.class,
        new QName("http://www.zas.admin.ch/wupispid/ws/queryService/2", "SpidQueryServiceBindingV2"),
        false,
        "wsdl/ech0214.wsdl",
        true,
        false,
        false,
        false);

}
