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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * Tests for {@link RecipientListValidator}.
 * @author Jens Riemschneider
 */
public class RecipientListValidatorTest {
    private static final RecipientListValidator validator = new RecipientListValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate(Arrays.asList("Some Hospital|^Welby"));
        validator.validate(Arrays.asList("|^Welby"));
        validator.validate(Arrays.asList("|ONLYID"));
        validator.validate(Arrays.asList("Some Hospital"));
        validator.validate(Arrays.asList("Some Hospital", "|^Welby"));
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
//      This check is disabled for compatibility with older versions.
//        assertFails(Arrays.<String>asList());
        assertFails(Arrays.asList(""));
        assertFails(Arrays.asList("^LOL"));
        assertFails(Arrays.asList("Some Hospital|^Welby||"));
        assertFails(Arrays.asList("|Some Hospital|^Welby|"));
        assertFails(Arrays.asList("Some Hospital", ""));
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
