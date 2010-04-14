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
package org.openehealth.ipf.commons.core.purgeable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Basis class for special map whose items can be purged on the basis 
 * of their creation time.
 * <p>
 * This class is <i>not</i> intended to implement all methods of {@link Map}.
 * 
 * @author Dmytro Rud
 */
abstract public class PurgeableMap<K, T extends PurgeableObject> implements PurgeableCollection {

    private final Map<K, T> map = Collections.synchronizedMap(new HashMap<K, T>());

    protected T get(K key) {
        return map.get(key);
    }

    protected void put(K key, T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must be not null");
        }
        map.put(key, value);
    }

    protected T remove(K key) {
        return map.remove(key);
    }
    
    public synchronized void purge(long ttl) {
        long current = System.currentTimeMillis();
        Object[] keys = map.keySet().toArray();
        for (Object key : keys) {
            T item = map.get(key);
            if ((item != null) && (current - item.getCreationTimestamp() > ttl)) {
                map.remove(key);
            }
        }
    }
}
