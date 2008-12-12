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
package org.openehealth.ipf.modules.hl7dsl

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*

/**
 * @author Christian Ohr
 */
public class ShortcutSyntaxTest extends GroovyTestCase{

     def msg1
     def msg2
     
     void setUp() {
         msg1 = load('msg-01.hl7')
         msg2 = load('msg-06.hl7')
     }
     
     void testShortCut22() {
         assert 'Nachname' == msg1.NK1(0)[2][1].value
         assert 'Nachname' == msg1.NK1[2][1].value
         assert 'Nachname' == msg1.NK1[2].value
         
         // PID-5 in HL7v2 is not repeatable, and the family name component is a primitive (ST)
         assert 'Nachname' == msg1.PID[5][1].value
         assert 'Vorname'  == msg1.PID[5][2].value
         assert 'Nachname' == msg1.PID[5].value
     }
     
     void testShortCut25() {
         assert 'Nachname' == msg2.NK1(0)[2][1][1].value
         assert 'Nachname' == msg2.NK1(0)[2][1].value
         assert 'Nachname' == msg2.NK1[2][1].value
         assert 'Nachname' == msg2.NK1[2].value
         
         // PID-5 in HL7v2 is repeatable, and the family name component is again a component (FN)
         assert 'Nachname' == msg2.PID[5](0)[1][1].value
         assert 'Nachname' == msg2.PID[5](0)[1].value
         assert 'Nachname' == msg2.PID[5][1].value
         assert 'Nachname' == msg2.PID[5].value
         assert 'Vorname'  == msg2.PID[5](0)[2].value
         assert 'Vorname'  == msg2.PID[5][2].value
         assert 'Vorname'  == msg2.PID[5][2].value
     }
}
