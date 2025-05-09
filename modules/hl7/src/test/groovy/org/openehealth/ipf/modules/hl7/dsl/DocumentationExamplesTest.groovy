/*
 * Copyright 2011 the original author or authors.
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

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.v25.group.ORU_R01_PATIENT_RESULT
import ca.uhn.hl7v2.model.v25.message.ORU_R01
import ca.uhn.hl7v2.model.v25.segment.MSH
import ca.uhn.hl7v2.model.v25.segment.NK1
import ca.uhn.hl7v2.model.v25.segment.PID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*
import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.load
import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.make

/**
 * Make sure that there are no underlinded methods/fields in this class
 * 
 * @author Mitko Kolev
 *
 */
class DocumentationExamplesTest extends groovy.test.GroovyAssert {

    ORU_R01 message
	
	@BeforeEach
	void setUp(){
		message = load('dsl/oru-r01-25.hl7')
	}
	
	@Test
	void testSegmentNewFeatures(){
		// message.MSH.getPath()
		message.MSH.from(message.MSH)
		message.MSH.isEmpty()
		message.MSH.count(3)
		
		// message.PATIENT_RESULT(1).PATIENT.getPath()
	}
	
	@Test
	public void testConstruction(){
        ORU_R01 messageCopy = message.copy()
        ORU_R01 messageFromString = make(messageCopy.toString())
	}
	
	@Test
	public void testNavigation(){
		MSH msh = message.MSH
		ORU_R01_PATIENT_RESULT patientResult = message.PATIENT_RESULT(0)
		PID patient  = patientResult.PATIENT.PID
		Segment pid = message.PATIENT_RESULT.PATIENT.PID
		def pidDef = message.PATIENT_RESULT.PATIENT.PID
	}
	
	@Test
	public void testNavigationToFields(){
		Composite composite = message.MSH[3]     // MSH-3 = sending application composite field
		Primitive primitive = message.MSH[3][2]  // MSH-3-2 = universal ID primitive field
		primitive = message.MSH.sendingApplication.universalIDType
		
		ca.uhn.hl7v2.model.v23.message.ORU_R01 message23 = new ca.uhn.hl7v2.model.v23.message.ORU_R01()
		def messageType = message23.MSH.messageType.messageType // only works for HL7 v2.2 and 2.3 messages
		messageType = message.MSH.messageType.messageCode     // only works for HL7 v2.4\+ messages
		messageType = message.MSH[9][1]                       // works for all HL7 versions
	}
	
	@Test
	public void testFieldValues(){
		Primitive val = message.MSH[3][2]
		String primitiveValueEncode = message.MSH[3][2].encode()
		String primitiveValueString = message.MSH[3][2].toString()
		assertEquals(primitiveValueEncode, primitiveValueString)
		if ("2.16.840.1.113883.19.3.1.6".equals(message.MSH[3][2].encode())) {
			
		}
	}	
	@Test
	public void testNullValues(){
		Primitive firstStreet = message.PATIENT_RESULT.PATIENT.PID[11][1][1]
		
		assertEquals ''  , firstStreet.value2
		assertEquals '""', firstStreet.encode()
		assertTrue         firstStreet.isNullValue()
	}
	
	@Test
	public void testRepetitions(){
		def group = message.PATIENT_RESULT(0).PATIENT  // access first PATIENT_RESULT group
		NK1 nk1   = group.NK1(0)            // access first NK1 segment
		def phone = nk1[5](0)                          // access first NK1-5 field (phone)
		nk1.count(1)
		
		List phones = nk1[5]()  // returns a list of phone elements

	}
	
	
	
	@Test
	void testAccessingTargetObjects(){
		def group = message.PATIENT_RESULT(0).PATIENT  // access first PATIENT_RESULT group

		int cardinality2 = group.NK1(0).getMaxCardinality(3)         // equivalent
		String segmentName2 = group.NK1(0).name                      // equivalent
	}
	
	
	@Test
	public void testSmartNavigation(){
		def nk1 = message.PATIENT_RESULT(0).PATIENT.NK1(0) // Repetitions of groups or segments:
		def phoneNumber = nk1[5](0)[1]                     // Repetition of fields
        def name = nk1[2]
		def familyName  = name[1][1]                       // Name is first component of FN composite type
		def group = message.PATIENT_RESULT.PATIENT
		
		assert message.PATIENT_RESULT.PATIENT == message.PATIENT_RESULT(0).PATIENT //groups
		assert group.NK1(0)[5](0)[1].value == group.NK1[5](0)[1].value             // segment
	}		

	@Test
	public void testSmartNavigation2(){
		def group = message.PATIENT_RESULT.PATIENT
		assert group.NK1(0)[5](0)[1].value == group.NK1[5][1].value                // field
		
		assert group.NK1(0)[5](0)[1].value == group.NK1[5].value
		assert group.NK1(0)[2][1][1].value == group.NK1[2].value
		PID pid = message.PATIENT_RESULT.PATIENT.PID // 'full' expression
		
		assertEquals ''  , pid[11][1][1].value2           // 'full' expression
		assertEquals '""', pid[11][1][1].value
		assertTrue pid[11][1][1].isNullValue()

		assertEquals ''  , pid[11].value2                 // 'smart' expression
		assertEquals '""', pid[11].value
		assertTrue pid[11].isNullValue()
	}
	
	
	@Test
	void testSegmentAndGroupEmptiniess(){
		assertFalse(message.PATIENT_RESULT.PATIENT.VISIT.PV1.isEmpty())
		assertFalse(message.PATIENT_RESULT.PATIENT.VISIT.PV1.isEmpty())
		assertFalse(message.PATIENT_RESULT.isEmpty())             
	}
	

}