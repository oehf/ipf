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
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.commons.map.extend.MappingExtension
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Adds a couple of methods to common HAPI model classes. This makes HAPI
 * features accessible via the HL7 DSL, which only works with adapters of
 * the model classes.
 * 
 * @author Christian Ohr
 * @author Martin Krasser
 */
class HapiModelExtension {

    MappingService mappingService
     
	def extensions = {

        // ----------------------------------------------------------------
        //  Extensions to Collection for mapping values
        // ----------------------------------------------------------------
        
        Collection.metaClass.map = {
                mappingService?.get(it, normalizeCollection(delegate))                  
        }

        Collection.metaClass.map = { mappingKey, defaultValue ->
                mappingService?.get(mappingKey, normalizeCollection(delegate), defaultValue)                    
        }
        
        Collection.metaClass.mapReverse = { 
            mappingService?.getKey(it, normalizeCollection(delegate))                   
        }

        Collection.metaClass.mapReverse = { mappingKey, defaultValue ->
            mappingService?.getKey(mappingKey, normalizeCollection(delegate), defaultValue)                 
        }           

        Collection.metaClass.methodMissing = MappingExtension.methodMissingLogic.curry(mappingService, normalizeCollection)
            
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
        
        Type.metaClass.methodMissing = MappingExtension.methodMissingLogic.curry(mappingService, {it.encode()}) 

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
	
    static def normalizeCollection = { Collection c -> 
        c.collect {
            it instanceof Type ? MessageUtils.pipeEncode(it) : it.toString()
        }
    }
    
}
