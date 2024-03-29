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

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import java.util.regex.Pattern;


/**
 * Validator for OIDs.
 * @author Jens Riemschneider
 */
public class OIDValidator implements ValueValidator {
    private static final Pattern OID_PATTERN =
            Pattern.compile("[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+");

    @Override
    public void validate(String oid) throws XDSMetaDataException {
        requireNonNull(oid, "oid cannot be null");
        
        metaDataAssert(oid.length() <= 64, OID_TOO_LONG, oid);
        metaDataAssert(OID_PATTERN.matcher(oid).matches(), INVALID_OID, oid);
    }
}
