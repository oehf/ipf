/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti14.component;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.openehealth.ipf.commons.ihe.xds.ItiServiceInfo;
import org.openehealth.ipf.commons.ihe.xds.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiEndpoint;
import org.openehealth.ipf.platform.camel.ihe.xds.iti14.audit.Iti14ClientAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti14.audit.Iti14ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti14.service.Iti14Service;
import java.net.URISyntaxException;

/**
 * The Camel endpoint for the ITI-14 transaction.
 */
public class Iti14Endpoint extends DefaultItiEndpoint {
    /**
     * Constructs the endpoint.
     * @param endpointUri
     *          the endpoint URI.
     * @param address
     *          the endpoint address from the URI.
     * @param iti14Component
     *          the component creating this endpoint.
     * @throws URISyntaxException
     *          if the endpoint URI was not a valid URI.
     */
    public Iti14Endpoint(String endpointUri, String address, Iti14Component iti14Component) throws URISyntaxException {
        super(endpointUri, address, iti14Component);
    }

    public Producer createProducer() throws Exception {
        ItiAuditStrategy auditStrategy = 
            isAudit() ? new Iti14ClientAuditStrategy(isAllowIncompleteAudit()) : null;
        return new Iti14Producer(this, ItiServiceInfo.ITI_14, auditStrategy);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        ItiAuditStrategy auditStrategy = 
            isAudit() ? new Iti14ServerAuditStrategy(isAllowIncompleteAudit()) : null;
        return new DefaultItiConsumer(this, processor, ItiServiceInfo.ITI_14, auditStrategy, Iti14Service.class);
    }
}
