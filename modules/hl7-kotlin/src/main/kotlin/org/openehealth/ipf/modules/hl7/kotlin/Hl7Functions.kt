/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin

import ca.uhn.hl7v2.ErrorCode
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.ModelClassFactory
import ca.uhn.hl7v2.util.ReflectionUtil
import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author Christian Ohr
 */


fun messageStructure(messageType: String, triggerEvent: String, version: String, factory: ModelClassFactory): String {
    val canonicalStructName = "${messageType}_$triggerEvent"
    return factory.getMessageStructureForEvent(canonicalStructName, Version.versionOf(version)) ?: canonicalStructName
}

fun newMessage(context: HapiContext, eventType: String, triggerEvent: String, version: String): Message {
    val factory = context.modelClassFactory
    val structName = if (eventType == "ACK") "ACK" else messageStructure(eventType, triggerEvent, version, factory)
    val c = factory.getMessageClass(structName, version, true)
            ?: throw HL7Exception("Can't instantiate message $structName", ErrorCode.UNSUPPORTED_MESSAGE_TYPE)
    val msg = ReflectionUtil.instantiateMessage(c, factory) as AbstractMessage
    msg.parser = context.genericParser
    msg.initQuickstart(eventType, triggerEvent, "P2")
    msg["MSH"][11][2] = "T"
    return msg
}

/**
 * Creates a new segment for the provided message
 *
 * @param name segment name
 * @param message message for which the segment shall be created
 * @return new segment
 */
fun newSegment(name: String, message: Message): Segment {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getSegmentClass(name, message.version) ?: throw HL7Exception("Can't instantiate Segment $name")
    return ReflectionUtil.instantiateStructure(c, message, context.modelClassFactory)
}

/**
 * Creates a new group for the provided message
 *
 * @param name group name
 * @param message message for which the group shall be created
 * @return new group
 */
fun newGroup(name: String, message: Message): Group {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getGroupClass(name, message.version) ?: throw HL7Exception("Can't instantiate Group $name")
    return ReflectionUtil.instantiateStructure(c, message, context.modelClassFactory)
}

/**
 * Creates a new primitive for the provided message. Example: newPrimitive('SI', msg, '1')
 *
 * @param name primitive name
 * @param message message for which the primitive shall be created
 * @param value primitive value
 * @return new primitive
 */
fun newPrimitive(name: String, message: Message): Primitive {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getTypeClass(name, message.version) ?: throw HL7Exception("Can't instantiate Type $name")
    val constructor = c.getConstructor(Message::class.java)
    return constructor.newInstance(message) as Primitive
}

/**
 * Creates a new composite for the provided message. The provided map contains
 * entries, which represent the component values, where the component name is the
 * key of an entry. Example: newComposite('CE', msg, [identifier:'BRO'])
 *
 * @param name composite name
 * @param message message for which the composite shall be created
 * @param list the list with the values for the composite
 * @return new composite
 */
fun newComposite(name: String, message: Message): Composite {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getTypeClass(name, message.version) ?: throw HL7Exception("Can't instantiate Type $name")
    val constructor = c.getConstructor(Message::class.java)
    return constructor.newInstance(message) as Composite
}


fun <T: Message> loadHl7(context: HapiContext, resource: String, charset: Charset = Charsets.UTF_8): T {
    val txt = Hl7DslException::class.java.getResource(resource).readText(charset)
    return makeHl7(context, txt)
}

fun <T: Message> loadHl7(context: HapiContext, stream: InputStream, charset: Charset = Charsets.UTF_8): T {
    return makeHl7(context, stream.bufferedReader(charset).use { it.readText() })
}

fun <T: Message> makeHl7(context: HapiContext, txt: String): T = context.genericParser.parse(txt) as T
