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
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.make;

import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01OBXValidatorTest extends AbstractPCD01ValidatorTest {

    @Test
    public void testSyntheticMessageTrimmed() throws HL7Exception{
        MessageAdapter<ORU_R01> adapter = make(maximumMessage.toString().trim());
        validate(adapter);
        assertObservationCount(5, adapter);
    }
    
    //################ OBX Segment tests ###############################
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBX1() {
        validate(maxMsgReplace("OBX|1|NM|528391", "OBX||NM|528391"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testWrongSetIDForOBX1() {
        validate(maxMsgReplace("OBX|2||150020", "OBX|1||150020"));
    }
    
    @Test(expected = HL7Exception.class) 
    public void testMissingOBX2_filledOBX5() {
        validate(maxMsgReplace("OBX|1|NM|528391", "OBX|1||528391"));
    }
    
    @Test
    public void testOBX2_SN() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1|SN|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|>80|"));
    }
    
    
    @Ignore
    @Test(expected = ValidationException.class) 
    public void testMissingOBX2_filledOBX11() {
        // The check "OBX-2 must be valued if the value of OBX-11 is not X" seems to be too restrictive
        // add the checkOBX2WhenOBX11NotX in PCD01Validator to switch on the check. 
       validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1||528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1||"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBX3() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1|NM||1||"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBX3() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1|NM|    |1||"));
    }
    
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBX3_2() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|", "OBX|1|NM|528391^^MDC|1|"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBX3_2() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|", "OBX|1|NM|528391^  ^MDC|1|"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBX4() {
        validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|||"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBX4() {
       validate(maxMsgReplace("OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC|1|80|", "OBX|1|NM|528391^MDC_DEV_SPEC_PROFILE_BP^MDC| ||"));
    }
    
    @Test(expected = ValidationException.class)
    @Ignore
    public void testSameValueIn2OBX3AndOBX4Fields() {
        //TODO The spec does not say explicitly that the OBX-3 and OBX-4 can not be the same, ignoring currently this test
        validate(maxMsgReplace("OBX|4|NM|1500212^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.2|", "OBX|4|NM|1500212^MDC_PRESS_BLD_NONINV_SYS^MDC|1.0.1.1|"));
    }
    
    @Test 
    public void testMissingOBX5() {
        validate(maxMsgReplace("|80|mmHg|", "||mmHg|"));
    }
    
    @Test 
    public void testMissingOBX5_OBX6() {
        validate(maxMsgReplace("|80|mmHg|", "|||"));
    }
    
    @Test 
    public void testMissingOBX7() {
        validate(maxMsgReplace("|75 - 90|TEST|||R|", "||TEST|||R|"));
    }
    
    @Test 
    public void testMissingOBX8() {
       validate(maxMsgReplace("|75 - 90|TEST|||R|", "|75 - 90||||R|"));
    }
    
    @Test(expected = ValidationException.class) 
    @Ignore
    public void testMissingOBX6_filledOBX5() {
        //This seems to be guaranteed by the parser, therefore no explicit check is needed
        //The parser produces empty OBX-5 in this case (the isEmpty() DSL method returns true).
        validate( maxMsgReplace("|80|mmHg|", "|80||"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBX11() {
        validate(maxMsgReplace("|75 - 90|TEST|||R|||", "|75 - 90|TEST||||||"));
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBR7_OBR8_OBX14() {
        MessageAdapter<ORU_R01> msg;
        msg = make(maximumMessage.toString().replace("|20090813095715+0500|20090813105715+0500", "||"));
        msg = make(msg.toString().replace("|R|||20090813095725+0500|", "|R||||"));
        validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    @Ignore
    public void testEquivalenceOfOBX14AndOBX19() {
        //TODO Ignoring the test, as the spec says 'should' and 'may' for OBX-14 and OBX-19
        validate(maxMsgReplace("|0123456789ABCDEF^EUI-64|20090813095725+0500||", "|0123456789ABCDEF^EUI-64|20100813095725+0500||"));
    }
    
    //check for messages without OBR-7, OBR-8 and OBX-14
    //check: OBX-14 and OBX-19 should be equivalent
    private void assertObservationCount(int expected, MessageAdapter<ORU_R01> adapter){
        ORU_R01 msg = (ORU_R01)adapter.getTarget();
        int observationsInMsg = msg.getPATIENT_RESULT().getORDER_OBSERVATION().getOBSERVATIONReps();
        assertEquals(expected, observationsInMsg);
    }
    
}