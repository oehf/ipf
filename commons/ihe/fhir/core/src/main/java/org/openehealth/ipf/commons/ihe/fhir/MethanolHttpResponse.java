/*
 * Copyright 2021 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class MethanolHttpResponse extends BaseHttpResponse {

    private final HttpResponse<InputStream> response;
    private boolean responseBuffered = false;
    private byte[] responseBuffer;

    public MethanolHttpResponse(HttpResponse<InputStream> response, StopWatch stopWatch) {
        super(stopWatch);
        this.response = response;
    }

    /**
     * Buffers the response entity. This is usually only called when HAPI client interceptors
     * want to do some logging or response capturing.
     *
     * @throws IOException
     */
    @Override
    public void bufferEntity() throws IOException {
        if (responseBuffered) return;
        try (var respEntity = readEntity()) {
            if (respEntity != null) {
                responseBuffered = true;
                try {
                    responseBuffer = IOUtils.toByteArray(respEntity);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        }
    }

    @Override
    public void close() {
        try {
            response.body().close();
        } catch (IOException ignore) {
        }
    }

    @Override
    public Reader createReader() {
        return new InputStreamReader(response.body());
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        return response.headers().map();
    }

    @Override
    public List<String> getHeaders(String name) {
        return response.headers().allValues(name);
    }

    @Override
    public String getMimeType() {
        return response.headers().firstValue(Constants.HEADER_CONTENT_TYPE).orElse(null);
    }

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public int getStatus() {
        return response.statusCode();
    }

    @Override
    public String getStatusInfo() {
        return "";
    }

    @Override
    public InputStream readEntity() throws IOException {
        return responseBuffered ? new ByteArrayInputStream(responseBuffer) : response.body();
    }

}
