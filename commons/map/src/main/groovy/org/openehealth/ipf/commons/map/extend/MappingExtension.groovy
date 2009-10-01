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
        	delegate.replaceAll('^.') { it ? it[0].toLowerCase() : it } 
        }
        
        String.metaClass.map = { 
        	mappingService?.get(it, delegate)
        }

        String.metaClass.map = { mappingKey, defaultValue ->
    		mappingService?.get(mappingKey, delegate, defaultValue)
        }

        String.metaClass.mapReverse = {
    		mappingService?.getKey(it, delegate)
        }

        String.metaClass.mapReverse = { mappingKey, defaultValue ->
			mappingService?.getKey(mappingKey, delegate, defaultValue)
        }
        
        String.metaClass.keySystem = {
        		mappingService?.getKeySystem(delegate)
        }

        String.metaClass.valueSystem = {
        		mappingService?.getValueSystem(delegate)
        }

        String.metaClass.keys = { 
        		mappingService?.keys(delegate)
        }

        String.metaClass.values = { 
				mappingService?.values(delegate)
        }
        
        String.metaClass.hasKey = { 
        		mappingService?.keys(delegate)?.contains(it)
        }

        String.metaClass.hasValue = { 
				mappingService?.values(delegate)?.contains(it)
        }

        String.metaClass.methodMissing = MappingExtensionHelper.methodMissingLogic.curry(mappingService, {it})
        
	}
	
}
