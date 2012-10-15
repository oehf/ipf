package org.openehealth.ipf.modules.hl7.extend

import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.commons.map.extend.MappingExtensionHelper
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator;

import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.validation.ValidationContext

class Hl7ExtensionModule {

    // ----------------------------------------------------------------
    //  Extensions to Collection for mapping values
    //  (depend on mapping service)
    // ----------------------------------------------------------------

    static Object map(Collection delegate, Object key) {
        mappingService()?.get(key, normalizeCollection(delegate))
    }

    static Object map(Collection delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, normalizeCollection(delegate), defaultValue)
    }

    static Object mapReverse(Collection delegate, Object value) {
        mappingService()?.getKey(value, normalizeCollection(delegate))
    }

    static Object mapReverse(Collection delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, normalizeCollection(delegate), defaultValue)
    }

    static Object methodMissing(Collection delegate, String name, args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), normalizeCollection, name, args)
    }

    // ----------------------------------------------------------------
    //  Extensions to HAPI Types
    //  (depend on mapping service)
    // ----------------------------------------------------------------

    static Object map(Type delegate, Object key) {
        mappingService()?.get(key, delegate.encode())
    }

    static Object map(Type delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, delegate.encode(), defaultValue)
    }

    static Object mapReverse(Type delegate, Object value) {
        mappingService()?.getKey(value, delegate.encode())
    }

    static Object mapReverse(Type delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, delegate.encode(), defaultValue)
    }

    static Object methodMissing(Type delegate, String name, args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), { delegate.encode() }, name, args)
    }
        
    // ----------------------------------------------------------------
    //  Extensions to HAPI messages
    // ----------------------------------------------------------------

    static boolean matches(Message delegate, String type, String event, String version) {
        (type == '*' || type == MessageUtils.eventType(delegate)) &&
        (event == '*' || event == MessageUtils.triggerEvent(delegate)) &&
        (version == '*' || version == delegate.version)
    }
    
    static String getEventType(Message delegate) {
        MessageUtils.eventType(delegate)
    }

    static String getTriggerEvent(Message delegate) {
        MessageUtils.triggerEvent(delegate)
    }
    
    static String getMessageStructure(Message delegate) {
        MessageUtils.messageStructure(delegate)
    }

    static Message respond(AbstractMessage delegate, String eventType, String triggerEvent) {
        MessageUtils.response(delegate.modelClassFactory, delegate, eventType, triggerEvent)
    }

    static Message ack(AbstractMessage delegate) {
        MessageUtils.ack(delegate.modelClassFactory, delegate)
    }

    static Message nak(AbstractMessage delegate, String cause, AckTypeCode ackTypeCode) {
        MessageUtils.nak(delegate.modelClassFactory, delegate, cause, ackTypeCode)
    }
    
    static Message nak(AbstractMessage delegate, AbstractHL7v2Exception e, AckTypeCode ackTypeCode) {
        MessageUtils.nak(delegate.modelClassFactory, delegate, e, ackTypeCode)
    }

    static Message dump(Message delegate) {
        MessageUtils.dump(delegate)
    }

    static void validate(Message delegate, ValidationContext context) {
        new HL7Validator().validate(delegate, context)
    }
    
    static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating)
    }
    
    static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating, index)
    }
    
    static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating)
    }
    
    static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating, index)
    }

    private static def normalizeCollection = { Collection c ->
        c.collect { it instanceof Type ? it.encode() : it.toString() }
    }

    private static MappingService mappingService() {
        ContextFacade.getBean(MappingService)
    }           

}