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
import org.openehealth.ipf.commons.ihe.hl7v3.pcc1.Pcc1AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.pcc1.Pcc1PortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class QED implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v3InteractionId {
        PCC_1(PCC1_WS_CONFIG);

        @Getter private Hl7v3ContinuationAwareWsTransactionConfiguration wsTransactionConfiguration;
    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }


    private static final Hl7v3ValidationProfile pcc1RequestValidationProfile = new Hl7v3ValidationProfile(
            new Row("QUPC_IN043100UV01",        Row.DEFAULT_XSD, null),
            new Row("QUQI_IN000003UV01",        Row.DEFAULT_XSD, null),
            new Row("QUQI_IN000003UV01_Cancel", Row.DEFAULT_XSD, null)
    );
    private static final Hl7v3ValidationProfile pcc1ResponseValidationProfile = new Hl7v3ValidationProfile(
            new Row("QUPC_IN043200UV01", Row.DEFAULT_XSD, null),
            new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, null)
    );

    private final static String NS_URI = "urn:ihe:pcc:qed:2007";
    private final static Hl7v3ContinuationAwareWsTransactionConfiguration PCC1_WS_CONFIG = new Hl7v3ContinuationAwareWsTransactionConfiguration(
            "qed-pcc1",
            "Query For Existing Data",
            true,
            new Pcc1AuditStrategy(false),
            new Pcc1AuditStrategy(true),
            new QName(NS_URI, "ClinicalDataSource_Service", "qed"),
            Pcc1PortType.class,
            new QName(NS_URI, "ClinicalDataSource_Binding_Soap12", "qed"),
            false,
            "wsdl/pcc1/pcc1-raw.wsdl",
            "QUPC_IN043200UV01",
            "QUPC_IN043200UV01",
            false,
            false,
            pcc1RequestValidationProfile,
            pcc1ResponseValidationProfile,
            "QUPC_IN043100UV01",
            "QUPC_IN043200UV01");
}
