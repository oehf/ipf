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
package org.openehealth.ipf.platform.camel.core.adapter;

import static org.openehealth.ipf.platform.camel.core.util.Exchanges.prepareResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.core.modules.api.Converter;
import org.openehealth.ipf.platform.camel.core.io.ReadableByteArrayOutputStream;
import org.openehealth.ipf.platform.camel.core.io.ReadableStreamResult;
import org.openehealth.ipf.platform.camel.core.io.ReadableStringWriter;


/**
 * Adapts a {@link Converter}. 
 * 
 * @author Martin Krasser
 */
public class ConverterAdapter extends ProcessorAdapter {

    private final Converter converter;
    
    /**
     * Creates a new {@link ConverterAdapter} and sets the delegate
     * {@link Converter}.
     * 
     * @param converter
     *            a converter.
     */
    public ConverterAdapter(Converter converter) {
        this.converter = converter;
    }

    /**
     * Processes input data and populates the output message. This method
     * delegates message processing to more specialized <code>doProcess</code>
     * implementations.
     * 
     * @param exchange
     *            message exchange where to write processing results.
     * @param inputData
     *            input data.
     * @param inputParams
     *            input parameters.
     * @throws Exception
     *             if a processing error occurs.
     * 
     * @see #doProcess(InputStream, OutputStream, Object...)
     * @see #doProcess(Reader, Writer, Object...)
     * @see #doProcess(Source, Result, Object...)
     * @see #doProcess(String, Object...)
     */
    @Override
    protected void doProcess(Exchange exchange, Object inputData, 
            Object... inputParams) throws Exception {
        
        if (inputData instanceof InputStream inputStream) {
            var outputData = new ReadableByteArrayOutputStream();
            doProcess(inputStream, outputData, inputParams);
            prepareResult(exchange).setBody(outputData.inputStream());
        } else if (inputData instanceof Reader reader) {
            var outputData = new ReadableStringWriter();
            doProcess(reader, outputData, inputParams);
            prepareResult(exchange).setBody(outputData.reader());
        } else if (inputData instanceof Source source) {
            var outputData = new ReadableStreamResult();
            doProcess(source, outputData, inputParams);
            prepareResult(exchange).setBody(outputData.source());
        } else if (inputData instanceof String s) {
            prepareResult(exchange).setBody(doProcess(s, inputParams));
        } else {
            throw new IllegalArgumentException(
                    "input data class not supported: " + inputData.getClass());
        }
        
    }
    
    /**
     * Processes <code>inputData</code> and <code>inputParams</code> and
     * writes converted result to <code>outputData</code>. Conversion is
     * delegated to the {@link Converter} set at construction time.
     * 
     * @param inputData
     *            input data
     * @param outputData
     *            output data
     * @param inputParams
     *            input parameters
     * @throws IOException
     *             if a system-level problem occurs
     */
    protected void doProcess(InputStream inputData, OutputStream outputData, Object... inputParams) throws IOException {
        converter.convert(inputData, outputData, inputParams);
    }
    
    /**
     * Processes <code>inputData</code> and <code>inputParams</code> and
     * writes converted result to <code>outputData</code>. Conversion is
     * delegated to the {@link Converter} set at construction time.
     * 
     * @param inputData
     *            input data
     * @param outputData
     *            output data
     * @param inputParams
     *            input parameters
     * @throws IOException
     *             if a system-level problem occurs
     */
    protected void doProcess(Reader inputData, Writer outputData, Object... inputParams) throws IOException {
        converter.convert(inputData, outputData, inputParams);
    }
    
    /**
     * Processes <code>inputData</code> and <code>inputParams</code> and
     * writes converted result to <code>outputData</code>. Conversion is
     * delegated to the {@link Converter} set at construction time.
     * 
     * @param inputData
     *            input data
     * @param outputData
     *            output data
     * @param inputParams
     *            input parameters
     * @throws IOException
     *             if a system-level problem occurs
     */
    protected void doProcess(Source inputData, Result outputData, Object... inputParams) throws IOException {
        converter.convert(inputData, outputData, inputParams);
    }
    
    /**
     * Processes <code>inputData</code> and <code>inputParams</code> and
     * returns the converted result. Conversion is delegated to the
     * {@link Converter} set at construction time.
     * 
     * @param inputData
     *            input data
     * @param inputParams
     *            input parameters
     * @return converted result.
     */
    protected String doProcess(String inputData, Object... inputParams) {
        return converter.convert(inputData, inputParams);
    }
    
}
