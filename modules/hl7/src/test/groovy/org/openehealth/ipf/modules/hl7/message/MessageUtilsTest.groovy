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
package org.openehealth.ipf.modules.hl7.message

import ca.uhn.hl7v2.parser.*
import ca.uhn.hl7v2.model.*
import org.openehealth.ipf.modules.hl7.AckTypeCode

/**
 * @author Christian Ohr
 *
 */
public class MessageUtilsTest extends GroovyTestCase {
	
	void testAck() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = parser.parse(msgText)        
        def response = MessageUtils.ack(msg)
        assert response.MSH.messageType.messageType.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'A01'
 	}
	
	void testNak() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = parser.parse(msgText)        
        def response = MessageUtils.nak(msg, 'Some bad |&^\r\n mistake', AckTypeCode.AR)
        assert response instanceof Message
        assert response.MSH.messageType.messageType.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'A01'
        assert response.MSA.acknowledgementCode.value == 'AR'
        def reparsed = parser.parse(parser.encode(response))
        assert reparsed instanceof Message
	}

	void testAck25() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)        
        def response = MessageUtils.ack(msg)
        assert response.MSH.messageType.messageCode.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'Q22'
	}
	
	void testResponseVersion22AckStructure() {
		def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(origMsg, 'ACK', null)        
        assert parser.encode(respMsg).contains('|ACK^A01|')
	}
	
	void testResponseVersion25AckStructure() {
		def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(origMsg, 'ACK', null)        
        assert parser.encode(respMsg).contains('|ACK^Q22^ACK|')
	}
	
	void testResponseVersion25MappedStructureName() {
		def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(origMsg, 'RSP', 'K22')
        assert parser.encode(respMsg).contains('|RSP^K22^RSP_K21|')
	}
	
	void testPipeEncode() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        assert MessageUtils.pipeEncode(msg.MSH) == 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11350110|P|2.5'
        assert MessageUtils.pipeEncode(msg.MSH.messageType) == 'QBP^Q22'	
	}
	
	void testDump() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-02.hl7')?.text
        def msg = parser.parse(msgText)
        String s = MessageUtils.dump(msg)
	}
		
	void testEventType() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        assert 'QBP' == MessageUtils.eventType(msg)
	}
	
	void testTriggerEvent() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        assert 'Q22' == MessageUtils.triggerEvent(msg)
	}

	void testVersionUnknownMessage() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-04.hl7')?.text
        def msg = parser.parse(msgText)
        assert '2.5' == msg.version
	}
	
	void testEventTypeUnknownMessage() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-04.hl7')?.text
        def msg = parser.parse(msgText)
        assert 'ABC' == MessageUtils.eventType(msg)
	}
	
	void testTriggerEventUnknownMessage() {
		def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-04.hl7')?.text
        def msg = parser.parse(msgText)
        assert 'XYZ' == MessageUtils.triggerEvent(msg)
	}
}
