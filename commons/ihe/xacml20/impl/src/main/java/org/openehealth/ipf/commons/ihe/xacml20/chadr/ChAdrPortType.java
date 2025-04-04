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
package org.openehealth.ipf.commons.ihe.xacml20.chadr;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.ws.Action;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.ObjectFactory;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;


// Differences between ITI-79 and ADR service endpoint interfaces are only WS-Addressing actions

@WebService(targetNamespace = "urn:ihe:iti:2014:ser", name = "AuthorizationDecisionsManager_PortType", portName = "AuthorizationDecisionsManager_Port_Soap12")
@XmlSeeAlso({
        ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.delegation.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ObjectFactory.class,
        org.herasaf.xacml.core.policy.impl.ObjectFactory.class,
        org.herasaf.xacml.core.context.impl.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.ObjectFactory.class,
        org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory.class,
        org.apache.xml.security.binding.xmlenc.ObjectFactory.class,
        org.apache.xml.security.binding.xmldsig.ObjectFactory.class,
        org.apache.xml.security.binding.xop.ObjectFactory.class,
        })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ChAdrPortType {

    @WebMethod(operationName = "AuthorizationDecisionsManager_AuthorizationDecisionsQuery")
    @Action(input = "urn:e-health-suisse:2015:policy-enforcement:AuthorizationDecisionRequest", output = "urn:e-health-suisse:2015:policy-enforcement:XACMLAuthzDecisionResponse")
    @WebResult(name = "Response", targetNamespace = "urn:oasis:names:tc:SAML:2.0:protocol", partName = "body")
    ResponseType authorizationDecisionQuery(
            @WebParam(partName = "body", name = "XACMLAuthzDecisionQueryType", targetNamespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol")
            XACMLAuthzDecisionQueryType body
    );
}
