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

import ca.uhn.hl7v2.Location
import ca.uhn.hl7v2.model.*

/**
 * Represents the repeating field with the given index.
 * The field is always a child of the segment.
 *
 * @author Christian Ohr
 * @since 3.5
 */
internal class RepeatableField (
        val elements: Array<out Type>,
        private val segment: Segment,
        private val field: Int) : Type, Iterable<Type> {

    override fun iterator(): Iterator<Type> = elements.iterator()

    // Not TODO(): that throws NotImplementedError, an Error rather than an Exception, which escapes
    // the catch(Exception) handlers of callers such as the HL7 validators.
    override fun accept(visitor: MessageVisitor?, currentLocation: Location?): Boolean =
            throw UnsupportedOperationException(
                    "A repeatable field cannot be visited as a whole; visit its repetitions individually")

    override fun getMessage(): Message = segment.message

    override fun clear() = elements.forEach { it.clear() }

    override fun provideLocation(parentLocation: Location?, index: Int, repetition: Int): Location =
            elementAt(0).provideLocation(parentLocation, index, repetition)

    override fun isEmpty(): Boolean = elements.all { it.isEmpty }

    override fun getName(): String = elementAt(0).name

    override fun parse(string: String?): Unit =
            throw UnsupportedOperationException(
                    "A repeatable field cannot be parsed as a whole; parse into a single repetition")

    override fun encode(): String =
        elements.joinToString(getSeparator().toString()) { it.encode() }


    override fun getExtraComponents(): ExtraComponents = elementAt(0).extraComponents


    fun elementAt(rep: Int): Type = if (elements.size <= rep) segment.nrp(field) else elements[rep]

    fun count(): Int = elements.size

    private fun getSeparator(): Char {
        return message.encodingCharacters.repetitionSeparator
    }
}