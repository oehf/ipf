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

import static org.apache.commons.lang3.Validate.noNullElements;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;

import java.util.List;


/**
 * Validates a list of recipients containing organizations and/or persons
 * and/or telecommunication addresses.
 * @author Jens Riemschneider
 */
public class RecipientListValidator implements ValueListValidator {
    private final XCNValidator xcnValidator = new XCNValidator();
    private final XONValidator xonValidator = new XONValidator();
    
    @Override
    public void validate(List<String> values) throws XDSMetaDataException {
        noNullElements(values, "values cannot contain null elements");
//        This check is disabled for compatibility with older versions.
//        metaDataAssert(!values.isEmpty(), RECIPIENT_LIST_EMPTY);
        
        for (String value : values) {
            metaDataAssert(!value.isEmpty(), RECIPIENT_EMPTY);

            String[] parts = value.split("\\|", 4);
            metaDataAssert((parts.length > 0) && (parts.length <= 3), INVALID_RECIPIENT, value);

            if (! parts[0].isEmpty()) {
                xonValidator.validate(parts[0]);
            }

            if ((parts.length > 1) && (! parts[1].isEmpty())) {
                xcnValidator.validate(parts[1]);
            }
        }
    }
}
