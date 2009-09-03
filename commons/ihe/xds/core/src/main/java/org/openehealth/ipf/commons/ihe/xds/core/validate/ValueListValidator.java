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

import java.util.List;


/**
 * Provides validation of a list of values for compatibility with other data types.
 * @author Jens Riemschneider
 */
public interface ValueListValidator {
    /**
     * Validates the given values.
     * @param values
     *          the values.
     * @throws XDSMetaDataException
     *          if the validation failed.
     */
    void validate(List<String> values) throws XDSMetaDataException;
}
