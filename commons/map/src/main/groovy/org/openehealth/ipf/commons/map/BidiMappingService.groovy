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

import org.springframework.beans.factory.InitializingBean
import org.springframework.core.io.Resource

/**
 * An simple example of a MappingService implementation, backed by a
 * nested Map structure. It also allows bidrectional mapping, i.e.
 * mapping from the value back to the key (if unique). The mapping
 * is initialized by an external Groovy script, see {@link MappingsBuilder}
 * for details on the syntax. 
 * 
 * @author Christian Ohr
 * @author Martin Krasser
 * @see MappingsBuilder
 *
 */
class BidiMappingService implements MappingService, InitializingBean {

	private static final String KEYSYSTEM  = '_%KEYSYSTEM%_'
	private static final String VALUESYSTEM = '_%VALUESYSTEM%_'
	private static final String ELSE = '_%ELSE%_'
   	private static final String SEPARATOR = '~'
	
	def map = [:]	
    def reverseMap = [:]
	def separator

    boolean ignoreResourceNotFound = false
    List<Resource> resources = []

	
    public BidiMappingService() {
    	this(SEPARATOR)
    }

    public BidiMappingService(String separator) {
    	this.separator = separator
    }

    // bean configuration support
    void setMappingScript(Resource resource) {
        this.resources.add(resource)
    }
    
    // bean configuration support 
    void setMappingScripts(Resource[] resources) {
        this.resources.addAll(resources)
    }

    @Override
    void afterPropertiesSet() {
        addMappingScripts(resources.toArray(new Resource[0]))
    }

    // Read in the mapping definition
    synchronized void addMappingScript(Resource resource) {
        Binding binding = new Binding()
        GroovyShell shell = new GroovyShell(binding);
        evaluateResource(resource, shell, binding)
    }
    
    // Read in several mapping definition
    synchronized void addMappingScripts(Resource[] resources) {
        Binding binding = new Binding()
        GroovyShell shell = new GroovyShell(binding);
        resources.each { resource ->
           evaluateResource(resource, shell, binding)    
        }
    }
    
    private void evaluateResource(Resource resource, GroovyShell shell, Binding binding) {
    	try {
        	shell.evaluate(resource.inputStream)
        	Closure c = binding.mappings
        	def mb = new MappingsBuilder()
        	c?.delegate = this
        	map.putAll(mb.mappings(c))
           	updateReverseMap()   		
        } catch (IOException e) {
            if (! ignoreResourceNotFound) {
                throw e
            }
        } catch (Exception e) {
    		// TODO handle CompilationException (error in script)
    		throw e
    	}
        
    }
     	
 	public Object get(Object mappingKey, Object key){
 		splitKey(retrieve(map, mappingKey, joinKey(key)))
 	}	

 	public Object getKey(Object mappingKey, Object value){
 		splitKey(retrieve(reverseMap, mappingKey, joinKey(value)))
 	}	

 	public Object get(Object mappingKey, Object key, Object defaultValue){
 		def v = splitKey(retrieve(map, mappingKey, joinKey(key)))
 		v ? v :	defaultValue
 	}	

 	public Object getKey(Object mappingKey, Object value, Object defaultValue){
 		checkMappingKey(reverseMap, mappingKey)
 		def v = splitKey(retrieve(reverseMap, mappingKey, joinKey(value)))
 		v ? v : defaultValue
 	}
 	
 	public Object getKeySystem(Object mappingKey){
 		checkMappingKey(map, mappingKey)
 		map[mappingKey][KEYSYSTEM]
 	}	

 	public Object getValueSystem(Object mappingKey){
 		checkMappingKey(map, mappingKey)
 		map[mappingKey][VALUESYSTEM]
 	}	

 	public Set<?> mappingKeys(){
 		map.keySet()
 	}	

 	public Set<?> keys(Object mappingKey){
 		checkMappingKey(map, mappingKey)
 		map[mappingKey].keySet().findAll { !(it.startsWith('_%')) }
 	}
 	
 	public Collection<?> values(Object mappingKey){
 		checkMappingKey(map, mappingKey)
 		map[mappingKey].findAll({ 
          !(it.key.startsWith('_%')) 
        }).values()
 	}
 	
 	private Object retrieve(Map m, Object mappingKey, Object key) {
 		checkMappingKey(m, mappingKey)
 		m[mappingKey].get(key, retrieveElse(m, mappingKey, key))
 	}
 	
 	private Object retrieveElse(Map m, Object mappingKey, Object key) {
 		def elseClause = m[mappingKey][ELSE]
 		if (elseClause instanceof Closure) {
 			return elseClause.call(key)
 		}
 		elseClause
 	}
 	
    private void updateReverseMap() {
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
    
 	private void checkMappingKey(Map map, Object mappingKey) {
 		if (!map[mappingKey])
 			throw new IllegalArgumentException("Unknown key $mappingKey")
 	} 	
    
    private Object joinKey(Object x) {
    	if (x instanceof Collection) {
    		return x.join(separator)
    	} else {
    		return x
    	}
   }
    
    private Object splitKey(Object x) {
    	if (x instanceof String) {
    		def list = x?.split(separator)?.collect { it.toString() }
    		return list?.size() == 1 ? list[0] : list
    	} else {
    		return x
    	}
    }
 }
