/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.hl7v2.tracing

import ca.uhn.hl7v2.HapiContext
import ca.uhn.hl7v2.model.Message
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.modules.hl7.message.MessageUtils

import static org.junit.jupiter.api.Assertions.*

/**
 * @author Christian Ohr
 */
class Hl7MessageSetterGetterTest {

    private static final HapiContext CONTEXT = HapiContextFactory.createHapiContext()

    @Test
    void testInjectAndExtract() {
        Message r01 = MessageUtils.makeMessage(CONTEXT, 'ORU', 'R01','2.5')
        r01.addNonstandardSegment('ZTR')
        Hl7MessageSetter setter = new Hl7MessageSetter()
        setter.put(r01, 'key1', 'value1')
        setter.put(r01, 'key2', 'value2')
        setter.put(r01, 'key3', 'value~with^reserved|characters')

        assertNotNull(r01.get('ZTR'))

        Hl7MessageGetter getter = new Hl7MessageGetter()
        assertEquals('value1', getter.get(r01, 'key1'))
        assertEquals('value2', getter.get(r01, 'key2'))
        assertEquals('value~with^reserved|characters', getter.get(r01, 'key3'))
        assertNull(getter.get(r01, 'key4'))
    }
}
