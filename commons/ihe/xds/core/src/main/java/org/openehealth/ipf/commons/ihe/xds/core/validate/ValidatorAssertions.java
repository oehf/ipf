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

/**
 * Provides assertions for validators.
 * @author Jens Riemschneider
 */
public abstract class ValidatorAssertions {
    private ValidatorAssertions() {
        throw new UnsupportedOperationException("Utility class");
    }
    
    /**
     * Evaluates the condition and throws an exception if the condition is not met.
     * @param condition
     *          the condition.
     * @param errorMessage
     *          the error message for the exception.
     * @param details
     *          objects required by the message text formatting.
     * @throws XDSMetaDataException
     *          if the validation failed.
     */
    public static void metaDataAssert(boolean condition, ValidationMessage errorMessage, Object... details) throws XDSMetaDataException {
        if (!condition) {
            throw new XDSMetaDataException(errorMessage, details);
        }
    }
}