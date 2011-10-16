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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import org.apache.cxf.interceptor.InterceptorProvider;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3WsTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v3.Hl7v3Endpoint;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsComponent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Camel endpoint class for ITI-55 XCPD Responding Gateway.
 * @author Dmytro Rud
 */
public class Iti55Endpoint extends Hl7v3Endpoint<Hl7v3WsTransactionConfiguration> {
    private ExecutorService deferredResponseExecutorService;

    public Iti55Endpoint(
            String endpointUri,
            String address,
            AbstractWsComponent<Hl7v3WsTransactionConfiguration> component,
            InterceptorProvider customInterceptors)
    {
        super(endpointUri, address, component, customInterceptors);
    }


    /**
     * Returns the executor service which is used for handling the "Deferred Response"
     * option.  When no instance has been configured using the corresponding
     * {@link #setDeferredResponseExecutorService(ExecutorService) setter},
     * a default one will be returned (fixed thread pool with max. 5 threads).
     *
     * @return
     *      executor service instance.
     */
    public synchronized ExecutorService getDeferredResponseExecutorService() {
        if (deferredResponseExecutorService == null) {
            deferredResponseExecutorService = Executors.newFixedThreadPool(5);
        }
        return deferredResponseExecutorService;
    }


    /**
     * Configures the executor service which is used for handling
     * the "Deferred Response" option.
     *
     * @param deferredResponseExecutorService
     *      executor service instance.
     */
    public void setDeferredResponseExecutorService(ExecutorService deferredResponseExecutorService) {
        this.deferredResponseExecutorService = deferredResponseExecutorService;
    }
}
