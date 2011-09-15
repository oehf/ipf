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

import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.ItiServiceInfo;

import javax.xml.namespace.QName;

/**
 * @author Dmytro Rud
 */
public class Hl7v3ServiceInfo extends ItiServiceInfo {

    private final InteractionId interactionId;
    private final String nakRootElementName;
    private final boolean nakNeedControlActProcess;

    /**
     * Constructs the service info.
     *
     * @param interactionId
     *      id of the interaction (transaction) which corresponds to this service info.
     * @param serviceName
     *      the qualified name of the service.
     * @param serviceClass
     *      the class of the service interface.
     * @param bindingName
     *      the qualified name of the binding to use.
     * @param mtom
     *      {@code true} if this service requires MTOM.
     * @param wsdlLocation
     *      the location of the WSDL of this webservice.
     * @param nakRootElementName
     *      root element name of automatically generated NAKs.
     * @param nakNeedControlActProcess
     *      when {@code true}, the <code>&lt;controlActProcess&gt;</code>
     *      of the request message will be included into the NAK.
     * @param auditRequestPayload
     *      whether request payload is needed for ATNA audit.
     */
    public Hl7v3ServiceInfo(
            InteractionId interactionId,
            QName serviceName,
            Class<?> serviceClass,
            QName bindingName,
            boolean mtom,
            String wsdlLocation,
            String nakRootElementName,
            boolean nakNeedControlActProcess,
            boolean auditRequestPayload)
    {
        super(serviceName, serviceClass, bindingName, mtom, wsdlLocation, true, false, auditRequestPayload);

        this.interactionId = interactionId;
        this.nakRootElementName = nakRootElementName;
        this.nakNeedControlActProcess = nakNeedControlActProcess;
    }

    public InteractionId getInteractionId() {
        return interactionId;
    }

    public boolean isNakNeedControlActProcess() {
        return nakNeedControlActProcess;
    }

    public String getNakRootElementName() {
        return nakRootElementName;
    }

}
