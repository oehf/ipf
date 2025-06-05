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
package org.openehealth.ipf.modules.hl7.extend

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.v22.datatype.ID
import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v24.message.ACK
import ca.uhn.hl7v2.model.v25.segment.NK1
import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.util.Terser
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.modules.hl7.parser.GroovyCustomModelClassFactory

import static org.easymock.EasyMock.*
import static org.junit.jupiter.api.Assertions.*

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
class HapiModelExtensionTest {
	
	static def customGroovyPackageName = 'org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v25'

    @BeforeAll
    static void setUp() {
        BidiMappingService mappingService = new BidiMappingService()
        mappingService.setMappingScript(HapiModelExtensionTest.class.getResource("/example2.map"))
        ModelClassFactory mcf = new CustomModelClassFactory()
        HapiContext context = new DefaultHapiContext(mcf)
        Registry registry = createMock(Registry)
        ContextFacade.setRegistry(registry)
        expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        expect(registry.bean(ModelClassFactory)).andReturn(mcf).anyTimes()
        expect(registry.bean(HapiContext)).andReturn(context).anyTimes()
        replay(registry)
    }
	
    @Test
    void testMatches() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
    	assertTrue msg.matches('*', '*', '*')
    	assertTrue msg.matches('ADT', 'A01', '*')
    	assertTrue msg.matches('ADT', 'A01', '2.2')
    	assertFalse msg.matches('ADT', 'A02', '*') 
    	assertFalse msg.matches('ADT', 'A01', '2.3') 
        msgText = this.class.classLoader.getResource('msg-02.hl7')?.text
        msg = new GenericParser().parse(msgText)        
    	assertTrue msg.matches('*', '*', '*')
    	assertTrue msg.matches('ORU', 'R01', '*')
    	assertTrue msg.matches('ORU', 'R01', '2.4')
    	assertFalse msg.matches('ORU', 'A02', '*')
    	assertFalse msg.matches('ORU', 'R01', '2.3')
    }
    
    @Test
    void testAck() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
    	Message ack = msg.generateACK()
    	assertEquals 'ACK', ack.MSH.messageType.messageType.value
    	assertEquals 'AA', ack.MSA.acknowledgementCode.value
    }

    @Test
    void testNak() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
    	Message nak = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception("blarg", 204))
    	assertEquals 'ACK', nak.MSH.messageType.messageType.value
    	assertEquals 'AR', nak.MSA.acknowledgementCode.value 
    	assertEquals 'AR', nak.MSA.acknowledgementCode.value
    }
    
    @Test
    void testNakCause() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
    	Message nak = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception("blarg"))
    	assertEquals 'ACK', nak.MSH.messageType.messageType.value
    	assertEquals 'AR', nak.MSA.acknowledgementCode.value
    }

    @Test
    void testNak25() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
    	Message nak = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception("blarg", 204))
    	assertEquals 'ACK', nak.MSH.messageType.messageCode.value
    	assertEquals 'AR', nak.MSA.acknowledgmentCode.value
    }
    
    @Test
    void testNak25Cause() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = new GenericParser().parse(msgText)
        Message nak = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception("blarg"))
    	assertEquals 'ACK', nak.MSH.messageType.messageCode.value
    	assertEquals 'AR', nak.MSA.acknowledgmentCode.value
    }
    
    @Test
    void testDefaultNak() {
    	ACK nak = Message.defaultNak(new HL7Exception("blarg", 204), AcknowledgmentCode.AE, "2.4")
    	assertEquals 'ACK', nak.MSH.messageType.messageType.value
    	assertEquals 'AE', nak.MSA.acknowledgementCode.value
        String msg = nak.ERR.getErrorCodeAndLocation(0).codeIdentifyingError.alternateText.value
    	assertEquals 'blarg', msg
    }

    @Test
    void testDefaultNak25() {
    	Message nak = Message.defaultNak(new HL7Exception("blarg", 204), AcknowledgmentCode.AE, "2.5")
    	assertEquals 'ACK', nak.MSH.messageType.messageCode.value
    	assertEquals 'AE', nak.MSA.acknowledgmentCode.value
    	assertEquals 'blarg', nak.MSA.textMessage.value
    }
    
    @Test
    void testMakeMessage() {
        Message msg = Message.ADT_A01('2.5')
    	assertEquals 'ADT', msg.MSH.messageType.messageCode.value
        assertEquals 'A01', msg.MSH.messageType.triggerEvent.value
        assertEquals '2.5', msg.MSH.versionID.versionID.value
    }

    @Test
    void testMakeSegment() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = new GenericParser().parse(msgText)
        Segment nk1 = Segment.NK1(msg)
    	assertTrue nk1 instanceof NK1
    }

    @Test
    void testMakeComposite() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
        Composite ce = Composite.CE(msg, [identifier:'BRO'])
        assertEquals 'BRO', ce.identifier.value
   }

    @Test
    void testMakePrimitive() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
        Primitive si = Primitive.SI(msg, '1')
        assertEquals '1', si.value
    }

    @Test
    void testHAPIExampleCreateMessageFromScratch() {
        Message msg = Message.ADT_A01('2.4')
        msg.MSH.with {
            sendingApplication.namespaceID.value = 'TestSendingSystem' 
            sequenceNumber.value = '123'
        }
        msg.PID.with {
            getPatientName(0).familyName.surname.value = 'Doe'
            getPatientName(0).givenName.value = 'John'
        	getPatientIdentifierList(0).ID.value = '123456'
        }
    }
    
    @Test
    void testHAPIExamplePopulateOBX() {
        Message msg = Message.ORU_R01('2.5')
        msg.PATIENT_RESULT.ORDER_OBSERVATION.with {
        // Populate OBR
            OBR.with {
                setIDOBR.value = '1'
                fillerOrderNumber.entityIdentifier.value = '1234'
                fillerOrderNumber.namespaceID.value = 'LAB'
                universalServiceIdentifier.identifier.value = '88304'
            }
            getOBSERVATION(0).OBX.with {
                setIDOBX.value = '1'
                observationIdentifier.identifier.value = "88304"
                observationSubID.value = '1'
                // The first OBX has a value type of CE. So first, we populate OBX-2 with "CE"...
                valueType.value = 'CE'
                Composite ce = Composite.CE(msg, [identifier:'T57000', text:'GALLBLADDER', nameOfCodingSystem:'SNM'])
                getObservationValue(0).data = ce
            }
            // Now we populate the second OBX
            getOBSERVATION(1).OBX.with {
                setIDOBX.value = '2'
                observationSubID.value = '2'
                
                // The second OBX in the sample message has an extra subcomponent at
                // OBX-3-1. This component is actually an ST, but the HL7 specification allows
                // extra subcomponents to be tacked on to the end of a component. This is
                // uncommon, but HAPI nontheless allows it.
                observationIdentifier.identifier.value = 88304
                Primitive st = Primitive.ST(msg, 'MDT')
                observationIdentifier.identifier.extraComponents.getComponent(0).data = st
                
                // The first OBX has a value type of TX. So first, we populate OBX-2 with "TX"...
                valueType.value = 'TX'
                Primitive tx = Primitive.TX(msg, 'MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE')
                getObservationValue(0).data = tx
            }
        }
    }
            
    @Test
    void testList() {
        assert ['a','b'].map('listTest') == ['c','d'] 
        assert ['x','y'].map('listTest', ['a','b']) == (['x','y'].map('listTest') ?: ['a','b'])
        assert ['x','y'].map('listTest2') == ['c','d']
        assert ['x','y'].map('listTest2', ['a','b']) == ['c','d']
        ADT_A01 msg = new ADT_A01()
        msg.initQuickstart('ADT', 'A01', 'P')
        def x = new ID(msg, 100)
    	def y = new ID(msg, 100)
    	x.setValue('a')
    	y.setValue('b')
    	assert [x,y].map('listTest') == ['c','d']    	
    }
    
    @Test
    void testTypeMap() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)        
        assertEquals 'IMP', msg.PV1.patientClass.map('encounterType')
        //assert msg.PV1.patientClass.mapEncounterType() == 'IMP'
        //assert msg.MSH.messageType.mapMessageType() == 'PRPA_IN402001'
    }
        
    @Test
    void testKeyAndValueSystems() {
        assertEquals '2.16.840.1.113883.12.4', 'encounterType'.keySystem()
        assertEquals '2.16.840.1.113883.5.4', 'encounterType'.valueSystem()
    }
    
    @Test
    void testUnknownKeySystem() {
        assertThrows(IllegalArgumentException.class) {
            'Y'.keySystem()
        }
    }

    @Test
    void testEncode() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = new GenericParser().parse(msgText)
        assertEquals 'ADT^A01', msg.MSH.messageType.encode() 
        assertEquals 'MSH|^~\\&|SAP-ISH|HZL|||20040805152637||ADT^A01|123456|T|2.2|||ER', msg.MSH.encode()     	
    }
	
	// Parse a message with a ModelClassFactory that dynamically loads Groovy HL7 Model Classes.
	// This tests the Message#addSegment extension
    // TODO dynamically loaded Groovy files seem 
    @Test
	void testParseWithCustomGroovyClasses() {
		String msgText = this.class.classLoader.getResource('msg-09.hl7')?.text
		def customModelClasses = ['2.5' : [customGroovyPackageName] as String[]]
		ModelClassFactory customFactory = new GroovyCustomModelClassFactory(customModelClasses)
		Parser parser = new PipeParser(customFactory)
		Message hapiMessage = parser.parse(msgText)
		Segment s = hapiMessage.get('ZBE')
		assertTrue s.class.name.contains(customGroovyPackageName)
		assertEquals '1234', Terser.get(s, 1, 0, 1, 1)
	}
}

