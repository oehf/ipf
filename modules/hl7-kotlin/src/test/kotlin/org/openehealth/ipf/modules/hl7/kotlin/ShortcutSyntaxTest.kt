/*
 * Copyright 2018 the original author or authors.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *         http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin

import ca.uhn.hl7v2.DefaultHapiContext
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.v231.message.ADT_A40
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Christian Ohr
 */
class ShortcutSyntaxTest {

    val context = DefaultHapiContext()
    private val msg1: Message = loadHl7(context, "/msg-01.hl7")
    private val msg2: Message = loadHl7(context, "/msg-06.hl7")
    private val msg3: Message = loadHl7(context, "/msg-02.hl7")
    

    @Test
    fun testShortCut22() {
        assertEquals( "Nachname", msg1["NK1"](0)[2][1].value)
        assertEquals( "Nachname", msg1["NK1"][2][1].value)
        assertEquals( "Nachname",  msg1["NK1"][2].value)

        // PID-5 in HL7v2 is not repeatable, and the family name component is a primitive (ST)
        assertEquals( "Nachname" ,  msg1["PID"][5][1].value)
        assertEquals( "Vorname"  ,  msg1["PID"][5][2].value)
        assertEquals( "Nachname" ,  msg1["PID"][5].value)
    }

    @Test
    fun testShortCut25OnRepeatableSegment() {
        assertEquals( "Nachname" ,  msg2["NK1"](0)[2][1][1].value)
        assertEquals( "Nachname" ,  msg2["NK1"](0)[2][1].value)
        assertEquals( "Nachname" ,  msg2["NK1"][2][1].value)
        assertEquals( "Nachname" ,  msg2["NK1"][2].value)
        assertEquals( "333-4444" ,  msg2["NK1"](0)[5](0)[1].value)
        assertEquals( "333-5555" ,  msg2["NK1"](0)[5](1)[1].value)
        assertEquals( "333-4444" ,  msg2["NK1"][5].value)
        assertEquals( "333-5555" ,  msg2["NK1"][5](1).value)
    }

    @Test
    fun testShortCut25OnRepeatableField() {
        // PID-5 in HL7v2 is repeatable, and the family name component is again a component (FN)
        assertEquals( "Nachname" ,  msg2["PID"][5](0)[1][1].value)
        assertEquals( "Nachname" ,  msg2["PID"][5](0)[1].value)
        assertEquals( "Nachname" ,  msg2["PID"][5][1].value)
        assertEquals( "Nachname" ,  msg2["PID"][5].value)
        assertEquals( "Vorname"  ,  msg2["PID"][5](0)[2].value)
        assertEquals( "Vorname"  ,  msg2["PID"][5][2].value)
        assertEquals( "Vorname"  ,  msg2["PID"][5][2].value)
    }

    @Test
    fun testShortCut25OnRepeatableGroup() {
        assertEquals( "TEST" ,  msg3["PATIENT_RESULT", 0]["PATIENT"]["PID"][5][1].value)
        assertEquals( "TEST" ,  msg3["PATIENT_RESULT"]["PATIENT"]["PID"][5][1].value)
        assertEquals( "TEST" ,  msg3["PATIENT_RESULT"]["PATIENT"]["PID"][5].value)
    }

    @Test
    fun testWriteShortCutOnRepeatableField() {
        // PID-5 in HL7v2 is repeatable, and the family name component is again a component (FN)
        msg2["PID"][5](0)[1][1] = "Nachname1"
        assertEquals( "Nachname1" ,  msg2["PID"][5][1].value)
        msg2["PID"][5][1][1] = "Nachname2"
        assertEquals( "Nachname2" ,  msg2["PID"][5][1].value)
        msg2["PID"][5][1] = "Nachname3"
        assertEquals( "Nachname3" ,  msg2["PID"][5][1].value)
        msg2["PID"][5] = "Nachname4"
        assertEquals( "Nachname4" ,  msg2["PID"][5][1].value)
        msg2["PID"][5][1] = msg2["PID"][5][2]
        assertEquals( "Vorname" ,  msg2["PID"][5][1].value)
        msg2["PID"][5][1] = msg2["NK1"][2]
        assertEquals( "Nachname" ,  msg2["PID"][5][1].value)
        msg2["PID"][5] = msg2["NK1"][2]
        assertEquals( "Nachname" ,  msg2["PID"][5][1].value)
    }

    @Test
    fun testWriteShortCutOnRepeatableSegment() {
        msg2["NK1"](0)[2][1][1] = "Nachname1"
        assertEquals( "Nachname1" ,  msg2["NK1"][2][1].value)
        msg2["NK1"](0)[2][1] = "Nachname2"
        assertEquals( "Nachname2" ,  msg2["NK1"][2][1].value)
        msg2["NK1"][2][1] = "Nachname3"
        assertEquals( "Nachname3" ,  msg2["NK1"][2][1].value)
        msg2["NK1"][2] = "Nachname4"
        assertEquals( "Nachname4" ,  msg2["NK1"][2][1].value)
    }

    @Test
    fun testUndesiredReplication() {
        val adt = ADT_A40()
        val grp = adt["PIDPD1MRGPV1"]
        grp["PID"][5][1] = "X"
        grp["PID"][5][2] = "Y"
        assertEquals(1, adt.count("PIDPD1MRGPV1"))
        assertEquals("X", adt["PIDPD1MRGPV1"]["PID"][5][1].value)
    }
    
}