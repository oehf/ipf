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
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests for {@link OIDValidator}.
 * @author Jens Riemschneider
 */
public class OIDValidatorTest {
    private static final OIDValidator validator = new OIDValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("1.2323");
        validator.validate("1.0.2323");
        validator.validate("1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.456");

        var errInfo = new ErrorInfo(ErrorCode.PATIENT_ID_DOES_NOT_MATCH, "context1", Severity.ERROR, "location1", null);
        System.out.println(errInfo);
    }

    @Test
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("1");
        assertFails("1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.45.67.89.1.23.4567");
        assertFails("01.23.45.67");
        assertFails("1.23.045.67");
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
