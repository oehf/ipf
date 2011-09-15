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

import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.HL7Exception;


/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01MSHValidatorTest {

    MessageAdapter maximumMessage = load("pcd01/valid-pcd01-MaximumRequest2.hl7");

    
    Pcd01Validator validator = new Pcd01Validator();

    public Pcd01Validator getValiadtor(){
        return validator;
    }
    
    @Test
    public void testMaximalMessage() {
        getValiadtor().validate(maximumMessage);
    }
      
    //################ MSH Segment tests ###############################
    @Test(expected = ValidationException.class)
    public void testMissingMSH3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("AcmeInc^ACDE48234567ABCD^EUI-64", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH7() {
        MessageAdapter msg = make(maximumMessage.toString().replace("20090713090030+0500", ""));
        getValiadtor().validate(msg);
    }
    
    
    @Test(expected = HL7Exception.class)
    public void testMissingMSH9() {
        MessageAdapter msg = make(maximumMessage.toString().replace("ORU^R01^ORU_R01", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH9dot1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("ORU^R01^ORU_R01", "^R01^ORU_R01"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH9dot1_9dot3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("ORU^R01^ORU_R01", "^R01"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = Exception.class)
    public void testMissingMSH9dot2_9dot3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("ORU^R01^ORU_R01", "ORU"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH10() {
        MessageAdapter msg = make(maximumMessage.toString().replace("MSGID1234", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH11() {
        MessageAdapter msg = make(maximumMessage.toString().replace("P", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testWrongMSH11() {
        MessageAdapter msg = make(maximumMessage.toString().replace("P", "X"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = HL7Exception.class)
    public void testMissingMSH12() {        
        MessageAdapter msg = make(maximumMessage.toString().replace("2.6", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH21() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "^HL7^2.16.840.1.113883.9.n.m^HL7"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot2_3_4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testMissingMSH21dot2_3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("IHE PCD ORU-R01 2006^HL7^2.16.840.1.113883.9.n.m^HL7", "IHE PCD ORU-R01 2006^^^HL7"));
        getValiadtor().validate(msg);
    }
    
        
}