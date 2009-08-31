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
package org.openehealth.ipf.commons.ihe.xds.utils;

import javax.activation.DataSource;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Data source used in tests to provide a large content stream.
 * @author Jens Riemschneider
 */
public class LargeDataSource implements DataSource {
    /** Content stream size for tests with large content. Must be larger than 64K to ensure
     * CXF does use MTOM. */
    public static final int STREAM_SIZE = 70000;

    public InputStream getInputStream() throws IOException {
        return new InputStream() {
            private int idx;

            @Override
            public int read() throws IOException {
                if (idx >= STREAM_SIZE) {
                    return -1;
                }
                ++idx;
                return 65;
            }

            @Override
            public void close() throws IOException {
                junit.framework.Assert.assertEquals(STREAM_SIZE, idx);
                super.close();
            }
        };
    }

    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        return "test/plain";
    }

    public String getName() {
        return "dummy";
    }
}
