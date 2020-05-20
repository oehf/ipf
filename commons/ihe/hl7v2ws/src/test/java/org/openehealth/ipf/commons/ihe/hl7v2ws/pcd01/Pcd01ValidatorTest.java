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
import ca.uhn.hl7v2.model.v26.message.ACK;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class Pcd01ValidatorTest extends AbstractPCD01ValidatorTest {

    static String MSH = "MSH|^~\\&|AcmeInc^ACDE48234567ABCD^EUI-64||||20090713090030+0500||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^1.3.6.1.4.1.19376.1.6.1.1.1^ISO\r";
    static String PID = "PID|||789567^^^Imaginary Hospital^PI||Doe^John^Joseph^^^^L^A|||M\r";
    static String OBR = "OBR|1|AB12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|||20090813095715+0500\r";
    static String OBX1 = "OBX|1||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|||||||R|||||||0123456789ABCDEF^EUI-64\r";
    static String OBX2 = "OBX|2||150020^MDC_PRESS_BLD_NONINV^MDC|1.0.1|||||||R|||20090813095715+0500\r";
    static String OBX3 = "OBX|3|NM|150021^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|120|266016^MDC_DIM_MMHG^MDC|||||R\r";
    static String OBX4 = "OBX|4|NM|150022^MDC_PRESS_BLD_NONINV_DIA^MDC|1.0.1.2|80|266016^MDC_DIM_MMHG^MDC|||||R\r";
    static String VALID = MSH + PID + OBR + OBX1 + OBX2 + OBX3 + OBX4;
    
    static String ACK_MSH = "MSH|^~\\&|Stepstone||AcmeInc^ACDE48234567ABCD^EUI64||20090726095731+0500||ACK^R01^ACK|AMSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7\r";
    static String MSA = "MSA|CE|20070701132554000008\r";
    static String ERR = "ERR|||100|E|||Missing required OBR segment\r";
    static String VALID_RESPONSE =  ACK_MSH + MSA + ERR;
    
   
    @Test
    public void testMessageSectionJ2() throws HL7Exception {
        ORU_R01 msg1 = load(getParser(), "pcd01/valid-pcd01-request.hl7v2");
        validate(msg1);
    }

    @Test
    public void testMessageSectionE11() throws HL7Exception {
        ORU_R01 msg2 = load(getParser(), "pcd01/valid-pcd01-request2.hl7v2");
        validate(msg2);
    }

    @Test
    public void testSyntheticMessage() throws HL7Exception{
        var msg3 = (ORU_R01)getParser().parse(VALID);
        validate(msg3);
        assertObservationCount(4, msg3);
    }
    
    @Test
    public void testSyntheticMessageTrimmed() throws HL7Exception{
        var msg4 = (ORU_R01)getParser().parse(VALID.trim());
        validate(msg4);
        assertObservationCount(4, msg4);
    }
    
    @Test
    public void testResponseMessage()  throws HL7Exception {
        ACK rsp = load(getParser(), "pcd01/valid-pcd01-response.hl7v2");
        validate(rsp);
    }
    
    @Test
    public void testResponseMessage2() throws HL7Exception  {
        ACK rsp2 = load(getParser(), "pcd01/valid-pcd01-response2.hl7v2");
        validate(rsp2);
    }
    
    @Test
    public void testSyntheticResponseMessage() throws HL7Exception {
        validate(getParser().parse(VALID_RESPONSE));
    }

    
    @Test
    public void testNoPID() throws HL7Exception {
        //no PID is allowed (see the definition of ORU^R01) 
        validate(getParser().parse(VALID.replace(PID, "")));
    }
    
    @Test
    public void testOnlyFamilyName() throws HL7Exception {
        validate(getParser().parse(VALID.replace("Doe^John^Joseph", "Doe^^")));
    }
    
    @Test
    public void testNotPresentPatientName() throws HL7Exception {
        validate(getParser().parse(VALID.replace("Doe^John^Joseph", "^^")));
    }
    

    // ///////// Negative tests
    @Test(expected = HL7Exception.class)
    public void testNoOBR() throws HL7Exception {
        validate(getParser().parse(VALID.replace(OBR, "")));
    }

    @Ignore
    @Test(expected = HL7Exception.class)
    public void testSyntheticResponseUnsupportedCODE() throws HL7Exception {
    	//code SN is not supported
        validate(getParser().parse(VALID_RESPONSE.replace("MSA|CE|", "MSA|SN|")));
    }
    

    /////////////////// Field cheks
    @Test(expected = HL7Exception.class)
    public void testIncompletePatientId() throws HL7Exception {
        validate(getParser().parse(VALID.replace("789567", "")));
    }

    @Test(expected = HL7Exception.class)
    public void testObservationIdentifierNotPresent() throws HL7Exception {
        validate(getParser().parse(VALID.replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", "")));
    }
    
    @Test(expected = HL7Exception.class)
    public void testObservationWithNoSubId() throws HL7Exception {
        validate(getParser().parse(VALID.replace("PROFILE_BP^MDC|1|", "PROFILE_BP^MDC||")));
    }
    
    @Test(expected = HL7Exception.class)
    public void testObservationWithNoSubId2() throws HL7Exception {
        validate(getParser().parse(VALID.replace("|1.0.1|", "||")));
    }
    
    private void assertObservationCount(int expected, ORU_R01 msg){
        var observationsInMsg = msg.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATIONReps();
        assertEquals(expected, observationsInMsg);
    }

}
