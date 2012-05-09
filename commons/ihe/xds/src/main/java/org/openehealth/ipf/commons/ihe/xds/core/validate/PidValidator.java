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

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Validates a PID string.
 * @author Jens Riemschneider
 */
public class PidValidator implements ValueValidator {
    private static final Pattern PID_PATTERN = Pattern.compile("\\s*PID-([1-9][0-9]*)\\|(.*)\\s*");

    @Override
    public void validate(String value) throws XDSMetaDataException {
        notNull(value, "value cannot be null");
        
        Matcher matcher = PID_PATTERN.matcher(value);
        metaDataAssert(matcher.matches(), INVALID_PID, value);
                
        int number = Integer.parseInt(matcher.group(1));
        metaDataAssert(number != 2 && number != 4 && number != 12 && number != 19,
                UNSUPPORTED_PID, value);
    }    
}
