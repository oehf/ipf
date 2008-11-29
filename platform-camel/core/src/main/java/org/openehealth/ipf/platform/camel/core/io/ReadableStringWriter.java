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
package org.openehealth.ipf.platform.camel.core.io;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * A {@link StringWriter} that allows the creation of a {@link Reader} from its
 * internal string buffer representation.
 * 
 * @author Martin Krasser
 */
public class ReadableStringWriter extends StringWriter {

    /**
     * Returns a {@link Reader} from this writer's internal string buffer
     * representation.
     * 
     * @return a reader to read from.
     * 
     * @see #getBuffer()
     */
    public Reader reader() {
        return new StringReader(getBuffer().toString());
    }
    
}
