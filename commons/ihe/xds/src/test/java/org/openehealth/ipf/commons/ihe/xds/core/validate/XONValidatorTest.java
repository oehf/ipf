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

/**
 * Tests for {@link XONValidator}.
 * @author Jens Riemschneider
 */
public class XONValidatorTest {
    private static final XONValidator validator = new XONValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("Some Hospital^^^^^&1.2.3.4.5.6.7.8.9.1789&ISO^^^^45");
        validator.validate("Some Hospital^^^^^^^^^1.2.3.4.5.6.7.8.9.1789.45");
        validator.validate("Some Hospital");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("^^^^^&1.2.3.4.5.6.7.8.9.1789&ISO^^^^45");
        assertFails("Some Hospital^^^^^&1.2.3.4.5.6.7.8.9.1789^^^^45");
        assertFails("Some Hospital^^^^^&1.2.3.4.5.6.7.8.9.1789&LOL^^^^45");
        assertFails("Some Hospital^^^^^&LOL&ISO^^^^45");
        assertFails("Some Hospital^^LOL");
        assertFails("Some Hospital^^^^^^^^^LOL");
        assertFails("Some Hospital^^^^^LOL&1.2.3.4.5.6.7.8.9.1789&ISO^^^^45");
        assertFails("Some Hospital^1.2");
        assertFails("Some Hospital^^^1.2");
        assertFails("Some Hospital^^^^1.2");
        assertFails("Some Hospital^^^^^^1.2");
        assertFails("Some Hospital^^^^^^^1.2");
        assertFails("Some Hospital^^^^^^^^1.2");
        assertFails("Some Hospital^^^^^^^^^^1.2");
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
