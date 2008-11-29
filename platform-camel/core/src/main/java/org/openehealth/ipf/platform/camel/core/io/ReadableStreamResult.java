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

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * A {@link StreamResult} that allows the creation of a {@link Source} from its
 * internal data representation.
 * 
 * @author Martin Krasser
 */
public class ReadableStreamResult extends StreamResult {

    public ReadableStreamResult() {
        super(new StringWriter());
    }
    
    /**
     * Returns a {@link Source} from this result's internal data representation.
     * 
     * @return a source to read from.
     */
    public Source source() {
        return new StreamSource(new StringReader(getWriter().toString()));
    }

}
