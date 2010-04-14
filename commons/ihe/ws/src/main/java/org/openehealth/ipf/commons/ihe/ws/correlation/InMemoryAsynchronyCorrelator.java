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

import org.openehealth.ipf.commons.core.purgeable.PurgeableMap;

/**
 * Simple in-memory implementation of asynchronous message correlator.
 * @author Dmytro Rud
 */
public class InMemoryAsynchronyCorrelator 
        extends PurgeableMap<String, AsynchronyCorrelationItem> 
        implements AsynchronyCorrelator 
{
    @Override
    public void put(
            String messageId, 
            String serviceEndpoint, 
            String correlationKey, 
            String requestPayload) 
    {
        put(messageId,new AsynchronyCorrelationItem(
                serviceEndpoint, correlationKey, requestPayload));
    }
    
    @Override
    public String getServiceEndpoint(String messageId) {
        AsynchronyCorrelationItem asynchronyCorrelationItem = get(messageId);
        return (asynchronyCorrelationItem != null) ? 
                asynchronyCorrelationItem.getServiceEndpoint() : null;
    }
    
    @Override
    public String getCorrelationKey(String messageId) {
        AsynchronyCorrelationItem asynchronyCorrelationItem = get(messageId);
        return (asynchronyCorrelationItem != null) ? 
                asynchronyCorrelationItem.getCorrelationKey() : null;
    }

    @Override
    public String getRequestPayload(String messageId) {
        AsynchronyCorrelationItem asynchronyCorrelationItem = get(messageId);
        return (asynchronyCorrelationItem != null) ? 
                asynchronyCorrelationItem.getRequestPayload() : null;
    }

    @Override
    public boolean delete(String messageId) {
        return remove(messageId) != null;
    }
}
