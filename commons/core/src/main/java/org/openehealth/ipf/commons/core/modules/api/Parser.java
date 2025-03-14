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
package org.openehealth.ipf.commons.core.modules.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.transform.Source;

/**
 * Defines methods for parsing a external representation of information into an
 * internal model.
 * 
 * @author Christian Ohr
 * 
 * @param <S>
 *            the internal model
 */
public interface Parser<S> {

    /**
     * Parses a message and returns an internal representation of the
     * information.
     * 
     * @param message message
     * @param params parse parameters
     * @return the parsed message
     */
    S parse(String message, Object... params);

    /**
     * Parses a message and returns an internal representation of the
     * information.
     * 
     * @param message message to be parsed
     * @param params parse parameters
     * @return the parsed message
     * @throws IOException
     *             if reading from stream fails
     */
    S parse(InputStream message, Object... params) throws IOException;

    /**
     * Parses a message and returns an internal representation of the
     * information.
     * 
     * @param source message to be parsed
     * @param params parse parameters
     * @return the parsed message
     * @throws IOException
     *             if reading from stream fails
     */
    S parse(Source source, Object... params) throws IOException;

    /**
     * Parses a message and returns an internal representation of the
     * information.
     *
     * @param reader message to be parsed
     * @param params parse parameters
     * @return the parsed message
     * @throws IOException
     *             if reading from stream fails
     */
    S parse(Reader reader, Object... params) throws IOException;

}
