/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.streaming;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_ENCODING;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;

/**
 * Request wrapper serving a body that has been replaced in memory. The replacement is always uncompressed and
 * of known length, so {@code Content-Length} is corrected and {@code Content-Encoding} is hidden.
 *
 * @author Christian Ohr
 */
class ReplacedBodyRequest extends HttpServletRequestWrapper {

    private final byte[] body;

    /**
     * The servlet contract expects repeated calls to hand back the same, progressively consumed stream, so it is
     * created once rather than per call.
     */
    private ServletInputStream inputStream;

    ReplacedBodyRequest(HttpServletRequest request, byte[] body) {
        super(request);
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() {
        if (inputStream == null) {
            inputStream = newInputStream();
        }
        return inputStream;
    }

    private ServletInputStream newInputStream() {
        var delegate = new ByteArrayInputStream(body);
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return delegate.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                throw new UnsupportedOperationException("Asynchronous reads are not supported");
            }

            @Override
            public int read() {
                return delegate.read();
            }

            @Override
            public int read(byte[] b, int off, int len) {
                return delegate.read(b, off, len);
            }

            @Override
            public int available() {
                return delegate.available();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(body), charset()));
    }

    private Charset charset() {
        var encoding = getCharacterEncoding();
        return encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
    }

    @Override
    public int getContentLength() {
        return body.length;
    }

    @Override
    public long getContentLengthLong() {
        return body.length;
    }

    @Override
    public String getHeader(String name) {
        if (CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return String.valueOf(body.length);
        }
        if (CONTENT_ENCODING.equalsIgnoreCase(name)) {
            return null;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return Collections.enumeration(List.of(String.valueOf(body.length)));
        }
        if (CONTENT_ENCODING.equalsIgnoreCase(name)) {
            return Collections.emptyEnumeration();
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(Collections.list(super.getHeaderNames()).stream()
                .filter(name -> !CONTENT_ENCODING.equalsIgnoreCase(name))
                .collect(Collectors.toList()));
    }

    @Override
    public int getIntHeader(String name) {
        return CONTENT_LENGTH.equalsIgnoreCase(name) ? body.length : super.getIntHeader(name);
    }
}
