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
package org.openehealth.ipf.commons.lbs.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * An input stream based on a text.
 * <p>
 * This stream can be asked if {@link #close()} has been called.
 * @author Jens Riemschneider
 */
public class TextInputStream extends InputStream {
    private final byte[] content;
    private boolean streamWasClosed;        
    private int index;

    /**
     * Constructs the stream via a given test text
     * @param text
     *          text returned when reading the stream
     */
    public TextInputStream(String text) {
        this.content = text.getBytes();
    }
    
    /**
     * Constructs the stream via a default text (Hello World)
     */
    public TextInputStream() {
        this("Hello World");
    }
    
    /**
     * @return <code>true</code> if the stream was closed
     */
    public boolean isClosed() {
        return streamWasClosed;
    }

    @Override
    public int read() throws IOException {
        return index == content.length ? -1 : content[index++];
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        streamWasClosed = true;
    }
}