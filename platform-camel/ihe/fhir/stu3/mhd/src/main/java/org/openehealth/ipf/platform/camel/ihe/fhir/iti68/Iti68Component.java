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

import org.apache.camel.Endpoint;
import org.apache.camel.component.servlet.ServletComponent;
import org.apache.camel.component.servlet.ServletEndpoint;
import org.apache.camel.http.common.HttpMethods;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategy;
import org.openehealth.ipf.commons.ihe.fhir.MHD;
import org.openehealth.ipf.commons.ihe.fhir.iti68.Iti68AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableComponent;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Component for MHD Retrieve Document (ITI-68)
 *
 * @author Christian Ohr
 * @since 3.4
 */
public class Iti68Component extends ServletComponent implements InterceptableComponent, AuditableComponent<Iti68AuditDataset> {

    public Iti68Component() {
        super(Iti68Endpoint.class);
    }


    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Iti68Endpoint endpoint = (Iti68Endpoint)super.createEndpoint(uri, remaining, parameters);
        // Ensure that the audit/auditContext parameter is evaluated
        endpoint.setConfig(new AuditableEndpointConfiguration(this, parameters));
        return endpoint;
    }

    @Override
    protected Iti68Endpoint createServletEndpoint(String endpointUri, ServletComponent component, URI httpUri) throws Exception {
        Iti68Endpoint endpoint = new Iti68Endpoint(endpointUri, component, httpUri);
        endpoint.setHttpMethodRestrict(HttpMethods.GET.name());
        return endpoint;
    }

    @Override
    protected boolean lenientContextPath() {
        return false;
    }

    @Override
    public List<Interceptor<?>> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor<?>> getAdditionalProducerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public AuditStrategy<Iti68AuditDataset> getClientAuditStrategy() {
        return MHD.RetrieveInteractions.ITI_68.getTransactionConfiguration().getClientAuditStrategy();
    }

    @Override
    public AuditStrategy<Iti68AuditDataset> getServerAuditStrategy() {
        return MHD.RetrieveInteractions.ITI_68.getTransactionConfiguration().getServerAuditStrategy();
    }
}
