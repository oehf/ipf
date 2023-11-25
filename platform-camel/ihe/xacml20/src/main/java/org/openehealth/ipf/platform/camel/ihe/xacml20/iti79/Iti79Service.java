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
package org.openehealth.ipf.platform.camel.ihe.xacml20.iti79;

import org.openehealth.ipf.commons.ihe.xacml20.iti79.Iti79PortType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;
import org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20QueryService;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class Iti79Service extends Xacml20QueryService implements Iti79PortType {

    public Iti79Service(String homeCommunityId) {
        super(homeCommunityId);
    }

    @Override
    public ResponseType authorizationDecisionQuery(XACMLAuthzDecisionQueryType request) {
        return doProcessRequest(request);
    }

}
