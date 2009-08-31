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

/**
 * Thrown if an XDS specific validation failed.
 * @author Jens Riemschneider
 */
public class XDSValidationException extends Exception {
    private static final long serialVersionUID = -5504983188742730323L;

    /**
     * Constructs the exception.
     * @param message
     *          the detail message.
     */
    public XDSValidationException(String message) {
        super(message);
    }
}
