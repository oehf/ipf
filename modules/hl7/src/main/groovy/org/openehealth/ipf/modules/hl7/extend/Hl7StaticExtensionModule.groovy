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
import ca.uhn.hl7v2.HapiContext;
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
        MessageUtils.newMessage(context(), name, args[0])
    }

    public static Object $static_methodMissing(Segment delegate, String name, Object args) {
        MessageUtils.newSegment(context(), name, args[0])
    }

    public static Object $static_methodMissing(Composite delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof Map) {
            MessageUtils.newComposite(context(), name, args[0], args[1])
        } else {
            MessageUtils.newComposite(context(), name, args[0], null)
        }
    }

    public static Object $static_methodMissing(Primitive delegate, String name, Object args) {
        if (args.size() > 1 && args[1] instanceof String) {
            MessageUtils.newPrimitive(context(), name, args[0], args[1])
        } else {
            MessageUtils.newPrimitive(context(), name, args[0], null)
        }
    }

	/**
	 * @DSLDoc http://repo.openehealth.org/confluence/display/ipf2/Extensions+to+HAPI
	 */
	public static Message defaultNak(Message delegate, HL7Exception e, AcknowledgmentCode ackTypeCode, String version) {
        MessageUtils.defaultNak(e, ackTypeCode, version)
    }

    private static ModelClassFactory factory() {
        ContextFacade.getBean(ModelClassFactory)
    }

    private static HapiContext context() {
        ContextFacade.getBean(HapiContext)
    }
}
