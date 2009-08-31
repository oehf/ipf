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

import java.util.Map


/**
 * Groovy builder for generating a map suitable for the BidiMappingService.
 * Mapping definitions are always in the following Groovy syntax:
 * 
 * <pre>
 * mappings = {
 *   mappingKey1(['oid of key code system','oid of value code system'],
 *      key1  : 'value1',
 *      key2  : 'value2',
 *      (ELSE): Closure or Value
 *   )
 *   mappingKey2(
 *      ...
 *   )
 *   ...
 *   
 * }
 * <pre>
 * 
 * The list of OIDs is optional. The ELSE element can be either a Closure or a normal Object,
 * the latter is converted to String before it's returned. The closure takes the key as only 
 * parameter, so that 
 * <pre>
 * ...
 * (ELSE) : { it }
 * ...
 * </pre>
 * will return the passed key, and thus an unknown key is mapped to itself.
 * Multivalued key or values are composite Strings separated by a tilde "~".
 * 
 * @author Christian Ohr
 *
 */
class MappingsBuilder extends BuilderSupport {

	private static final String KEYSYSTEM  = '_%KEYSYSTEM%_'
	private static final String VALUESYSTEM = '_%VALUESYSTEM%_'
	private static final String ELSE = '_%ELSE%_'
	
	/* (non-Javadoc)
	 * @see groovy.util.BuilderSupport#setParent(java.lang.Object, java.lang.Object)
	 */
	protected void setParent(Object parent, Object child){
			parent[child['name']] = child[child['name']] ?: child
			child.remove('name')
	}
	
	/* (non-Javadoc)
	 * @see groovy.util.BuilderSupport#createNode(java.lang.Object)
	 */
	 protected Object createNode(Object name){
		if (name != 'mappings') {
		    return createNode(name, null, null)
		} else {
			return [:]
		}
	}
	
	/* (non-Javadoc)
	 * @see groovy.util.BuilderSupport#createNode(java.lang.Object, java.lang.Object)
	 */
	 protected Object createNode(Object name, Object value){
		createNode name, null, value
	}
	
	/* (non-Javadoc)
	 * @see groovy.util.BuilderSupport#createNode(java.lang.Object, java.util.Map)
	 */
	 protected Object createNode(Object name, Map attrs){
		createNode name, attrs, null
	}
	
	/* (non-Javadoc)
	 * @see groovy.util.BuilderSupport#createNode(java.lang.Object, java.util.Map, java.lang.Object)
	 */
	 protected Object createNode(Object name, Map attrs, Object value){
		def node = [name:name]
		if (attrs) {
			node[name] = attrs
		}
		if (value instanceof List) {
			node[name][KEYSYSTEM] = value[0]
			node[name][VALUESYSTEM] = value[1]			
		}
		return node
	}
	
	 protected void nodeCompleted(Object parent, Object node) {
	 }
	 

}
