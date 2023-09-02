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

package org.openehealth.ipf.commons.ihe.xacml20

import org.apache.commons.lang3.Validate
import org.herasaf.xacml.core.context.impl.ActionType
import org.herasaf.xacml.core.context.impl.AttributeType
import org.herasaf.xacml.core.context.impl.AttributeValueType
import org.herasaf.xacml.core.context.impl.EnvironmentType
import org.herasaf.xacml.core.context.impl.RequestType
import org.herasaf.xacml.core.context.impl.ResourceType
import org.herasaf.xacml.core.context.impl.SubjectType
import org.herasaf.xacml.core.policy.impl.IdReferenceType
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.IiDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.DeletePolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UpdatePolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.XACMLPolicySetIdReferenceStatementType
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.NameIDType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusCodeType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLPolicyStatementType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType

import javax.xml.datatype.DatatypeFactory

class ChPpqMessageCreator {

    private static final HL7V3_OBJECT_FACTORY = new org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory()
    private static final XACML_CONTEXT_OBJECT_FACTORY = new org.herasaf.xacml.core.context.impl.ObjectFactory()
    private static final XACML_POLICY_OBJECT_FACTORY = new org.herasaf.xacml.core.policy.impl.ObjectFactory()
    private static final XML_OBJECT_FACTORY = DatatypeFactory.newInstance()

    private final String homeCommunityId

    ChPpqMessageCreator(String homeCommunityId) {
        this.homeCommunityId = Validate.notEmpty(homeCommunityId)
    }

    private AssertionType createAssertion() {
        return new AssertionType(
                ID: '_' + UUID.randomUUID(),
                issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                version: '2.0',
                issuer: new NameIDType(
                        nameQualifier: PpqConstants.NAME_QUALIFIER_EHEALTH_SUISSSE_COMMUNITY_INDEX,
                        value: homeCommunityId,
                ),
        )
    }

    private AssertionType createSubmitAssertion(Collection<PolicySetType> policySets) {
        def assertion = createAssertion()
        assertion.statementOrAuthnStatementOrAuthzDecisionStatement << new XACMLPolicyStatementType(
                policyOrPolicySet: policySets
        )
        return assertion
    }

    private AssertionType createDeleteAssertion(Collection<String> policySetIds) {
        def assertion = createAssertion()
        assertion.statementOrAuthnStatementOrAuthzDecisionStatement << new XACMLPolicySetIdReferenceStatementType(
                policySetIdReference: policySetIds.collect { new IdReferenceType(value: it) }
        )
        return assertion
    }

    AddPolicyRequest createAddPolicyRequest(Collection<PolicySetType> policySets) {
        return new AddPolicyRequest(assertion: createSubmitAssertion(policySets))
    }

    UpdatePolicyRequest createUpdatePolicyRequest(Collection<PolicySetType> policySets) {
        return new UpdatePolicyRequest(assertion: createSubmitAssertion(policySets))
    }

    DeletePolicyRequest createDeletePolicyRequest(Collection<String> policySetIds) {
        return new DeletePolicyRequest(assertion: createDeleteAssertion(policySetIds))
    }

    private static XACMLPolicyQueryType createPolicyQueryElement() {
        return new XACMLPolicyQueryType(
                ID: '_' + UUID.randomUUID(),
                issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                version: '2.0',
        )
    }

    XACMLPolicyQueryType createPolicyQuery(II patientId) {
        def query = createPolicyQueryElement()
        query.requestOrPolicySetIdReferenceOrPolicyIdReference << XACML_CONTEXT_OBJECT_FACTORY.createRequest(
                new RequestType(
                        subjects: [
                                new SubjectType(),
                        ],
                        resources: [
                                new ResourceType(
                                        attributes: [
                                                new AttributeType(
                                                        attributeId: PpqConstants.AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID,
                                                        dataType: new IiDataTypeAttribute(),
                                                        attributeValues: [
                                                                new AttributeValueType(
                                                                        content: [
                                                                                HL7V3_OBJECT_FACTORY.createInstanceIdentifier(patientId),
                                                                        ],
                                                                ),
                                                        ],
                                                ),
                                        ]
                                ),
                        ],
                        action: new ActionType(),
                        environment: new EnvironmentType(),
                )
        )
        return query
    }

    XACMLPolicyQueryType createPolicyQuery(List<String> policySetIds) {
        def query = createPolicyQueryElement()
        query.getRequestOrPolicySetIdReferenceOrPolicyIdReference().addAll(policySetIds.collect { id ->
            XACML_POLICY_OBJECT_FACTORY.createPolicySetIdReference(new IdReferenceType(value: id))
        })
        return query
    }

    ResponseType createPositivePolicyQueryResponse(List<PolicySetType> policySets) {
        def assertion = createAssertion()
        assertion.statementOrAuthnStatementOrAuthzDecisionStatement << new XACMLPolicyStatementType(
                policyOrPolicySet: policySets,
        )
        return new ResponseType(
                ID: '_' + UUID.randomUUID(),
                issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                version: '2.0',
                status: new StatusType(
                        statusCode: new StatusCodeType(value: Xacml20Utils.SAML20_STATUS_SUCCESS),
                ),
                assertionOrEncryptedAssertion: [assertion],
        )
    }

    ResponseType createNegativePolicyQueryResponse(String statusCode) {
        return new ResponseType(
                ID: '_' + UUID.randomUUID(),
                issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                version: '2.0',
                status: new StatusType(
                        statusCode: new StatusCodeType(value: statusCode),
                ),
                assertionOrEncryptedAssertion: [createAssertion()],
        )
    }

}
