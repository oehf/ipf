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



/**
 * @author Kingsley Nwaigbo
 * 
 */
public class QA_Pcd01PIDPV1ValidatorTest {

    MessageAdapter maximumMessage = load("pcd01/valid-pcd01-MaximumRequest2.hl7");

    
    Pcd01Validator validator = new Pcd01Validator();

    public Pcd01Validator getValiadtor(){
    	return validator;
    }
    
    
    //################ PID Segment tests ###############################
    
    @Test(expected = ValidationException.class)  
    public void testMissingPID3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testSpaceAsPID3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", " "));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testMissingPID3_1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "^^^Imaginary Hospital&1.3.4.565&ISO^PI"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testMissingPID34_1_2_3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "111222333444"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testMissingPID34_1_2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("111222333444^^^Imaginary Hospital&1.3.4.565&ISO^PI", "111222333444^^^ISO"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)  
    public void testMissingPID5() {
        MessageAdapter msg = make(maximumMessage.toString().replace("Doe^John^Joseph", ""));
        getValiadtor().validate(msg);
    }
    
    @Test  
    public void testMissingPID5_1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("Doe^John^Joseph", "^John^Joseph"));
        getValiadtor().validate(msg);
    }
    
    @Test 
    public void testMissingPID5_2_3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("Doe^John^Joseph", "Doe^^"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingPV12() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|I|", "||"));
        getValiadtor().validate(msg);
    }
  
}