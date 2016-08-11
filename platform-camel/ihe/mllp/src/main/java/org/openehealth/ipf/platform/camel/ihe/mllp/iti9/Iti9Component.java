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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti9;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.QueryAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerSegmentEchoingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;

import java.util.Collections;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.hl7v2.PIX.Interactions.ITI_9;

/**
 * Camel component for ITI-9 (PIX Query).
 *
 * @author Dmytro Rud
 */
public class Iti9Component extends MllpTransactionComponent<QueryAuditDataset> {


    public Iti9Component() {
        super(ITI_9);
    }

    public Iti9Component(CamelContext camelContext) {
        super(camelContext, ITI_9);
    }

    @Override
    public List<Interceptor> getAdditionalConsumerInterceptors() {
        return Collections.singletonList(new ConsumerSegmentEchoingInterceptor("QPD"));
    }
}
