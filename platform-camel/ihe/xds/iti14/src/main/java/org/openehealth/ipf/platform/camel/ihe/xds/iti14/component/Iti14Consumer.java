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

import org.apache.camel.Processor;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.DefaultItiConsumer;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ItiServiceInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.ItiAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti14.audit.Iti14ServerAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.xds.iti14.service.Iti14Service;

/**
 * The Camel consumer for the ITI-14 transaction.
 */
public class Iti14Consumer extends DefaultItiConsumer {
    /**
     * Constructs the consumer.
     * @param endpoint
     *          the endpoint creating this consumer.
     * @param processor
     *          the processor to start processing incoming exchanges.
     * @param serviceInfo
     *          info describing the service.
     */
    public Iti14Consumer(Iti14Endpoint endpoint, Processor processor, ItiServiceInfo<?> serviceInfo) {
        super(endpoint, processor, new Iti14Service(), serviceInfo);
    }

    @Override
    public ItiAuditStrategy createAuditStrategy(boolean allowIncompleteAudit) {
        return new Iti14ServerAuditStrategy(allowIncompleteAudit);
    }
}
