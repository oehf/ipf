/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.xds.core.validate;

import org.junit.Test;
import org.openehealth.ipf.commons.core.URN;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 *
 */
public class UUIDValidatorTest {

    private static final UUIDValidator validator = new UUIDValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate(UUID.randomUUID().toString());
    }

    @Test
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("1");
    }

    @Test
    public void testGetAsUUID() throws URISyntaxException {
        var random = UUID.randomUUID();
        assertEquals(random, validator.getAsUUID("urn:uuid:" + random.toString()).get());
        assertEquals(random, validator.getAsUUID(new URI("urn", "uuid", random.toString())).get());
        assertEquals(random, validator.getAsUUID(new URN(random)).get());
        assertFalse(validator.getAsUUID("gablorg").isPresent());
    }

    @Test
    public void testGetSymbolic() throws URISyntaxException {
        assertFalse(validator.getSymbolicId(new URI("urn", "uuid", UUID.randomUUID().toString()).toString()).isPresent());
        assertTrue(validator.getSymbolicId("gablorg").isPresent());
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
