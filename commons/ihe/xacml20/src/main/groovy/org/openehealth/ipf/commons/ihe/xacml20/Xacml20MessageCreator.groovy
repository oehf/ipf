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
import org.herasaf.xacml.core.context.impl.ObjectFactory as XacmlContextObjectFactory
import org.herasaf.xacml.core.policy.impl.ObjectFactory as XacmlPolicyObjectFactory
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory as Hl7v3ObjectFactory
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.NameIDType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusCodeType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusType

import javax.xml.datatype.DatatypeFactory

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
class Xacml20MessageCreator {

    protected static final Hl7v3ObjectFactory HL7V3_OBJECT_FACTORY = new Hl7v3ObjectFactory()
    protected static final XacmlContextObjectFactory XACML_CONTEXT_OBJECT_FACTORY = new XacmlContextObjectFactory()
    protected static final XacmlPolicyObjectFactory XACML_POLICY_OBJECT_FACTORY = new XacmlPolicyObjectFactory()
    protected static final DatatypeFactory XML_OBJECT_FACTORY = DatatypeFactory.newInstance()

    private final String homeCommunityId

    Xacml20MessageCreator(String homeCommunityId) {
        this.homeCommunityId = Validate.notEmpty(homeCommunityId as String, 'Home community ID shall be provided')
    }

    NameIDType createIssuer() {
        return new NameIDType(
            nameQualifier: EprConstants.NAME_QUALIFIER_EHEALTH_SUISSSE_COMMUNITY_INDEX,
            value: homeCommunityId,
        )
    }

    AssertionType createAssertion() {
        return new AssertionType(
            ID: '_' + UUID.randomUUID(),
            issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
            version: '2.0',
            issuer: createIssuer(),
        )
    }

    protected static ResponseType createResponse(Xacml20Status status, String statusMessage, AssertionType assertion, String requestId) {
        return new ResponseType(
            ID: '_' + UUID.randomUUID(),
            issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
            version: '2.0',
            inResponseTo: requestId,
            status: new StatusType(
                statusCode: new StatusCodeType(value: status.code),
                statusMessage: statusMessage,
            ),
            assertionOrEncryptedAssertion: [assertion],
        )
    }

    ResponseType createNegativeQueryResponse(Xacml20Status status, String statusMessage, String requestId) {
        return createResponse(status, statusMessage, createAssertion(), requestId)
    }

    ResponseType createNegativeQueryResponse(Exception exception, String requestId) {
        return (exception instanceof Xacml20Exception)
            ? createNegativeQueryResponse(exception.status, exception.message, requestId)
            : createNegativeQueryResponse(Xacml20Status.RESPONDER_ERROR, exception.message, requestId)
    }

}
