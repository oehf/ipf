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
package org.openehealth.ipf.platform.camel.lbs.http.converter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataSource;

import org.apache.camel.Converter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource;
import org.openehealth.ipf.platform.camel.lbs.http.process.ResourceList;

/**
 * Type converters specific to the Large Binary Store
 * <p>
 * Note: Packages containing type converters need to be registered in the file
 *       META-INF/services/org/apache/camel/TypeConverter of the jar containing the
 *       converters.
 *       
 * @author Jens Riemschneider
 */
@Converter
public final class LbsHttpConverter {
    private static final DataSource INVALID_DATA_SOURCE = new InvalidDataSource();

    private LbsHttpConverter() {
        throw new UnsupportedOperationException("Utility classes cannot be instantiated");
    }
    
    private static final Log log = LogFactory.getLog(LbsHttpConverter.class);
    
    /**
     * Converts a resource list to an input stream
     * <p>
     * This method returns the input stream for the first resource in the resource list
     * @param resourceList
     *          the resource list
     * @return the input stream. The stream must be closed by the caller of the converter.
     *          This also applies when using the converter implicitly via 
     *          {@link org.apache.camel.Message#getBody(Class))}. Example:
     *          <blockquote><pre><code>
     *          InputStream input = exchange.getIn().getBody(InputStream.class);
     *          try {
     *              ...
     *          }
     *          finally {
     *              input.close();
     *          }
     *          </code></pre></blockquote>
     * @throws IOException
     *          if an {@code IOException} was thrown by the resource
     */
    @Converter
    public static InputStream toInputStream(ResourceList resourceList) throws IOException {
        return toDataSource(resourceList).getInputStream();
    }
    
    /**
     * Converts a resource list to a data source
     * <p>
     * This method returns the data source for the first resource in the resource list
     * @param resourceList
     *          the resource list
     * @return the data source
     */
    @Converter
    public static DataSource toDataSource(ResourceList resourceList) {
        return toResourceDataSource(resourceList);
    }
    
    /**
     * Converts a resource list to a resource data source
     * <p>
     * This method returns the data source for the first resource in the resource list
     * @param resourceList
     *          the resource list
     * @return the data source
     */
    @Converter
    public static ResourceDataSource toResourceDataSource(ResourceList resourceList) {
        if (resourceList.size() == 0) {
            log.debug("creating a substitute data source because there is no content in the resource list");
            new ResourceDataSource("invalid", INVALID_DATA_SOURCE);
        }
        return resourceList.get(0);
    }
    
    /**
     * Converts a resource list to a String
     * <p>
     * This method returns the string representation for the first resource in the resource list
     * @param resourceList
     *          the resource list
     * @return text that was contained in the first element of the resource list
     * @throws IOException
     *          if an {@code IOException} was thrown by the resource
     */
    @Converter
    public static String toString(ResourceList resourceList) throws IOException {
        InputStream inputStream = toInputStream(resourceList);
        try {
            String result = IOUtils.toString(inputStream);
            log.debug("converted resource list to string: " + resourceList + " -> " + result);
            return result;
        }
        finally {
            inputStream.close();
        }
    }
    
    /**
     * Converts a resource list to a byte array
     * <p>
     * This method returns the byte array for the first resource in the resource list
     * @param resourceList
     *          the resource list
     * @return byte array representing the content of the resource
     * @throws IOException
     *          if an {@code IOException} was thrown by the resource
     */
    @Converter
    public static byte[] toByteArray(ResourceList resourceList) throws IOException {
        log.debug("converted resource to byte[]: " + resourceList);
        InputStream inputStream = toInputStream(resourceList);
        try {
            return IOUtils.toByteArray(inputStream);
        }
        finally {
            inputStream.close();
        }
    }

    private static final class InvalidDataSource implements DataSource {
        @Override
        public String getContentType() {
            return "unknown/unknown";
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(new byte[] {});
        }

        @Override
        public String getName() {
            return "invalid";
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("read only data source");
        }
    }
}
