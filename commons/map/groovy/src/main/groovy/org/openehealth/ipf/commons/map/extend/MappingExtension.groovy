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
package org.openehealth.ipf.commons.map.extend

import org.openehealth.ipf.commons.map.Mappings

import static org.openehealth.ipf.commons.map.extend.MappingExtensionModule.*

/**
 * Adds a couple of methods to common HAPI model classes. This makes HAPI
 * features accessible via the HL7 DSL, which only works with adapters of
 * the model classes.
 * 
 * @author Christian Ohr
 * @author Martin Krasser
 */
class MappingExtension {

	/**
	 * Kept so that existing Spring configuration which sets this property still works. The
	 * extensions installed below resolve the mappings from the registry themselves, so nothing
	 * reads it.
	 */
	Mappings mappingService

	def extensions = {
			
        // ----------------------------------------------------------------
        //  Extensions to Strings for mapping values
        // ----------------------------------------------------------------

        String.metaClass.firstLower = {
        	firstLower(delegate as String)
        }
        
        String.metaClass.map = { 
        	map(it, delegate as String)
        }

        String.metaClass.map = { String mappingKey, String defaultValue ->
            map(it, delegate as String, defaultValue)
        }

        String.metaClass.mapReverse = {
            mapReverse(it, delegate as String)
        }

        String.metaClass.mapReverse = { String mappingKey, String defaultValue ->
            mapReverse(it, delegate as String, defaultValue)
        }
        
        String.metaClass.keySystem = {
        	keySystem(delegate as String)
        }

        String.metaClass.valueSystem = {
        	valueSystem(delegate as String)
        }

        String.metaClass.keys = { 
        	keys(delegate as String)
        }

        String.metaClass.values = { 
			values(delegate as String)
        }
        
        String.metaClass.hasKey = { 
        	hasKey(delegate as String, it)
        }

        String.metaClass.hasValue = { 
			hasValue(delegate as String, it)
        }

        String.metaClass.methodMissing = { String name, args
            methodMissing(delegate, name, args)
        }
        
	}
	
}
