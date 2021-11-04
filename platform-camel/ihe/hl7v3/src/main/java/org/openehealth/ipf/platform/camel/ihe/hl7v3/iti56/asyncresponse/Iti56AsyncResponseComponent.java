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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti56.asyncresponse;

import org.apache.camel.Endpoint;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3AsyncResponseEndpoint;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Component;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.hl7v3.XCPD.Interactions.ITI_56_ASYNC_RESPONSE;

/**
 * Camel component for the ITI-56 XCPD Initiating Gateway actor
 * (receiver of asynchronous responses).
 */
public class Iti56AsyncResponseComponent extends Hl7v3Component<Hl7v3WsTransactionConfiguration> {

    public Iti56AsyncResponseComponent() {
        super(ITI_56_ASYNC_RESPONSE);
    }

    // Required because of base class
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) {
        return new Hl7v3AsyncResponseEndpoint<>(uri, remaining, this, parameters, Iti56AsyncResponseService.class);
    }

}