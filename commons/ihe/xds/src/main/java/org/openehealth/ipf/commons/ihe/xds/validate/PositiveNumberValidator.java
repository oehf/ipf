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
package org.openehealth.ipf.commons.ihe.xds.validate;

import static org.apache.commons.lang.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.validate.ValidatorAssertions.*;

import java.util.regex.Pattern;


/**
 * Validates for a positive number.
 * @author Jens Riemschneider
 */
public class PositiveNumberValidator implements ValueValidator {
    @Override
    public void validate(String value) throws XDSMetaDataException {
        notNull(value, "value cannot be null");
        metaDataAssert(Pattern.matches("[0-9]+", value), 
                INVALID_NUMBER_FORMAT, value);
    }
}
