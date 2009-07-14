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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.validate;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidatorAssertions.metaDataAssert;
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.ValidationMessage.*;

import java.util.regex.Pattern;


/**
 * Validator for OIDs.
 * @author Jens Riemschneider
 */
public class OIDValidator implements ValueValidator {
    @Override
    public void validate(String oid) throws XDSMetaDataException {
        notNull(oid, "oid cannot be null");
        
        metaDataAssert(oid.length() <= 64, OID_TOO_LONG, oid);
        metaDataAssert(Pattern.matches("[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+", oid),
                INVALID_OID, oid);
    }
}
