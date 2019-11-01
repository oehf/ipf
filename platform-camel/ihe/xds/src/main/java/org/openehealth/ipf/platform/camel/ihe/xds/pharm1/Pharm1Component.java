package org.openehealth.ipf.platform.camel.ihe.xds.pharm1;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.xds.XDS;
import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsQueryAuditDataset;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsComponent;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.iti18.Iti18Service;

public class Pharm1Component extends XdsComponent<XdsQueryAuditDataset> {
    public Pharm1Component() {
        super(XDS.Interactions.ITI_18);
    }

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new XdsEndpoint<XdsQueryAuditDataset>(uri, remaining, this, parameters, Iti18Service.class) {
            public AbstractWsProducer<XdsQueryAuditDataset, WsTransactionConfiguration<XdsQueryAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<XdsQueryAuditDataset, WsTransactionConfiguration<XdsQueryAuditDataset>> endpoint, JaxWsClientFactory<XdsQueryAuditDataset> clientFactory) {
                return new SimpleWsProducer(endpoint, clientFactory, AdhocQueryRequest.class, AdhocQueryResponse.class);
            }
        };
    }
}
