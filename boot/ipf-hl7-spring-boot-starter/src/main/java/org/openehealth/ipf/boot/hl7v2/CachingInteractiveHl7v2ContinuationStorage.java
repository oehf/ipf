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

package org.openehealth.ipf.boot.hl7v2;

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.InteractiveContinuationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * InteractiveContinuationStorage that uses a Spring cache abstraction
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class CachingInteractiveHl7v2ContinuationStorage implements InteractiveContinuationStorage {

    private static final transient Logger LOG = LoggerFactory.getLogger(CachingInteractiveHl7v2ContinuationStorage.class);
    private static final String INTERACTIVE_CONTINUATION_CACHE = "interactiveHl7v2ContinuationCache";
    private final Cache cache;

    public CachingInteractiveHl7v2ContinuationStorage(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(INTERACTIVE_CONTINUATION_CACHE);
    }

    @Override
    public void put(String continuationPointer, String chainId, Message fragment) {
        InteractiveContinuationChain chain = cache.get(chainId, InteractiveContinuationChain.class);
        if (chain == null) {
            LOG.debug("Create chain for storage key {}", chainId);
            chain = new InteractiveContinuationChain();
            cache.put(chainId, chain);
        }
        chain.put(continuationPointer, fragment);
    }

    @Override
    public Message get(String continuationPointer, String chainId) {
        InteractiveContinuationChain chain = cache.get(chainId, InteractiveContinuationChain.class);
        if (chain != null) {
            return chain.get(continuationPointer);
        }
        return null;
    }

    @Override
    public boolean delete(String chainId) {
        if (cache.get(chainId) != null) {
            cache.evict(chainId);
            return true;
        }
        return false;
    }

    /**
     * Chain of interactive continuation fragments of a query's response.
     * <p>
     * Keys correspond to continuation pointers of the fragments;
     * the key of the first fragment is <code>null</code>.
     */
    private static class InteractiveContinuationChain implements Serializable {
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
