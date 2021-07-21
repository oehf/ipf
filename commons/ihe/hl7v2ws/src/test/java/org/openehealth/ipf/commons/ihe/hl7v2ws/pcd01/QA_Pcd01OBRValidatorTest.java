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

import ca.uhn.hl7v2.HL7Exception;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Kingsley Nwaigbo
 * 
 */
@Disabled
public class QA_Pcd01OBRValidatorTest extends AbstractPCD01ValidatorTest {

    @Test
    public void testMaximalMessage() throws HL7Exception {
        validate(maximumMessage);
    }

    // ################ OBR Segment tests ###############################
    @Test
    public void testMissingOBR1() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("OBR|1|", "OBR||")));
    }

    @Test
    public void testWrongSetIDForOBR1() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("OBR|2|AB112233", "OBR|1|AB112233")));
    }

    @Test
    public void testMissingOBR3() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", "")));
    }

    @Test
    public void testSpaceAsOBR3() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64",
                " ")));
    }

    @Test
    public void testEmptyOBR3_1() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64",
                "^AcmeAHDInc^ACDE48234567ABCD^EUI-64")));
    }

    @Test
    public void testOnlyOBR31_32() throws HL7Exception {
        validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64",
                               "CD12345^AcmeAHDInc"));
    }

    @Test
    public void testOnlyOBR31_33_34() throws HL7Exception {
        validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64",
                               "CD12345^^ACDE48234567ABCD^EUI-64"));
    }

    @Test
    public void testOnlyOBR31() {
        assertThrows(ValidationException.class, () -> validate(maxMsgReplace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64",
                "CD12345")));
    }

    @Test
    public void testMissingOBR4() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", "")));
    }

    @Test
    public void testSpaceAsOBR4() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", " ")));
    }

    @Test
    public void testMissingOBR4_2() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC",
                "528391^^MDC")));
    }

    @Test
    public void testSpaceAsOBR4_2() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC",
                "528391^ ^MDC")));
    }

    @Disabled
    @Test
    public void testOBR7_olderThanOBR8() {
        assertThrows(HL7Exception.class, () -> validate(maxMsgReplace("|20090813095715+0500|20090813105715+0500",
                "|20100813095715+0500|20090813105715+0500")));
    }

}
