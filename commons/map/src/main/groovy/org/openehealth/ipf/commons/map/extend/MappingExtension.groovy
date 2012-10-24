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

import org.codehaus.groovy.runtime.InvokerHelperimport org.openehealth.ipf.commons.map.MappingService

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

	MappingService mappingService

	def extensions = {
			
        // ----------------------------------------------------------------
        //  Extensions to Strings for mapping values
        // ----------------------------------------------------------------

        String.metaClass.firstLower = {
        	firstLower(delegate)
        }
        
        String.metaClass.map = { 
        	map(it, delegate)
        }

        String.metaClass.map = { mappingKey, defaultValue ->
            map(it, delegate, defaultValue)
        }

        String.metaClass.mapReverse = {
            mapReverse(it, delegate)
        }

        String.metaClass.mapReverse = { mappingKey, defaultValue ->
            mapReverse(it, delegate, defaultValue)
        }
        
        String.metaClass.keySystem = {
        	keySystem(delegate)
        }

        String.metaClass.valueSystem = {
        	valueSystem(delegate)
        }

        String.metaClass.keys = { 
        	keys(delegate)
        }

        String.metaClass.values = { 
			values(delegate)
        }
        
        String.metaClass.hasKey = { 
        	hasKey(delegate, it)
        }

        String.metaClass.hasValue = { 
			hasValue(delegate, it)
        }

        String.metaClass.methodMissing = { String name, args
            methodMissing(delegate, name, args)
        }
        
	}
	
}
