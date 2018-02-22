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

import ca.uhn.hl7v2.*
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.model.MessageVisitors.*
import ca.uhn.hl7v2.parser.ModelClassFactory
import ca.uhn.hl7v2.util.ReflectionUtil
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Top-level functions for handling HL7 messages in Kotlin
 *
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
    msg.initQuickstart(eventType, triggerEvent, "P")
    if (msg.atLeastVersion("2.3")) msg["MSH"][11][2] = "T"
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
fun newPrimitive(name: String, message: Message, value: String? = null): Primitive {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getTypeClass(name, message.version) ?: throw HL7Exception("Can't instantiate Type $name")
    val constructor = c.getConstructor(Message::class.java)
    val p = constructor.newInstance(message) as Primitive
    p.value = value
    return p
}

/**
 * Creates a new composite for the provided message. The provided map contains
 * entries, which represent the component values, where the component name is the
 * key of an entry. Example: newComposite('CE', msg, [identifier:'BRO'])
 *
 * @param name composite name
 * @param message message for which the composite shall be created
 * @return new composite
 */
fun newComposite(name: String, message: Message): Composite {
    val context = message.parser.hapiContext
    val c = context.modelClassFactory.getTypeClass(name, message.version) ?: throw HL7Exception("Can't instantiate Type $name")
    val constructor = c.getConstructor(Message::class.java)
    val composite = constructor.newInstance(message) as Composite

    return composite
}


/**
 * Loads a HL7 message from a classpath resource and parses it into a message
 *
 * @param context HAPI context
 * @param resource resource name
 * @param charset charset, defaults to UTF8
 * @return typed message
 */
fun <T: Message> loadHl7(context: HapiContext, resource: String, charset: Charset = Charsets.UTF_8): T =
    makeHl7(context, Hl7DslException::class.java.getResource(resource).readText(charset))

/**
 * Loads a HL7 message from a InputStream and parses it into a message
 *
 * @param context HAPI context
 * @param stream input stream
 * @param charset charset, defaults to UTF8
 * @return typed message
 */
fun <T: Message> loadHl7(context: HapiContext, stream: InputStream, charset: Charset = Charsets.UTF_8): T =
    makeHl7(context, stream.bufferedReader(charset).use { it.readText() })

/**
 * Parses a HL7 text string into a message
 *
 * @param context HAPI context
 * @param txt HL7 message string
 * @return typed message
 */
fun <T: Message> makeHl7(context: HapiContext, txt: String): T = context.genericParser.parse(txt) as T

/**
 * Iterate over a [group] and do something with every structure and its location string
 *
 * @param group group to be traversed
 * @param c closure
 * @return the group being traversed
 */
fun eachWithIndex(group: Group, c: (Structure, String) -> Unit): Group = group.apply {
    accept(visitStructures(EachStructureLocationVisitor(c)))
}

/**
 * Find all locations of structures within the [group] that match the predicate [c]
 *
 * @param group group to be traversed
 * @param c predicate
 * @return list of location strings
 */
fun findIndexValues(group: Group, c: (Structure) -> Boolean): List<String> =
        findPairs(group, c).map { it.first }

/**
 * Find the  location of the first structure within the [group] that matches the predicate [c]
 *
 * @param group group to be traversed
 * @param c predicate
 * @return location string
 */
fun findIndexOf(group: Group, c: (Structure) -> Boolean): String? =
        findPairs(group, c, true).firstOrNull()?.first

/**
 * Find the  location of the last structure within the [group] that matches the predicate [c]
 *
 * @param group group to be traversed
 * @param c predicate
 * @return location string
 */
fun findLastIndexOf(group: Group, c: (Structure) -> Boolean): String? =
        findPairs(group, c).lastOrNull()?.first

/**
 * It is easier to access the first/last element from a list of pairs instead of from a map
 */
private fun findPairs(group: Group, c: (Structure) -> Boolean, findFirst: Boolean = false): List<Pair<String, Structure>> {
    val visitor = FindStructureVisitor(c, findFirst)
    group.accept(visitStructures(visitor))
    return visitor.paths.map { terserToDsl(it.first) to it.second }
}


internal fun terserToDsl(location: Location): String {
    val terserSpec = location.toString()
    val start = if (terserSpec.startsWith('/')) 1 else 0
    val end = if (terserSpec.endsWith('/')) terserSpec.length - 1 else terserSpec.length
    return terserSpec.substring(start, end).replace('/', '.')
}