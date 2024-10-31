/*
 * Copyright 2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.client.api.BaseHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.routing.RoutingSupport;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpException;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class ApacheHttpRequest5 extends BaseHttpRequest implements IHttpRequest {

    private final ClassicHttpRequest request;
    private final CloseableHttpClient httpClient;

    public ApacheHttpRequest5(CloseableHttpClient httpClient, ClassicHttpRequest request) {
        this.request = request;
        this.httpClient = httpClient;
    }

    @Override
    public void addHeader(String name, String value) {
        request.addHeader(name, value);
    }

    @Override
    public IHttpResponse execute() throws IOException {
        var responseStopWatch = new StopWatch();
        try {
            // BaseClient will close the response stream for us
            var response = httpClient.executeOpen(RoutingSupport.determineHost(request), request, null);
            return new ApacheHttpResponse5(response, responseStopWatch);
        } catch (HttpException e) {
            throw new IOException(e);
        }

    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        Map<String, List<String>> result = new HashMap<>();
        for (var header : request.getHeaders()) {
            if (!result.containsKey(header.getName())) {
                result.put(header.getName(), new LinkedList<>());
            }
            result.get(header.getName()).add(header.getValue());
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public String getRequestBodyFromStream() throws IOException {
        var entity = request.getEntity();
        if (entity.isRepeatable()) {
            final var contentTypeHeader = request.getFirstHeader("Content-Type");
            var charset = contentTypeHeader == null
                ? null
                : ContentType.parse(contentTypeHeader.getValue()).getCharset();
            return IOUtils.toString(entity.getContent(), charset);
        }
        return null;
    }

    @Override
    public String getUri() {
        return request.getRequestUri();
    }

    @Override
    public void setUri(String uri) {
        request.setUri(URI.create(uri));
    }

    @Override
    public String getHttpVerbName() {
        return request.getMethod();
    }

    @Override
    public void removeHeaders(String name) {
        request.removeHeaders(name);
    }
}
