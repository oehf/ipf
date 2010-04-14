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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openehealth.ipf.commons.core.purgeable.PurgeableObject;

import ca.uhn.hl7v2.model.Message;

/**
 * Chain of interactive continuation fragments of a query's response.
 * <p>
 * Keys correspond to continuation pointers of the fragments; 
 * the key of the first fragment is <code>null</code>. 
 */
public class ContinuationChain extends PurgeableObject {
    private final Map<String, Message> responseMessages = 
        Collections.synchronizedMap(new HashMap<String, Message>());
    
    public void put(String continuationPointer, Message message) {
        responseMessages.put(continuationPointer, message);
    }
    
    public Message get(String continuationPointer) {
        return responseMessages.get(continuationPointer);
    }
}

