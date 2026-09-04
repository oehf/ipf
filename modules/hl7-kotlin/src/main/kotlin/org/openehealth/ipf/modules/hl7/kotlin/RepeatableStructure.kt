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
 * Represents the repeating structure with the given name.
 * The structure is always a child of the group.
 *
 * @author Christian Ohr
 * @since 3.5
 */
internal class RepeatableStructure(
        val elements: Array<out Structure>,
        private val group: Group,
        private val name: String) : Structure, Iterable<Structure> {

    override fun iterator(): Iterator<Structure> = elements.iterator()

    // Not TODO(): that throws NotImplementedError, an Error rather than an Exception, which escapes
    // the catch(Exception) handlers of callers such as the HL7 validators.
    override fun accept(visitor: MessageVisitor?, currentLocation: Location?): Boolean =
            throw UnsupportedOperationException(
                    "A repeatable structure cannot be visited as a whole; visit its repetitions individually")

    override fun getMessage(): Message = group.message

    override fun provideLocation(parentLocation: Location?, index: Int, repetition: Int): Location =
            elementAt(0).provideLocation(parentLocation, index, repetition)

    override fun getParent(): Group = group

    override fun isEmpty(): Boolean = elements.all { it.isEmpty }

    override fun getName(): String = name

    fun count(): Int = elements.size

    fun elementAt(rep: Int): Structure = if (elements.size <= rep) group.nrp(name) else elements[rep]
}