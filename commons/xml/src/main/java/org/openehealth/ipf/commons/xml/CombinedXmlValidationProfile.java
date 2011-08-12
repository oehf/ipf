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
package org.openehealth.ipf.commons.xml;

import java.util.Map;

/**
 * Interface for combined (XML Schema + Schematron) validation profiles.
 * @author Dmytro Rud
 */
public interface CombinedXmlValidationProfile {

    /**
     * @param rootElementName
     *      local name of root XML element of the document that is being validated.
     * @return
     *      <code>true</code> when the given element name is acceptable,
     *      <code>false</code> otherwise
     */
    boolean isValidRootElement(String rootElementName);

    /**
     * @param rootElementName
     *      local name of root XML element of the document that is being validated
     *      (already checked for acceptance).
     * @return
     *      path to XML Schema definition for the given XML root element,
     *      or <code>null</code>, when no XML Schema validation should be performed.
     */
    String getXsdPath(String rootElementName);

    /**
     * @param rootElementName
     *      local name of root XML element of the document that is being validated.
     *      (already checked for acceptance).
     * @return
     *      path to Schematron template for the given XML root element,
     *      or <code>null</code>, when no Schematron validation should be performed.
     */
    String getSchematronPath(String rootElementName);

    /**
     * @return
     *      a map containing custom Schematron validation parameters,
     *      or <code>null</code>, when such parameters are not necessary.
     */
    Map<String, Object> getCustomSchematronParameters();
}
