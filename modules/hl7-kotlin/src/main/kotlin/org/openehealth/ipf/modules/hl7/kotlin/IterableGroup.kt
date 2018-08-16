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

import ca.uhn.hl7v2.model.AbstractGroup
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.util.ReadOnlyMessageIterator

/**
 * This class makes a [Group] effectively iterable and opens it for [Iterable]
 * extension functions.
 *
 * @author Christian Ohr
 * @since 3.5
 */
class IterableGroup(private val g: Group) : Iterable<Structure>, AbstractGroup(g.message, g.message.parser.factory) {

    override fun iterator(): Iterator<Structure> =
            ReadOnlyMessageIterator.createPopulatedStructureIterator(g, Structure::class.java)

}