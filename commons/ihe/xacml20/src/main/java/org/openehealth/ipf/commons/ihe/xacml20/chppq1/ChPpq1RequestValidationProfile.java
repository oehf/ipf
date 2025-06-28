/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.xacml20.chppq1;

import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile;

import java.util.Map;

public class ChPpq1RequestValidationProfile implements CombinedXmlValidationProfile {

    @Override
    public boolean isValidRootElement(String rootElementName) {
        return "AddPolicyRequest".equals(rootElementName)
                || "UpdatePolicyRequest".equals(rootElementName)
                || "DeletePolicyRequest".equals(rootElementName);
    }

    @Override
    public String getXsdPath(String rootElementName) {
        return "schema/epr-policy-administration-combined-schema-1.3-local.xsd";
    }

    @Override
    public String getSchematronPath(String rootElementName) {
        return "schematron/epr-patient-specific-policies.sch";
    }

    @Override
    public Map<String, Object> getCustomSchematronParameters(String rootElementName) {
        return null;
    }

}
