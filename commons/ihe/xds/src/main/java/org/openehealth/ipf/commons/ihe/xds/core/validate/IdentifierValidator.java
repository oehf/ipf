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

import java.util.regex.Pattern;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_IDENTIFIER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;


/**
 * Validator for data type "Identifier"
 * @author Dmytro Rud
 */
public class IdentifierValidator implements ValueValidator {
    private static final Pattern PATTERN = Pattern.compile("[\\x21-\\x7E]+");

    @Override
    public void validate(String identifier) throws XDSMetaDataException {
        notNull(identifier, "identifier cannot be null");
        metaDataAssert(PATTERN.matcher(identifier).matches(), INVALID_IDENTIFIER, identifier);
    }
}
