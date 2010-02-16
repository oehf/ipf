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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HashValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.UriValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

/**
 * Tests for {@link HashValidator}.
 * @author Jens Riemschneider
 */
public class UriValidatorTest {
    private static final UriValidator validator = new UriValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate(Arrays.asList("http://localhost:8080"));
        validator.validate(Arrays.asList("1|http://localhost:8080"));
        validator.validate(Arrays.asList("1|http://localhost:8080", "2|/a", "3|/a", "4|/a", "5|/a", "6|/a", "7|/a", "8|/a", "9|/a"));
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails(Arrays.asList(""));
        assertFails(Arrays.asList("://localhost:8080"));
        assertFails(Arrays.asList("0|http://localhost"));
        assertFails(Arrays.asList("10|http://localhost"));
        assertFails(Arrays.asList("A|http://localhost"));
        assertFails(Arrays.asList("2|http://localhost"));
        assertFails(Arrays.asList("1|http://localhost", "3|/sub"));
    }

    private static void assertFails(List<String> value) {
        try {
            validator.validate(value);
            fail("Expected exception: " + XDSMetaDataException.class + " for " + value);
        } catch (XDSMetaDataException e) {
            // Expected
        }
    }
}
