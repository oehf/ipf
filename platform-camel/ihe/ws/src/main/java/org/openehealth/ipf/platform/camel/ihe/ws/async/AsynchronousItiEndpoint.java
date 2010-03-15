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
package org.openehealth.ipf.platform.camel.ihe.ws.async;

import org.apache.camel.Component;
import org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiEndpoint;

/**
 * Camel endpoint for Web Service-based IHE transactions with support
 * for correlation of asynchronous conversations.
 *  
 * @author Dmytro Rud
 */
abstract public class AsynchronousItiEndpoint extends DefaultItiEndpoint {
    private AsynchronyCorrelator correlator = null;

    protected AsynchronousItiEndpoint(String endpointUri, String address, Component component) {
        super(endpointUri, address, component);
    }

    /**
     * Configures the asynchrony correlator for this endpoint.
     */
    public void setCorrelator(AsynchronyCorrelator correlator) {
        this.correlator = correlator;
    }
    
    /**
     * Returns the correlator.
     */
    public AsynchronyCorrelator getCorrelator() {
        return correlator;
    }
}
