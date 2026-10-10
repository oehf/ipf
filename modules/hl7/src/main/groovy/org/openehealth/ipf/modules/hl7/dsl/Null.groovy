/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.dsl

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.Location
import ca.uhn.hl7v2.model.AbstractType
import ca.uhn.hl7v2.model.DataTypeException
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.MessageVisitor

/**
 * 
 * Null helps to handle non-existing repeatable elements transparently
 * without throwing an Exception. It is returned when accessing a component index &gt; 1
 * of a primitive, so that expressions remain valid for HL7 versions where a primitive field
 * has become a composite in later versions.
 * 
 * @author Christian Ohr
 *
 */
class Null extends AbstractType {

    Null(Message message) {
        super(message)
    }

    def getAt(int idx) {
        this
    }

    static String getValue() {
		null
	}

    static String getValue2() {
        null
    }

    static boolean isNullValue() {
        false
    }

    static String getValueOr(String defaultValue) {
        defaultValue
    }

    static String valueOr(String defaultValue) {
        defaultValue
    }

    String toString() {
        null
    }

    @Override
    String encode() {
        ''
    }

    /**
     * @throws HL7DslException always, as Null cannot hold a value
     */
    void from(Object value) {
        throw new HL7DslException('Cannot assign a value to Null')
    }

    static void setValue(String value) throws DataTypeException {
		throw new DataTypeException("Cannot assign a value Null")
	}

    @Override
    boolean isEmpty() throws HL7Exception {
        true
    }

    @Override
    boolean accept(MessageVisitor visitor, Location currentLocation) throws HL7Exception {
        return false
    }
}
