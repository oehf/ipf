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
package org.openehealth.ipf.commons.ihe.hl7v2ws;

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.commons.ihe.hl7v2.NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01PortType;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.gazelle.validation.profile.pcd.PcdTransactions;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PCD implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v2WsInteractionId {
        PCD_01(PCD_01_HL7V2_CONFIG, PCD_01_NAK_FACTORY, PCD_01_WS_CONFIG);

        @Getter private Hl7v2TransactionConfiguration hl7v2TransactionConfiguration;
        @Getter private NakFactory nakFactory;
        @Getter private WsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final String NS_URI = "urn:ihe:pcd:dec:2010";
    private static final WsTransactionConfiguration PCD_01_WS_CONFIG = new WsTransactionConfiguration(
            "pcd-pcd01",
            "Communicate PCD Data",
            false,
            null,
            null,
            new QName(NS_URI, "DeviceObservationConsumer_Service", "ihe"),
            Pcd01PortType.class,
            new QName(NS_URI, "DeviceObservationConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/pcd01/pcd01.wsdl",
            true,
            false,
            false,
            false);

    private static final Hl7v2TransactionConfiguration PCD_01_HL7V2_CONFIG = new Hl7v2TransactionConfiguration(
            "pcd-pcd01",
            "Communicate PCD Data",
            false,
            null,
            null,
            new Version[] {Version.V26},
            "PCD01",
            "IPF",
            ErrorCode.APPLICATION_INTERNAL_ERROR,
            ErrorCode.APPLICATION_INTERNAL_ERROR,
            new String[] {"ORU"},
            new String[] {"R01"},
            new String[] {"ACK"},
            new String[] {"*"},
            null,
            null,
            HapiContextFactory.createHapiContext(PcdTransactions.PCD1));

    private static final NakFactory PCD_01_NAK_FACTORY = new NakFactory(PCD_01_HL7V2_CONFIG, false, "ACK^R01^ACK");
}
