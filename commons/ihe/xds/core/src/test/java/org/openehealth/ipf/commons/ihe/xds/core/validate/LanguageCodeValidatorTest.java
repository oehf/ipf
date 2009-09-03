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
import org.openehealth.ipf.commons.ihe.xds.core.validate.LanguageCodeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link LanguageCodeValidator}.
 * @author Jens Riemschneider
 */
public class LanguageCodeValidatorTest {
    private static final LanguageCodeValidator validator = new LanguageCodeValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("en");
        validator.validate("abcdefgh");
        validator.validate("en-US");
        validator.validate("en-us");
        validator.validate("abcdefgh-ab123456");
        validator.validate("abcdefgh-ab123456-23");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("-US");
        assertFails("ab234");
        assertFails("ab|");
        assertFails("abcdefghi");
        assertFails("abcdefgh-");
        assertFails("abcdefgh-|");
        assertFails("abcdefgh-12345678-");
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
