/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.validate.PidValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link PidValidator}.
 * @author Jens Riemschneider
 */
public class PidValidatorTest {
    private static final PidValidator validator = new PidValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("PID-3|");
        validator.validate("PID-3|something");
        validator.validate("PID-44|something");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("PID-N|something");
        assertFails("PID-0|something");
        assertFails("PID-|something");
        assertFails("POD-3|something");
        assertFails("PI-3|something");
        assertFails("PID-3");
        assertFails("PID-2|something");
        assertFails("PID-4|something");
        assertFails("PID-12|something");
        assertFails("PID-19|something");
    }

    private static void assertFails(String value) {
        try {
            validator.validate(value);
            fail("Expected exception: " + XDSMetaDataException.class + " for " + value);
        } catch (XDSMetaDataException e) {
            // Expected
        }
    }
}
