/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2.storage;

import ca.uhn.hl7v2.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.Cache;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * An JCache-based implementation of {@link InteractiveContinuationStorage}.
 *
 * @author Dmytro Rud
 */
public class JCacheInteractiveContinuationStorage implements InteractiveContinuationStorage {

    private static final Logger log = LoggerFactory.getLogger(JCacheInteractiveContinuationStorage.class);
    private final Cache<String, InteractiveContinuationChain> cache;


    public JCacheInteractiveContinuationStorage(Cache<String, InteractiveContinuationChain> ehcache) {
        requireNonNull(ehcache);
        this.cache = ehcache;
    }


    @Override
    public void put(String continuationPointer, String chainId, Message fragment) {
        var chain = cache.get(chainId);
        if (chain == null) {
            log.debug("Create chain for storage key {}", chainId);
            chain = new InteractiveContinuationChain();
            cache.put(chainId, chain);
        }
        chain.put(continuationPointer, fragment);
    }


    @Override
    public Message get(
            String continuationPointer,
            String chainId)
    {
        var chain = cache.get(chainId);
        if (chain != null) {
            return chain.get(continuationPointer);
        }
        return null;
    }


    @Override
    public boolean delete(String chainId) {
        return cache.remove(chainId);
    }


    
    /**
     * Chain of interactive continuation fragments of a query's response.
     * <p>
     * Keys correspond to continuation pointers of the fragments;
     * the key of the first fragment is <code>null</code>.
     */
    public static class InteractiveContinuationChain implements Serializable {
        private final Map<String, Message> responseMessages =
            Collections.synchronizedMap(new HashMap<>());

        public void put(String continuationPointer, Message message) {
            responseMessages.put(continuationPointer, message);
        }

        public Message get(String continuationPointer) {
            return responseMessages.get(continuationPointer);
        }
    }

}
