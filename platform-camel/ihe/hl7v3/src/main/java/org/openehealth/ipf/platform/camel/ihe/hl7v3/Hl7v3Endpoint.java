/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.commons.ihe.ws.WsInteractionId;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.util.Map;

/**
 * Camel endpoint implementation for HL7v3-based IHE components.
 *
 * @author Dmytro Rud
 */
public abstract class Hl7v3Endpoint<ConfigType extends Hl7v3WsTransactionConfiguration>
        extends AbstractWsEndpoint<Hl7v3AuditDataset, ConfigType> {

    protected Hl7v3Endpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<Hl7v3AuditDataset, ConfigType, ? extends WsInteractionId<ConfigType>> component,
            Map<String, Object> parameters,
            Class<? extends AbstractWebService> serviceClass) {
        super(endpointUri, address, component, parameters, serviceClass);
    }

    @Override
    public JaxWsClientFactory<Hl7v3AuditDataset> getJaxWsClientFactory() {
        return new Hl7v3ClientFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getComponent().getClientAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getFeatures(),
                getProperties(),
                getCorrelator());
    }


    @Override
    public JaxWsServiceFactory<Hl7v3AuditDataset> getJaxWsServiceFactory() {
        return new Hl7v3ServiceFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy() : null,
                getAuditContext(),
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

}
