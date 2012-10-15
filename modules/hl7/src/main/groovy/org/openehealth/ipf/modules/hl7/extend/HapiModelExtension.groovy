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

import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.commons.map.extend.MappingExtensionHelper
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory

import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.ModelClassFactory

/**
 * Adds a couple of methods to common HAPI model classes. This makes HAPI
 * features accessible via the HL7 DSL, which only works with adapters of
 * the model classes.
 * 
 * @author Christian Ohr
 * @author Martin Krasser
 */
class HapiModelExtension {


	def extensions = {

		// ----------------------------------------------------------------
		//  Extensions to Collection for mapping values
		//  (depend on mapping service)
		// ----------------------------------------------------------------

		Collection.metaClass.map = {
			Hl7ExtensionModule.map(delegate, it)
		}

		Collection.metaClass.map = { mappingKey, defaultValue ->
			Hl7ExtensionModule.map(delegate, mappingKey, defaultValue)
		}

		Collection.metaClass.mapReverse = {
			Hl7ExtensionModule.mapReverse(delegate, it)
		}

		Collection.metaClass.mapReverse = { mappingKey, defaultValue ->
			Hl7ExtensionModule.mapReverse(delegate, mappingKey, defaultValue)
		}

		Collection.metaClass.methodMissing { String name, args ->
            Hl7ExtensionModule.methodMissing(delegate, name, args)
		}

		// ----------------------------------------------------------------
		//  Extensions to HAPI Primitives
		//  (depend on mapping service)
		// ----------------------------------------------------------------

		Type.metaClass.map = {
			Hl7ExtensionModule.map(delegate, it)
		}

		Type.metaClass.map = { mappingKey, defaultValue ->
            Hl7ExtensionModule.map(delegate, mappingKey, defaultValue)
		}

		Type.metaClass.mapReverse = {
            Hl7ExtensionModule.mapReverse(delegate, it)
		}

		Type.metaClass.mapReverse = { mappingKey, defaultValue ->
            Hl7ExtensionModule.mapReverse(delegate, mappingKey, defaultValue)
		}

		Type.metaClass.methodMissing = {String name, args ->
            Hl7ExtensionModule.methodMissing(delegate, name, args)
        }

		// ----------------------------------------------------------------
		//  Extensions to HAPI messages
		// ----------------------------------------------------------------

		Message.metaClass.matches = { String type, String event, String version ->
			Hl7ExtensionModule.matches(delegate, type, event, version)
		}

		Message.metaClass.getEventType = { ->
			Hl7ExtensionModule.getEventType(delegate)
		}

		Message.metaClass.getTriggerEvent = { ->
			Hl7ExtensionModule.getTriggerEvent(delegate)
		}

		Message.metaClass.getMessageStructure = { ->
			Hl7ExtensionModule.getMessageStructure(delegate)
		}
        
		Message.metaClass.'static'.methodMissing = { String name, args ->
            Hl7StaticExtensionModule.methodMissing(delegate, name, args)
		}

		/**
		 * Positive acknowledgement for the message. The MessageAdapter
		 * is populated with the same Parser instance.
		 **/
		AbstractMessage.metaClass.respond =  { String eventType, String triggerEvent ->
			Hl7ExtensionModule.respond(delegate, eventType, triggerEvent)
		}

		/**
		 * Positive acknowledgement for the message. The MessageAdapter
		 * is populated with the same Parser instance.
		 **/
		AbstractMessage.metaClass.ack =  {
			Hl7ExtensionModule.ack(delegate)
		}

		/**
		 * Negative acknowledgement for the underlying message. The MessageAdapter
		 * is populated with the same Parser instance.
		 **/
		AbstractMessage.metaClass.nak = { String cause, AckTypeCode ackTypeCode ->
			Hl7ExtensionModule.nak(delegate, cause, ackTypeCode)
		}

		/**
		 * Negative acknowledgement for the underlying message. The MessageAdapter
		 * is populated with the same Parser instance.
		 **/
		AbstractMessage.metaClass.nak = { AbstractHL7v2Exception e, AckTypeCode ackTypeCode ->
			Hl7ExtensionModule.nak(delegate, e, ackTypeCode)
		}

		/**
		 * Negative acknowledgement from scratch. The MessageAdapter
		 * is populated with the same Parser instance.
		 **/
		AbstractMessage.metaClass.'static'.defaultNak = { AbstractHL7v2Exception e, AckTypeCode ackTypeCode, String version  ->
			Hl7StaticExtensionModule.defaultNak(delegate, e, ackTypeCode, version)
		}

		/**
		 * Dumps the message for debugging purposes using a 
		 * hierarchical structure
		 **/
		Message.metaClass.dump = { ->
			Hl7ExtensionModule.dump(delegate)
		}

		/**
		 * Validates a message against a validation context 
		 **/
		Message.metaClass.validate = { validationContext ->
			Hl7ExtensionModule.validate(delegate, validationContext)
		}

		AbstractMessage.metaClass.addSegment = { String name, boolean required, boolean repeating ->
            Hl7ExtensionModule.addSegment(delegate, name, required, repeating)
		}

		AbstractMessage.metaClass.addSegment = { String name, boolean required, boolean repeating, int index ->
            Hl7ExtensionModule.addSegment(delegate, name, required, repeating, index)
		}

		AbstractMessage.metaClass.addGroup = { String name, boolean required, boolean repeating ->
            Hl7ExtensionModule.addGroup(delegate, name, required, repeating)
		}

		AbstractMessage.metaClass.addGroup = { String name, boolean required, boolean repeating, int index ->
            Hl7ExtensionModule.addGroup(delegate, name, required, repeating, index)
		}

		// ----------------------------------------------------------------
		//  Extensions to HAPI Segments
		// ----------------------------------------------------------------

		Segment.metaClass.'static'.methodMissing = { String segmentName, args ->
            Hl7StaticExtensionModule.methodMissing(delegate, segmentName, args)
		}

		// ----------------------------------------------------------------
		//  Extensions to HAPI Composites
		// ----------------------------------------------------------------

		Composite.metaClass.'static'.methodMissing = { String compositeName, args ->
            Hl7StaticExtensionModule.methodMissing(delegate, compositeName, args)
		}

		// ----------------------------------------------------------------
		//  Extensions to HAPI Primitives
		// ----------------------------------------------------------------

		Primitive.metaClass.'static'.methodMissing = { String primitiveName, args ->
            Hl7StaticExtensionModule.methodMissing(delegate, primitiveName, args)
		}

	}

}
