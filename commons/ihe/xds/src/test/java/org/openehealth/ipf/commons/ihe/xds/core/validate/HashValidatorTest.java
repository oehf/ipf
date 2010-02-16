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
import org.openehealth.ipf.commons.ihe.xds.core.validate.HashValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link HashValidator}.
 * @author Jens Riemschneider
 */
public class HashValidatorTest {
    private static final HashValidator validator = new HashValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("da39a3ee5e6b4b0d3255bfef95601890afd80709");
        validator.validate("da39A3ee5e6b4b0d3255bfef95601890afd80709");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("qa39a3ee5e6b4b0d3255bfef95601890afd80709");
        assertFails("da39a3ee5e6b4b0d3255bfef95601890afd8070");
        assertFails("da39a3ee5e6b4b0d3255bfef95601890afd807091");
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
