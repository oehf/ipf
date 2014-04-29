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
package org.openehealth.ipf.commons.ihe.hl7v2ws.wan;

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;

import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01ValidatorTest;

import ca.uhn.hl7v2.HL7Exception;

/**
 * @author Mitko Kolev
 * 
 */
public class ContinuaWanValidatorTest extends Pcd01ValidatorTest {
    ContinuaWanValidator validator = new ContinuaWanValidator();

    public ContinuaWanValidator getValidator() {
        return validator;
    }

    @Test
    public void testOximeterMessage() {
        validate(load("wan/valid-oximeter-continua-wan.hl7v2"));
    }
    
    @Test
    public void testWeightScaleMessage() {
        validate(load("wan/valid-scale-continua-wan.hl7v2"));
    }
    
    @Test
    public void testBPMessage() {
        validate(load("wan/valid-bp-continua-wan.hl7v2"));
    }

    @Test
    public void testFitnessMessage() {
        validate(load("wan/valid-fitness-continua-wan.hl7v2"));
    }
    @Test
    public void testFitnessAndActivityMessage() {
        validate(load("wan/valid-fitness-and-activity-continua-wan.hl7v2"));
    }

    @Test
    public void testThermometerMessage() {
        validate(load("wan/valid-thermometer-continua-wan.hl7v2"));
    }
    
    @Test
    public void testGlucoseMessage() {
        validate(load("wan/valid-glucose-continua-wan.hl7v2"));
    }
    
    @Test
    public void testResponseMessage() {
        validate(load("wan/valid-wan-response.hl7v2"));
    }
    
    @Test(expected=ValidationException.class)
    public void testResponseMessage2() {
        validate(rsp2);
    }
    
    @Test(expected=ValidationException.class)
    public void testInvalidResponseMessage() {
        validate(load("wan/invalid-wan-response.hl7v2"));
    }
    
    @Ignore
    @Override
    public void testSyntheticResponseMessage() {
    }

    @Test(expected = HL7Exception.class)
    public void testInvalidGlucoseMessage() {
        // When OBX-5 is filled, obx-2 mus not be null. The message can not be parsed.
        // The default obx-2 type must be set with the system property
        // DEFAULT_OBX2_TYPE_PROP
        validate(load("wan/invalid-glucose-continua-wan.hl7v2"));
    }

}
