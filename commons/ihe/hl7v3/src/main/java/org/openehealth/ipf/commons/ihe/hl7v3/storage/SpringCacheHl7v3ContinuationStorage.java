/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.storage;


import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * Hl7v3ContinuationStorage that uses a Spring cache abstraction
 *
 * @author Christian Ohr
 * @since 3.2
 */
public class SpringCacheHl7v3ContinuationStorage implements Hl7v3ContinuationStorage {

    private static final String INTERACTIVE_CONTINUATION_CACHE = "interactiveHl7v3ContinuationCache";

    private final Cache cache;

    private static final String MESSAGE_SUFFIX = ".message";
    private static final String LAST_RESULT_NUMBER_SUFFIX = ".lastIndex";
    private static final String CONTINUATION_QUANTITY_SUFFIX = ".quantity";

    public SpringCacheHl7v3ContinuationStorage(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(INTERACTIVE_CONTINUATION_CACHE);
    }

    @Override
    public void storeMessage(String key, String message) {
        cache.put(key + MESSAGE_SUFFIX, message);
    }

    @Override
    public String getMessage(String key) {
        return cache.get(key + MESSAGE_SUFFIX, String.class);
    }

    @Override
    public void storeLastResultNumber(String key, int lastResultNumber) {
        cache.put(key + LAST_RESULT_NUMBER_SUFFIX, lastResultNumber);
    }

    @Override
    public int getLastResultNumber(String key) {
        var i = cache.get(key + LAST_RESULT_NUMBER_SUFFIX, Integer.class);
        return (i != null) ? i : -1;
    }

    @Override
    public void storeContinuationQuantity(String key, int continuationQuantity) {
        cache.put(key + CONTINUATION_QUANTITY_SUFFIX, continuationQuantity);
    }

    @Override
    public int getContinuationQuantity(String key) {
        var i = cache.get(key + CONTINUATION_QUANTITY_SUFFIX, Integer.class);
        return (i != null) ? i : -1;
    }

    @Override
    public boolean remove(String key) {
        cache.evict(key + LAST_RESULT_NUMBER_SUFFIX);
        cache.evict(key + CONTINUATION_QUANTITY_SUFFIX);
        if (cache.get(key + MESSAGE_SUFFIX) != null) {
            cache.evict(key + MESSAGE_SUFFIX);
            return true;
        }
        return false;
    }

}

