/*
 * Copyright 2009 the original author or authors.
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

import lombok.Getter;
import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Hl7v3ValidationProfile implements CombinedXmlValidationProfile {

    static class Row {
        private static final String HL7V3_SCHEMAS_PATH = "/schema/HL7V3/NE2008/multicacheschemas/";

        public static final String DEFAULT_XSD = "";
        public static final String GAZELLE_PIXPDQV3_SCHEMATRON = "/schematron/gazelle-pixpdqv3.sch";

        @Getter final String rootElementName;
        @Getter final String xsdPath;
        @Getter final String schematronPath;
        @Getter final String schematronPhase;

        Row(String rootElementName, String xsdPath, String schematronPath) {
            this.rootElementName = rootElementName;
            if (DEFAULT_XSD.equals(xsdPath)) {
                int pos1 = rootElementName.indexOf('_');
                int pos2 = rootElementName.indexOf('_', pos1 + 1);
                String documentName = (pos2 > 0) ? rootElementName.substring(0, pos2) : rootElementName;
                this.xsdPath = HL7V3_SCHEMAS_PATH + documentName + ".xsd";
            } else {
                this.xsdPath = xsdPath.startsWith("/") ? xsdPath : HL7V3_SCHEMAS_PATH + xsdPath;
            }
            this.schematronPath = schematronPath;
            this.schematronPhase = GAZELLE_PIXPDQV3_SCHEMATRON.equals(schematronPath) ? rootElementName : null;
        }
    }


    private final Map<String, Row> map;

    public Hl7v3ValidationProfile(Row... rows) {
        map = new HashMap<String, Row>(rows.length);
        for (Row row : rows) {
            map.put(row.rootElementName, row);
        }
    }


    @Override
    public boolean isValidRootElement(String rootElementName) {
        return map.containsKey(rootElementName);
    }

    @Override
    public String getXsdPath(String rootElementName) {
        return map.get(rootElementName).getXsdPath();
    }

    @Override
    public String getSchematronPath(String rootElementName) {
        return map.get(rootElementName).getSchematronPath();
    }

    @Override
    public Map<String, Object> getCustomSchematronParameters(String rootElementName) {
        String phase = map.get(rootElementName).getSchematronPhase();
        return (phase != null) ? Collections.<String, Object>singletonMap("phase", phase) : null;
    }

}
