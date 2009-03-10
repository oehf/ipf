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
package org.openehealth.ipf.platform.camel.http;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.http.HttpEndpoint;
import org.apache.camel.component.http.HttpOperationFailedException;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpProducer extends org.apache.camel.component.http.HttpProducer {
    private static final transient Log LOG = LogFactory.getLog(HttpProducer.class);

    /**
     * Constructs the producer
     * @param endpoint
     *          endpoint that this producer is created for
     */
    public HttpProducer(HttpEndpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        HttpMethod method = createMethod(exchange);
        Message in = exchange.getIn();
        HeaderFilterStrategy strategy = ((HttpEndpoint)getEndpoint()).getHeaderFilterStrategy();

        // propagate headers as HTTP headers
        for (String headerName : in.getHeaders().keySet()) {
            String headerValue = in.getHeader(headerName, String.class);
            if (strategy != null && !strategy.applyFilterToCamelHeaders(headerName, headerValue)) {
                method.addRequestHeader(headerName, headerValue);
            }
        }

        // lets store the result in the output message.
        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing http " + method.getName() + " method: " + method.getURI().toString());
        }
        int responseCode = executeMethod(method);
        if (LOG.isDebugEnabled()) {
            LOG.debug("Http responseCode: " + responseCode);
        }

        if (responseCode >= 100 && responseCode < 300) {
            Message answer = exchange.getOut(true);

            answer.setHeaders(in.getHeaders());
            answer.setHeader(HTTP_RESPONSE_CODE, responseCode);
            answer.setBody(extractResponseBody(method));

            // propagate HTTP response headers
            Header[] headers = method.getResponseHeaders();
            for (Header header : headers) {
                String name = header.getName();
                String value = header.getValue();
                if (strategy != null && !strategy.applyFilterToExternalHeaders(name, value)) {
                    answer.setHeader(name, value);
                }
            }
        } else {
            HttpOperationFailedException exception = null;
            Header[] headers = method.getResponseHeaders();
            InputStream is =  extractResponseBody(method);
            if (responseCode >= 300 && responseCode < 400) {
                String redirectLocation;
                Header locationHeader = method.getResponseHeader("location");
                if (locationHeader != null) {
                    redirectLocation = locationHeader.getValue();
                    exception = new HttpOperationFailedException(responseCode, method.getStatusLine(), redirectLocation, headers, is);
                } else {
                    // no redirect location
                    exception = new HttpOperationFailedException(responseCode, method.getStatusLine(), headers, is);
                }
            } else {
                // internal server error (error code 500)
                exception = new HttpOperationFailedException(responseCode, method.getStatusLine(), headers, is);
            }

            if (exception != null) {                    
                throw exception;
            }
        }
    }
    
    protected static InputStream extractResponseBody(final HttpMethod method) throws IOException {
        InputStream originalStream = method.getResponseBodyAsStream();
        return new AutoCloseInputStream(originalStream, method);
    }

    private static final class AutoCloseInputStream extends FilterInputStream {
        private final HttpMethod method;
        private boolean closed;

        private AutoCloseInputStream(InputStream in, HttpMethod method) {
            super(in);
            this.method = method;
        }

        @Override
        public int read() throws IOException {
            if (closed) {
                return -1;
            }                    
            return checkClose(super.read());
        }

        @Override
        public int read(byte[] b) throws IOException {
            if (closed) {
                return -1;
            }                    
            return checkClose(super.read(b));
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (closed) {
                return -1;
            }                    
            return checkClose(super.read(b, off, len));
        }

        @Override
        public void close() throws IOException {
            super.close();
            closed = true;
            method.releaseConnection();
        }

        private int checkClose(int read) throws IOException {
            if (read == -1) {
                close();
            }
            return read;
        }
    }
}
