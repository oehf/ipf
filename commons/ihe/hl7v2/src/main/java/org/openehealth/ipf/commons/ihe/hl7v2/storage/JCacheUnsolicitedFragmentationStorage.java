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

import javax.cache.Cache;

import static java.util.Objects.requireNonNull;


/**
 * A storage of HL7 v2 unsolicited fragmentation accumulators.
 * @author Dmytro Rud
 */
public class JCacheUnsolicitedFragmentationStorage implements UnsolicitedFragmentationStorage {

    private final Cache<String, StringBuilder> cache;

    public JCacheUnsolicitedFragmentationStorage(Cache<String, StringBuilder> cache) {
        requireNonNull(cache);
        this.cache = cache;
    }

    @Override
    public void put(String key, StringBuilder accumulator) {
        cache.put(key, accumulator);
    }

    @Override
    public StringBuilder getAndRemove(String key) {
        var value = cache.get(key);
        if (value != null) {
            cache.remove(key);
            return value;
        }
        return null;
    }
}
