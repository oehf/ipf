/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.chadr

import groovy.transform.CompileStatic
import org.herasaf.xacml.core.context.impl.*
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageCreator
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Status
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLAuthzDecisionStatementType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType

import static org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.AttributeIds

@CompileStatic
class AdrMessageCreator extends Xacml20MessageCreator {

    private static final ObjectFactory OBJECT_FACTORY = new ObjectFactory()

    AdrMessageCreator(String homeCommunityId) {
        super(homeCommunityId)
    }

    XACMLAuthzDecisionQueryType createAdrRequest(AdrAttributes<SubjectType> subjectAttrs, AdrAttributes<ResourceType> resourceAttrs, String actionId) {
        def request = new XACMLAuthzDecisionQueryType(
                ID: '_' + UUID.randomUUID(),
                issueInstant: XML_OBJECT_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                version: '2.0',
                issuer: createIssuer(),
                returnContext: false,
                inputContextOnly: false,
        )
        request.rest << OBJECT_FACTORY.createRequest(new RequestType(
                subjects: subjectAttrs.createAdrRequestParts(),
                resources: resourceAttrs.createAdrRequestParts(),
                action: new ActionType(attributes: [
                        AdrUtils.createAttr(AttributeIds.XACML_1_0_ACTION_ID, new AnyURIDataTypeAttribute(), actionId),
                ]),
                environment: new EnvironmentType(),
        ))
        return request
    }

    ResponseType createAdrResponse(XACMLAuthzDecisionQueryType adrRequest, Xacml20Status status, List <ResultType> results) {
        def assertion = createAssertion()
        assertion.statementOrAuthnStatementOrAuthzDecisionStatement << new XACMLAuthzDecisionStatementType(
                response: new org.herasaf.xacml.core.context.impl.ResponseType(
                        results: results,
                ),
        )
        return createResponse(status, null, assertion, adrRequest.ID)
    }

}