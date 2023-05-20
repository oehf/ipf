/*
 * Copyright 2023 the original author or authors.
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link IdentifierValidator}.
 * @author Dmytro Rud
 */
public class IdentifierValidatorTest {
    private static final IdentifierValidator validator = new IdentifierValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("1.2323");
        validator.validate("abcd");
        validator.validate(UUID.randomUUID().toString());
    }
    
    @Test
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails(" ");
        assertFails(" identifier");
        assertFails("\u1234");
        assertFails("здоровенькі були");
        assertFails("Grüsse");
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
