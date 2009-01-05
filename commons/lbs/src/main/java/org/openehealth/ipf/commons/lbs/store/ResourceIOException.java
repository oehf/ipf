/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.lbs.store;

/**
 * Thrown if an attempt to access a resource in a store failed because of an IO
 * related problem.
 * @author Jens Riemschneider
 */
public class ResourceIOException extends RuntimeException {
    /** serial version UID */
    private static final long serialVersionUID = 3018995764035990713L;

    /**
     * Standard exception constructor (see {@link Exception#Exception(String, Throwable)}). 
     * @param message    
     *          see {@link Exception#Exception(String, Throwable)}
     * @param cause     
     *          see {@link Exception#Exception(String, Throwable)}
     */
    public ResourceIOException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Standard exception constructor (see {@link Exception#Exception(String)}). 
     * @param message
     *          see {@link Exception#Exception(String)}
     */
    public ResourceIOException(String message) {
        super(message);
    }

    /**
     * Standard exception constructor (see {@link Exception#Exception(Throwable)}). 
     * @param cause
     *          see {@link Exception#Exception(Throwable)}
     */
    public ResourceIOException(Throwable cause) {
        super(cause);
    }

    
}
