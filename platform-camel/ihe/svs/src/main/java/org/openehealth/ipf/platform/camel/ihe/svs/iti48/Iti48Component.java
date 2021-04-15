package org.openehealth.ipf.platform.camel.ihe.svs.iti48;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.ws.*;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;
import org.openehealth.ipf.platform.camel.ihe.ws.SimpleWsProducer;

import java.util.Map;

/**
 * The Camel component for the ITI-48 transaction.
 *
 * @author Quentin Ligier
 */
public class Iti48Component extends AbstractWsComponent<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>, WsInteractionId<WsTransactionConfiguration<SvsAuditDataset>>> {

    public Iti48Component() {
        super(ITI_48);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new AbstractWsEndpoint<>(uri, remaining, this, parameters, Iti48Service.class) {

            @Override
            public JaxWsClientFactory<SvsAuditDataset> getJaxWsClientFactory() {
                return new JaxWsRequestClientFactory<>(
                        getComponent().getWsTransactionConfiguration(),
                        getServiceUrl(),
                        isAudit() ? getClientAuditStrategy() : null,
                        getAuditContext(),
                        getCustomInterceptors(),
                        getFeatures(),
                        getProperties(),
                        getCorrelator(),
                        getSecurityInformation());
            }

            @Override
            public JaxWsServiceFactory<SvsAuditDataset> getJaxWsServiceFactory() {
                return new JaxWsRequestServiceFactory<>(
                        getComponent().getWsTransactionConfiguration(),
                        getServiceAddress(),
                        isAudit() ? getComponent().getServerAuditStrategy() : null,
                        getAuditContext(),
                        getCustomInterceptors(),
                        getRejectionHandlingStrategy());
            }

            @Override
            public AbstractWsProducer<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>, ?, ?> getProducer(AbstractWsEndpoint<SvsAuditDataset, WsTransactionConfiguration<SvsAuditDataset>> endpoint, JaxWsClientFactory<SvsAuditDataset> clientFactory) {
                return new SimpleWsProducer<>(
                        endpoint, clientFactory, RetrieveValueSetRequest.class, RetrieveValueSetResponse.class
                );
            }
        };
    }
}
