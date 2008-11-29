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
package org.openehealth.ipf.commons.core.modules.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import javax.xml.transform.Result;
import javax.xml.transform.Source;

/**
 * Abstract class that serializes parsing, translating and rendering steps.
 * 
 * @author Christian Ohr
 * 
 * @param <S>
 *            output type of parse(), input type translate()
 * @param <T>
 *            output type of translate() input type of render()
 */
public abstract class Converter<S, T> implements Parser<S>, Renderer<T>, Transmogrifier<S, T> {

    /**
     * Parses a message and renders the internal model into a different external
     * representation.
     * 
     * @param message
     * @return
     * @throws ParseException
     * @throws RenderException
     */
    public final String convert(String message, Object... params) {
        S parsed = parse(message, params);
        T translated = zap(parsed, params);
        return render(translated, params);
    }

    /**
     * Parses a message and renders the internal model into a different external
     * representation.
     * 
     * @param message
     * @param params
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws RenderException
     */
    public final OutputStream convert(InputStream in, OutputStream out, Object... params) throws IOException {
        S parsed = parse(in, params);
        T translated = zap(parsed, params);
        return render(translated, out, params);
    }

    /**
     * Parses a message and renders the internal model into a different external
     * representation.
     * 
     * @param source
     * @param params
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws RenderException
     */
    public final Result convert(Source source, Result result, Object... params) throws IOException {
        S parsed = parse(source, params);
        T translated = zap(parsed, params);
        return render(translated, result, params);
    }

    /**
     * Parses a message and renders the internal model into a different external
     * representation.
     * 
     * @param source
     * @param params
     * @return
     * @throws IOException
     * @throws ParseException
     * @throws RenderException
     */
    public final Writer convert(Reader reader, Writer writer, Object... params) throws IOException {
        S parsed = parse(reader, params);
        T translated = zap(parsed, params);
        return render(translated, writer, params);
    }

}
