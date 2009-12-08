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

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for {@link XCNValidator}.
 * @author Jens Riemschneider
 */
public class XCNValidatorTest {
    private static final XCNValidator validator = new XCNValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("11375^Welby^Marcus^J^Jr. MD^Dr^^^&1.2.840.113619.6.197&ISO");
        validator.validate("11375^^^^^^^^&1.2.840.113619.6.197&ISO");
        validator.validate("abc^^^^^^^^&1.2.840.113619.6.197&ISO");
        validator.validate("^Welby^^^^^^^");
        validator.validate("^Welby");
        validator.validate("abc");
        validator.validate("abc^^^^^^^^");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("11375^^^^^^^^&1.2.840.113619.6.197");
        assertFails("11375^^^^^^^^&1.2.840.113619.6.197&LOL");
        assertFails("11375^^^^^^^^&&ISO");
        assertFails("11375^^^^^^^^&abc&ISO");
        assertFails("^^^^^^^^&1.2.840.113619.6.197&ISO");
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
