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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq2;

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.chppq2.ChPpq2PortType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
@Slf4j
public class ChPpq2Service extends AbstractWebService implements ChPpq2PortType {

    @Override
    public ResponseType policyQuery(XACMLPolicyQueryType request) {
        var result = process(request);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug(getClass().getSimpleName() + " service failed", exception);
            var response = Xacml20Utils.createXacmlQueryResponse("urn:oasis:names:tc:SAML:2.0:status:Responder");
            response.getStatus().setStatusMessage(exception.getMessage());
            return response;
        }
        return result.getMessage().getBody(ResponseType.class);
    }

}
