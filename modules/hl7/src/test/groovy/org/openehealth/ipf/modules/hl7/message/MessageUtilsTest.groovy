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

import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory

import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.model.v22.message.ADT_A04
import ca.uhn.hl7v2.model.v25.datatype.CE
import ca.uhn.hl7v2.model.v25.datatype.SI
import ca.uhn.hl7v2.model.v25.message.ADT_A01
import ca.uhn.hl7v2.model.v25.segment.NK1
import ca.uhn.hl7v2.model.v251.message.ACK
import ca.uhn.hl7v2.parser.*

/**
 * @author Christian Ohr
 *
 */
public class MessageUtilsTest extends GroovyTestCase {
    
    private ModelClassFactory factory
    
    void setUp() throws Exception {
        factory = new CustomModelClassFactory()
    }
    
    void testAck() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = parser.parse(msgText)
        def response = MessageUtils.ack(factory, msg)
        assert response.MSH.messageType.messageType.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'A01'
    }
    
    void testNak() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def msg = parser.parse(msgText)
        def response = MessageUtils.nak(factory, msg, 'Some bad |&^\r\n mistake', AckTypeCode.AR)
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
        def response = MessageUtils.ack(factory, msg)
        assert response.MSH.messageType.messageCode.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'Q22'
    }
    
    void testResponseVersion22AckStructure() {
        def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(factory, origMsg, 'ACK', null)
        assert parser.encode(respMsg).contains('|ACK^A01|')
    }
    
    void testResponseVersion25AckStructure() {
        def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(factory, origMsg, 'ACK', null)
        assert parser.encode(respMsg).contains('|ACK^Q22^ACK|')
    }
    
    void testResponseVersion25MappedStructureName() {
        def parser = new GenericParser()
        def origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def origMsg = parser.parse(origMsgText)
        def respMsg = MessageUtils.response(factory, origMsg, 'RSP', 'K22')
        assert parser.encode(respMsg).contains('|RSP^K22^RSP_K21|')
    }
    
    void testMakeMessageVersion22() {
        def msg = MessageUtils.newMessage(factory, 'ADT_A04', '2.2')
        def parser = new GenericParser()
        assert msg instanceof ADT_A04
        def encoded = parser.encode(msg)
        assert encoded.contains('|ADT^A04|')
    }
    
    void testMakeMessageVersion25() {
        def msg = MessageUtils.newMessage(factory, 'ADT_A04', '2.5')
        def parser = new GenericParser()
        assert msg instanceof ADT_A01
        def encoded = parser.encode(msg)
        assert encoded.contains('|ADT^A04^ADT_A01|')
    }
    
    void testMakeNK1SegmentVersion25() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        def segment = MessageUtils.newSegment(factory, 'NK1', msg)
        assert segment instanceof NK1
    }
    
    void testMakeNAKVersion251() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-10.hl7')?.text
        def msg = parser.parse(msgText)
        def nak = MessageUtils.nak(factory, msg, 'Some bad |&^\r\n mistake', AckTypeCode.AR)
        assert nak instanceof ACK
        def encoded = parser.encode(nak)
        assert encoded.contains('|ACK^R01^ACK|')
    }
    
    void testMakeCECompositeVersion25() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        def type = MessageUtils.newComposite(factory, 'CE', msg, [identifier:'BRO'])
        assert type instanceof CE
        assert type.identifier.value == 'BRO'
    }
    
    void testMakeSIPrimitiveVersion25() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        def si = MessageUtils.newPrimitive(factory, 'SI', msg, '1')
        assert si instanceof SI
        assert si.value == '1'
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
    
    void testMessageStructure() {
        def parser = new GenericParser()
        def msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        def msg = parser.parse(msgText)
        assert 'QBP_Q21' == MessageUtils.messageStructure(msg)
        msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        msg = parser.parse(msgText)
        assert 'ADT_A01' == MessageUtils.messageStructure(msg)
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
