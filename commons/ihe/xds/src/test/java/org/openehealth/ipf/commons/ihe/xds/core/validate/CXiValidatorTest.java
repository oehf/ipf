/*
 * Copyright 2013 the original author or authors.
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
 * Tests for {@link CXiValidator}.
 * @author Dmytro Rud
 */
public class CXiValidatorTest {
    private static final CXiValidator validator = new CXiValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("11379^^^ABCD&1.3.6367.3.7&ISO^urn:ihe:iti:xds:2013:uniqueId");
        validator.validate("11379^^^^urn:ihe:iti:xds:2013:uniqueId");
        validator.validate("11379^^^&1.3.6367.3.7&ISO^urn:ihe:iti:xds:2013:uniqueId^");
        validator.validate("11379^^^ABCD^urn:ihe:iti:xds:2013:uniqueId^^^^");
    }

    @Test
    public void testValidateBadCase() throws XDSMetaDataException {
        assertFails("");
        assertFails("11379^^^");
        assertFails("11379^^^&1.3.6367.3.7&ISO^urn:ihe:iti:xds:2013:uniqueId^&1.2.3.4&KRYSO");
        assertFails("11379^^^&1.3.6367.3.7&^urn:ihe:iti:xds:2013:uniqueId");
        assertFails("^^^&1.3.6367.3.7&ISO^urn:ihe:iti:xds:2013:uniqueId^^^");
        assertFails("11379^^^&1.3.6367.3.7&ISO^urn:ihe:iti:xds:2013:uniqueId^wrong");
        assertFails("11379^^^&1.3.6367.3.7&ISO^^^");
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
