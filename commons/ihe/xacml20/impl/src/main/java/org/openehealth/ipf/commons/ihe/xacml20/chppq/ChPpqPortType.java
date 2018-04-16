package org.openehealth.ipf.commons.ihe.xacml20.chppq;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20JaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.*;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;

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
        })
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@DataBinding(Xacml20JaxbDataBinding.class)
public interface ChPpqPortType {

    @WebMethod(operationName = "PolicyRepository_PolicyQuery")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:PolicyQuery", output = "urn:e-health-suisse:2015:policy-administration:PolicyQueryResponse")
    @WebResult(name = "Response", targetNamespace = "urn:oasis:names:tc:SAML:2.0:protocol", partName = "body")
    public ResponseType policyQuery(
            @WebParam(partName = "body", name = "XACMLPolicyQuery", targetNamespace = "urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:protocol")
                    XACMLPolicyQueryType body
    );

    @WebMethod(operationName = "PolicyRepository_DeletePolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:DeletePolicy", output = "urn:e-health-suisse:2015:policy-administration:DeletePolicyResponse", fault = {@FaultAction(className = UnknownPolicySetIdFaultMessage.class, value = "urn:e-health-suisse:2015:policy-administration:DeletePolicyFault")})
    @WebResult(name = "EpdPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EpdPolicyRepositoryResponse deletePolicy(
            @WebParam(partName = "body", name = "DeletePolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    DeletePolicyRequest body
    ) throws UnknownPolicySetIdFaultMessage;

    @WebMethod(operationName = "PolicyRepository_UpdatePolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:UpdatePolicy", output = "urn:e-health-suisse:2015:policy-administration:UpdatePolicyResponse", fault = {@FaultAction(className = UnknownPolicySetIdFaultMessage.class, value = "urn:e-health-suisse:2015:policy-administration:UpdatePolicyFault")})
    @WebResult(name = "EpdPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EpdPolicyRepositoryResponse updatePolicy(
            @WebParam(partName = "body", name = "UpdatePolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    UpdatePolicyRequest body
    ) throws UnknownPolicySetIdFaultMessage;

    @WebMethod(operationName = "PolicyRepository_AddPolicy")
    @Action(input = "urn:e-health-suisse:2015:policy-administration:AddPolicy", output = "urn:e-health-suisse:2015:policy-administration:AddPolicyResponse")
    @WebResult(name = "EpdPolicyRepositoryResponse", targetNamespace = "urn:e-health-suisse:2015:policy-administration", partName = "body")
    public EpdPolicyRepositoryResponse addPolicy(
            @WebParam(partName = "body", name = "AddPolicyRequest", targetNamespace = "urn:e-health-suisse:2015:policy-administration")
                    AddPolicyRequest body
    );
}
