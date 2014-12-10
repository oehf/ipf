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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.core.InteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;

import javax.xml.namespace.QName;

/**
 * @author Dmytro Rud
 */
public class Hl7v3WsTransactionConfiguration extends WsTransactionConfiguration {

    @Getter private final InteractionId interactionId;
    @Getter private final String nakRootElementName;
    @Getter private final String controlActProcessCode;

    /**
     * Constructs the service info.
     *
     * @param interactionId
     *      id of the interaction (transaction) which corresponds to this service info.
     * @param serviceName
     *      the qualified name of the service.
     * @param sei
     *      service endpoint interface.
     * @param bindingName
     *      the qualified name of the binding to use.
     * @param mtom
     *      {@code true} if this service requires MTOM.
     * @param wsdlLocation
     *      the location of the WSDL of this webservice.
     * @param nakRootElementName
     *      root element name of automatically generated NAKs.
     * @param controlActProcessCode
     *      when not {@code null}, the {@code &lt;controlActProcess&gt;}
     *      element with the given code ID will be created in the NAK.
     * @param auditRequestPayload
     *      whether request payload is needed for ATNA audit.
     * @param supportAsynchrony
     *      whether producers can request asynchronous responses via WSA.
     */
    public Hl7v3WsTransactionConfiguration(
            InteractionId interactionId,
            QName serviceName,
            Class<?> sei,
            QName bindingName,
            boolean mtom,
            String wsdlLocation,
            String nakRootElementName,
            String controlActProcessCode,
            boolean auditRequestPayload,
            boolean supportAsynchrony)
    {
        super(serviceName, sei, bindingName, mtom, wsdlLocation,
                true, false, auditRequestPayload, supportAsynchrony);

        this.interactionId = interactionId;
        this.nakRootElementName = nakRootElementName;
        this.controlActProcessCode = controlActProcessCode;
    }

}
