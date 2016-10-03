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

package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.core.Constants;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;

/**
 * Interceptor that adds the HL7InteractionId to the camel headers
 */
public class ConsumerRequestInteractionSetterInterceptor extends InterceptorSupport<HL7v2Endpoint> {

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader(Constants.INTERACTION_ID_NAME, getEndpoint().getInteractionId());
        getWrappedProcessor().process(exchange);
    }
}
