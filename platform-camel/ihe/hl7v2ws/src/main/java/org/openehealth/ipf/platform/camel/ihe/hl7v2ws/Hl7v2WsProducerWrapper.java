/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws;

import org.apache.camel.Producer;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerAdaptingInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerInputAcceptanceInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerOutputAcceptanceInterceptor;

/**
 * @author Dmytro Rud
 */
public class Hl7v2WsProducerWrapper {

    private static final String CHARSET = "UTF-8";

    public static Producer wrapProducer(
            Hl7v2ConfigurationHolder configurationHolder,
            Producer producer)
    {
        producer = new ProducerMarshalInterceptor(configurationHolder, CHARSET, producer);
        producer = new ProducerOutputAcceptanceInterceptor(configurationHolder, producer);
        producer = new ProducerInputAcceptanceInterceptor(configurationHolder, producer);
        producer = new ProducerAdaptingInterceptor(configurationHolder, CHARSET, producer);
        return producer;
    }
}
