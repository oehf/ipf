package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.ChPpqAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class ChPpqEndpoint extends AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> {

    public ChPpqEndpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ? extends WsInteractionId> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass)
    {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    @Override
    public JaxWsClientFactory<ChPpqAuditDataset> getJaxWsClientFactory() {
        return new JaxWsRequestClientFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getClientAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getFeatures(),
                getProperties(),
                getCorrelator());
    }

    @Override
    public JaxWsServiceFactory<ChPpqAuditDataset> getJaxWsServiceFactory() {
        return new JaxWsRequestServiceFactory<>(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

    @Override
    public AbstractWsProducer<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>, ?, ?> getProducer(
            AbstractWsEndpoint<ChPpqAuditDataset, WsTransactionConfiguration<ChPpqAuditDataset>> endpoint,
            JaxWsClientFactory<ChPpqAuditDataset> clientFactory)
    {
        return new ChPpqProducer(endpoint, clientFactory);
    }

}
