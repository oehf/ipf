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

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Validation profiles for HL7v3-based transactions supported by IPF.
 * @author Dmytro Rud
 */
public class Hl7v3ValidationProfiles {

    private static final Map<InteractionId, CombinedXmlValidationProfile> REQUEST_VALIDATION_PROFILES =
            new HashMap<>();

    private static final Map<InteractionId, CombinedXmlValidationProfile> RESPONSE_VALIDATION_PROFILES =
            new HashMap<>();

    public static CombinedXmlValidationProfile getRequestValidationProfile(InteractionId id) {
        return REQUEST_VALIDATION_PROFILES.get(id);
    }

    public static CombinedXmlValidationProfile getResponseValidationProfile(InteractionId id) {
        return RESPONSE_VALIDATION_PROFILES.get(id);
    }


    private static void registerRequestValidationProfile(InteractionId id, String[][] rows) {
        REQUEST_VALIDATION_PROFILES.put(id, new Hl7v3ValidationProfile(rows));
    }

    private static void registerResponseValidationProfile(InteractionId id, String[][] rows) {
        RESPONSE_VALIDATION_PROFILES.put(id, new Hl7v3ValidationProfile(rows));
    }


    static {
        /********** REQUESTS **********/

        String[][] iti44RequestValidationRows = new String[][]{
                new String[]{"PRPA_IN201301UV02", null},
                new String[]{"PRPA_IN201302UV02", null},
                new String[]{"PRPA_IN201304UV02", null}
        };

        registerRequestValidationProfile(ITI_44_PIX, iti44RequestValidationRows);
        registerRequestValidationProfile(ITI_44_XDS, iti44RequestValidationRows);

        registerRequestValidationProfile(ITI_45, new String[][]{
                new String[]{"PRPA_IN201309UV02", null}
        });

        registerRequestValidationProfile(ITI_46, new String[][]{
                new String[]{"PRPA_IN201302UV02", null}
        });

        registerRequestValidationProfile(ITI_47, new String[][]{
                new String[]{"PRPA_IN201305UV02", "iti47/PRPA_IN201305UV02"},
                new String[]{"QUQI_IN000003UV01", null},
                new String[]{"QUQI_IN000003UV01_Cancel", null}
        });

        registerRequestValidationProfile(ITI_55, new String[][]{
                new String[]{"PRPA_IN201305UV02", "iti55/PRPA_IN201305UV02"}
        });

        registerRequestValidationProfile(ITI_56, new String[][]{
                new String[]{"PatientLocationQueryRequest", null, "IHE/XCPD_PLQ"}
        });

        registerRequestValidationProfile(PCC_1, new String[][]{
                new String[]{"QUPC_IN043100UV01", null},
                new String[]{"QUQI_IN000003UV01", null},
                new String[]{"QUQI_IN000003UV01_Cancel", null}
        });


        /********** RESPONSES**********/

        String[][] iti44ResponseValidationRows = new String[][]{
                new String[]{"MCCI_IN000002UV01", null}
        };

        registerResponseValidationProfile(ITI_44_PIX, iti44ResponseValidationRows);
        registerResponseValidationProfile(ITI_44_XDS, iti44ResponseValidationRows);

        registerResponseValidationProfile(ITI_45, new String[][]{
                new String[]{"PRPA_IN201310UV02", null}
        });


        registerResponseValidationProfile(ITI_46, new String[][]{
                new String[]{"MCCI_IN000002UV01", null}
        });

        registerResponseValidationProfile(ITI_47, new String[][]{
                new String[]{"PRPA_IN201306UV02", "iti47/PRPA_IN201306UV02"},
                new String[]{"MCCI_IN000002UV01", null}
        });

        registerResponseValidationProfile(ITI_55, new String[][]{
                new String[]{"PRPA_IN201306UV02", "iti55/PRPA_IN201306UV02"},
                new String[]{"MCCI_IN000002UV01", null}
        });

        registerResponseValidationProfile(ITI_56, new String[][]{
                new String[]{"PatientLocationQueryResponse", null, "IHE/XCPD_PLQ"}
        });

        registerResponseValidationProfile(PCC_1, new String[][]{
                new String[]{"QUPC_IN043200UV01", null},
                new String[]{"MCCI_IN000002UV01", null}
        });

    }
}
