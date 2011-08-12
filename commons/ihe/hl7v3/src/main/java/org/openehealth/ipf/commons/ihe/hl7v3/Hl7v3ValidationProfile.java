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

import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Hl7v3ValidationProfile implements CombinedXmlValidationProfile {
    private static final String HL7V3_SCHEMAS_PATH = "HL7V3/NE2008/multicacheschemas/";

    private final String[][] rows;


    public Hl7v3ValidationProfile(String[][] rows) {
        this.rows = rows;
    }


    @Override
    public boolean isValidRootElement(String rootElementName) {
        return (getRow(rootElementName) != null);
    }


    @Override
    public String getXsdPath(String rootElementName) {
        String[] row = getRow(rootElementName);

        int pos1 = rootElementName.indexOf('_');
        int pos2 = rootElementName.indexOf('_', pos1 + 1);
        rootElementName = (pos2 > 0) ? rootElementName.substring(0, pos2) : rootElementName;

        StringBuilder sb = new StringBuilder("schema/");
        if (row.length > 2) {
            sb.append(row[2]);
        } else {
            sb.append(HL7V3_SCHEMAS_PATH).append(rootElementName);
        }
        return sb.append(".xsd").toString();
    }


    @Override
    public String getSchematronPath(String rootElementName) {
        String[] row = getRow(rootElementName);
        return (row[1] != null)
                ? new StringBuilder("schematron/").append(row[1]).append(".sch.xml").toString()
                : null;
    }


    @Override
    public Map<String, Object> getCustomSchematronParameters() {
        return null;
    }


    private String[] getRow(String rootElementName) {
        for (String[] row : rows) {
            if (row[0].equals(rootElementName)) {
                return row;
            }
        }
        return null;
    }
}
