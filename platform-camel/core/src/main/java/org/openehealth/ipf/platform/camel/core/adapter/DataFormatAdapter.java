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

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.camel.Exchange;
import org.apache.camel.spi.DataFormat;
import org.openehealth.ipf.commons.core.modules.api.Converter;
import org.openehealth.ipf.commons.core.modules.api.Parser;
import org.openehealth.ipf.commons.core.modules.api.Renderer;

/**
 * @author Martin Krasser
 */
public class DataFormatAdapter extends AdapterSupport implements DataFormat {

    private Parser parser;
    private Renderer renderer;
    
    public DataFormatAdapter(Parser parser) {
        this(parser, null);
    }
    
    public DataFormatAdapter(Renderer renderer) {
        this(null, renderer);
    }
    
    public DataFormatAdapter(Parser parser, Renderer renderer) {
        this.parser = parser;
        this.renderer = renderer;
    }
    
    public DataFormatAdapter(Converter converter) {
        this.parser = converter;
        this.renderer = converter;
    }
    
    @Override
    public void marshal(Exchange exchange, Object graph, OutputStream stream) throws Exception {
        Object input = adaptInput(exchange);
        Object params = adaptParams(exchange);
        renderer.render(input, stream, params);
    }

    @Override
    public Object unmarshal(Exchange exchange, InputStream stream) throws Exception {
        Object params = adaptParams(exchange);
        return parser.parse(stream, params);
    }

}