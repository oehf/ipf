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
package org.openehealth.ipf.commons.ihe.ws.correlation;

import org.apache.commons.lang.Validate;
import org.openehealth.ipf.commons.core.purgeable.PurgeableObject;

/**
 * @author Dmytro Rud
 */
class AsynchronyCorrelationItem extends PurgeableObject {
    private final String serviceEndpoint;
    private final String correlationKey;
    private String requestPayload;
    
    public AsynchronyCorrelationItem(
            String serviceEndpoint, 
            String correlationKey) 
    {
        Validate.notEmpty(serviceEndpoint);
        
        this.serviceEndpoint = serviceEndpoint;
        this.correlationKey = correlationKey;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public String getCorrelationKey() {
        return correlationKey;
    }

    public String getRequestPayload() {
        return requestPayload;
    }
    
    public void setRequestPayload(String requestPayload) {
        this.requestPayload = requestPayload;
    }
}