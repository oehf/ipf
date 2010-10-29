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
import ca.uhn.hl7v2.model.primitive.CommonTS
import ca.uhn.hl7v2.parser.ModelClassFactory

import org.codehaus.groovy.runtime.InvokerHelper

import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.commons.map.extend.MappingExtension
import org.openehealth.ipf.commons.map.extend.MappingExtensionHelper
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory

/**
 * Adds a couple of methods to common HAPI model classes. This makes HAPI
 * features accessible via the HL7 DSL, which only works with adapters of
 * the model classes.
 * 
 * @author Christian Ohr
 * @author Martin Krasser
 */
class HapiModelExtension {

     ModelClassFactory factory = new CustomModelClassFactory()    
     
    MappingService mappingService
    
	def extensions = {

        // ----------------------------------------------------------------
        //  Extensions to Collection for mapping values
        //  (depend on mapping service)
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

        Collection.metaClass.methodMissing = MappingExtensionHelper.methodMissingLogic.curry(mappingService, normalizeCollection)
            
        // ----------------------------------------------------------------
        //  Extensions to HAPI Primitives
        //  (depend on mapping service)
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
        
        Type.metaClass.methodMissing = MappingExtensionHelper.methodMissingLogic.curry(mappingService, {it.encode()}) 

        // ################################################################
        //
        //  TODO: refactor to extension methods
        //
        // ################################################################
        
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
		
        Message.metaClass.getMessageStructure = { ->
			MessageUtils.messageStructure(delegate)    				    
        }		
        
        Message.metaClass.'static'.methodMissing = { String name, args ->
            MessageUtils.newMessage(factory, name, args[0])
        }        

	    /**
	     * Positive acknowledgement for the message. The MessageAdapter
	     * is populated with the same Parser instance.
	     **/
	     Message.metaClass.respond =  { String eventType, String triggerEvent ->
             MessageUtils.response(factory, delegate, eventType, triggerEvent)
	     }
        
	    /**
	     * Positive acknowledgement for the message. The MessageAdapter
	     * is populated with the same Parser instance.
	     **/
	     Message.metaClass.ack =  {
	         MessageUtils.ack(factory, delegate)
	     }

	    /**
	     * Negative acknowledgement for the underlying message. The MessageAdapter
	     * is populated with the same Parser instance.
	     **/
	    Message.metaClass.nak = { String cause, AckTypeCode ackTypeCode -> 
            MessageUtils.nak(factory, delegate, cause, ackTypeCode)
	    }
		
	    /**
	     * Negative acknowledgement for the underlying message. The MessageAdapter
	     * is populated with the same Parser instance.
	     **/
	    Message.metaClass.nak = { AbstractHL7v2Exception e, AckTypeCode ackTypeCode ->
            MessageUtils.nak(factory, delegate, e, ackTypeCode)
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
		
		Message.metaClass.addSegment = { String name, boolean required, boolean repeating ->
			delegate.add(delegate.modelClassFactory.getSegmentClass(name, getVersion()), required, repeating)
        }
		
		Message.metaClass.addSegment = { String name, boolean required, boolean repeating, int index ->
			delegate.add(delegate.modelClassFactory.getSegmentClass(name, getVersion()), required, repeating, index)
		}

		Message.metaClass.addGroup = { String name, boolean required, boolean repeating ->
			delegate.add(delegate.modelClassFactory.getGroupClass(name, getVersion()), required, repeating)
		}

		Message.metaClass.addGroup = { String name, boolean required, boolean repeating, int index ->
			delegate.add(delegate.modelClassFactory.getGroupClass(name, getVersion()), required, repeating, index)
		}
		
        // ----------------------------------------------------------------
        //  Extensions to HAPI Segments
        // ----------------------------------------------------------------

        Segment.metaClass.'static'.methodMissing = { String segmentName, args ->
            MessageUtils.newSegment(factory, segmentName, args[0])	                
        }
        
        // ----------------------------------------------------------------
        //  Extensions to HAPI Composites
        // ----------------------------------------------------------------

        Composite.metaClass.'static'.methodMissing = { String compositeName, args ->
            if (args.size() > 1 && args[1] instanceof Map) {
                MessageUtils.newComposite(factory, compositeName, args[0], args[1])
            } else {
                MessageUtils.newComposite(factory, compositeName, args[0], null)	                
            }
        }

        Type.metaClass.encode = { ->
        	MessageUtils.pipeEncode(delegate)
        }
        
        Structure.metaClass.encode = { ->
    		MessageUtils.pipeEncode(delegate)
	    }

        // ----------------------------------------------------------------
        //  Extensions to HAPI Primitives
        // ----------------------------------------------------------------

        Primitive.metaClass.'static'.methodMissing = { String primitiveName, args ->
            if (args.size() > 1 && args[1] instanceof String) {
                MessageUtils.newPrimitive(factory, primitiveName, args[0], args[1])
            } else {
                MessageUtils.newPrimitive(factory, primitiveName, args[0], null)                    
            }
        }

	}
	
    // ################################################################
    //
    //  Place extension methods here ...
    //
    // ################################################################
     
    private static def normalizeCollection = { Collection c -> 
        c.collect {
            it instanceof Type ? MessageUtils.pipeEncode(it) : it.toString()
        }
    }
    
}
