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
package org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddPolicyRequest_QNAME = new QName("urn:e-health-suisse:2015:policy-administration", "AddPolicyRequest");
    private final static QName _UpdatePolicyRequest_QNAME = new QName("urn:e-health-suisse:2015:policy-administration", "UpdatePolicyRequest");
    private final static QName _DeletePolicyRequest_QNAME = new QName("urn:e-health-suisse:2015:policy-administration", "DeletePolicyRequest");
    private final static QName _UnknownPolicySetId_QNAME = new QName("urn:e-health-suisse:2015:policy-administration", "UnknownPolicySetId");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddPolicyRequest }
     * 
     */
    public AddPolicyRequest createAddPolicyRequest() {
        return new AddPolicyRequest();
    }

    /**
     * Create an instance of {@link UpdatePolicyRequest }
     * 
     */
    public UpdatePolicyRequest createUpdatePolicyRequest() {
        return new UpdatePolicyRequest();
    }

    /**
     * Create an instance of {@link DeletePolicyRequest }
     * 
     */
    public DeletePolicyRequest createDeletePolicyRequest() {
        return new DeletePolicyRequest();
    }

    /**
     * Create an instance of {@link EpdPolicyRepositoryResponse }
     * 
     */
    public EpdPolicyRepositoryResponse createEpdPolicyRepositoryResponse() {
        return new EpdPolicyRepositoryResponse();
    }

    /**
     * Create an instance of {@link UnknownPolicySetId }
     * 
     */
    public UnknownPolicySetId createUnknownPolicySetId() {
        return new UnknownPolicySetId();
    }

    /**
     * Create an instance of {@link AssertionBasedRequestType }
     * 
     */
    public AssertionBasedRequestType createAssertionBasedRequestType() {
        return new AssertionBasedRequestType();
    }

    /**
     * Create an instance of {@link XACMLPolicySetIdReferenceStatementType }
     * 
     */
    public XACMLPolicySetIdReferenceStatementType createXACMLPolicySetIdReferenceStatementType() {
        return new XACMLPolicySetIdReferenceStatementType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddPolicyRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:e-health-suisse:2015:policy-administration", name = "AddPolicyRequest")
    public JAXBElement<AddPolicyRequest> createAddPolicyRequest(AddPolicyRequest value) {
        return new JAXBElement<AddPolicyRequest>(_AddPolicyRequest_QNAME, AddPolicyRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdatePolicyRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:e-health-suisse:2015:policy-administration", name = "UpdatePolicyRequest")
    public JAXBElement<UpdatePolicyRequest> createUpdatePolicyRequest(UpdatePolicyRequest value) {
        return new JAXBElement<UpdatePolicyRequest>(_UpdatePolicyRequest_QNAME, UpdatePolicyRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeletePolicyRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:e-health-suisse:2015:policy-administration", name = "DeletePolicyRequest")
    public JAXBElement<DeletePolicyRequest> createDeletePolicyRequest(DeletePolicyRequest value) {
        return new JAXBElement<DeletePolicyRequest>(_DeletePolicyRequest_QNAME, DeletePolicyRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownPolicySetId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:e-health-suisse:2015:policy-administration", name = "UnknownPolicySetId")
    public JAXBElement<UnknownPolicySetId> createUnknownPolicySetId(UnknownPolicySetId value) {
        return new JAXBElement<UnknownPolicySetId>(_UnknownPolicySetId_QNAME, UnknownPolicySetId.class, null, value);
    }

}
