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

import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.keyString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uhn.hl7v2.model.Message;

/**
 * A HL7 v2 interactive continuation storage which stores all fragments in the memory.
 * @author Dmytro Rud
 */
public class InMemoryContinuationStorage implements ContinuationStorage {
    private static final transient Log LOG = LogFactory.getLog(InMemoryContinuationStorage.class);

    // Maps QPD-2 + MSH-3 to a chain of fragments.
    private final Map<String, Chain> chains = 
        Collections.synchronizedMap(new HashMap<String, Chain>(100));
    
    
    @Override
    public Message getFragment(
            String continuationPointer, 
            String queryTag,
            String msh31, 
            String msh32, 
            String msh33) 
    {
        String storageKey = keyString(queryTag, msh31, msh32, msh33);
        Chain chain = chains.get(storageKey);
        return (chain != null) ? chain.get(continuationPointer) : null;
    }


    @Override
    public void putFragment(
            String continuationPointer, 
            String queryTag,
            String msh31, 
            String msh32, 
            String msh33, 
            Message fragment) 
    {
        String storageKey = keyString(queryTag, msh31, msh32, msh33);
        Chain chain = chains.get(storageKey);
        if (chain == null) {
            LOG.debug("Create chain for query tag " + queryTag);
            chain = new Chain();
            chains.put(storageKey, chain);
        }
        chain.put(continuationPointer, fragment);
    }


    @Override
    public boolean deleteFragments(String queryTag, String msh31, String msh32, String msh33) {
        String storageKey = keyString(queryTag, msh31, msh32, msh33);
        return chains.remove(storageKey) != null;
    }

    
    @Override
    public void purge(long timestamp) {
        for (String storageKey : chains.keySet()) {
            Chain chain = chains.get(storageKey);
            if ((chain != null) && (chain.getCreationTimestamp() < timestamp)) {
                chains.remove(storageKey);
            }
        }
    }

    
    /**
     * Chain of response fragments for a query.
     * <p>
     * Keys correspond to continuation pointers of the fragments (MSH-14, if any); 
     * the key of the first fragment is <code>null</code>. 
     */
    private static class Chain {
        private final long creationTimestamp = System.currentTimeMillis();
        private final Map<String, Message> responseMessages = 
            Collections.synchronizedMap(new HashMap<String, Message>());
        
        public void put(String continuationPointer, Message message) {
            responseMessages.put(continuationPointer, message);
        }
        
        public Message get(String continuationPointer) {
            return responseMessages.get(continuationPointer);
        }

        public long getCreationTimestamp() {
            return creationTimestamp;
        }
    }

}
