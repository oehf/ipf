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
package org.openehealth.ipf.platform.camel.flow.util;

import org.apache.camel.spi.DataFormat;

/**
 * Thrown to indicate an exception during a
 * {@link DataFormat#marshal(org.apache.camel.Exchange, Object, java.io.OutputStream)}
 * or
 * {@link DataFormat#unmarshal(org.apache.camel.Exchange, java.io.InputStream)}
 * operation.
 * 
 * @author Martin Krasser
 */
@SuppressWarnings("serial")
public class DataFormatException extends RuntimeException {

    public DataFormatException() {
        super();
    }

    public DataFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataFormatException(String message) {
        super(message);
    }

    public DataFormatException(Throwable cause) {
        super(cause);
    }

}
