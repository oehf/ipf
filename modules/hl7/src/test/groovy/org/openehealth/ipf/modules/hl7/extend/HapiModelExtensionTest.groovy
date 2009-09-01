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
import ca.uhn.hl7v2.model.Message

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
	
    def mappingService
	def defMappingExtension
    def hl7MappingExtension

    static {
        ExpandoMetaClass.enableGlobally()
    }
    
    void setUp() {
        mappingService = new BidiMappingService()
        mappingService.setMappingScript(new ClassPathResource("example2.map"))
        defMappingExtension = new MappingExtension()
        hl7MappingExtension = new HapiModelExtension()
        defMappingExtension.mappingService = mappingService
        hl7MappingExtension.mappingService = mappingService
        defMappingExtension.extensions.call()
        hl7MappingExtension.extensions.call()
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

