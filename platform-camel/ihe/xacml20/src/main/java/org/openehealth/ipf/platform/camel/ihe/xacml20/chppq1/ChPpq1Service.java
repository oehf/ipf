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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq1;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1PortType;
import org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants;
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.*;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
@Slf4j
public class ChPpq1Service extends AbstractWebService implements ChPpq1PortType {

    @Override
    public EprPolicyRepositoryResponse addPolicy(AddPolicyRequest request) {
        try {
            return processPolicyRequest(request);
        } catch (UnknownPolicySetIdFaultMessage unknownPolicySetIdFaultMessage) {
            return errorResponse();
        }
    }

    @Override
    public EprPolicyRepositoryResponse updatePolicy(UpdatePolicyRequest request) throws UnknownPolicySetIdFaultMessage {
        return processPolicyRequest(request);
    }

    @Override
    public EprPolicyRepositoryResponse deletePolicy(DeletePolicyRequest request) throws UnknownPolicySetIdFaultMessage {
        return processPolicyRequest(request);
    }

    private static EprPolicyRepositoryResponse errorResponse() {
        EprPolicyRepositoryResponse repositoryResponse = new EprPolicyRepositoryResponse();
        repositoryResponse.setStatus(PpqConstants.StatusCode.FAILURE);
        return repositoryResponse;
    }

    private <T extends AssertionBasedRequestType> EprPolicyRepositoryResponse processPolicyRequest(T request) throws UnknownPolicySetIdFaultMessage {
        Exchange result = process(request);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            if (exception instanceof UnknownPolicySetIdFaultMessage) {
                throw (UnknownPolicySetIdFaultMessage) exception;
            }
            return errorResponse();
        }
        return Exchanges.resultMessage(result).getBody(EprPolicyRepositoryResponse.class);
    }

}
