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
package org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01;

import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

import ca.uhn.hl7v2.HL7Exception;

/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01MSHValidatorTest extends AbstractPCD01ValidatorTest {

    @Test
    public void testMaximalMessage() {
        validate(maximumMessage);
    }

    // ################ MSH Segment tests ###############################
    @Test(expected = ValidationException.class)
    public void testMissingMSH3() {
        validate(maxMsgReplace("AcmeInc^ACDE48234567ABCD^EUI-64", ""));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH7() {
        validate(maxMsgReplace("20090713090030+0500", ""));
    }

    @Test(expected = HL7Exception.class)
    public void testMissingMSH9() {
        validate(maxMsgReplace("ORU^R01^ORU_R01", ""));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH9dot1() {
        validate(maxMsgReplace("ORU^R01^ORU_R01", "^R01^ORU_R01"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH9dot1_9dot3() {
        validate(maxMsgReplace("ORU^R01^ORU_R01", "^R01"));
    }

    @Test(expected = Exception.class)
    public void testMissingMSH9dot2_9dot3() {
        validate(maxMsgReplace("ORU^R01^ORU_R01", "ORU"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH10() {
        validate(maxMsgReplace("MSGID1234", ""));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH11() {
        validate(maxMsgReplace("P", ""));
    }

    @Test(expected = ValidationException.class)
    public void testWrongMSH11() {
        validate(maxMsgReplace("P", "X"));
    }

    @Test(expected = HL7Exception.class)
    public void testMissingMSH12() {
        validate(maxMsgReplace("2.6", ""));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH21() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               ""));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot1() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "^HL7^2.16.840.1.113883.9.n.m^HL7"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot2_3_4() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot2_3() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^^^HL7"));
    }

}