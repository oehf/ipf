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
