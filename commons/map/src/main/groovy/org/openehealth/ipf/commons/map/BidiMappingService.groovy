/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.map
/**
 * An simple example of a MappingService implementation, backed by a
 * nested Map structure. It also allows bidrectional mapping, i.e.
 * mapping from the value back to the key (if unique). The mapping
 * is initialized by an external Groovy script, see {@link MappingsBuilder}
 * for details on the syntax.
 * <p>
 * As of version 3.1, the add/setMappingScript(Resource) methods have been outfactored
 * into the class SpringBidiMappingService located in the ipf-commons-spring module.
 *
 * @author Christian Ohr
 * @author Martin Krasser
 * @see MappingsBuilder
 *
 */
class BidiMappingService implements MappingService {

    protected static final String KEYSYSTEM = '_%KEYSYSTEM%_'
    protected static final String VALUESYSTEM = '_%VALUESYSTEM%_'
    protected static final String ELSE = '_%ELSE%_'
    protected static final String SEPARATOR = '~'

    Map<Object, Map> map = [:]
    Map reverseMap = [:]
    String separator
    boolean ignoreResourceNotFound = false

    List<URL> scripts = []

    public BidiMappingService() {
        this(SEPARATOR)
    }

    public BidiMappingService(String separator) {
        this.separator = separator
    }

    synchronized void setMappingScript(URL script) {
        this.scripts.add(script)
        evaluateMappingScript(script)
    }

    synchronized void setMappingScripts(URL[] scripts) {
        this.scripts.addAll(scripts)
        evaluateMappingScripts(scripts)
    }

    /**
     * @deprecated use #setMappingScript
     */
    void addMappingScript(URL script) {
        evaluateMappingScript(script)
    }

    /**
     * @deprecated use #setMappingScript
     */
    void addMappingScripts(URL[] scripts) {
        evaluateMappingScripts(scripts)
    }

    void clearMappings() {
        scripts = []
        map.clear()
        reverseMap.clear()
    }

    private void evaluateMappingScript(URL script) {
        Binding binding = new Binding()
        GroovyShell shell = new GroovyShell(binding);
        evaluateResource(script, shell, binding)
    }

    private void evaluateMappingScripts(URL[] scripts) {
        Binding binding = new Binding()
        GroovyShell shell = new GroovyShell(binding);
        scripts.each { script ->
            evaluateResource(script, shell, binding)
        }
    }

    private void evaluateResource(URL url, GroovyShell shell, Binding binding) {
        shell.evaluate(url.toURI())
        Closure c = binding.mappings
        def mb = new MappingsBuilder()
        c?.delegate = this
        map.putAll(mb.mappings(c))
        updateReverseMap()
    }

    @Override
    public Object get(Object mappingKey, Object key) {
        splitKey(retrieve(map, mappingKey, joinKey(key)))
    }

    @Override
    public Object getKey(Object mappingKey, Object value) {
        splitKey(retrieve(reverseMap, mappingKey, joinKey(value)))
    }

    @Override
    public Object get(Object mappingKey, Object key, Object defaultValue) {
        def v = splitKey(retrieve(map, mappingKey, joinKey(key)))
        v ? v : defaultValue
    }

    @Override
    public Object getKey(Object mappingKey, Object value, Object defaultValue) {
        checkMappingKey(reverseMap, mappingKey)
        def v = splitKey(retrieve(reverseMap, mappingKey, joinKey(value)))
        v ? v : defaultValue
    }

    @Override
    public Object getKeySystem(Object mappingKey) {
        checkMappingKey(map, mappingKey)
        map[mappingKey][KEYSYSTEM]
    }

    @Override
    public Object getValueSystem(Object mappingKey) {
        checkMappingKey(map, mappingKey)
        map[mappingKey][VALUESYSTEM]
    }

    @Override
    public Set<?> mappingKeys() {
        map.keySet()
    }

    @Override
    public Set<?> keys(Object mappingKey) {
        checkMappingKey(map, mappingKey)
        Set<?> result = map[mappingKey].keySet().findAll { !(it.startsWith('_%')) }
        result
    }

    @Override
    public Collection<?> values(Object mappingKey) {
        checkMappingKey(map, mappingKey)
        map[mappingKey].findAll({
            !(it.key.startsWith('_%'))
        }).values()
    }

    protected Object retrieve(Map<Object, Map> m, Object mappingKey, Object key) {
        checkMappingKey(m, mappingKey)
        m[mappingKey][key] ?: retrieveElse(m, mappingKey, key)
    }

    protected Object retrieveElse(Map<Object, Map> m, Object mappingKey, Object key) {
        def elseClause = m[mappingKey][ELSE]
        if (elseClause instanceof Closure) {
            return elseClause.call(key)
        }
        elseClause
    }

    protected void updateReverseMap() {
        def reverseElseKey
        map?.each { mappingKey, m ->
            reverseMap[mappingKey] = [:]
            m.each { key, value ->
                if (key != KEYSYSTEM && key != VALUESYSTEM) {
                    if (key != ELSE) {
                        if (value != ELSE) {
                            reverseMap[mappingKey][joinKey(value)] = key
                        } else {
                            reverseMap[mappingKey][ELSE] = key
                            reverseElseKey = key
                        }
                    }
                }
            }
            if (reverseElseKey) {
                m.remove(reverseElseKey)
                reverseElseKey = null
            }
        }
    }

    protected void checkMappingKey(Map<Object, Map> map, Object mappingKey) {
        if (!map[mappingKey])
            throw new IllegalArgumentException("Unknown key $mappingKey")
    }

    protected Object joinKey(Object x) {
        if (x instanceof Collection) {
            return x.join(separator)
        } else {
            return x
        }
    }

    protected Object splitKey(Object x) {
        if (x instanceof String) {
            def list = x?.split(separator)?.collect { it.toString() }
            return list?.size() == 1 ? list[0] : list
        } else {
            return x
        }
    }

    private URL getURL(String location) throws IOException {
        URL url;
        if (location.startsWith("/")) {
            url = getClass().getResource(location);
        } else {
            try {
                // Try to parse the location as a URL...
                url = new URL(location);
            } catch (MalformedURLException ex) {
                // No URL -> resolve as resource path.
                url = getClass().getClassLoader().getResource(location);
            }
        }
        return url
    }
}
