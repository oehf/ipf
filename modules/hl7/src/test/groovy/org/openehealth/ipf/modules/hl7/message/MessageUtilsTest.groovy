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

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.v22.message.ADT_A04
import ca.uhn.hl7v2.model.v25.datatype.CE
import ca.uhn.hl7v2.model.v25.datatype.SI
import ca.uhn.hl7v2.model.v25.message.ADT_A01
import ca.uhn.hl7v2.model.v25.segment.NK1
import ca.uhn.hl7v2.model.v251.message.ACK
import ca.uhn.hl7v2.parser.ModelClassFactory
import ca.uhn.hl7v2.parser.Parser
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.modules.hl7.parser.CustomModelClassFactory

/**
 * @author Christian Ohr
 *
 */
public class MessageUtilsTest {

    private Parser parser
    private HapiContext context
    
    @Before
    void setUp() throws Exception {
        ModelClassFactory factory = new CustomModelClassFactory()
        context = new DefaultHapiContext(factory)
        parser = context.getGenericParser()
    }

    @Test
    void testAck() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = parser.parse(msgText)
        Message response = msg.generateACK()
        assert response.MSH.messageType.messageType.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'A01'
    }

    @Test
    void testNak() {
        String msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message msg = parser.parse(msgText)
        Message response = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception('Some bad |&^\n mistake'))
        assert response instanceof Message
        assert response.MSH.messageType.messageType.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'A01'
        assert response.MSA.acknowledgementCode.value == 'AR'
        Message reparsed = parser.parse(parser.encode(response))
        assert reparsed instanceof Message
    }
    
    @Test
    void testAck25() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        Message response = msg.generateACK()
        assert response.MSH.messageType.messageCode.value == 'ACK'
        assert response.MSH.messageType.triggerEvent.value == 'Q22'
    }
    
    @Test
    void testResponseVersion22AckStructure() {
        String origMsgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        Message origMsg = parser.parse(origMsgText)
        Message respMsg = MessageUtils.response(origMsg, 'ACK', null)
        assert parser.encode(respMsg).contains('|ACK^A01|')
    }
    
    @Test
    void testResponseVersion25AckStructure() {
        String origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message origMsg = parser.parse(origMsgText)
        Message respMsg = MessageUtils.response(origMsg, 'ACK', null)
        assert parser.encode(respMsg).contains('|ACK^Q22|')
    }
    
    @Test
    void testResponseVersion25MappedStructureName() {
        String origMsgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message origMsg = parser.parse(origMsgText)
        Message respMsg = MessageUtils.response(origMsg, 'RSP', 'K22')
        assert parser.encode(respMsg).contains('|RSP^K22^RSP_K21|')
    }
    
    @Test
    void testMakeMessageVersion22() {
        Message msg = MessageUtils.newMessage(context, 'ADT_A04', '2.2')
        assert msg instanceof ADT_A04
        String encoded = parser.encode(msg)
        assert encoded.contains('|ADT^A04|')
    }
    
    @Test
    void testMakeMessageVersion25() {
        Message msg = MessageUtils.newMessage(context, 'ADT_A04', '2.5')
        assert msg instanceof ADT_A01
        String encoded = parser.encode(msg)
        assert encoded.contains('|ADT^A04^ADT_A01|')
    }
    
    @Test
    void testMakeNK1SegmentVersion25() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        Segment segment = MessageUtils.newSegment('NK1', msg)
        assert segment instanceof NK1
    }
    
    @Test
    void testMakeNAKVersion251() {
        String msgText = this.class.classLoader.getResource('msg-10.hl7')?.text
        Message msg = parser.parse(msgText)
        Message nak = msg.generateACK(AcknowledgmentCode.AR, new HL7Exception('Some bad |&^\r\n mistake'))
        assert nak instanceof ACK
        String encoded = parser.encode(nak)
        assert encoded.contains('|ACK^R01^ACK|')
    }
    
    @Test
    void testMakeCECompositeVersion25() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        Composite type = MessageUtils.newComposite('CE', msg, [identifier:'BRO'])
        assert type instanceof CE
        assert type.identifier.value == 'BRO'
    }
    
    @Test
    void testMakeSIPrimitiveVersion25() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        Primitive si = MessageUtils.newPrimitive('SI', msg, '1')
        assert si instanceof SI
        assert si.value == '1'
    }
    
    @Test
    void testPipeEncode() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        assert MessageUtils.pipeEncode(msg.MSH) == 'MSH|^~\\&|MESA_PD_CONSUMER|MESA_DEPARTMENT|MESA_PD_SUPPLIER|XYZ_HOSPITAL|||QBP^Q22|11350110|P|2.5'
        assert MessageUtils.pipeEncode(msg.MSH.messageType) == 'QBP^Q22'
    }
    
    @Test
    void testEventType() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        assert 'QBP' == MessageUtils.eventType(msg)
    }
    
    @Test
    void testTriggerEvent() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        assert 'Q22' == MessageUtils.triggerEvent(msg)
    }
    
    @Test
    void testMessageStructure() {
        String msgText = this.class.classLoader.getResource('msg-03.hl7')?.text
        Message msg = parser.parse(msgText)
        assert 'QBP_Q21' == MessageUtils.messageStructure(msg)
        msgText = this.class.classLoader.getResource('msg-01.hl7')?.text
        msg = parser.parse(msgText)
        assert 'ADT_A01' == MessageUtils.messageStructure(msg)
    }
    
    @Test
    void testEventTypeUnknownMessage() {
        String msgText = this.class.classLoader.getResource('msg-04.hl7')?.text
        Message msg = parser.parse(msgText)
        assert 'ABC' == MessageUtils.eventType(msg)
    }
    
    @Test
    void testTriggerEventUnknownMessage() {
        String msgText = this.class.classLoader.getResource('msg-04.hl7')?.text
        Message msg = parser.parse(msgText)
        assert 'XYZ' == MessageUtils.triggerEvent(msg)
    }
}
