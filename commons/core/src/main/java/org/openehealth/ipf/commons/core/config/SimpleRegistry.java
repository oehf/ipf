package org.openehealth.ipf.commons.core.config;


import java.util.HashMap;
import java.util.Map;

/**
 * A simple registry implementation that can e.g. be used in tests to
 * abstract away more complex bean registries like Spring. This is
 * sometimes easier to use than mocking the requests to individual
 * methods.
 * Note that no synchronization is done for adding beans or clearing
 * the registry.
 */
public class SimpleRegistry implements Registry {

    private Map<String, Object> beans = new HashMap<String, Object>();

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
        Map<String, T> result = new HashMap<String, T>();
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            if (requiredType.isAssignableFrom(entry.getValue().getClass()))
                result.put(entry.getKey(), (T) entry.getValue());
        }
        return result;
    }

    public Object register(String name, Object object) {
        return beans.put(name, object);
    }

    public void clear() {
        beans.clear();
    }
}
