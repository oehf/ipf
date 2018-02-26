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

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.Severity
import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.EncodingCharacters
import ca.uhn.hl7v2.parser.FixFieldDataType
import ca.uhn.hl7v2.util.DeepCopy
import ca.uhn.hl7v2.validation.builder.EncodingRuleBuilder
import ca.uhn.hl7v2.validation.builder.MessageRuleBuilder
import ca.uhn.hl7v2.validation.builder.PrimitiveRuleBuilder
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.modules.hl7.kotlin.validation.model.LambdaEncodingRule
import org.openehealth.ipf.modules.hl7.kotlin.validation.model.LambdaMessageRule
import org.openehealth.ipf.modules.hl7.kotlin.validation.model.LambdaPrimitiveTypeRule

/**
 * @author Christian Ohr
 */


// Extension functions/properties for Visitable ---------------------------------------------------

/**
 * Visitable#isEmpty is not originally exposed as property because it throws an exception
 */
val Visitable.empty: Boolean get() = isEmpty

fun <T : MessageVisitor> Visitable.accept(visitor: T): T = MessageVisitors.visit(this, visitor)


// Extension functions/properties for Type --------------------------------------------------------

/**
 * getValue()/value/nullValue Properties for Type
 */
val Type.value: String? get() = stringValue(this)
val Type.value2: String? get() = if (nullValue) "" else value
val Type.nullValue: Boolean get() = stringValue(this) == "\"\""

/**
 * getValue or a default if null
 */
fun Type.getValueOr(v: String?): String? = if (value != null) value else v

fun Type.valueOr(v: String?): String? = getValueOr(v)

/**
 * Sets a type to the specified [source] value. Depending on the source and target type, only a subset
 * of the value is used
 *
 * @param source value for the type
 */
fun Type.from(source: Any?): Unit =
        when (this) {
            is Primitive -> value = stringValue(source)
            is Composite -> if (source is Composite) DeepCopy.copy(source, this) else get(1).from(stringValue(source))
            is Variable -> data.from(source)
            is RepeatableField -> elementAt(0).from(source)
            else -> unknownType()
        }

/**
 * Returns the component [idx] of the given type. If applied on [Primitive], the idx must be 1. If applied
 * on a repeatable field, the component [idx] of its first repetition is returned.
 * @param idx component index, starting with 1
 * @return component [idx]
 * @throws Hl7DslException if a component with index > 1 shall be retrieved from a [Primitive]
 */
operator fun Type.get(idx: Int): Type =
        when (this) {
            is Primitive -> if (idx == 1) this else throw Hl7DslException("Index out of bounds for primitive")
            is Variable -> data[idx]
            is Composite -> getComponent(componentIndex(idx))
            is RepeatableField -> elementAt(0)[idx]
            else -> unknownType()
        }

/**
 * Sets the component [idx] to value [v]. If applied on [Primitive], the idx must be 1. If applied
 * on a repeatable field, the component [idx] of its first repetition is set.
 *
 * @param idx component index, starting with 1
 * @param v any value, i.e. another HL7 type or object implementing toString
 */
operator fun Type.set(idx: Int, v: Any?) = get(idx).from(v)

/**
 * Access repetition [rep] of a type. If the type is not repeatable, [rep] must be 0.
 *
 * @param rep repetition, starting with 0
 * @return type
 */
operator fun Type.invoke(rep: Int): Type =
        (this as? RepeatableField)?.elementAt(rep) ?: if (rep > 0) notRepeatable() else this

/**
 * Access repetitions of a type as array. If the type is not repeatable, an array with one
 * type is returned
 *
 * @return type array
 */
operator fun Type.invoke(): Array<out Type> =
        (this as? RepeatableField)?.elements ?: arrayOf(this)

// Destructuring support for up to 10 components

operator fun Type.component1(): Type = get(1)
operator fun Type.component2(): Type = get(2)
operator fun Type.component3(): Type = get(3)
operator fun Type.component4(): Type = get(4)
operator fun Type.component5(): Type = get(5)
operator fun Type.component6(): Type = get(6)
operator fun Type.component7(): Type = get(7)
operator fun Type.component8(): Type = get(8)
operator fun Type.component9(): Type = get(9)
operator fun Type.component10(): Type = get(10)


fun Type.map(key: Any?): Any? = mappingService().get(key, encode())
fun Type.map(key: Any?, defaultValue: Any?): Any? = mappingService().get(key, encode(), defaultValue)
fun Type.mapReverse(key: Any?): Any? = mappingService().getKey(key, encode())
fun Type.mapReverse(key: Any?, defaultValue: Any?): Any? = mappingService().getKey(key, encode(), defaultValue)


// Extension functions/properties for ExtraComponent ------------------------------------------

/**
 * getValue()/value/nullValue Properties for ExtraComponents
 */
val ExtraComponents.value: String? get() = stringValue(this)
val ExtraComponents.value2: String? get() = if (nullValue) "" else value
val ExtraComponents.nullValue: Boolean get() = stringValue(this) == "\"\""

operator fun ExtraComponents.get(idx: Int): Variable = getComponent(idx)


// Extension functions/properties for Structure -----------------------------------------------

/**
 * Returns the field [idx] of the given [Segment].  If applied
 * on a repeatable segment, the field [idx] of its first repetition is returned.
 *
 * @param idx field index, starting with 1
 * @return field [idx]
 * @throws Hl7DslException if applied on a [Group]
 */
operator fun Structure.get(idx: Int): Type =
        when (this) {
            is Group -> useStructureName()
            is Segment -> {
                val field = getField(idx)
                if (getMaxCardinality(idx) == 1) {
                    if (field.isEmpty()) getField(idx, 0) else field[0]
                } else {
                    RepeatableField(getField(idx), this, idx)
                }
            }
            is RepeatableStructure -> elementAt(0)[idx]
            else -> unknownType()
        }

/**
 * Returns repetition [rep] of the field [idx] of the of the [Segment].
 * If applied on a non-repeatable field, the repetition [rep] must be 0.
 *
 * @param idx field index, starting with 1
 * @param rep repetition index, starting with 0
 * @return field [idx]
 * @throws Hl7DslException if applied on a [Group] or a non-repeatable field with [rep] > 0
 */
operator fun Structure.get(idx: Int, rep: Int): Type = get(idx)(rep)

/**
 * Returns the structure with name [name] within the given [Group].  If applied
 * on a repeatable group, the structure [name] of its first repetition is returned.
 *
 * @param name structure name
 * @return structure
 * @throws Hl7DslException if applied on a [Segment]
 */
operator fun Structure.get(name: String): Structure =
        when (this) {
            is Segment -> useFieldNumber()
            is Group -> if (isRepeating(name)) RepeatableStructure(getAll(name), this, name) else get(name)
            is RepeatableStructure -> elementAt(0)[name]
            else -> unknownType()
        }

/**
 * Returns repetition [rep] of the structure with name [name] within the given [Group].
 * If applied on a non-repeatable structure, the repetition [rep] must be 0.
 *
 * @param name structure name
 * @param rep repetition index, starting with 0
 * @return name structure name
 * @throws Hl7DslException if applied on a [Segment] or a non-repeatable structure with [rep] > 0
 */
operator fun Structure.get(name: String, rep: Int): Structure = get(name)(rep)

/**
 * Returns repetition [rep] of a structure. If applied on a non-repeatable structure, the repetition [rep] must be 0.
 * @param rep repetition index, starting with 0
 * @return structure repetition
 * @throws Hl7DslException if applied on a non-repeatable structure with [rep] > 0
 */
operator fun Structure.invoke(rep: Int): Structure =
        (this as? RepeatableStructure)?.elementAt(rep) ?: if (rep > 0) notRepeatable() else this

/**
 * Returns all repetitions of a structure as array. If the structure is not repeatable, an array with one
 * structure is returned
 *
 * @return array of structures
 */
operator fun Structure.invoke(): Array<out Structure> =
        (this as? RepeatableStructure)?.elements ?: arrayOf(this)

/**
 * Sets a segment to the specified [source] segment.
 *
 * @param source value for the type
 * @throws Hl7DslException if applied on a [Group]
 */
fun Structure.from(source: Segment): Unit =
        when (this) {
            is Segment -> DeepCopy.copy(source, this)
            is RepeatableStructure -> elementAt(0).from(source)
            else -> throw Hl7DslException("Unsupported operation, cannot set a group")
        }

/**
 * Sets the field [idx] to value [v]. If applied
 * on a repeatable field, the component [idx] of its first repetition is set.
 *
 * @param idx component index, starting with 1
 * @param v any value, i.e. another HL7 type or object implementing toString
 */
operator fun Structure.set(idx: Int, v: Any?) = get(idx).from(v)

/**
 * Sets the repetition [rep] of field [idx] to value [v].
 *
 * @param idx component index, starting with 1
 * @param rep repetition starting with 0
 * @param v any value, i.e. another HL7 type or object implementing toString
 */
operator fun Structure.set(idx: Int, rep: Int, v: Any?) = get(idx, rep).from(v)


/**
 * Sets the segment with name [name] from the provided [segment]. If applied
 * on a repeatable structure, the segment [name] of its first repetition is set.
 *
 * @param name structure name
 * @param segment segment of a matching class
 */
operator fun Structure.set(name: String, segment: Segment) = get(name).from(segment)

/**
 * Sets the repetition [rep] of the segment with name [name] from the provided [segment].
 *
 * @param name structure name
 * @param rep repetition
 * @param segment segment of a matching class
 */
operator fun Structure.set(name: String, rep: Int, segment: Segment) = get(name, rep).from(segment)

// Counting

/**
 * Returns the number of repetitions of the field with index [idx] in this [Segment].
 *
 * @param idx field index, starting with 1
 * @return number of repetitions
 * @throws Hl7DslException if applied on a [Group]
 */
fun Structure.count(idx: Int): Int =
        when (this) {
            is Group -> useStructureName()
            is Segment -> getField(idx).size
            is RepeatableStructure -> elementAt(0).count(idx)
            else -> unknownType()
        }

/**
 * Returns the number of repetitions of the structure with name [name] in this [Group].
 *
 * @param name structure name
 * @return number of repetitions
 * @throws Hl7DslException if applied on a [Segment]
 */
fun Structure.count(name: String): Int =
        when (this) {
            is Group -> getAll(name).size
            is Segment -> useFieldNumber()
            is RepeatableStructure -> elementAt(0).count(name)
            else -> unknownType()
        }


/**
 * Adds and returns a repetition to the field with index [idx] in this [Segment].
 *
 * @param idx field index, starting with 1
 * @return new field
 * @throws Hl7DslException if applied on a [Group]
 */
fun Structure.nrp(idx: Int): Type =
        when (this) {
            is Group -> useStructureName()
            is Segment -> getField(idx, count(idx))
            is RepeatableStructure -> elementAt(0).nrp(idx)
            else -> unknownType()
        }

/**
 * Adds and returns a repetition to the structure with name [name] in this [Group].
 *
 * @param name structure name
 * @return new field
 * @throws Hl7DslException if applied on a [Segment]
 */
fun Structure.nrp(name: String): Structure =
        when (this) {
            is Group -> get(name, count(name))
            is Segment -> useFieldNumber()
            is RepeatableStructure -> elementAt(0).nrp(name)
            else -> unknownType()
        }

/**
 * Returns the path of the structure within its message
 */
val Structure.path: String? get() = message.findIndexOf { it === this }

/**
 * Sets the [Varies] field OBX-5 to represent the provided [type]
 */
fun Structure.setObx5Type(type: String, desiredRepetitionsCount: Int = 1) {
    val className = this::class.qualifiedName
    if (className != null && !className.endsWith(".OBX")) throw Hl7DslException("only OBX segments can be served by this method")
    for (i in 0 until desiredRepetitionsCount - count(5)) {
        nrp(5)
    }
    this[2] = type
    FixFieldDataType.fixOBX5(this as Segment, message.parser.factory, message.parser.parserConfiguration)
}


// Extension functions/properties for Group -----------------------------------------------


fun Group.eachWithIndex(c: (Structure, String) -> Unit): Group = eachWithIndex(this, c)
fun Group.findIndexValues(c: (Structure) -> Boolean): List<String> = findIndexValues(this, c)
fun Group.findIndexOf(c: (Structure) -> Boolean): String? = findIndexOf(this, c)
fun Group.findLastIndexOf(c: (Structure) -> Boolean): String? = findLastIndexOf(this, c)


/**
 * Opens a HL7 [Group]/[Message] to all methods and extension functions applicable for kotlin Iterables
 *
 * @return iterable group
 */
fun Group.asIterable(): IterableGroup = IterableGroup(this)

/**
 * Allows to use a forach loop on a HL7 [Group]/[Message]
 *
 * @return iterator
 */
operator fun Group.iterator(): Iterator<Structure> = asIterable().iterator()


// Extension functions/properties for Message -----------------------------------------------

/**
 * Copies a message
 *
 * @return message copy
 */
fun <T : Message> T.copy(): T = parser.parse(encode()) as T

fun Message.validate(context: HapiContext?) {
    val ctx = context ?: parser.hapiContext
    // We could also write an exception handler on top of SimpleValidationExceptionHandler that
    // encapsulates the behavior below, but that may restrict custom validation...
    val handler = SimpleValidationExceptionHandler(ctx)
    handler.minimumSeverityToCollect = Severity.ERROR
    try {
        if (ctx.getMessageValidator<Boolean>().validate(this, handler)) {
            throw ValidationException("Message validation failed", handler.exceptions)
        }
    } catch (e: HL7Exception) {
        throw ValidationException("Message validation failed", e)
    }
}

val Message.eventType: String get() = get("MSH")[9][1].value!!
val Message.triggerEvent: String get() = get("MSH")[9][2].value!!
val Message.messageStructure: String
    get() = messageStructure(
            eventType, triggerEvent, version, parser.hapiContext.modelClassFactory)

val Message?.encodingCharacters: EncodingCharacters
    get() =
        if (this != null)
            EncodingCharacters.getInstance(this) else
            EncodingCharacters('|', "^~\\&")


/**
 *  @return a response message with the basic MSH fields already populated
 */
fun <T : Message> Message.respond(responseEvent: String, responseTrigger: String? = null): T {

    // make message of correct version
    val trigger = responseTrigger ?: this.triggerEvent
    val out = newMessage(parser.hapiContext, responseEvent, trigger, version)

    // populate outbound MSH using data from inbound message ...
    val mshIn = get("MSH")
    val mshOut = out["MSH"]

    mshOut[1] = mshIn[1]
    mshOut[2] = mshIn[2]
    mshOut[3] = mshIn[5]
    mshOut[4] = mshIn[6]
    mshOut[5] = mshIn[3]
    mshOut[6] = mshIn[4]
    mshOut[11] = mshIn[11]

    if ("MSA" in out.names) {
        out["MSA"][2] = mshIn[10]
    }
    return out as T
}

/**
 * @return true if the version of this [Message] is at least [otherVersion]
 */
fun Message.atLeastVersion(otherVersion: String): Boolean =
        Version.versionOf(this.version) >= Version.versionOf(otherVersion)

// Extension functions/properties for Rule Builders -----------------------------------------------

fun MessageRuleBuilder.checkIf(check: (Message) -> Array<ca.uhn.hl7v2.validation.ValidationException>): MessageRuleBuilder = test(LambdaMessageRule(check))
fun EncodingRuleBuilder.checkIf(check: (String) -> Array<ca.uhn.hl7v2.validation.ValidationException>): EncodingRuleBuilder = test(LambdaEncodingRule(check))
fun PrimitiveRuleBuilder.checkIf(check: (String?) -> Array<ca.uhn.hl7v2.validation.ValidationException>): PrimitiveRuleBuilder = test(LambdaPrimitiveTypeRule(check))


// Exceptions due to invalid access

private fun <T> Structure.unknownType(): T = throw Hl7DslException("Unknown structure type ${this::class.simpleName}")
private fun <T> Type.unknownType(): T = throw Hl7DslException("Unknown field type ${this::class.simpleName}")
private fun <T> Visitable.notRepeatable(): T = throw Hl7DslException("Type ${this::class.simpleName} is not repeatable")
private fun <T> useFieldNumber(): T = throw Hl7DslException("Use field number as index")
private fun <T> useStructureName(): T = throw Hl7DslException("Use structure name as index")

private fun componentIndex(idx: Int) = if (idx < 1) throw Hl7DslException("component index must be in range 1..n") else idx - 1

private fun mappingService(): MappingService = ContextFacade.getBean(MappingService::class.java)

// Get a string value out of types or any object

private fun stringValue(o: Any?): String? =
        when (o) {
            null -> null
            is Primitive -> o.value
            is Variable -> stringValue(o.data)
            is Composite -> stringValue(o[1])
            is RepeatableField -> stringValue(o(0))
            is ExtraComponents -> stringValue(o.getComponent(1))
            else -> o.toString()
        }
