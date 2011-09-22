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
package org.openehealth.ipf.modules.hl7dsl.util

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.*
import static org.openehealth.ipf.modules.hl7dsl.util.Messages.*

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter

import ca.uhn.hl7v2.model.v22.message.ADT_A01
import ca.uhn.hl7v2.model.v24.message.ORU_R01

/**
 * @author Martin Krasser
 */
public class MessagesTest extends GroovyTestCase {

     MessageAdapter<ADT_A01> msg1
     MessageAdapter<ORU_R01> msg2
     
     void setUp() {
         msg1 = load('msg-01.hl7')
         msg2 = load('msg-04.hl7')
     }
     
     void testCopyMessage1() {
         def msg1Copy = msg1.empty()
         copyMessage(msg1.target, msg1Copy.target)
         assert msg1.toString() == msg1Copy.toString()
     }
    
     void testCopyMessage2() {
         def msg2Copy = msg2.empty()
         copyMessage(msg2.target, msg2Copy.target)
         assert msg2.toString() == msg2Copy.toString()
     }
    
}
