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
import org.openehealth.ipf.commons.ihe.xds.core.validate.TimeValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link TimeValidator}.
 * @author Jens Riemschneider
 */
public class TimeValidatorTest {
    private static final TimeValidator validator = new TimeValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("1980");
        validator.validate("198001");
        validator.validate("198012");
        validator.validate("19800101");
        validator.validate("19800131");
        validator.validate("1980010100");
        validator.validate("1980010123");
        validator.validate("198001010100");
        validator.validate("198001010159");
        validator.validate("19800101010100");
        validator.validate("19800101010159");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("19AA");
        assertFails("198000");
        assertFails("198013");
        assertFails("198021");
        assertFails("19800100");
        assertFails("19800132");
        assertFails("19800141");
        assertFails("1980010124");
        assertFails("1980010131");
        assertFails("198001010160");
        assertFails("19800101010160");
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
