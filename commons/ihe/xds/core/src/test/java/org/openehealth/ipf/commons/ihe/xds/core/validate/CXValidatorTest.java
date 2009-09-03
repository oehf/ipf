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
import org.openehealth.ipf.commons.ihe.xds.core.validate.CXValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link CXValidator}.
 * @author Jens Riemschneider
 */
public class CXValidatorTest {
    private static final CXValidator validator = new CXValidator();

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
