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
package org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v24.segment

import org.openehealth.ipf.modules.hl7.HL7v2Exception

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.model.AbstractSegment
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v24.datatype.EI
import ca.uhn.hl7v2.model.v24.datatype.ST
import ca.uhn.hl7v2.model.v24.datatype.TS
import ca.uhn.hl7v2.parser.ModelClassFactory

/**
 * The ZBE segment is intended to be used for information that details ADT
 * movement information. Each ADT event (i.e. admission, discharge, transfer,
 * visit) has a unique identifier to allow for updates at a later point in time.
 * Furthermore, other medical information like diagnoses or documents can refer
 * to this movement using the identifier as reference.
 * <p>
 * The segment is defined by the German Patient Management Profile and extended
 * by the "Historic Movement" option of the IHE ITI Supplement Patient
 * Administration Framework (PAM) Integration Profile.
 * 
 * This custom segment is compiled and loaded at runtime (unlike Java custom
 * segments, which are already available as .class file. Take care to load required
 * custom types in the same way. For this purpose the HapiModelExtension provides
 * addType extension methods, that take a String instead of a Class as
 * first parameter.  
 *
 * @author Christian Ohr
 */
public class ZBE extends AbstractSegment {

	/**
	 * @param parent
	 * @param factory
	 */
	public ZBE(Group parent, ModelClassFactory factory) {
		super(parent, factory)
		Message message = getMessage()
		try {
			add(EI, true, 0, 999, [ message ] as Object[], null)
			add(TS, true, 1, 26, [ message ] as Object[], null)
			add(TS, false, 1, 26, [ message ] as Object[], null)
			add(ST, true, 1, 10, [ message ] as Object[], null)
		} catch (HL7Exception he) {
			throw new HL7v2Exception(he)
		}
	}

	/**
	 * Returns movement ID (ZBE-1).
	 *
	 * @param rep index of repeating field
	 * @return movement ID
	 */
	public EI getMovementID(int rep) {
		return getTypedField(1, rep)
	}

	/**
	 * Returns movement IDs (ZBE-1).
	 *
	 * @return movement IDs
	 */
	public EI[] getMovementID() {
        return getTypedField(1, new EI[0])
	}

	/**
	 * Returns movement start date (ZBE-2).
	 *
	 * @return movement start date (required)
	 */
	public TS getStartMovementDateTime() {
		return getTypedField(2, 0)
	}

	/**
	 * Returns movement end date (ZBE-3).
	 *
	 * @return movement end date (optional)
	 */
	public TS getStartMovementEndTime() {
		return getTypedField(3, 0)
	}

	/**
	 * Returns movement action (ZBE-4).
	 *
	 * @return movement action (required, one of INSERT, DELETE, UPDATE, REFERENCE)
	 */
	public ST getAction() {
		return getTypedField(4, 0)
	}

}