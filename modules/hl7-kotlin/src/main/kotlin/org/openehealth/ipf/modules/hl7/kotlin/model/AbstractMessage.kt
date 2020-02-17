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

package org.openehealth.ipf.modules.hl7.kotlin.model

import ca.uhn.hl7v2.model.AbstractMessage
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.parser.DefaultModelClassFactory
import ca.uhn.hl7v2.parser.ModelClassFactory
import java.util.*

/**
 * @author Christian Ohr
 * @since 3.5
 */
abstract class AbstractMessage(factory: ModelClassFactory = DefaultModelClassFactory()) : AbstractMessage(factory) {

    init {
        for (structure in structures(LinkedHashMap()).entries) {
            add(structure.key, structure.value.required, structure.value.repeating)
        }
    }

    protected abstract fun structures(
            structures: Map<Class<out Structure>, Cardinality>): Map<Class<out Structure>, Cardinality>

    protected enum class Cardinality(val required: Boolean, val repeating: Boolean) {
        REQUIRED(true, false),
        OPTIONAL(true, true),
        REQUIRED_REPEATING(false, false),
        OPTIONAL_REPEATING(false, true)
    }
}