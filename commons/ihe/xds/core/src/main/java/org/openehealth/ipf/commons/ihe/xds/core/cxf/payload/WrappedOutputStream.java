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
package org.openehealth.ipf.commons.ihe.xds.core.cxf.payload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An implementation of output stream which serves as a proxy for another output
 * stream instance and collects the data pieces to be written in a string buffer
 * (these pieces are XML and/or MIME artifacts).
 * <p>
 * Collecting of data is automagically deactivated on first read access. After
 * that, only dumb proxy functionality remains.
 * 
 * @author Dmytro Rud
 */
public class WrappedOutputStream extends FilterOutputStream {

    private final StringBuilder payloadCollector;
    private boolean isActive;

    /**
     * Constructor.
     * 
     * @param os the output data stream to be wrapped
     */
    public WrappedOutputStream(OutputStream os) {
        super(os);
        this.isActive = true;
        this.payloadCollector = new StringBuilder();
    }

    /**
     * Returns the collected message payload and deactivates 
     * any further data collecting.
     * @return SOAP payload as a <code>String</code>
     */
    public String getCollectedPayloadAndDeactivate() {
        isActive = false;
        return payloadCollector.toString();
    }

    /* ----- implementation of standard OutputStream methods ----- */

    @Override
    public void write(byte[] b) throws IOException {
        super.write(b);
        if (isActive) {
            payloadCollector.append(new String(b));
        }
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        super.write(b, off, len);
        if (isActive) {
            payloadCollector.append(new String(b, off, len));
        }
    }
}
