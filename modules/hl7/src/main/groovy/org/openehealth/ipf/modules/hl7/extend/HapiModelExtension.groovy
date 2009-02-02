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
package org.openehealth.ipf.modules.hl7.extend

import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.GenericParser
import org.codehaus.groovy.runtime.InvokerHelperimport ca.uhn.hl7v2.model.primitive.CommonTS
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.mappings.MappingService
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Adds a couple of methods to common HAPI model classes. This makes HAPI
 * features accessible via the HL7 DSL, which only works with adapters of
 * the model classes.
 * 
 * @author Christian Ohr
 *
 */
public class HapiModelExtension {

	MappingService mappingService;

    private def normalize(Collection c) {
    	c.collect { 
    		it instanceof Type ?
    				MessageUtils.pipeEncode(it) :
    				it.toString()
    	}
    }
	
	def extensions = {
			
	        // ----------------------------------------------------------------
	        //  Extensions to Strings for mapping HAPI Primitive values
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
	        
	        String.metaClass.methodMissing = { String name, args ->
	        	def result
	        	if (name.startsWith('mapReverse')) {
	        		def key = name.minus('mapReverse').firstLower()
	       			result = InvokerHelper.invokeMethod(mappingService, 'getKey', [key, delegate, *args])
	        	} else if (name.startsWith('map')) {
	        		def key = name.minus('map').firstLower()
	       			result = InvokerHelper.invokeMethod(mappingService, 'get', [key, delegate, *args])
	        	} else {
	        		throw new MissingMethodException(name, delegate.class, args)
	        	}
	        	result
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

	        // ----------------------------------------------------------------
	        //  Extensions to ArrayList for mapping HAPI Primitive values
	        // ----------------------------------------------------------------
	        
	        Collection.metaClass.map = {
	        		mappingService?.get(it, normalize(delegate))	        		
	        }

	        Collection.metaClass.map = { mappingKey, defaultValue ->
	        		mappingService?.get(mappingKey, normalize(delegate), defaultValue)	        		
	        }
	        
	        Collection.metaClass.mapReverse = { 
    			mappingService?.getKey(it, normalize(delegate))	        		
	        }

	        Collection.metaClass.mapReverse = { mappingKey, defaultValue ->
    			mappingService?.getKey(mappingKey, normalize(delegate), defaultValue)	        		
	        }	        

	        Collection.metaClass.methodMissing = { String name, args ->
        		def result
        		if (name.startsWith('mapReverse')) {
        			def key = name.minus('mapReverse').firstLower()
        			result = InvokerHelper.invokeMethod(mappingService, 'getKey', [key, normalize(delegate), *args])
        		} else if (name.startsWith('map')) {
        			def key = name.minus('map').firstLower()
        			result = InvokerHelper.invokeMethod(mappingService, 'get', [key, normalize(delegate), *args])
        		} else {
        			throw new MissingMethodException(name, delegate.class, args)
        		}
        		result
	        }

	        
	        // ----------------------------------------------------------------
	        //  Extensions to HAPI messages
	        // ----------------------------------------------------------------

	        Message.metaClass.matches = { String type, String event, String version ->
				 (type == '*' || type == MessageUtils.eventType(delegate)) && 
				 (event == '*' || event == MessageUtils.triggerEvent(delegate)) && 
				 (version == '*' || version == delegate.version)    				    
			}
	        
	        Message.metaClass.getEventType = { ->
				MessageUtils.eventType(delegate)    				    
	        }
	        
	        Message.metaClass.getTriggerEvent = { ->
				MessageUtils.triggerEvent(delegate)    				    
	        }

		    /**
		     * Positive acknowledgement for the message. The MessageAdapter
		     * is populated with the same Parser instance.
		     **/
		     Message.metaClass.respond =  { String eventType, String triggerEvent ->
		    	 MessageUtils.response(delegate, eventType, triggerEvent)
		     }
	        
		    /**
		     * Positive acknowledgement for the message. The MessageAdapter
		     * is populated with the same Parser instance.
		     **/
		     Message.metaClass.ack =  {
		    	 MessageUtils.ack(delegate)
		    }

		    /**
		     * Negative acknowledgement for the underlying message. The MessageAdapter
		     * is populated with the same Parser instance.
		     **/
		    Message.metaClass.nak = { String cause, AckTypeCode ackTypeCode -> 
		    	 MessageUtils.nak(delegate, cause, ackTypeCode)
		    }
	        
			
		    /**
		     * Negative acknowledgement for the underlying message. The MessageAdapter
		     * is populated with the same Parser instance.
		     **/
		    Message.metaClass.nak = { AbstractHL7v2Exception e, AckTypeCode ackTypeCode ->
	    		MessageUtils.nak(delegate, e, ackTypeCode)
		    }
	        

		    /**
		     * Negative acknowledgement from scratch. The MessageAdapter
		     * is populated with the same Parser instance.
		     **/
	        Message.metaClass.'static'.defaultNak = { AbstractHL7v2Exception e, AckTypeCode ackTypeCode, String version  ->
	        	MessageUtils.defaultNak(e, ackTypeCode, version)
		    }

	        
		    /**
		     * Dumps the message for debugging purposes using a 
		     * hierarchical structure
		     **/
	        Message.metaClass.dump = { ->
        		MessageUtils.dump(delegate)
		     }

		    /**
		     * Validates a message against a validation context 
		     **/
		    Message.metaClass.validate = { validationContext ->
	    		MessageUtils.validate(delegate, validationContext)
		    }
	        
	        // ----------------------------------------------------------------
	        //  Extensions to HAPI Primitives
	        // ----------------------------------------------------------------

	        Type.metaClass.map = { 
	        	mappingService?.get(it, delegate.encode())
	        }
	        
	        Type.metaClass.map = { mappingKey, defaultValue ->
        		mappingService?.get(mappingKey, delegate.encode(), defaultValue)
		    }

	        Type.metaClass.mapReverse = { 
        		mappingService?.getKey(it, delegate.encode())
		     }
        
	        Type.metaClass.mapReverse = { mappingKey, defaultValue ->
    			mappingService?.getKey(mappingKey, delegate.encode(), defaultValue)
		    }

	        
	        Type.metaClass.methodMissing = { String name, args ->
        		def result
        		if (name.startsWith('mapReverse')) {
        			def key = name.minus('mapReverse').firstLower()
        			result = InvokerHelper.invokeMethod(mappingService, 'getKey', [key, delegate.encode(), *args])
        		} else if (name.startsWith('map')) {
        			def key = name.minus('map').firstLower()
        			result = InvokerHelper.invokeMethod(mappingService, 'get', [key, delegate.encode(), *args])
        		} else {
        			throw new MissingMethodException(name, delegate.class, args)
        		}
        		result		     
        	}

	        // ----------------------------------------------------------------
	        //  Extensions to HAPI Composites
	        // ----------------------------------------------------------------

	        Type.metaClass.encode = { ->
	        	MessageUtils.pipeEncode(delegate)
	        }
	        
	        Structure.metaClass.encode = { ->
        		MessageUtils.pipeEncode(delegate)
		    }
	}
	
}
