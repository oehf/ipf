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
package org.openehealth.ipf.platform.camel.http;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class HugeContentInputStream extends InputStream {
    public static final long CONTENT_SIZE = 1024 * 1024 * 100 + 5;

    private long readBytes;
    private int count;
    
    @Override
    public int read() throws IOException {
        if (readBytes == CONTENT_SIZE) {
            return -1;
        }
        ++readBytes;
        return 'L';
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (readBytes == CONTENT_SIZE) {
            return -1;
        }

        long sizeToRead = len;
        if (readBytes + sizeToRead > CONTENT_SIZE) {
            sizeToRead = Math.max(0, CONTENT_SIZE - readBytes);
        }
        
        Arrays.fill(b, off, (int)(off + sizeToRead), (byte)65);
        readBytes += sizeToRead;
        if (++count == 1000) {
            count = 0;
        }
        return (int) sizeToRead;
    }
    
    public long getReadBytes() {
        return readBytes;
    }
    
    @Override
    public void close() throws IOException {
        super.close();
        assertEquals(CONTENT_SIZE, readBytes);
        readBytes = 0;
    }
}
