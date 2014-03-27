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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message;


import org.junit.Test;
import ca.uhn.hl7v2.parser.Parser;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Whether the custom Parser/CustomModelClassFactory are also copied on message-copy
 *
 * @author Boris Stanojevic
 */
public class CustomMessageCopyTest {

    private static final Parser PARSER = CustomModelClassUtils.createParser("pdq", "2.5");

    @Test
    public void testCopyDefaultQBP(){
        MessageAdapter QBP_Q21_copy = MessageAdapters.load("qbp.hl7").copy();
        assertTrue(QBP_Q21_copy.getGroup() instanceof ca.uhn.hl7v2.model.v25.message.QBP_Q21);
        assertTrue(
            ((ca.uhn.hl7v2.model.v25.message.QBP_Q21)QBP_Q21_copy.getGroup()).getQPD()
                    instanceof ca.uhn.hl7v2.model.v25.segment.QPD);
    }

    @Test
    public void testCopyCustomQBP(){
        MessageAdapter QBP_Q21_copy = MessageAdapters.load(PARSER, "qbp.hl7").copy();
        assertTrue(QBP_Q21_copy.getGroup() instanceof QBP_Q21);
        assertTrue(((QBP_Q21) QBP_Q21_copy.getGroup()).getQPD() instanceof QPD);

        assertEquals(PARSER, QBP_Q21_copy.getParser());
        assertEquals(PARSER.getFactory(), QBP_Q21_copy.getParser().getFactory());
    }

}
