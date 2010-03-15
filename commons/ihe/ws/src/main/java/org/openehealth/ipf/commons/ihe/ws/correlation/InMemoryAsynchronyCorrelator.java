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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;

/**
 * Simple in-memory implementation of async message correlator.
 * @author Dmytro Rud
 */
public class InMemoryAsynchronyCorrelator implements AsynchronyCorrelator {
    private final Map<String, Item> map = 
        Collections.synchronizedMap(new HashMap<String, Item>());

    @Override
    public void put(String messageId, String serviceEndpoint, String correlationKey) {
        map.put(messageId, new Item(serviceEndpoint, correlationKey));
    }
    
    @Override
    public String getServiceEndpoint(String messageId) {
        Item item = map.get(messageId);
        return (item != null) ? item.getServiceEndpoint() : null;
    }
    
    @Override
    public String getCorrelationKey(String messageId) {
        Item item = map.get(messageId);
        return (item != null) ? item.getCorrelationKey() : null;
    }

    @Override
    public void purge(long timestamp) {
        synchronized (map) {
            for (String messageId : map.keySet()) {
                Item item = map.get(messageId);
                if ((item != null) && (item.getCreationTimestamp() < timestamp)) {
                    map.remove(messageId);
                }
            }
        }
    }
    

    private static class Item {
        private final String serviceEndpoint;
        private final String correlationKey;
        private final long creationTimestamp;
        
        public Item(String serviceEndpoint, String correlationKey) {
            Validate.notEmpty(serviceEndpoint);
            
            this.serviceEndpoint = serviceEndpoint;
            this.correlationKey = correlationKey;
            this.creationTimestamp = System.currentTimeMillis();
        }

        public String getServiceEndpoint() {
            return serviceEndpoint;
        }

        public String getCorrelationKey() {
            return correlationKey;
        }

        public long getCreationTimestamp() {
            return creationTimestamp;
        }
    }
}
