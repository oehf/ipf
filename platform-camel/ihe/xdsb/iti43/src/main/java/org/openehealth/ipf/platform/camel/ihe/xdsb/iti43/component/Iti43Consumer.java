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
package org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.component;

import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.xdsb.commons.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.xdsb.iti43.service.Iti43Service;
import org.apache.camel.Processor;

/**
 * The Camel consumer for the ITI-43 transaction.
 */
public class Iti43Consumer extends DefaultItiConsumer {
    /**
     * Constructs the consumer.
     * @param endpoint
     *          the endpoint creating this consumer.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param serviceInfo
     *          info describing the service.
     */
    public Iti43Consumer(Iti43Endpoint endpoint, Processor processor, ItiServiceInfo serviceInfo) {
        super(endpoint, processor, new Iti43Service(), serviceInfo);
    }
}
