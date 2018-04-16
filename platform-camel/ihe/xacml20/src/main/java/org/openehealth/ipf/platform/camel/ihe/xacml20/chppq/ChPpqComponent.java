package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import org.openehealth.ipf.commons.ihe.xacml20.CH_PPQ;
import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditDataset;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class ChPpqComponent extends AbstractWsComponent<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, WsInteractionId<WsTransactionConfiguration<ChPpqAuditDataset>>> {

    public ChPpqComponent() {
        super(CH_PPQ.Interactions.CH_PPQ);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new ChPpqEndpoint(uri, remaining, this, parameters, ChPpqService.class);
    }

}
