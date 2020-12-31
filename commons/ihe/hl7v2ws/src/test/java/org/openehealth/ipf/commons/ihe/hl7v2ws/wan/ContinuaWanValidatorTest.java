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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.v26.message.ACK;
import org.junit.Ignore;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.Pcd01ValidatorTest;

/**
 * @author Mitko Kolev
 * 
 */
@Ignore
public class ContinuaWanValidatorTest extends Pcd01ValidatorTest {


    @Test
    public void testOximeterMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-oximeter-continua-wan.hl7v2"));
    }
    
    @Test
    public void testWeightScaleMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-scale-continua-wan.hl7v2"));
    }
    
    @Test
    public void testBPMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-bp-continua-wan.hl7v2"));
    }

    @Test
    public void testFitnessMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-fitness-continua-wan.hl7v2"));
    }
    @Test
    public void testFitnessAndActivityMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-fitness-and-activity-continua-wan.hl7v2"));
    }

    @Test
    public void testThermometerMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-thermometer-continua-wan.hl7v2"));
    }
    
    @Test
    public void testGlucoseMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-glucose-continua-wan.hl7v2"));
    }
    
    @Override
    @Test
    public void testResponseMessage() throws HL7Exception {
        validate(load(getParser(), "wan/valid-wan-response.hl7v2"));
    }
    
    @Override
    @Test(expected=ValidationException.class)
    public void testResponseMessage2() throws HL7Exception {
        ACK rsp2 = load(getParser(), "pcd01/valid-pcd01-response2.hl7v2");
        validate(rsp2);
    }
    
    @Test(expected=ValidationException.class)
    public void testInvalidResponseMessage() throws HL7Exception {
        validate(load(getParser(), "wan/invalid-wan-response.hl7v2"));
    }
    
    @Ignore
    @Override
    public void testSyntheticResponseMessage() {
    }

    @Test(expected = HL7Exception.class)
    public void testInvalidGlucoseMessage() throws HL7Exception {
        // When OBX-5 is filled, obx-2 mus not be null. The message can not be parsed.
        // The default obx-2 type must be set with the system property
        // DEFAULT_OBX2_TYPE_PROP
        validate(load(getParser(), "wan/invalid-glucose-continua-wan.hl7v2"));
    }

}
