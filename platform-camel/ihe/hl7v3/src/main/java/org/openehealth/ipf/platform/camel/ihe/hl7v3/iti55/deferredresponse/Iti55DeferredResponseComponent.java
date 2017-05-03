/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55.deferredresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3AuditDataset;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.commons.ihe.ws.JaxWsClientFactory;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3AsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsProducer;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v3.XCPD.Interactions.ITI_55_DEFERRED_RESPONSE;

/**
 * Camel component for the ITI-55 XCPD Initiating Gateway actor
 * (receivers of deferred responses).
 */
public class Iti55DeferredResponseComponent extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {

    public static final String THREAD_POOL_NAME = "iti55.deferred.response";

    public Iti55DeferredResponseComponent() {
        super(ITI_55_DEFERRED_RESPONSE);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        return new Hl7v3AsyncResponseEndpoint<Hl7v3WsTransactionConfiguration>(uri, remaining, this,
                getCustomInterceptors(parameters),
                getFeatures(parameters),
                getSchemaLocations(parameters),
                getProperties(parameters),
                getSslContextParameters(parameters),
                Iti55DeferredResponseService.class) {
            @Override
            public AbstractWsProducer<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration, ?, ?> getProducer(AbstractWsEndpoint<Hl7v3AuditDataset, Hl7v3WsTransactionConfiguration> endpoint,
                                                  JaxWsClientFactory<Hl7v3AuditDataset> clientFactory) {
                return new Iti55DeferredResponseProducer(endpoint, clientFactory);
            }
        };
    }

}
