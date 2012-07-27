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

import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ClientFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.commons.ihe.ws.JaxWsServiceFactory;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.util.List;

/**
 * Camel endpoint implementation for HL7v3-based IHE components.
 * @author Dmytro Rud
 */
public class Hl7v3Endpoint<ConfigType extends Hl7v3WsTransactionConfiguration>
        extends AbstractWsEndpoint<AbstractWsComponent<ConfigType>>
{

    public Hl7v3Endpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<ConfigType> component,
            InterceptorProvider customInterceptors,
            List<AbstractFeature> features)
    {
        super(endpointUri, address, component, customInterceptors, features);
    }


    @Override
    public JaxWsClientFactory getJaxWsClientFactory() {
        return new Hl7v3ClientFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceUrl(),
                isAudit() ? getComponent().getClientAuditStrategy(isAllowIncompleteAudit()) : null,
                getCorrelator(),
                getCustomInterceptors());
    }


    @Override
    public JaxWsServiceFactory getJaxWsServiceFactory() {
        return new Hl7v3ServiceFactory(
                getComponent().getWsTransactionConfiguration(),
                getServiceAddress(),
                isAudit() ? getComponent().getServerAuditStrategy(isAllowIncompleteAudit()) : null,
                getCustomInterceptors(),
                getRejectionHandlingStrategy());
    }

}
