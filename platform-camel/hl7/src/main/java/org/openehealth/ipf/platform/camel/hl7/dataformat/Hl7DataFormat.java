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
package org.openehealth.ipf.platform.camel.hl7.dataformat;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;
import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * @author Martin Krasser
 */
public class Hl7DataFormat implements DataFormat {

    private String charset;

    private HapiContext context = new DefaultHapiContext();
    private Parser parser;
    
    public Hl7DataFormat() {
        charset = System.getProperty("file.encoding");
        context = new DefaultHapiContext();
        parser = context.getGenericParser();
    }
    
    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    public void setContext(HapiContext context) {
        this.context = context;
        this.parser = context.getGenericParser();
    }

    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        MessageAdapter<?> adapter = (MessageAdapter<?>)graph;
        adapter.writeTo(new OutputStreamWriter(stream, charset));
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        return MessageAdapters.make(context.getGenericParser(), stream, charset);
        
    }

}
