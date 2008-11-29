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
package org.openehealth.ipf.platform.camel.test.transform.min;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.openehealth.ipf.commons.core.modules.api.Converter;

/**
 * @author Martin Krasser
 */
public class TestConverter extends Converter<String, String> {

    public String parse(String message, Object... params) {
        return "string: " + message;
    }

    public String parse(Reader reader, Object... params) throws IOException {
        return "reader: " + IOUtils.toString(reader);
    }

    public String parse(InputStream message, Object... params) throws IOException {
        return "stream: " + IOUtils.toString(message);
    }

    public String parse(Source source, Object... params) throws IOException {
        return "source: " + toString(source);
    }

    public Result render(String model, Result result, Object... params) throws IOException {
        StreamResult r = (StreamResult)result;
        IOUtils.write(model, r.getWriter());
        return r;
    }

    public OutputStream render(String model, OutputStream result, Object... params) throws IOException {
        IOUtils.write(model, result);
        return result;
    }

    public Writer render(String model, Writer result, Object... params) throws IOException {
        IOUtils.write(model, result);
        return result;
    }

    public String render(String model, Object... params) {
        return model;
    }

    public String zap(String object, Object... params) {
        return object;
    }

    private static String toString(Source source) throws IOException {
        StreamSource s = (StreamSource)source;
        return IOUtils.toString(s.getReader());
    }
    
}
