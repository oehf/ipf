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


/**
 * Validator that accepts any value.
 * <p>
 * This validator is used to simply test for the correct number of occurrences
 * of a value.
 * @author Jens Riemschneider
 */
public class NopValidator implements ValueValidator {
    @Override
    public void validate(String value) throws XDSMetaDataException {
        notNull(value, "value cannot be null");
        // Does nothing by design.
    }
}
