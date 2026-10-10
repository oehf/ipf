/*
 * Copyright 2026 the original author or authors.
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
import ca.uhn.hl7v2.model.AbstractType
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.MessageVisitor

/**
 * Returned when accessing a component index > 1 of a primitive, so that DSL expressions remain
 * valid for HL7 versions where a primitive field has become a composite in later versions.
 * A Null is empty, has a null value, returns itself for any component and cannot be assigned a value.
 *
 * @author Christian Ohr
 * @since 6.0
 */
class Null(message: Message) : AbstractType(message) {

    override fun isEmpty(): Boolean = true

    override fun encode(): String = ""

    override fun accept(visitor: MessageVisitor?, currentLocation: Location?): Boolean = false

    override fun toString(): String = "Null"
}
