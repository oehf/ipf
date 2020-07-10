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

import org.apache.commons.lang3.ClassUtils;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xacml20.audit.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1PortType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.*;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class ChPpq1Producer extends AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, AssertionBasedRequestType, EprPolicyRepositoryResponse> {

    public ChPpq1Producer(AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint, JaxWsClientFactory<ChPpqAuditDataset> clientFactory) {
        super(endpoint, clientFactory, AssertionBasedRequestType.class, EprPolicyRepositoryResponse.class);
    }

    @Override
    protected EprPolicyRepositoryResponse callService(Object clientObject, AssertionBasedRequestType body) throws Exception {
        var client = (ChPpq1PortType) clientObject;
        if (body instanceof AddPolicyRequest) {
            var request = (AddPolicyRequest) body;
            return client.addPolicy(request);
        } else if (body instanceof UpdatePolicyRequest) {
            var request = (UpdatePolicyRequest) body;
            return client.updatePolicy(request);
        } else if (body instanceof DeletePolicyRequest) {
            var request = (DeletePolicyRequest) body;
            return client.deletePolicy(request);
        } else {
            throw new RuntimeException("Cannot dispatch request of the type " + ClassUtils.getSimpleName(body, "<null>"));
        }
    }
}
