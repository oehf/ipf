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

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.BaseHttpClient;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MutableRequest;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MethanolHttpClient extends BaseHttpClient {

    private final Methanol client;
    private final boolean async;

    public MethanolHttpClient(Methanol client,
                              boolean async,
                              StringBuilder url,
                              Map<String, List<String>> ifNoneExistParams,
                              String ifNoneExistString,
                              RequestTypeEnum requestType,
                              List<Header> headers) {
        super(url, ifNoneExistParams, ifNoneExistString, requestType, headers);
        this.client = client;
        this.async = async;
    }

    @Override
    protected IHttpRequest createHttpRequest() {
        return createHttpRequest(HttpRequest.BodyPublishers.noBody());
    }

    @Override
    protected IHttpRequest createHttpRequest(byte[] content) {
        return createHttpRequest(HttpRequest.BodyPublishers.ofByteArray(content));
    }

    @Override
    protected IHttpRequest createHttpRequest(Map<String, List<String>> params) {
        var builder = params.entrySet().stream()
                .map(entry -> String.format("%s=%s",
                        encode(entry.getKey()),
                        encode(String.join(",", entry.getValue()))))
                .collect(Collectors.joining("&"));
        return createHttpRequest(HttpRequest.BodyPublishers.ofString(builder));
    }

    private static String encode(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    @Override
    protected IHttpRequest createHttpRequest(String content) {
        return createHttpRequest(HttpRequest.BodyPublishers.ofString(content, StandardCharsets.UTF_8));
    }

    protected IHttpRequest createHttpRequest(HttpRequest.BodyPublisher publisher) {
        var mutableRequest = MutableRequest.create(myUrl.toString())
                .method(myRequestType.name(), publisher);
        return async ? new MethanolHttpRequest.Async(client, mutableRequest) : new MethanolHttpRequest.Sync(client, mutableRequest);
    }

}
