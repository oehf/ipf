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
package org.openehealth.ipf.platform.camel.core.process.splitter.support;

import org.apache.camel.util.ObjectHelper;
import org.openehealth.ipf.platform.camel.core.process.splitter.Splitter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

/**
 * String-based iterator class that reads a file line by line
 * This class can be used within a split rule expression to extract each line
 * of a given file and generate a new exchange via the {@link Splitter}.
 * E.g. in Groovy, you can use this iterator like this:
 * 
 * <code>
 *       ...
 *            .split { exchange -> 
 *                String filename = exchange.getIn().getBody();
 *                return new TextFileIterator(filename);
 *            }
 *       ...
 * </code>
 * 
 * The intention of this class is to read long text files without loading the
 * whole file into memory. The largest portion of the file that is kept in 
 * memory is an individual line as read by {@link BufferedReader#readLine()}.
 * 
 * @author Jens Riemschneider
 */
public class TextFileIterator implements Iterator<String> {
    private static final LineSplitterLogic DEFAULT_LINE_SPLITTER_LOGIC = 
        new NoopLineSplitterLogic();
    
    private BufferedReader reader;
    private String[] nextSplitLine;
    private int curSplitLineIdx;
    private final LineSplitterLogic lineSplitterLogic;
    private boolean closed;
    
    /**
     * Creates an iterator for a given file name
     * @param filename
     *          name of the file that should be read
     * @throws IOException
     *          If the file does not exist or could not be read
     */
    public TextFileIterator(String filename) throws IOException {
        
        ObjectHelper.notNull(filename, "filename");

        lineSplitterLogic = DEFAULT_LINE_SPLITTER_LOGIC;
        
        try {
            reader = new BufferedReader(new FileReader(filename));
            readNextLine();
        }
        catch (IOException e) {
            close();
            throw e;
        }
    }

    /**
     * Creates an iterator for a given file name
     * @param filename
     *          name of the file that should be read
     * @param lineSplitterLogic
     *          logic that is used to split lines into individual iteration steps
     * @throws IOException
     *          If the file does not exist or could not be read
     */
    public TextFileIterator(String filename, LineSplitterLogic lineSplitterLogic) 
        throws IOException {
        
        ObjectHelper.notNull(filename, "filename");
        ObjectHelper.notNull(lineSplitterLogic, "lineSplitterLogic");
        
        this.lineSplitterLogic = lineSplitterLogic;
        reader = new BufferedReader(new FileReader(filename));
        
        readNextLine();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return nextSplitLine != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public String next() {
        var curLine = nextSplitLine[curSplitLineIdx];
        advance();
        return curLine;
    }

    /* (non-Javadoc)
     * Not supported for this iterator
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        close();
        throw new UnsupportedOperationException();
    }
    
    /** @return {@code true} if any underlying resources were closed
     */
    public boolean isClosed() {
        return closed;
    }
    
    /**
     * Closes any open resources and stops an active iteration
     */
    public void close() {
        closed = true;
        if (reader != null) {
            try {
                reader.close();            
            } catch (IOException e) {
                // Ignored, because we simply try to close the stream to avoid
                // resources being locked.
            }
        }
    }

    
    private void advance() {
        try {
            if (hasNext()) {
                if (curSplitLineIdx < nextSplitLine.length - 1) {
                    ++curSplitLineIdx;
                }
                else {
                    readNextLine();
                }
            }
        } catch (IOException e) {
            close();
            throw new IllegalStateException(e);
        }
    }

    private void readNextLine() throws IOException {
        var nextLine = reader.readLine();
        if (nextLine != null) {
            nextSplitLine = lineSplitterLogic.splitLine(nextLine);
            curSplitLineIdx = 0;
        }
        else {
            nextSplitLine = null;
            close();
        }
    }
}

