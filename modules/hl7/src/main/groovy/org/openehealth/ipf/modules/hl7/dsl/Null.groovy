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
 * NullAdapter helps to handle non-existing repeatable elements transparently
 * without throwing an Exception. 
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
    	
	String getValue() {
		null
	}

    String getValueOr(String defaultValue) {
        defaultValue
    }
	
    public String toString() {
        null
    }
	
	void setValue(String value) throws DataTypeException {
		throw new DataTypeException("Cannot assign a value Null")
	}

    @Override
    boolean accept(MessageVisitor visitor, Location currentLocation) throws HL7Exception {
        return false
    }
}
