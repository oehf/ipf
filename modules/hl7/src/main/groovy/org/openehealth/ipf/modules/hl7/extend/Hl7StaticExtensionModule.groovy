package org.openehealth.ipf.modules.hl7.extend;

import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;

import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * Adds static  extensions for Groovy
 * @DSL
 */
class Hl7StaticExtensionModule {

    public static Object $static_methodMissing(Message delegate, String name, Object args) {
        MessageUtils.newMessage(factory(), name, args[0])
    }

    public static Object $static_methodMissing(Segment delegate, String name, Object args) {
        MessageUtils.newSegment(factory(), name, args[0])
    }

    public static Object $static_methodMissing(Composite delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof Map) {
            MessageUtils.newComposite(factory(), name, args[0], args[1])
        } else {
            MessageUtils.newComposite(factory(), name, args[0], null)
        }
    }

    public static Object $static_methodMissing(Primitive delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof String) {
            MessageUtils.newPrimitive(factory(), name, args[0], args[1])
        } else {
            MessageUtils.newPrimitive(factory(), name, args[0], null)
        }
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
	public static Message defaultNak(Message delegate, AbstractHL7v2Exception e, AckTypeCode ackTypeCode, String version) {
        MessageUtils.defaultNak(e, ackTypeCode, version)
    }

    private static ModelClassFactory factory() {
        ContextFacade.getBean(ModelClassFactory)
    }
}
