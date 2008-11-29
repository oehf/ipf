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
package org.openehealth.ipf.platform.camel.core.extend;

/**
 * DSL extensions that make type checks at runtime throw this exception
 * to indicate DSL syntax errors in route configurations. 
 * 
 * @author Martin Krasser
 */
@SuppressWarnings("serial")
public class RouteModelException extends RuntimeException {

    public RouteModelException() {
        super();
    }

    public RouteModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteModelException(String message) {
        super(message);
    }

    public RouteModelException(Throwable cause) {
        super(cause);
    }

}
