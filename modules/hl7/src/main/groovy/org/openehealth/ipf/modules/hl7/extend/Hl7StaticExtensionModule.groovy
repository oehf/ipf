package org.openehealth.ipf.modules.hl7.extend

import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.parser.ModelClassFactory

class Hl7StaticExtensionModule {

    static Object $static_methodMissing(Message delegate, String name, args) {
        MessageUtils.newMessage(factory(), name, args[0])
    }

    static Object $static_methodMissing(Segment delegate, String name, args) {
        MessageUtils.newSegment(factory(), name, args[0])
    }

    static Object $static_methodMissing(Composite delegate, String name, args) {
        if (args.size() > 1 && args[1] instanceof Map) {
            MessageUtils.newComposite(factory(), name, args[0], args[1])
        } else {
            MessageUtils.newComposite(factory(), name, args[0], null)
        }
    }
    
    static Object $static_methodMissing(Primitive delegate, String name, args) {
        if (args.size() > 1 && args[1] instanceof String) {
            MessageUtils.newPrimitive(factory(), name, args[0], args[1])
        } else {
            MessageUtils.newPrimitive(factory(), name, args[0], null)
        }
    }

    static Message defaultNak(Message delegate, AbstractHL7v2Exception e, AckTypeCode ackTypeCode, String version) {
        MessageUtils.defaultNak(e, ackTypeCode, version)
    }

    private static ModelClassFactory factory() {
        ContextFacade.getBean(ModelClassFactory)
    }
}
