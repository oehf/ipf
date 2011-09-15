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


import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.make;

import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;



/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01DataTypesTest {

    MessageAdapter maximumMessage = load("pcd01/valid-pcd01-MaximumRequest2.hl7");

    
    Pcd01Validator validator = new Pcd01Validator();

    public Pcd01Validator getValiadtor(){
    	return validator;
    }
    
      
    //################ Data types ###############################
    
    @Test
    public void testCWE_AllFilled_MSH19() {
        MessageAdapter msg = make(maximumMessage.toString().replace("EN^English^ISO659", "EN^English^ISO659^en-US^US English^ISO659^1.0^2.8^Some text"));
        getValiadtor().validate(msg);
    }
     
    @Test(expected = ValidationException.class)
    public void testMissingCWE2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("EN^English^ISO659", "EN^^ISO659^en-US^US English^ISO659^1.0^2.8^Some text"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testCX_AllFilled_PID3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "111222333444^6430^M11^Imaginary Hospital&1.3.4.565&ISO^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingCX1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "^6430^M11^Imaginary Hospital&1.3.4.565&ISO^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingCX4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "111222333444^6430^M11^^PI^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingCX5() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "111222333444^6430^M11^Imaginary Hospital&1.3.4.565&ISO^^AssigningFac&3.4.5.6&ISO^20090713^20090713^Ju01&test&test2^Ag01&test3&test4"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testEI_AllFilled_MSH21() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingEI1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "^HL7^2.16.840.1.113883.9.n.m^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingEI2_3_4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testMissingEI2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testMissingEI3_4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^HZLN"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testWrongISOFormatInEI3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9.n.m^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testWrongEUI64FormatInEI3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^^2.16.840.1.113883.9n-zzzz.ww.yy^EUI-64"));
        getValiadtor().validate(msg);
    }

    @Test
    public void testHD_AllFilledEUI64_MSH3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("AcmeInc^ACDE48234567ABCD^EUI-64", "AcmeInc^ACDE48234567ABCD^EUI-64"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testHD_WrongEUI64Format() {
        MessageAdapter msg = make(maximumMessage.toString().replace("AcmeInc^ACDE48234567ABCD^EUI-64", "AcmeInc^1.2.34444.444^EUI-64"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testHD_AllFilledISO_MSH3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("AcmeInc^ACDE48234567ABCD^EUI-64", "AcmeInc^1.2.3.4^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testHD_WrongISOFormat() {
        MessageAdapter msg = make(maximumMessage.toString().replace("AcmeInc^ACDE48234567ABCD^EUI-64", "AcmeInc^ACDE48234567ABCD^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testXPN_AllFilled_PID5() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|Doe^John^Joseph^^^^L^A|", "|Doe^John^Joseph^Snr^Dr.^^L^A^BN&Birthname^^G^20090713090030+0500^20090713090030+0500^Phd|"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testOnlyXPN1_7() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|Doe^John^Joseph^^^^L^A|", "|Doe^^^^^^L|"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingXPN7() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|Doe^John^Joseph^^^^L^A|", "|Doe|"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testXTN_AllFilledPID13_PRN() {
        MessageAdapter msg = make(maximumMessage.toString().replace("^PRN^PH^^^^123456", "^PRN^PH^wan@continua.com^001^760^123456^02^Any text^GA"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testXTN_AllFilledPID13_NET() {
        MessageAdapter msg = make(maximumMessage.toString().replace("^PRN^PH^^^^123456", "^NET^X.400^wan@continua.com^001^760^123456^02^Any text^GA"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingXTN2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("^PRN^PH^^^^123456", "^^PH^wan@continua.com^001^760^123456^02^Any text^GA"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingXTN3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("^PRN^PH^^^^123456", "^NET^^wan@continua.com^001^760^123456^02^Any text^GA"));
        getValiadtor().validate(msg);
    }
    
    @Ignore @Test
    public void testCNE() {
        MessageAdapter msg = make(maximumMessage.toString().replace("", ""));
        getValiadtor().validate(msg);
    }
    
       
        
}
