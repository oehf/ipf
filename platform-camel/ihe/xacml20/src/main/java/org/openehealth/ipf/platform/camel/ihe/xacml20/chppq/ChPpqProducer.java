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

import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqPortType;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.apache.commons.lang3.ClassUtils;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

/**
 * @author Dmytro Rud
 */
public class ChPpqProducer extends AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, Object, Object> {

    public ChPpqProducer(AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint, JaxWsClientFactory<ChPpqAuditDataset> clientFactory) {
        super(endpoint, clientFactory, Object.class, Object.class);
    }

    @Override
    protected Object callService(Object clientObject, Object body) throws Exception {
        ChPpqPortType client = (ChPpqPortType) clientObject;
        if (body instanceof XACMLPolicyQueryType) {
            XACMLPolicyQueryType request = (XACMLPolicyQueryType) body;
            return client.policyQuery(request);
        } else if (body instanceof AddPolicyRequest) {
            AddPolicyRequest request = (AddPolicyRequest) body;
            return client.addPolicy(request);
        } else if (body instanceof UpdatePolicyRequest) {
            UpdatePolicyRequest request = (UpdatePolicyRequest) body;
            return client.updatePolicy(request);
        } else if (body instanceof DeletePolicyRequest) {
            DeletePolicyRequest request = (DeletePolicyRequest) body;
            return client.deletePolicy(request);
        } else {
            throw new RuntimeException("Cannot dispatch request of the type " + ClassUtils.getSimpleName(body, "<null>"));
        }
    }
}
