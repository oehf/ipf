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
package org.openehealth.ipf.commons.ihe.hl7v3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.IntegrationProfile;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.Row;
import org.openehealth.ipf.commons.ihe.hl7v3.iti44.Iti44AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti44.Iti44PixPortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti44.Iti44XdsPortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti45.Iti45AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti45.Iti45PortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti46.Iti46AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti46.Iti46PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.DEFAULT_XSD;
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.GAZELLE_PIXPDQV3_SCHEMATRON;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PIXV3 implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v3InteractionId {
        ITI_44_PIX(ITI_44_PIX_WS_CONFIG),
        ITI_44_XDS(ITI_44_XDS_WS_CONFIG),
        ITI_45(ITI_45_WS_CONFIG),
        ITI_46(ITI_46_WS_CONFIG);

        @Getter private Hl7v3WsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final String NS_URI_PIX = "urn:ihe:iti:pixv3:2007";
    private static final String NS_URI_XDS = "urn:ihe:iti:xds-b:2007";

    private static final Hl7v3ValidationProfile ITI_44_REQUEST_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201301UV02", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON),
            new Row("PRPA_IN201302UV02", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON),
            new Row("PRPA_IN201304UV02", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3ValidationProfile ITI_44_RESPONSE_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("MCCI_IN000002UV01", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3ValidationProfile ITI_45_REQUEST_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201309UV02", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3ValidationProfile ITI_45_RESPONSE_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201310UV02", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3ValidationProfile ITI_46_REQUEST_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201302UV02", DEFAULT_XSD, null)
    );

    private static final Hl7v3ValidationProfile ITI_46_RESPONSE_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("MCCI_IN000002UV01", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3WsTransactionConfiguration ITI_44_PIX_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "pixv3-iti44",
            "Patient Identity Feed HL7 V3",
            false,
            new Iti44AuditStrategy(false),
            new Iti44AuditStrategy(true),
            new QName(NS_URI_PIX, "PIXManager_Service", "ihe"),
            Iti44PixPortType.class,
            new QName(NS_URI_PIX, "PIXManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-pix-raw.wsdl",
            "MCCI_IN000002UV01",
            null,
            false,
            false,
            ITI_44_REQUEST_VALIDATION_PROFILE,
            ITI_44_RESPONSE_VALIDATION_PROFILE);

    private final static Hl7v3WsTransactionConfiguration ITI_44_XDS_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xds-iti44",
            ITI_44_PIX_WS_CONFIG.getDescription(),
            ITI_44_PIX_WS_CONFIG.isQuery(),
            ITI_44_PIX_WS_CONFIG.getClientAuditStrategy(),
            ITI_44_PIX_WS_CONFIG.getServerAuditStrategy(),
            new QName(NS_URI_XDS, "DocumentRegistry_Service", "ihe"),
            Iti44XdsPortType.class,
            new QName(NS_URI_XDS, "DocumentRegistry_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti44/iti44-xds-raw.wsdl",
            "MCCI_IN000002UV01",
            null,
            false,
            false,
            ITI_44_REQUEST_VALIDATION_PROFILE,
            ITI_44_RESPONSE_VALIDATION_PROFILE);

    private static final Hl7v3WsTransactionConfiguration ITI_45_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "pixv3-iti45",
            "PIXV3 Query",
            true,
            new Iti45AuditStrategy(false),
            new Iti45AuditStrategy(true),
            new QName(NS_URI_PIX, "PIXManager_Service", "ihe"),
            Iti45PortType.class,
            new QName(NS_URI_PIX, "PIXManager_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti45/iti45-raw.wsdl",
            "PRPA_IN201310UV02",
            "PRPA_TE201310UV02",
            false,
            false,
            ITI_45_REQUEST_VALIDATION_PROFILE,
            ITI_45_RESPONSE_VALIDATION_PROFILE);

    private final static Hl7v3WsTransactionConfiguration ITI_46_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "pixv3-iti46",
            "PIXV3 Update Notification",
            false,
            new Iti46AuditStrategy(false),
            new Iti46AuditStrategy(true),
            new QName(NS_URI_PIX, "PIXConsumer_Service", "ihe"),
            Iti46PortType.class,
            new QName(NS_URI_PIX, "PIXConsumer_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti46/iti46-raw.wsdl",
            "MCCI_IN000002UV01",
            null,
            false,
            false,
            ITI_46_REQUEST_VALIDATION_PROFILE,
            ITI_46_RESPONSE_VALIDATION_PROFILE);

}
