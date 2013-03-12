package org.openehealth.ipf.modules.hl7.extend;

import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.map.MappingService;
import org.openehealth.ipf.commons.map.extend.MappingExtensionHelper;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7.validation.support.HL7Validator;

import static org.openehealth.ipf.modules.hl7.extend.ExtensionUtils.normalizeCollection;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.validation.ValidationContext;

/**
 * Adds HL7 extensions for Groovy
 * @DSL
 */
public class Hl7ExtensionModule {

    // ----------------------------------------------------------------
    //  Extensions to Collection for mapping values
    //  (depend on mapping service)
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object map(Collection delegate, Object key) {
        mappingService()?.get(key, normalizeCollection(delegate));
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
	 public static Object map(Collection delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, normalizeCollection(delegate), defaultValue);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object mapReverse(Collection delegate, Object value) {
        mappingService()?.getKey(value, normalizeCollection(delegate));
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object mapReverse(Collection delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, normalizeCollection(delegate), defaultValue);
    }

    static Object methodMissing(Collection delegate, String name, Object args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), normalizeCollection, name, args);
    }

    // ----------------------------------------------------------------
    //  Extensions to HAPI Types
    //  (depend on mapping service)
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object map(Type delegate, Object key) {
        mappingService()?.get(key, delegate.encode());
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object map(Type delegate, Object key, Object defaultValue) {
        mappingService()?.get(key, delegate.encode(), defaultValue);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object mapReverse(Type delegate, Object value) {
        mappingService()?.getKey(value, delegate.encode());
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Object mapReverse(Type delegate, Object value, Object defaultValue) {
        mappingService()?.getKey(value, delegate.encode(), defaultValue);
    }

    static Object methodMissing(Type delegate, String name, Object args) {
        MappingExtensionHelper.methodMissingLogic(mappingService(), { delegate.encode() }, name, args);
    }
        
    // ----------------------------------------------------------------
    //  Extensions to HAPI messages
    // ----------------------------------------------------------------

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static boolean matches(Message delegate, String type, String event, String version) {
        (type == '*' || type == MessageUtils.eventType(delegate)) &&
        (event == '*' || event == MessageUtils.triggerEvent(delegate)) &&
        (version == '*' || version == delegate.version);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String getEventType(Message delegate) {
        MessageUtils.eventType(delegate);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String getTriggerEvent(Message delegate) {
        MessageUtils.triggerEvent(delegate);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String getMessageStructure(Message delegate) {
        MessageUtils.messageStructure(delegate);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Message respond(AbstractMessage delegate, String eventType, String triggerEvent) {
        MessageUtils.response(delegate.modelClassFactory, delegate, eventType, triggerEvent);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Message ack(AbstractMessage delegate) {
        MessageUtils.ack(delegate.modelClassFactory, delegate);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Message nak(AbstractMessage delegate, String cause, AckTypeCode ackTypeCode) {
        MessageUtils.nak(delegate.modelClassFactory, delegate, cause, ackTypeCode);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Message nak(AbstractMessage delegate, AbstractHL7v2Exception e, AckTypeCode ackTypeCode) {
        MessageUtils.nak(delegate.modelClassFactory, delegate, e, ackTypeCode);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static Message dump(Message delegate) {
        MessageUtils.dump(delegate);
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static void validate(Message delegate, ValidationContext context) {
        new HL7Validator().validate(delegate, context);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String addSegment(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getSegmentClass(name, delegate.getVersion()), required, repeating, index);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating);
    }
    
	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
    public static String addGroup(AbstractMessage delegate, String name, boolean required, boolean repeating, int index) {
        delegate.add(delegate.modelClassFactory.getGroupClass(name, delegate.getVersion()), required, repeating, index);
    }

    private static MappingService mappingService() {
        ContextFacade.getBean(MappingService);
    }           

}