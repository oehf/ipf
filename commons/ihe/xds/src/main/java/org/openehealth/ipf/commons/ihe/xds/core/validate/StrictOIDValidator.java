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

import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_OID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.OID_TOO_LONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validator for Object Identifiers (OIDs) that enforces strict validation rules.
 * This validator ensures that OIDs conform to both length constraints and proper OID syntax.
 */
public class StrictOIDValidator implements ValueValidator {

    /**
     * Validates an Object Identifier (OID) string according to strict rules.
     * The validation ensures that the OID is not null, does not exceed 64 characters in length,
     * and conforms to the proper OID syntax as defined by the IETF GSS-API specification.
     *
     * @param oid the Object Identifier string to validate; must not be null
     * @throws XDSMetaDataException if the OID is null, exceeds 64 characters, or has invalid syntax
     */
    @Override
    public void validate(String oid) throws XDSMetaDataException {
        requireNonNull(oid, "oid cannot be null");
        metaDataAssert(oid.length() <= 64, OID_TOO_LONG, oid);
        try {
            new Oid(oid);
        } catch (GSSException e) {
            throw new XDSMetaDataException(INVALID_OID, oid);
        }
    }
}
