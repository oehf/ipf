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
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.modules.hl7.message.MessageUtils

/**
 * Adds static extensions for Groovy, allowing to create new HL7 messages and structures.
 *
 * @DSL
 */
class Hl7StaticExtensionModule {

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    public static Object $static_methodMissing(Message delegate, String name, Object args) {
        MessageUtils.newMessage(ContextFacade.getBean(HapiContext), name, args[0])
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    public static Object $static_methodMissing(Message delegate, HapiContext context, String name, Object args) {
        MessageUtils.newMessage(context, name, args[0])
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    public static Object $static_methodMissing(Segment delegate, String name, Object args) {
        MessageUtils.newSegment(name, args[0])
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    public static Object $static_methodMissing(Composite delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof Map) {
            MessageUtils.newComposite(name, args[0], args[1])
        } else {
            MessageUtils.newComposite(name, args[0], null)
        }
    }

    /**
     * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
     */
    public static Object $static_methodMissing(Primitive delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof String) {
            MessageUtils.newPrimitive(name, args[0], args[1])
        } else {
            MessageUtils.newPrimitive(name, args[0], null)
        }
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
	public static Message defaultNak(Message delegate, HL7Exception e, AcknowledgmentCode ackTypeCode, String version) {
        MessageUtils.defaultNak(e, ackTypeCode, version)
    }

}
