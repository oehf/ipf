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
package org.openehealth.ipf.platform.camel.lbs.converter;

import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataSource;

import org.apache.camel.Converter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public final class LbsConverter {
    private LbsConverter() {
        throw new UnsupportedOperationException("Utility classes cannot be instantiated");
    }
    
    private static final Log log = LogFactory.getLog(LbsConverter.class);
    
    /**
     * Converts a data source to an input stream
     * @param dataSource
     *          the data source
     * @return the input stream. The stream must be closed by the caller of the converter.
     *          This also applies when using the converter implicitly via 
     *          {@link Message#getBody(Class))}. Example:
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
     *          if an {@code IOException} was thrown by the data source
     */
    @Converter
    public static InputStream toInputStream(DataSource dataSource) throws IOException {
        log.info("converted data source to input stream: " + dataSource);
        return dataSource.getInputStream();
    }
    
    /**
     * Converts a data source to a String
     * @param dataSource
     *          the data source
     * @return text that was contained in the data source
     * @throws IOException
     *          if an {@code IOException} was thrown by the data source
     */
    @Converter
    public static String toString(DataSource dataSource) throws IOException {
        InputStream inputStream = dataSource.getInputStream();
        try {
            String result = IOUtils.toString(inputStream);
            log.info("converted data source to string: " + dataSource + " -> " + result);
            return result;
        }
        finally {
            inputStream.close();
        }
    }
    
    /**
     * Converts a data source to a byte array
     * @param dataSource
     *          the data source
     * @return byte array representing the content of the data source
     * @throws IOException
     *          if an {@code IOException} was thrown by the data source
     */
    @Converter
    public static byte[] toByteArray(DataSource dataSource) throws IOException {
        log.info("converted data source to byte[]: " + dataSource);
        InputStream inputStream = dataSource.getInputStream();
        try {
            return IOUtils.toByteArray(inputStream);
        }
        finally {
            inputStream.close();
        }
    }
}
