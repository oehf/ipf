/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.commons.xml;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * Abstract parent class for XML validators, transmogrifiers, and other classes
 * which cache static external resources in memory.
 * <br>
 * On the basis of validator/transmogrifier parameters (<code>Object... params</code>),
 * instances of this class determine:
 * <ul>
 * <li>Location of the raw resource (i.e. an unparsed XSLT document as opposed
 * to its compiled representation in memory) &mdash; method {@link #resourceLocation(Object...)},
 * <li>Parameters of resource instantiation (compilation) and execution (application) &mdash;
 * method {@link #resourceParameters(Object...)},
 * <li>Key to store the compiled resource in the cache &mdash;
 * method {@link #resourceCacheKey(Object...)}.
 * </ul>
 * The main method to get a ready-to-use resource instance is {@link #resource(Object...)},
 * which takes over instantiation, initialization, caching, and other aspects of resource
 * lifecycle.  For resource instantiation step, it calls the {@link #createResource(Object...)}
 * method, which is truly resource-type specific and thus declared as <code>abstract</code>
 * in the given base class.
 *
 * @param <T> resource type.
 */
abstract public class AbstractCachingXmlProcessor<T> {

    public static final String RESOURCE_LOCATION = "org.openehealth.ipf.commons.xml.ResourceLocation";

    private final ClassLoader classLoader;

    protected static abstract class Loader<S> {

        private S loaded;

        abstract protected S load() throws RuntimeException;

        synchronized S get() {
            if (loaded == null) loaded = load();
            return loaded;
        }
    }

    /**
     * Constructor.
     *
     * @param classLoader class loader, may be <code>null</code>.
     */
    protected AbstractCachingXmlProcessor(ClassLoader classLoader) {
        super();
        this.classLoader = classLoader == null ? this.getClass().getClassLoader() : classLoader;
    }

    /**
     * @return static cache for the configured resource type.
     * Note that the returned Map is not necessarily synchronized.
     */
    abstract protected ConcurrentMap<String, Loader<T>> getCache();

    /**
     * Extracts (constructs) resource location from validator/transmogrifier parameters.
     *
     * @param params validator/transmogrifier parameters.
     * @return resource location as a String.
     */
    protected String resourceLocation(Object... params) {
        if (params[0] instanceof String) {
            return (String) params[0];
        } else if (params[0] instanceof Map) {
            return (String) ((Map<?, ?>) params[0]).get(RESOURCE_LOCATION);
        } else if (params[0] instanceof SchematronProfile) {
            SchematronProfile p = (SchematronProfile) params[0];
            return p.getRules();
        }
        throw new IllegalStateException("Cannot extract resource location");
    }

    /**
     * Extracts (constructs) resource cache key validator/transmogrifier parameters.
     * <p/>
     * Per default, the key equals to the resource location.
     *
     * @param params validator/transmogrifier parameters.
     * @return cache key as a String.
     */
    protected String resourceCacheKey(Object... params) {
        return resourceLocation(params);
    }

    /**
     * Extracts (constructs) resource creation/application parameters from
     * validator/transmogrifier parameters.
     *
     * @param params validator/transmogrifier parameters.
     * @return resource creation/application parameters as a Map,
     * or <code>null</code> if not found.
     */
    protected Map<String, Object> resourceParameters(Object... params) {
        if (params[0] instanceof Map) {
            return (Map<String, Object>) params[0];
        } else if (params[0] instanceof SchematronProfile) {
            SchematronProfile p = (SchematronProfile) params[0];
            return p.getParameters();
        } else if (params.length > 1 && params[1] instanceof Map) {
            return (Map<String, Object>) params[1];
        }
        return null;
    }

    /**
     * Retrieves from the cache and returns the resource with the given location and
     * further attributes.  If necessary, creates the resource by means of
     * {@link #createResource(Object...)} and stores it into the cache.
     * The cache key is a combination of the location and further attributes.
     * <p/>
     * This method MUST be re-entrant, its result MUST be thread-safe.
     *
     * @param params validator/transmogrifier parameters.
     * @return resource instance.
     * @throws Exception
     */
    protected T resource(final Object... params) throws Exception {
        String key = resourceCacheKey(params);
        getCache().putIfAbsent(key, new Loader<T>() {
            @Override
            protected T load() throws RuntimeException {
                return createResource(params);
            }
        });
        return getCache().get(key).get();
    }

    /**
     * Creates a ready-to-use resource (e.g. an XML Schema instance) for the given key.
     * Insertion into the cache will happen externally, this method's purpose
     * is only to instantiate the resource to be cached.
     * <p/>
     * This method does not need to be re-entrant, but its result MUST be thread-safe.
     *
     * @param params validator/transmogrifier parameters.
     * @return resource of the configured type.
     */
    abstract protected T createResource(Object... params);

    /**
     * Loads the resource into memory and returns it as a Stream.
     * <p/>
     * This method does not need to be re-entrant.
     *
     * @param params validator/transmogrifier parameters.
     * @return resource of the configured type.
     */
    protected StreamSource resourceContent(Object... params) {
        String location = resourceLocation(params);
        URL url;
        try {
            if (location.startsWith("/")) {
                url = getClass().getResource(location);
            } else {
                try {
                    // Try to parse the location as a URL...
                    url = new URL(location);
                } catch (MalformedURLException ex) {
                    // No URL -> resolve as resource path.
                    url = getClass().getClassLoader().getResource(location);
                    if (url == null) throw new IOException("Location not found");
                }
            }
            return new StreamSource(url.openStream(), url.toExternalForm());
        } catch (IOException e) {
            throw new IllegalArgumentException("The resource " + location + " is not valid", e);
        }
    }

}
