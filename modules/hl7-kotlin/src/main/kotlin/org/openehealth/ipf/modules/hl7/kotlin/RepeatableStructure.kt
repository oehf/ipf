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
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.MessageVisitor
import ca.uhn.hl7v2.model.Structure

/**
 * Represents the repeating structure with the given name.
 * The structure is always a child of the group.
 *
 * @author Christian Ohr
 */
internal class RepeatableStructure(
        val elements: Array<out Structure>,
        private val group: Group,
        private val name: String) : Structure {

    override fun accept(visitor: MessageVisitor?, currentLocation: Location?): Boolean {
        TODO("not implemented")
    }

    override fun getMessage(): Message = group.message

    override fun provideLocation(parentLocation: Location?, index: Int, repetition: Int): Location {
        TODO("not implemented")
    }

    override fun getParent(): Group = group

    override fun isEmpty(): Boolean = !elements.any { !it.isEmpty }

    override fun getName(): String = name

    fun count(): Int = elements.size

    fun elementAt(rep: Int): Structure = if (elements.size <= rep) group.nrp(name) else elements[rep]
}