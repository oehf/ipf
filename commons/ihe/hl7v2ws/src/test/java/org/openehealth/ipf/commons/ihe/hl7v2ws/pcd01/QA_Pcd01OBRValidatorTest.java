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
public class QA_Pcd01OBRValidatorTest {

    MessageAdapter maximumMessage = load("pcd01/valid-pcd01-MaximumRequest2.hl7");

    
    Pcd01Validator validator = new Pcd01Validator();

    public Pcd01Validator getValiadtor(){
    	return validator;
    }
    
    @Test
    public void testMaximalMessage() {
    	getValiadtor().validate(maximumMessage);
    }
    
     //################ OBR Segment tests ###############################
    @Test(expected = ValidationException.class) 
    public void testMissingOBR1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("OBR|1|", "OBR||"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testWrongSetIDForOBR1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("OBR|2|AB112233", "OBR|1|AB112233"));
        getValiadtor().validate(msg);
    }
    
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBR3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBR3() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", " "));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testEmptyOBR3_1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", "^AcmeAHDInc^ACDE48234567ABCD^EUI-64"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBR3_1() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", " ^AcmeAHDInc^ACDE48234567ABCD^EUI-64"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testOnlyOBR31_32() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", "CD12345^AcmeAHDInc"));
        getValiadtor().validate(msg);
    }
    
    @Test
    public void testOnlyOBR31_33_34() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", "CD12345^^ACDE48234567ABCD^EUI-64"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class)
    public void testOnlyOBR31() {
        MessageAdapter msg = make(maximumMessage.toString().replace("CD12345^AcmeAHDInc^ACDE48234567ABCD^EUI-64", "CD12345"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBR4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", ""));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBR4() {
        MessageAdapter msg = make(maximumMessage.toString().replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", " "));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testMissingOBR4_2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", "528391^^MDC"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testSpaceAsOBR4_2() {
        MessageAdapter msg = make(maximumMessage.toString().replace("528391^MDC_DEV_SPEC_PROFILE_BP^MDC", "528391^ ^MDC"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testOBR8_withoutOBR7() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|20090813095715+0500|20090813105715+0500", "||20090813105715+0500"));
        getValiadtor().validate(msg);
    }
    
    @Test(expected = ValidationException.class) 
    public void testOBR7_olderThanOBR8() {
        MessageAdapter msg = make(maximumMessage.toString().replace("|20090813095715+0500|20090813105715+0500", "|20100813095715+0500|20090813105715+0500"));
        getValiadtor().validate(msg);
    }
    
    
}
