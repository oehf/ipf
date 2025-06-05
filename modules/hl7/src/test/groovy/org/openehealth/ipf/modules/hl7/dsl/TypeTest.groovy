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
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.v24.segment.OBX
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.junit.jupiter.api.Assertions.*
import static org.openehealth.ipf.modules.hl7.dsl.TestUtils.*
import ca.uhn.hl7v2.model.v24.message.ORU_R01

/**
 * @author Mitko Kolev
 *
 */
class TypeTest {

    Segment obx1
    Segment obx2
    Segment obx3
    Segment obr
    ORU_R01 msg2 = load('dsl/msg-02.hl7')

    @BeforeEach
    void setUp() {
        obr = msg2.PATIENT_RESULT.ORDER_OBSERVATION.OBR
        obx1 = msg2.PATIENT_RESULT.ORDER_OBSERVATION.OBSERVATION.OBX
        obx2 = msg2.PATIENT_RESULT.ORDER_OBSERVATION(1).OBSERVATION.OBX
        obx3 = msg2.PATIENT_RESULT.ORDER_OBSERVATION(1).OBSERVATION(1).OBX
    }

    @Test
    void testIsEmptyOBX1() {
        assertFieldEquals('1', obx1, 1)
        assertFieldEquals('NM', obx1, 2)
        assertFieldEquals('25026500^CREATININE, RANDOM URINE^^25026500^CREATININE, RANDOM URINE', obx1, 3)
        assertFieldsEmpty(obx1, 4)

        //should be [100~200~300] in message
        assertEquals('100', obx1[5](0).value)
        assertEquals('200', obx1[5](1).value)
        assertEquals('300', obx1[5](2).value)
        assertFalse(obx1[5].empty)
        assertTrue((boolean)obx1[5])

        assertFieldEquals('MG/DL', obx1, 6)
        assertFieldEquals('20-370', obx1, 7)
        assertFieldEquals('N', obx1, 8)
        assertFieldsEmpty(obx1, 9, 10)
        assertFieldEquals('F', obx1, 11)
        assertFieldsEmpty(obx1, 12, 13)
        assertFieldEquals('20050914130800', obx1, 14)
        assertFieldEquals('HL^^L', obx1, 15)
        assertFieldsEmpty(obx1, 16, 17)
    }

    @Test
    void testIsEmptyOBX2() {
        assertFieldEquals('1', obx2, 1)
        assertFieldEquals('NM', obx2, 2)
        assertFieldEquals('50060710^MICROALBUMIN^^50060710^MICROALBUMIN', obx2, 3)
        assertFieldsEmpty(obx3, 4)

        Repeatable field = obx2[5]
        assertEquals('400', field.elementAt(0).value)
        assertFalse(field.isEmpty())
        assertTrue((boolean)field)

        assertFieldEquals('MG/DL', obx2, 6)
        assertFieldsEmpty(obx2, 7, 8, 9, 10)
        assertFieldEquals('F', obx2, 11)
        assertFieldsEmpty(obx2, 12, 13)
        assertFieldEquals('20050914130800', obx2, 14)
        assertFieldEquals('HL^^L', obx2, 15)
        assertFieldsEmpty(obx2, 16, 17)
    }

    @Test
    void testIsEmptyOBX3() {
        assertFieldEquals('2', obx3, 1)
        assertFieldEquals('NM', obx3, 2)
        assertFieldEquals('50061100^MICROALBUMIN/CREATININE RATIO, RANDOM URINE^^50061100^MICROALBUMIN/CREATININE RATIO, RANDOM URINE', obx3, 3)
        assertFieldsEmpty(obx3, 4)

        Repeatable field = obx3[5]
        assertEquals('4', field.elementAt(0).value)
        assertFalse(field.isEmpty())
        assertTrue((boolean)field)

        assertFieldEquals('MCG/MG CREAT', obx3, 6)
        assertFieldEquals('<30', obx3, 7)
        assertFieldEquals('N', obx3, 8)
        assertFieldsEmpty(obx3, 9, 10)
        assertFieldEquals('F', obx3, 11)
        assertFieldsEmpty(obx3, 12, 13)
        assertFieldEquals('20050914130800', obx3, 14)

        assertTrue(obx3[15] instanceof Composite)
        assertFieldEquals('HL^^L', obx3, 15)
        assertFieldsEmpty(obx3, 16, 17)
    }

    @Test
    void testDelimetersAreRemoved() {
        String msgCopy = msg2.copy().toString()
        msgCopy = msgCopy.replace('25026500^CREATININE, RANDOM URINE^^25026500^CREATININE, RANDOM URINE', '^^^^')
        ORU_R01 emptyObx3Msg = make(msgCopy)
        OBX emptyObx3 = emptyObx3Msg.PATIENT_RESULT.ORDER_OBSERVATION.OBSERVATION.OBX
        String val = emptyObx3[3].encode()
        assertFieldsEmpty(emptyObx3, 3)
    }

    @Test
    void testIsEmptyMsg2OBR() {
        assertFieldEquals('1', obr, 1)
        assertFieldEquals('0007111', obr, 2)
        assertFieldEquals('HL007545P', obr, 3)
        assertFieldEquals('6517SBX=^MICROALBUMIN/CREATININE RATIO, RANDOM URINE ^^6517SBX=^MICROALBUMIN/CREATININE RATIO, RANDOM URINE ', obr, 4)
        assertFieldsEmpty(obr, 5, 6)
        assertFieldEquals('20050912100000', obr, 7)
        assertFieldsEmpty(obr, 8, 9, 10, 11, 12, 13)
        assertFieldEquals('20050912112200', obr, 14)
        assertFieldsEmpty(obr, 15)
        assertFieldEquals('B95594^UNKNOWN^UNKNOWN', obr, 16)
        assertFieldsEmpty(obr, 17, 18, 19)
    }

    void assertFieldEquals(String expected, data, int field) {
        String simpleName = data.getClass().getSimpleName()
        assertEquals(expected, data[field].encode())
        assertFalse(data[field].empty, "${simpleName}[${field}] must be not empty, but isEmpty() returns true")
        assertTrue((boolean)data[field])
    }

    void assertFieldsEmpty(Segment segment, int... fields) {
        String simpleName = segment.getClass().getSimpleName()
        for (field in fields) {
            assertTrue(segment[field].empty, "${simpleName}[${field}] must be empty, but isEmpty() returns false")
            if (segment[field]) {
                fail("${simpleName}[${field}] must be empty, but asBoolean() returns true")
            }
        }
    }

}
