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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link RecipientListValidator}.
 * @author Jens Riemschneider
 */
public class RecipientListValidatorTest {
    private static final RecipientListValidator validator = new RecipientListValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate(Collections.singletonList("Some Hospital|^Welby"));
        validator.validate(Collections.singletonList("|^Welby"));
        validator.validate(Collections.singletonList("|ID^^^^^^^^&1.2.840.113619.6.197&ISO"));
        validator.validate(Collections.singletonList("Some Hospital"));
        validator.validate(Arrays.asList("Some Hospital", "|^Welby"));
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
//      This check is disabled for compatibility with older versions.
//        assertFails(Arrays.<String>asList());
        assertFails(Collections.singletonList(""));
        assertFails(Collections.singletonList("^LOL"));
        assertFails(Collections.singletonList("Some Hospital|^Welby||"));
        assertFails(Collections.singletonList("|Some Hospital|^Welby|"));
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
