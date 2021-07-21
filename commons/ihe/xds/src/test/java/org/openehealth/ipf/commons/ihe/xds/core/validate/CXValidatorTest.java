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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link CXValidator}.
 * @author Jens Riemschneider
 */
public class CXValidatorTest {
    private static final CXValidator validator = new CXValidator(true);

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("45^^^&1.2.840.113519.6.197&ISO");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("45");
        assertFails("^^^&1.2.840.113519.6.197&ISO");
        assertFails("45^^^123&1.2.840.113519.6.197&ISO");
        assertFails("45^^^&LOL&ISO");
        assertFails("45^^^&1.2.840.113519.6.197");
        assertFails("45^^^&1.2.840.113519.6.197&LOL");
        assertFails("1^^^&1.2.3.4&ISO';'2^^^&1.2.3.4&ISO");
        assertFails("593603^^^&2.16.756.5.33.2.5.1.5.1&ISO''%20and%20611%3d611--%20");
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
