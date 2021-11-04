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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti47;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.audit.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ContinuationAwareWsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3ContinuationAwareEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v3.PDQV3.Interactions.ITI_47;

/**
 * The Camel component for the ITI-47 transaction (PDQ v3).
 */
public class Iti47Component extends Hl7v3Component<Hl7v3ContinuationAwareWsTransactionConfiguration> {

    public Iti47Component() {
        super(ITI_47);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new Hl7v3ContinuationAwareEndpoint(uri, remaining, this, parameters) {

            @Override
            protected AbstractWebService getCustomServiceInstance(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3ContinuationAwareWsTransactionConfiguration> endpoint) {
                var endpoint2 = (Hl7v3ContinuationAwareEndpoint) endpoint;
                return endpoint2.isSupportContinuation() ?
                        new Iti47ContinuationAwareService(endpoint2) :
                        new Iti47Service();
            }

        };
    }

}
