/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chppq1;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20JaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.ObjectFactory;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

/**
 * @since 3.5.1
 */
@WebService(targetNamespace = "urn:ihe:iti:ppq:2016", name = "PolicyRepository_PortType", portName = "PolicyRepository_Port_Soap12")
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
@DataBinding(Xacml20JaxbDataBinding.class)
public interface ChPpq1PortType {

    @WebMethod(operationName = "PolicyRepository_DeletePolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:DeletePolicy", output = "urn:e-health-suisse:2015:policy-administration:DeletePolicyResponse", fault = {@FaultAction(className = UnknownPolicySetIdFaultMessage.class, value = "urn:e-health-suisse:2015:policy-administration:DeletePolicyFault")})
    @WebResult(name = "EprPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EprPolicyRepositoryResponse deletePolicy(
            @WebParam(partName = "body", name = "DeletePolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    DeletePolicyRequest body
    ) throws UnknownPolicySetIdFaultMessage;

    @WebMethod(operationName = "PolicyRepository_UpdatePolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:UpdatePolicy", output = "urn:e-health-suisse:2015:policy-administration:UpdatePolicyResponse", fault = {@FaultAction(className = UnknownPolicySetIdFaultMessage.class, value = "urn:e-health-suisse:2015:policy-administration:UpdatePolicyFault")})
    @WebResult(name = "EprPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EprPolicyRepositoryResponse updatePolicy(
            @WebParam(partName = "body", name = "UpdatePolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    UpdatePolicyRequest body
    ) throws UnknownPolicySetIdFaultMessage;

    @WebMethod(operationName = "PolicyRepository_AddPolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:AddPolicy", output = "urn:e-health-suisse:2015:policy-administration:AddPolicyResponse")
    @WebResult(name = "EprPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EprPolicyRepositoryResponse addPolicy(
            @WebParam(partName = "body", name = "AddPolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    AddPolicyRequest body
    );
}
