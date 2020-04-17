/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.core.config;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A simple registry implementation that can e.g. be used in tests to
 * abstract away more complex bean registries like Spring. This is
 * sometimes easier to use than mocking the requests to individual
 * methods.
 * Note that no synchronization is done for adding beans or clearing
 * the registry.
 */
public class SimpleRegistry implements Registry {

    private Map<String, Object> beans = new HashMap<>();

    @Override
    public Object bean(String name) {
        return beans.get(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T bean(Class<T> requiredType) {
        for (Object value : beans.values()) {
            if (requiredType.isAssignableFrom(value.getClass())) return (T) value;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> beans(Class<T> requiredType) {
        Map<String, T> result = beans.entrySet().stream()
                .filter(entry -> requiredType.isAssignableFrom(entry.getValue().getClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, p -> (T) p.getValue()));
        return result;
    }

    public Object register(String name, Object object) {
        return beans.put(name, object);
    }

    public void clear() {
        beans.clear();
    }
}
