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

/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01DataTypesTest extends AbstractPCD01ValidatorTest {

    // ################ Data types ###############################

    @Test
    public void testCWE_AllFilled_MSH19() {
        validate(maxMsgReplace("EN^English^ISO659",
                               "EN^English^ISO659^en-US^US English^ISO659^1.0^2.8^Some text"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingCWE2() {
        validate(maxMsgReplace("EN^English^ISO659",
                               "EN^^ISO659^en-US^US English^ISO659^1.0^2.8^Some text"));
    }

    @Test
    public void testCX_AllFilled_PID3() {
        validate(maxMsgReplace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI",
                               "111222333444^6430^M11^Imaginary Hospital&1.3.4.565&ISO^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingCX1() {
        validate(maxMsgReplace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI",
                               "^6430^M11^Imaginary Hospital&1.3.4.565&ISO^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingCX4() {
        validate(maxMsgReplace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI",
                               "111222333444^6430^M11^^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingCX5() {
        validate(maxMsgReplace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI",
                               "111222333444^6430^M11^Imaginary Hospital&1.3.4.565&ISO^^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
    }

    @Test(expected = ValidationException.class)
    public void testEI_AllFilled_MSH21() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^ISO"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingEI1() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "^HL7^2.16.840.1.113883.9.n.m^ISO"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingEI2_3_4() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006"));
    }

    @Test
    public void testMissingEI2() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9^ISO"));
    }

    @Test
    public void testMissingEI3_4() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^HZLN"));
    }

    @Test(expected = ValidationException.class)
    public void testWrongISOFormatInEI3() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9.n.m^ISO"));
    }

    @Test(expected = ValidationException.class)
    public void testWrongEUI64FormatInEI3() {
        validate(maxMsgReplace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7",
                               "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9n-zzzz.ww.yy^EUI-64"));
    }

    @Test
    public void testHD_AllFilledEUI64_MSH3() {
        validate(maxMsgReplace("AcmeInc^ACDE48234567ABCD^EUI-64",
                               "AcmeInc^ACDE48234567ABCD^EUI-64"));
    }

    @Test(expected = ValidationException.class)
    public void testHD_WrongEUI64Format() {
        validate(maxMsgReplace("AcmeInc^ACDE48234567ABCD^EUI-64",
                               "AcmeInc^1.2.34444.444^EUI-64"));
    }

    @Test
    public void testHD_AllFilledISO_MSH3() {
        validate(maxMsgReplace("AcmeInc^ACDE48234567ABCD^EUI-64",
                               "AcmeInc^1.2.3.4^ISO"));
    }

    @Test(expected = ValidationException.class)
    public void testHD_WrongISOFormat() {
        validate(maxMsgReplace("AcmeInc^ACDE48234567ABCD^EUI-64",
                               "AcmeInc^ACDE48234567ABCD^ISO"));
    }

    @Test
    public void testXPN_AllFilled_PID5() {
        validate(maxMsgReplace("|Doe^John^Joseph^^^^L^A|",
                               "|Doe^John^Joseph^Snr^Dr.^^L^A^BN&Birthname^^G^20090713090030+0500^20090713090030+0500^Phd|"));
    }

    @Test
    public void testOnlyXPN1_7() {
        validate(maxMsgReplace("|Doe^John^Joseph^^^^L^A|", "|Doe^^^^^^L|"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingXPN7() {
        validate(maxMsgReplace("|Doe^John^Joseph^^^^L^A|", "|Doe|"));
    }

    @Test
    public void testXTN_AllFilledPID13_PRN() {
        validate(maxMsgReplace("^PRN^PH^^^^123456",
                               "^PRN^PH^wan@continua.com^001^760^123456^02^Any text^GA"));
    }

    @Test
    public void testXTN_AllFilledPID13_NET() {
        validate(maxMsgReplace("^PRN^PH^^^^123456",
                               "^NET^X.400^wan@continua.com^001^760^123456^02^Any text^GA"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingXTN2() {
        validate(maxMsgReplace("^PRN^PH^^^^123456",
                               "^^PH^wan@continua.com^001^760^123456^02^Any text^GA"));
    }

    @Test(expected = ValidationException.class)
    public void testMissingXTN3() {
        validate(maxMsgReplace("^PRN^PH^^^^123456",
                               "^NET^^wan@continua.com^001^760^123456^02^Any text^GA"));
    }

}
