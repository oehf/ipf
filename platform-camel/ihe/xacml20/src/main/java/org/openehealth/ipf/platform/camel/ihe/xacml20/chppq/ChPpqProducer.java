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
