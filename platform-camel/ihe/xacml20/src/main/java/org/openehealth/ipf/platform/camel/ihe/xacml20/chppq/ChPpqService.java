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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqPortType;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.*;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 *
 * @deprecated split into PPQ-1 and PPQ-2 in the Swiss EPR specification from March 2018.
 */
@Slf4j
@Deprecated
public class ChPpqService extends AbstractWebService implements ChPpqPortType {

    @Override
    public ResponseType policyQuery(XACMLPolicyQueryType request) {
        Exchange result = process(request);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            ResponseType response = Xacml20Utils.createXacmlQueryResponse("urn:oasis:names:tc:SAML:2.0:status:Responder");
            response.getStatus().setStatusMessage(exception.getMessage());
            return response;
        }
        return Exchanges.resultMessage(result).getBody(ResponseType.class);
    }

    @Override
    public EpdPolicyRepositoryResponse addPolicy(AddPolicyRequest request) {
        try {
            return processPolicyRequest(request);
        } catch (UnknownPolicySetIdFaultMessage unknownPolicySetIdFaultMessage) {
            return errorResponse();
        }
    }

    @Override
    public EpdPolicyRepositoryResponse updatePolicy(UpdatePolicyRequest request) throws UnknownPolicySetIdFaultMessage {
        return processPolicyRequest(request);
    }

    @Override
    public EpdPolicyRepositoryResponse deletePolicy(DeletePolicyRequest request) throws UnknownPolicySetIdFaultMessage {
        return processPolicyRequest(request);
    }

    private static EpdPolicyRepositoryResponse errorResponse() {
        EpdPolicyRepositoryResponse repositoryResponse = new EpdPolicyRepositoryResponse();
        repositoryResponse.setStatus(PpqConstants.StatusCode.FAILURE);
        return repositoryResponse;
    }

    private <T extends AssertionBasedRequestType> EpdPolicyRepositoryResponse processPolicyRequest(T request) throws UnknownPolicySetIdFaultMessage {
        Exchange result = process(request);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            if (exception instanceof UnknownPolicySetIdFaultMessage) {
                throw (UnknownPolicySetIdFaultMessage) exception;
            }
            return errorResponse();
        }
        return Exchanges.resultMessage(result).getBody(EpdPolicyRepositoryResponse.class);
    }

}
