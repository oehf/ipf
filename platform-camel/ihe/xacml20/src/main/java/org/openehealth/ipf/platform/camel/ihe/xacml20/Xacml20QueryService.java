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
package org.openehealth.ipf.platform.camel.ihe.xacml20;

import lombok.extern.slf4j.Slf4j;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageCreator;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.RequestAbstractType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.core.HomeCommunityUtils;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
@Slf4j
abstract public class Xacml20QueryService extends AbstractWebService {

    private final String homeCommunityId;

    public Xacml20QueryService(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

    public ResponseType doProcessRequest(RequestAbstractType request) {
        var exchange = process(request);
        var exception = Exchanges.extractException(exchange);
        if (exception != null) {
            log.debug("{} service failed", getClass().getSimpleName(), exception);
            var homeCommunityId = HomeCommunityUtils.getHomeCommunityId(exchange, this.homeCommunityId);
            var messageCreator = new Xacml20MessageCreator(homeCommunityId);
            return messageCreator.createNegativeQueryResponse(exception, request.getID());
        }
        var response = exchange.getMessage().getBody(ResponseType.class);
        response.setInResponseTo(request.getID());
        return response;
    }

}
