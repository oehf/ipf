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
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.core.InteractionProfile;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.Row;
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47ClientAuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47PortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti47.Iti47ServerAuditStrategy;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class PDQV3 implements InteractionProfile {

    @SuppressWarnings("unchecked")
    @AllArgsConstructor
    public enum Interactions implements Hl7v3InteractionId {

        ITI_47("pdqv3-iti47",
                "Patient Demographics Query HL7 V3",
                true,
                WS_CONFIG) {
            @Override
            public AuditStrategy<Hl7v3AuditDataset> getClientAuditStrategy() {
                return Iti47ClientAuditStrategy.getInstance();
            }

            @Override
            public AuditStrategy<Hl7v3AuditDataset> getServerAuditStrategy() {
                return Iti47ServerAuditStrategy.getInstance();
            }

        };

        @Getter private String name;
        @Getter private String description;
        @Getter private boolean query;
        @Getter private Hl7v3ContinuationAwareWsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Hl7v3ValidationProfile iti47RequestValidationProfile = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201305UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
            new Row("QUQI_IN000003UV01", Row.DEFAULT_XSD, null),
            new Row("QUQI_IN000003UV01_Cancel", Row.DEFAULT_XSD, null)
    );

    private static final Hl7v3ValidationProfile iti47ResponseValidationProfile = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201306UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
            new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private final static String NS_URI = "urn:ihe:iti:pdqv3:2007";
    private final static Hl7v3ContinuationAwareWsTransactionConfiguration WS_CONFIG = new Hl7v3ContinuationAwareWsTransactionConfiguration(
            new QName(NS_URI, "PDSupplier_Service", "ihe"),
            Iti47PortType.class,
            new QName(NS_URI, "PDSupplier_Binding_Soap12", "ihe"),
            false,
            "wsdl/iti47/iti47-raw.wsdl",
            "PRPA_IN201306UV02",
            "PRPA_TE201306UV02",
            false,
            false,
            iti47RequestValidationProfile,
            iti47ResponseValidationProfile,
            "PRPA_IN201305UV02",
            "PRPA_IN201306UV02");



}
