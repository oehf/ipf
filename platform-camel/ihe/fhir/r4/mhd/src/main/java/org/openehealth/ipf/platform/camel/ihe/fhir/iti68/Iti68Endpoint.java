/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.fhir.iti68;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.servlet.ServletComponent;
import org.apache.camel.component.servlet.ServletEndpoint;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableEndpoint;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Ohr
 * @since 3.6
 */
public class Iti68Endpoint extends ServletEndpoint
        implements InterceptableEndpoint<AuditableEndpointConfiguration, Iti68Component>, AuditableEndpoint<Iti68AuditDataset> {

    private AuditableEndpointConfiguration config;

    public Iti68Endpoint(String endPointURI, ServletComponent component, URI httpUri) throws URISyntaxException {
        super(endPointURI, component, httpUri);
    }

    void setConfig(AuditableEndpointConfiguration config) {
        this.config = config;
    }

    // Redirect consumer creation to include interceptor chain

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return InterceptableEndpoint.super.createConsumer(processor);
    }

    @Override
    public Consumer doCreateConsumer(Processor processor) throws Exception {
        return super.createConsumer(processor);
    }


    // No producers are ever created

    @Override
    public Producer doCreateProducer() throws Exception {
        return super.createProducer();
    }

    @Override
    public Iti68Component getInterceptableComponent() {
        return (Iti68Component) getComponent();
    }

    @Override
    public AuditableEndpointConfiguration getInterceptableConfiguration() {
        return config;
    }

    @Override
    public AuditStrategy<Iti68AuditDataset> getClientAuditStrategy() {
        return getInterceptableComponent().getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<Iti68AuditDataset> getServerAuditStrategy() {
        return getInterceptableComponent().getServerAuditStrategy();
    }

    @Override
    public AuditContext getAuditContext() {
        return getInterceptableConfiguration().getAuditContext();
    }

    @Override
    public List<Interceptor> createInitialConsumerInterceptorChain() {
        var initialChain = new ArrayList<Interceptor>();
        if (isAudit()) {
            initialChain.add(new Iti68ConsumerAuditInterceptor(getAuditContext()));
        }
        return initialChain;
    }

    @Override
    public List<Interceptor> createInitialProducerInterceptorChain() {
        // Producers are never created
        return null;
    }
}
