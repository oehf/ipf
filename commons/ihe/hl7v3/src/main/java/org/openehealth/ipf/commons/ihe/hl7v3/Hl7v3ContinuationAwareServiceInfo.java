/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3;

import org.apache.commons.lang.Validate;

import javax.xml.namespace.QName;

/**
 * @author Dmytro Rud
 */
public class Hl7v3ContinuationAwareServiceInfo extends Hl7v3ServiceInfo {

    private final String mainRequestRootElementName;
    private final String mainResponseRootElementName;


    public Hl7v3ContinuationAwareServiceInfo(
            QName serviceName,
            Class<?> serviceClass,
            QName bindingName,
            boolean mtom,
            String wsdlLocation,
            String[][] requestValidationProfiles,
            String[][] responseValidationProfiles,
            String nakRootElementName,
            boolean nakNeedControlActProcess,
            boolean auditRequestPayload,
            String mainRequestRootElementName,
            String mainResponseRootElementName)
    {
        super(serviceName, serviceClass, bindingName, mtom, wsdlLocation,
                requestValidationProfiles, responseValidationProfiles,
                nakRootElementName, nakNeedControlActProcess, auditRequestPayload);

        Validate.notEmpty(mainRequestRootElementName);
        Validate.notEmpty(mainResponseRootElementName);
        this.mainRequestRootElementName = mainRequestRootElementName;
        this.mainResponseRootElementName = mainResponseRootElementName;
    }


    /**
     * @return root XML element name for request messages
     * which correspond to the "main" operation of the transaction,
     * e.g. "PRPA_IN201305UV02" for PDQv3.
     */
    public String getMainRequestRootElementName() {
        return mainRequestRootElementName;
    }

    /**
     * @return root XML element name for response messages
     * which correspond to the "main" operation of the transaction,
     * e.g. "PRPA_IN201306UV02" for PDQv3.
     */
    public String getMainResponseRootElementName() {
        return mainResponseRootElementName;
    }

}
