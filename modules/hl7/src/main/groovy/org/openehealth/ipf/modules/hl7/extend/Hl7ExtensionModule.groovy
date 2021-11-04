/*
 * Copyright 2013 the original author or authors.
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

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.validation.builder.EncodingRuleBuilder
import ca.uhn.hl7v2.validation.builder.MessageRuleBuilder
import ca.uhn.hl7v2.validation.builder.PrimitiveRuleBuilder
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.commons.map.extend.MappingExtensionHelper
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.validation.model.AbstractSyntaxRule
import org.openehealth.ipf.modules.hl7.validation.model.ClosureEncodingRule
import org.openehealth.ipf.modules.hl7.validation.model.ClosureMessageRule
import org.openehealth.ipf.modules.hl7.validation.model.ClosurePrimitiveTypeRule


/**
 * Adds HL7 extensions for Groovy
 * @DSL
 */
class Hl7ExtensionModule {

    // ----------------------------------------------------------------
    //  Extensions to Collection for mapping values
    //  (depend on mapping service)
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object map(Collection delegate, Object key) {
        mappingService()?.get(key, normalizeCollection(delegate))
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
	 static Object map(Collection delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, normalizeCollection(delegate), defaultValue)
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object mapReverse(Collection delegate, Object value) {
        mappingService()?.getKey(value, normalizeCollection(delegate))
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object mapReverse(Collection delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, normalizeCollection(delegate), defaultValue)
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    static Object methodMissing(Collection delegate, String name, Object args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), normalizeCollection, name, args)
    }

    // ----------------------------------------------------------------
    //  Extensions to HAPI Types
    //  (depend on mapping service)
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object map(Type delegate, Object key) {
        mappingService()?.get(key, delegate.encode())
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object map(Type delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, delegate.encode(), defaultValue)
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object mapReverse(Type delegate, Object value) {
        mappingService()?.getKey(value, delegate.encode())
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Object mapReverse(Type delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, delegate.encode(), defaultValue)
    }

    static Object methodMissing(Type delegate, String name, Object args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), { delegate.encode() }, name, args)
    }
        
    // ----------------------------------------------------------------
    //  Extensions to HAPI messages
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static boolean matches(Message delegate, String type, String event, String version) {
        (type == '*' || type == MessageUtils.eventType(delegate)) &&
        (event == '*' || event == MessageUtils.triggerEvent(delegate)) &&
        (version == '*' || version == delegate.version)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String getEventType(Message delegate) {
        MessageUtils.eventType(delegate)
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String getTriggerEvent(Message delegate) {
        MessageUtils.triggerEvent(delegate)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String getMessageStructure(Message delegate) {
        MessageUtils.messageStructure(delegate)
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static Message respond(AbstractMessage delegate, String eventType, String triggerEvent) {
        MessageUtils.response(delegate, eventType, triggerEvent)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating, index)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating)
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating, index)
    }

    // ----------------------------------------------------------------
    //  Extensions to HAPI 2.2 validators for Abstract Syntax and
    //  closure validation
    // ----------------------------------------------------------------

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    static MessageRuleBuilder abstractSyntax(MessageRuleBuilder delegate, Object... args) {
        delegate.test(new AbstractSyntaxRule(args))
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    static MessageRuleBuilder checkIf(MessageRuleBuilder delegate, Closure closure) {
        delegate.test(new ClosureMessageRule(closure))
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    static PrimitiveRuleBuilder checkIf(PrimitiveRuleBuilder delegate, Closure closure) {
        delegate.test(new ClosurePrimitiveTypeRule(closure))
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    static EncodingRuleBuilder checkIf(EncodingRuleBuilder delegate, Closure closure) {
        delegate.test(delegate, new ClosureEncodingRule(closure))
    }


    private static MappingService mappingService() {
        ContextFacade.getBean(MappingService)
    }

    private static def normalizeCollection = { Collection c ->
        c.collect { it instanceof Type ? it.encode() : it.toString() }
    }

}