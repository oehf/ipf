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
package org.openehealth.ipf.platform.camel.flow.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;

/**
 * Utility to format data using {@link DataFormat}s.
 * 
 * @author Martin Krasser
 */
public class DataFormats {

    /**
     * Marshals an object to a byte array.
     * 
     * @param object
     *            object to be marshalled.
     * @param exchange
     *            message exchange.
     * @param dataFormat
     *            data format to use for marshalling.
     * @return marshalled object.
     * @throws DataFormatException
     * 
     * @see DataFormat#marshal(Exchange, Object, java.io.OutputStream)
     */
    public static byte[] marshal(Object object, Exchange exchange, DataFormat dataFormat) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            dataFormat.marshal(exchange, object, stream);
        } catch (Exception e) {
            throw new DataFormatException("failed to marshal object", e);
        }
        return stream.toByteArray();
    }
    
    /**
     * Unmarshals an object from a byte array.
     * 
     * @param bytes byte array to be unmarshalled. 
     * @param exchange
     *            message exchange.
     * @param dataFormat
     *            data format to use for unmarshalling.
     * @return unmarshalled object.
     * @throws DataFormatException
     * 
     * @see DataFormat#unmarshal(Exchange, java.io.InputStream)
     */
    public static Object unmarshal(byte[] bytes, Exchange exchange, DataFormat dataFormat) {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        try {
            return dataFormat.unmarshal(exchange, stream);
        } catch (Exception e) {
            throw new DataFormatException("failed to unmarshal object", e);
        }
    }
    
}
