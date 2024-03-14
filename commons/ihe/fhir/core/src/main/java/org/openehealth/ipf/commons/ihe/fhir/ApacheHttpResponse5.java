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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.apache.ApacheHttpResponse;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.impl.BaseHttpResponse;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ApacheHttpResponse5 extends BaseHttpResponse implements IHttpResponse {

    private static final Logger log = LoggerFactory.getLogger(ApacheHttpResponse.class);

    private final ClassicHttpResponse response;
    private boolean entityBuffered = false;
    private byte[] entityBytes;

    public ApacheHttpResponse5(ClassicHttpResponse response, StopWatch theRequestStopWatch) {
        super(theRequestStopWatch);
        this.response = response;
    }

    @Override
    public void bufferEntity() throws IOException {
        if (entityBuffered) {
            return;
        }
        try (var respEntity = readEntity()) {
            if (respEntity != null) {
                this.entityBuffered = true;
                try {
                    this.entityBytes = IOUtils.toByteArray(respEntity);
                } catch (IllegalStateException e) {
                    throw new InternalErrorException(Msg.code(1478) + e);
                }
            }
        }
    }

    @Override
    public void close() {
        if (response instanceof CloseableHttpResponse closeableHttpResponse) {
            try {
                closeableHttpResponse.close();
            } catch (IOException e) {
                log.debug("Failed to close response", e);
            }
        }
    }

    @Override
    public Reader createReader() throws IOException {
        var entity = response.getEntity();
        if (entity == null) {
            return new StringReader("");
        }
        return new InputStreamReader(readEntity(), getCharset(entity));
    }

    private Charset getCharset(HttpEntity entity) {
        if (entity.getContentType() != null && !entity.getContentType().isEmpty()) {
            var ct = ContentType.parse(entity.getContentType());
            if (ct.getCharset() != null) {
                return ct.getCharset();
            }
        }
        if (Constants.STATUS_HTTP_204_NO_CONTENT != response.getCode()) {
            log.debug("Response did not specify a charset, defaulting to utf-8");
        }
        return StandardCharsets.UTF_8;
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        Map<String, List<String>> headers = new HashMap<>();
        if (response.getHeaders() != null) {
            Arrays.stream(response.getHeaders()).forEach(header -> headers
                .computeIfAbsent(header.getName().toLowerCase(), k -> new ArrayList<>())
                .add(header.getValue()));
        }
        return headers;
    }

    @Override
    public List<String> getHeaders(String s) {
        var headers = response.getHeaders(s);
        if (headers == null) {
            return Collections.emptyList();
        }
        return Arrays.stream(headers)
            .map(NameValuePair::getValue)
            .toList();
    }

    @Override
    public String getMimeType() {
        ContentType ct = ContentType.parse(response.getEntity().getContentType());
        return ct != null ? ct.getMimeType() : null;
    }

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public int getStatus() {
        return response.getCode();
    }

    @Override
    public String getStatusInfo() {
        return response.getReasonPhrase();
    }

    @Override
    public InputStream readEntity() throws IOException {
        if (this.entityBuffered) {
            return new ByteArrayInputStream(entityBytes);
        } else if (response.getEntity() != null) {
            return response.getEntity().getContent();
        } else {
            return null;
        }
    }
}
