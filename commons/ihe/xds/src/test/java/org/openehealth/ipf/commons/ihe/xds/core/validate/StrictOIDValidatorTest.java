/*
 * Copyright 2026 the original author or authors.
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
 * Tests for {@link OIDValidator}.
 */
public class StrictOIDValidatorTest {
    private static final StrictOIDValidator validator = new StrictOIDValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("0.2.3");
        validator.validate("1.2.3");
        validator.validate("1.23");
        validator.validate("1.0.2323");
        validator.validate("1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.456");
    }

    @Test
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("1");
        assertFails("7.8.9");
        assertFails("12.4.5");
        assertFails("1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.4567");
        assertFails("1.23.-45.67");
        assertFails(".23.45.67");
        assertFails("23.45.67.");
        assertFails("333..23.45.67");
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
