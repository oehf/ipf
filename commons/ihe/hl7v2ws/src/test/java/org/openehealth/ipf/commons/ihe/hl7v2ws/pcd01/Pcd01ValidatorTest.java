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

import static org.junit.Assert.assertEquals;
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.make;

import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

/**
 * @author Mitko Kolev
 * 
 */
public class Pcd01ValidatorTest {
    MessageAdapter msg1 = load("pcd01/valid-pcd01-request.hl7v2");
    MessageAdapter msg2 = load("pcd01/valid-pcd01-request2.hl7v2");
    MessageAdapter rsp = load("pcd01/valid-pcd01-response.hl7v2");

    private static String MSH = "MSH|^~\\&|AcmeInc^ACDE48234567ABCD^EUI-64||||20090713090030+0500||ORU^R01^ORU_R01|MSGID1234|P|2.6|||NE|AL|||||IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7\r";
    private static String PID = "PID|||789567^^^Imaginary Hospital^PI||Doe^John^Joseph^^^^L^A|||M\r";
    private static String OBR = "OBR|1|AB12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|||20090813095715+0500\r";
    private static String OBX1 = "OBX|1||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|||||||R|||||||0123456789ABCDEF^EUI-64\r";
    private static String OBX2 = "OBX|2||150020^MDC_PRESS_BLD_NONINV^MDC|1.0.1|||||||R|||20090813095715+0500\r";
    private static String OBX3 = "OBX|3|NM|150021^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|120|266016^MDC_DIM_MMHG^MDC|||||R\r";
    private static String OBX4 = "OBX|4|NM|150022^MDC_PRESS_BLD_NONINV_DIA^MDC|1.0.1.2|80|266016^MDC_DIM_MMHG^MDC|||||R\r";
    private static String VALID = MSH + PID + OBR + OBX1 + OBX2 + OBX3 + OBX4;
    private Pcd01Validator validator = new Pcd01Validator();

    @Test
    public void testMessageSectionJ2() {
        validator.validate(msg1);
    }

    @Test
    public void testMessageSectionE11() {
        validator.validate(msg2);
    }

    @Test
    public void testSyntheticMessage() throws HL7Exception{
        MessageAdapter adapter = make(VALID);
        validator.validate(adapter);
        assertObservationCount(4, adapter);
    }
    
    @Test
    public void testSyntheticMessageTrimmed() throws HL7Exception{
        MessageAdapter adapter = make(VALID.trim());
        validator.validate(adapter);
        assertObservationCount(4, adapter);
    }
    
    @Test
    public void testResponseMessage() {
        validator.validate(rsp);
    }
    
    @Test
    public void testNoPID() {
        //no PID is allowed (see the definition of ORU^R01) 
        validator.validate(make(VALID.replace(PID,"")));
    }
    
    @Test
    public void testOnlyFamilyName() {
        MessageAdapter msg = make(VALID.replace("Doe^John^Joseph", "Doe^^"));
        validator.validate(msg);
    }
    

    // ///////// Negative tests
    @Test(expected = ValidationException.class)
    public void testNoOBR() {
        validator.validate(make(VALID.replace(OBR,"")));
    }

    /////////////////// Field cheks
    @Test(expected = ValidationException.class)
    public void testIncompletePatientId() {
        MessageAdapter msg = make(VALID.replace("789567", ""));
        validator.validate(msg);
    }

    @Test(expected = ValidationException.class)
    public void testNotPresentPatientName() {
        MessageAdapter msg = make(VALID.replace("Doe^John^Joseph", "^^"));
        validator.validate(msg);
    }
    
    
    @Test(expected = ValidationException.class)
    public void testObservationIdentifierNotPresent() {
        MessageAdapter msg = make(VALID.replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", ""));
        validator.validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testObservationWithNoSubId() {
        MessageAdapter msg = make(VALID.replace("PROFILE_BP^MDC|1|", "PROFILE_BP^MDC||"));
        validator.validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testObservationWithNoSubId2() {
        MessageAdapter msg = make(VALID.replace("|1.0.1|", "||"));
        validator.validate(msg);
    }
    
    private void assertObservationCount(int expected, MessageAdapter adapter){
        ORU_R01 msg = (ORU_R01)adapter.getTarget();
        int observationsInMsg = msg.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATIONReps();
        assertEquals(expected, observationsInMsg);
    }
}
