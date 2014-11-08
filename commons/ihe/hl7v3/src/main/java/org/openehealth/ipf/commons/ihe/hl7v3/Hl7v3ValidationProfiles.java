/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfile.Row;

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Validation profiles for HL7v3-based transactions supported by IPF.
 * @author Dmytro Rud
 */
public class Hl7v3ValidationProfiles {

    private static final Map<InteractionId, CombinedXmlValidationProfile> REQUEST_VALIDATION_PROFILES =
            new HashMap<InteractionId, CombinedXmlValidationProfile>();

    private static final Map<InteractionId, CombinedXmlValidationProfile> RESPONSE_VALIDATION_PROFILES =
            new HashMap<InteractionId, CombinedXmlValidationProfile>();

    public static CombinedXmlValidationProfile getRequestValidationProfile(InteractionId id) {
        return REQUEST_VALIDATION_PROFILES.get(id);
    }

    public static CombinedXmlValidationProfile getResponseValidationProfile(InteractionId id) {
        return RESPONSE_VALIDATION_PROFILES.get(id);
    }


    static {
        /********** REQUESTS **********/

        Hl7v3ValidationProfile iti44RequestValidationProfile = new Hl7v3ValidationProfile(
                new Row("PRPA_IN201301UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
                new Row("PRPA_IN201302UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
                new Row("PRPA_IN201304UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        );
        REQUEST_VALIDATION_PROFILES.put(ITI_44_PIX, iti44RequestValidationProfile);
        REQUEST_VALIDATION_PROFILES.put(ITI_44_XDS, iti44RequestValidationProfile);

        REQUEST_VALIDATION_PROFILES.put(ITI_45, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201309UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        ));

        REQUEST_VALIDATION_PROFILES.put(ITI_46, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201302UV02", Row.DEFAULT_XSD, null)
        ));

        REQUEST_VALIDATION_PROFILES.put(ITI_47, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201305UV02",        Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
                new Row("QUQI_IN000003UV01",        Row.DEFAULT_XSD, null),
                new Row("QUQI_IN000003UV01_Cancel", Row.DEFAULT_XSD, null)
        ));

        REQUEST_VALIDATION_PROFILES.put(ITI_55, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201305UV02", Row.DEFAULT_XSD, "/schematron/iti55/PRPA_IN201305UV02.sch.xml")
        ));

        REQUEST_VALIDATION_PROFILES.put(ITI_56, new Hl7v3ValidationProfile(
                new Row("PatientLocationQueryRequest", "/schema/IHE/XCPD_PLQ.xsd", null)
        ));

        REQUEST_VALIDATION_PROFILES.put(PCC_1, new Hl7v3ValidationProfile(
                new Row("QUPC_IN043100UV01",        Row.DEFAULT_XSD, null),
                new Row("QUQI_IN000003UV01",        Row.DEFAULT_XSD, null),
                new Row("QUQI_IN000003UV01_Cancel", Row.DEFAULT_XSD, null)
        ));


        /********** RESPONSES**********/

        Hl7v3ValidationProfile iti44ResponseValidationProfile = new Hl7v3ValidationProfile(
                new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        );
        RESPONSE_VALIDATION_PROFILES.put(ITI_44_PIX, iti44ResponseValidationProfile);
        RESPONSE_VALIDATION_PROFILES.put(ITI_44_XDS, iti44ResponseValidationProfile);

        RESPONSE_VALIDATION_PROFILES.put(ITI_45, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201310UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        ));

        RESPONSE_VALIDATION_PROFILES.put(ITI_46, new Hl7v3ValidationProfile(
                new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        ));

        RESPONSE_VALIDATION_PROFILES.put(ITI_47, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201306UV02", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON),
                new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        ));

        RESPONSE_VALIDATION_PROFILES.put(ITI_55, new Hl7v3ValidationProfile(
                new Row("PRPA_IN201306UV02", Row.DEFAULT_XSD, "/schematron/iti55/PRPA_IN201306UV02.sch.xml"),
                new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, Row.GAZELLE_PIXPDQV3_SCHEMATRON)
        ));

        RESPONSE_VALIDATION_PROFILES.put(ITI_56, new Hl7v3ValidationProfile(
                new Row("PatientLocationQueryResponse", "/schema/IHE/XCPD_PLQ.xsd", null)
        ));

        RESPONSE_VALIDATION_PROFILES.put(PCC_1, new Hl7v3ValidationProfile(
                new Row("QUPC_IN043200UV01", Row.DEFAULT_XSD, null),
                new Row("MCCI_IN000002UV01", Row.DEFAULT_XSD, null)
        ));

    }
}
