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

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.client.apache.BaseHttpClient;
import ca.uhn.fhir.rest.client.api.Header;
import ca.uhn.fhir.rest.client.api.IHttpClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApacheHttpClient5 extends BaseHttpClient implements IHttpClient {

    private final CloseableHttpClient client;

    public ApacheHttpClient5(CloseableHttpClient client,
                             StringBuilder url,
                             Map<String, List<String>> ifNoneExistParams,
                             String ifNoneExistString,
                             RequestTypeEnum requestType,
                             List<Header> theHeaders) {
        super(url, ifNoneExistParams, ifNoneExistString, requestType, theHeaders);
        this.client = client;
    }

    @Override
    protected IHttpRequest createHttpRequest() {
        return createHttpRequest((HttpEntity) null);
    }

    @Override
    protected IHttpRequest createHttpRequest(byte[] content) {
        return createHttpRequest(new ByteArrayEntity(content, null));
    }

    @Override
    protected IHttpRequest createHttpRequest(Map<String, List<String>> params) {
        List<NameValuePair> parameters = new ArrayList<>();
        params.forEach((key, value) -> value.stream()
            .map(s -> new BasicNameValuePair(key, s))
            .forEach(parameters::add));
        UrlEncodedFormEntity entity = createFormEntity(parameters);
        return createHttpRequest(entity);
    }

    private UrlEncodedFormEntity createFormEntity(List<NameValuePair> parameters) {
        return new UrlEncodedFormEntity(parameters, StandardCharsets.UTF_8);
    }

    @Override
    protected IHttpRequest createHttpRequest(String content) {
        return createHttpRequest(content.getBytes(StandardCharsets.UTF_8));
    }

    private ApacheHttpRequest5 createHttpRequest(HttpEntity entity) {
        return new ApacheHttpRequest5(client, constructRequest(entity).build());
    }

    private ClassicRequestBuilder constructRequest(HttpEntity entity) {
        String url = myUrl.toString();
        return switch (myRequestType) {
            case DELETE -> ClassicRequestBuilder.delete(url);
            case PATCH -> ClassicRequestBuilder.patch(url).setEntity(entity);
            case OPTIONS -> ClassicRequestBuilder.options(url);
            case POST -> ClassicRequestBuilder.post(url).setEntity(entity);
            case PUT -> ClassicRequestBuilder.put(url).setEntity(entity);
            default -> ClassicRequestBuilder.get(url);
        };
    }
}
