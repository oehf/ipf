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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MimeTypeValidatorTest {
    private static final MimeTypeValidator validator = new MimeTypeValidator();

    @ParameterizedTest
    @ValueSource(strings = {"text/xml", "application/hl7-v3", "application/fhir+xml"})
    public void testValidateGoodCases(String validMimeType) throws XDSMetaDataException {
        validator.validate(validMimeType);
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"something", "application/test blank", "application/is/wrong"})
    public void testValidateBadCases(String invalidMimeType) throws XDSMetaDataException {
        assertThrows(XDSMetaDataException.class, () -> validator.validate(invalidMimeType));
    }


}
