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
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55PortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55AsyncResponsePortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse.Iti55DeferredResponsePortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56AuditStrategy;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56PortType;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.asyncresponse.Iti56AsyncResponsePortType;

import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.DEFAULT_XSD;
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.GAZELLE_PIXPDQV3_SCHEMATRON;

/**
 * @author Christian Ohr
 * @since 3.2
 */
public class XCPD implements IntegrationProfile {

    @AllArgsConstructor
    public enum Interactions implements Hl7v3InteractionId<Hl7v3WsTransactionConfiguration> {
        ITI_55                  (ITI_55_WS_CONFIG),
        ITI_55_ASYNC_RESPONSE   (ITI_55_ASYNC_RESPONSE_WS_CONFIG),
        ITI_55_DEFERRED_RESPONSE(ITI_55_DEFERRED_RESPONSE_WS_CONFIG),
        ITI_56                  (ITI56_WS_CONFIG),
        ITI_56_ASYNC_RESPONSE   (ITI_56_ASYNC_RESPONSE_WS_CONFIG);

        @Getter private final Hl7v3WsTransactionConfiguration wsTransactionConfiguration;

    }

    @Override
    public List<InteractionId> getInteractionIds() {
        return Arrays.asList(Interactions.values());
    }

    private static final Hl7v3ValidationProfile ITI_55_REQUEST_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201305UV02", DEFAULT_XSD, "/schematron/iti55/PRPA_IN201305UV02.sch.xml")
    );

    private static final Hl7v3ValidationProfile ITI_56_REQUEST_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PatientLocationQueryRequest", "/schema/IHE/XCPD_PLQ.xsd", null)
    );

    private static final Hl7v3ValidationProfile ITI_55_RESPONSE_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PRPA_IN201306UV02", DEFAULT_XSD, "/schematron/iti55/PRPA_IN201306UV02.sch.xml"),
            new Row("MCCI_IN000002UV01", DEFAULT_XSD, GAZELLE_PIXPDQV3_SCHEMATRON)
    );

    private static final Hl7v3ValidationProfile ITI_56_RESPONSE_VALIDATION_PROFILE = new Hl7v3ValidationProfile(
            new Row("PatientLocationQueryResponse", "/schema/IHE/XCPD_PLQ.xsd", null)
    );

    private final static String NS_URI = "urn:ihe:iti:xcpd:2009";
    private final static Hl7v3WsTransactionConfiguration ITI_55_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xcpd-iti55",
            "Cross Gateway Patient Discovery",
            true,
            new Iti55AuditStrategy(false),
            new Iti55AuditStrategy(true),
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti55PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti55/iti55-raw.wsdl",
            "PRPA_IN201306UV02",
            "PRPA_TE201306UV02",
            false,
            true,
            ITI_55_REQUEST_VALIDATION_PROFILE,
            ITI_55_RESPONSE_VALIDATION_PROFILE,
            false,
            false);

    private final static Hl7v3WsTransactionConfiguration ITI_55_ASYNC_RESPONSE_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xcpd-iti55-async-response",
            "Cross Gateway Patient Discovery",
            true,
            null,
            new Iti55AuditStrategy(false),      // really!
            new QName(NS_URI, "InitiatingGateway_Service", "xcpd"),
            Iti55AsyncResponsePortType.class,
            new QName(NS_URI, "InitiatingGateway_Binding", "xcpd"),
            false,
            "wsdl/iti55/iti55-asyncresponse-raw.wsdl",
            null,
            null,
            false,
            false,
            ITI_55_REQUEST_VALIDATION_PROFILE,
            ITI_55_RESPONSE_VALIDATION_PROFILE,
            false,
            false);

    private final static Hl7v3WsTransactionConfiguration ITI_55_DEFERRED_RESPONSE_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xcpd-iti55-deferred-response",
            "Cross Gateway Patient Discovery",
            true,
            null,
            new Iti55AuditStrategy(false),      // really!
            new QName(NS_URI, "InitiatingGateway_Service", "xcpd"),
            Iti55DeferredResponsePortType.class,
            new QName(NS_URI, "InitiatingGatewayDeferredResponse_Binding", "xcpd"),
            false,
            "wsdl/iti55/iti55-deferred-response-raw.wsdl",
            null,
            null,
            false,
            false,
            ITI_55_REQUEST_VALIDATION_PROFILE,
            ITI_55_RESPONSE_VALIDATION_PROFILE,
            false,
            false);

    private final static Hl7v3WsTransactionConfiguration ITI56_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xcpd-iti56",
            "Cross Gateway Patient Location Query",
            true,
            new Iti56AuditStrategy(false),
            new Iti56AuditStrategy(true),
            new QName(NS_URI, "RespondingGateway_Service", "xcpd"),
            Iti56PortType.class,
            new QName(NS_URI, "RespondingGateway_Binding_Soap12", "xcpd"),
            false,
            "wsdl/iti56/iti56-raw.wsdl",
            null,
            null,
            true,
            true,
            ITI_56_REQUEST_VALIDATION_PROFILE,
            ITI_56_RESPONSE_VALIDATION_PROFILE,
            false,
            false);

    private final static Hl7v3WsTransactionConfiguration ITI_56_ASYNC_RESPONSE_WS_CONFIG = new Hl7v3WsTransactionConfiguration(
            "xcpd-iti56-async-response",
            "Cross Gateway Patient Location Query",
            true,
            null,
            new Iti56AuditStrategy(false),      // really!
            new QName(NS_URI, "InitiatingGateway_Service", "xcpd"),
            Iti56AsyncResponsePortType.class,
            new QName(NS_URI, "InitiatingGateway_Binding", "xcpd"),
            false,
            "wsdl/iti56/iti56-asyncresponse-raw.wsdl",
            null,
            null,
            false,
            false,
            ITI_56_REQUEST_VALIDATION_PROFILE,
            ITI_56_RESPONSE_VALIDATION_PROFILE,
            false,
            false);

}
