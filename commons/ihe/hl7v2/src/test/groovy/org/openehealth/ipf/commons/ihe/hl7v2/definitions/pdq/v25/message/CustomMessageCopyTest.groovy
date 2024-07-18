/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.parser.Parser
import ca.uhn.hl7v2.parser.PipeParser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertTrue

/**
 * Whether the custom Parser/CustomModelClassFactory are also copied on message-copy
 *
 * @author Boris Stanojevic
 */
class CustomMessageCopyTest {

    private static final Parser PARSER = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pdq", "2.5"),
            PixPdqTransactions.ITI21).pipeParser
    private String msg

    @BeforeEach
    void setup() {
        msg = getClass().getResourceAsStream("/qbp.hl7").text
    }

    @Test
    void testCopyDefaultQBP() throws HL7Exception {
        ca.uhn.hl7v2.model.v25.message.QBP_Q21 copy = new PipeParser().parse(msg).copyMessage()
        assertTrue(copy.getQPD() instanceof ca.uhn.hl7v2.model.v25.segment.QPD)
    }

    @Test
    void testCopyCustomQBP(){
        QBP_Q21 copy = PARSER.parse(msg).copyMessage()
        assertTrue(copy.getQPD() instanceof QPD)
        assertEquals(PARSER, copy.getParser())
        assertEquals(PARSER.getFactory(), copy.getParser().getFactory())
    }

}
