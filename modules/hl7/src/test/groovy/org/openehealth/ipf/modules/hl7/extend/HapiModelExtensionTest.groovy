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

import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.model.v25.segment.NK1

import org.openehealth.ipf.commons.core.extend.DefaultActivator
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.extend.MappingExtension
import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7.HL7v2Exception

import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
public class HapiModelExtensionTest extends GroovyTestCase {
	
    static def mappingService
	static def defMappingExtension
    static def hl7MappingExtension

    static {
        ExpandoMetaClass.enableGlobally()
    }
    
    void setUp() {
        if (mappingService) return
        mappingService = new BidiMappingService()
        mappingService.setMappingScript(new ClassPathResource("example2.map"))
        defMappingExtension = new MappingExtension()
        hl7MappingExtension = new HapiModelExtension()
        defMappingExtension.mappingService = mappingService
        hl7MappingExtension.mappingService = mappingService
        DefaultActivator activator = new DefaultActivator()
        activator.activate(defMappingExtension)
        activator.activate(hl7MappingExtension)
    }
	
    void testMatches() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	assert msg.matches('*', '*', '*')
    	assert msg.matches('ADT', 'A01', '*')
    	assert msg.matches('ADT', 'A01', '2.2')
    	assert msg.matches('ADT', 'A02', '*') == false
    	assert msg.matches('ADT', 'A01', '2.3') == false
        msgText = this.class.classLoader.getResource('msg-02.hl7')?.text
        msg = new GenericParser().parse(msgText)        
    	assert msg.matches('*', '*', '*')
    	assert msg.matches('ORU', 'R01', '*')
    	assert msg.matches('ORU', 'R01', '2.4')
    	assert msg.matches('ORU', 'A02', '*') == false
    	assert msg.matches('ORU', 'R01', '2.3') == false
    }
    
    void testAck() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	def ack = msg.ack()
    	assert ack.MSH.messageType.messageType.value == 'ACK'
    	assert ack.MSA.acknowledgementCode.value == 'AA'
    }

    void testNak() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	def nak = msg.nak(new HL7v2Exception("blarg", 204), AckTypeCode.AR)
    	assert nak.MSH.messageType.messageType.value == 'ACK'
    	assert nak.MSA.acknowledgementCode.value == 'AR'
    	assert nak.MSA.acknowledgementCode.value == 'AR'
    	//assert nak.MSA.textMessage.value == "blarg"
    	//println new GenericParser().encode(nak)
    }
    
    void testNakCause() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	def nak = msg.nak("blarg", AckTypeCode.AR)
    	assert nak.MSH.messageType.messageType.value == 'ACK'
    	assert nak.MSA.acknowledgementCode.value == 'AR'
    	//assert nak.MSA.textMessage.value == "blarg"
        //println new GenericParser().encode(nak)
    }

    void testNak25() {
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	def nak = msg.nak(new HL7v2Exception("blarg", 204), AckTypeCode.AR)
    	assert nak.MSH.messageType.messageCode.value == 'ACK'
    	assert nak.MSA.acknowledgmentCode.value == 'AR'
    	//assert nak.MSA.textMessage.value == "blarg"
        //println new GenericParser().encode(nak)
    }
    
    void testNak25Cause() {
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
    	def nak = msg.nak("blarg", AckTypeCode.AR)
    	assert nak.MSH.messageType.messageCode.value == 'ACK'
    	assert nak.MSA.acknowledgmentCode.value == 'AR'
    	//assert nak.MSA.textMessage.value == "blarg"
        //println new GenericParser().encode(nak)
    }
    
    void testDefaultNak() {
    	def nak = Message.defaultNak(new HL7v2Exception("blarg", 204), AckTypeCode.AE, "2.4")
    	assert nak.MSH.messageType.messageType.value == 'ACK'
    	assert nak.MSA.acknowledgementCode.value == 'AE'
    	assert nak.MSA.textMessage.value == "blarg"
        //println new GenericParser().encode(nak)
    }

    void testDefaultNak25() {
    	def nak = Message.defaultNak(new HL7v2Exception("blarg", 204), AckTypeCode.AE, "2.5")
    	assert nak.MSH.messageType.messageCode.value == 'ACK'
    	assert nak.MSA.acknowledgmentCode.value == 'AE'
    	assert nak.MSA.textMessage.value == "blarg"
        //println new GenericParser().encode(nak)
    }
    
    void testMakeMessage() {
        def msg = Message.ADT_A01('2.5')
    	assert msg.MSH.messageType.messageCode.value == 'ADT'
        assert msg.MSH.messageType.triggerEvent.value == 'A01'
        assert msg.MSH.versionID.versionID.value == '2.5'
    }

    void testMakeSegment() {
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = new GenericParser().parse(msgText)
        def nk1 = Segment.NK1(msg)
    	assert nk1 instanceof NK1
    }

    void testMakeComposite() {
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
        def ce = Composite.CE(msg, [identifier:'BRO'])
        assert ce.identifier.value == 'BRO'
   }

    void testMakePrimitive() {
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
        def si = Primitive.SI(msg, '1')
        assert si.value == '1'
    }

    void testHAPIExampleCreateMessageFromScratch() {
        def msg = Message.ADT_A01('2.4')
        msg.MSH.with {
            sendingApplication.namespaceID.value = 'TestSendingSystem' 
            sequenceNumber.value = '123'
        }
        msg.PID.with {
            getPatientName(0).familyName.surname.value = 'Doe'
            getPatientName(0).givenName.value = 'John'
        	getPatientIdentifierList(0).ID.value = '123456'
        }
        // println "Printing ER7 Encoded Message:"
        // println new PipeParser().encode(msg)
    }
    
    void testHAPIExamplePopulateOBX() {
        def msg = Message.ORU_R01('2.5')
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
                def ce = Composite.CE(msg, [identifier:'T57000', text:'GALLBLADDER', nameOfCodingSystem:'SNM'])
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
                def st = Primitive.ST(msg, 'MDT')
                observationIdentifier.identifier.extraComponents.getComponent(0).data = st
                
                // The first OBX has a value type of TX. So first, we populate OBX-2 with "TX"...
                valueType.value = 'TX'
                def tx = Primitive.TX(msg, 'MICROSCOPIC EXAM SHOWS HISTOLOGICALLY NORMAL GALLBLADDER TISSUE')
                getObservationValue(0).data = tx
            }
        }
        // println "Printing ER7 Encoded Message:"
        // println new PipeParser().encode(msg)
    }
            
        
    void testList() {
        assert ['a','b'].map('listTest') == ['c','d'] 
        assert ['x','y'].map('listTest', ['a','b']) == (['x','y'].map('listTest') ?: ['a','b'])
        assert ['x','y'].map('listTest2') == ['c','d']
        assert ['x','y'].map('listTest2', ['a','b']) == ['c','d']
        def x = new ca.uhn.hl7v2.model.v22.datatype.ID(null, 100)
    	def y = new ca.uhn.hl7v2.model.v22.datatype.ID(null, 100)
    	x.setValue('a')
    	y.setValue('b')
    	assert [x,y].map('listTest') == ['c','d']    	
    }
    
    void testTypeMap() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)        
        assert msg.PV1.patientClass.map('encounterType') == 'IMP'
        //assert msg.PV1.patientClass.mapEncounterType() == 'IMP'
        //assert msg.MSH.messageType.mapMessageType() == 'PRPA_IN402001'
    }
        
    void testKeyAndValueSystems() {
        assert 'encounterType'.keySystem() == '2.16.840.1.113883.12.4'
        assert 'encounterType'.valueSystem() == '2.16.840.1.113883.5.4'
        try {
        	assert 'Y'.keySystem()
        	fail()
        } catch (IllegalArgumentException e) {
        	// o.k.
        }
    }

    void testEncode() {
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = new GenericParser().parse(msgText)
        assert msg.MSH.messageType.encode() == 'ADT^A01'
        assert msg.MSH.encode() == 'MSH|^~\\&|SAP-ISH|HZL|||20040805152637||ADT^A01|123456|T|2.2|||ER'    	
    }
}

