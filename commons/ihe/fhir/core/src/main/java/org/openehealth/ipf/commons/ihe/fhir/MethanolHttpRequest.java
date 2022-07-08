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

import ca.uhn.fhir.rest.client.api.BaseHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import com.github.mizosoft.methanol.Methanol;
import com.github.mizosoft.methanol.MutableRequest;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Flow;

import static java.util.Objects.requireNonNull;

abstract class MethanolHttpRequest extends BaseHttpRequest implements IHttpRequest {

    protected final Methanol httpClient;
    protected final MutableRequest request;

    public MethanolHttpRequest(Methanol httpClient, MutableRequest request) {
        this.httpClient = httpClient;
        this.request = request;
    }

    @Override
    public void addHeader(String name, String value) {
        requireNonNull(name, "name");
        requireNonNull(value, "value");
        request.header(name, value);
    }

    @Override
    public Map<String, List<String>> getAllHeaders() {
        return Collections.unmodifiableMap(request.headers().map());
    }

    /**
     * Returns the request entity. This is usually only called when HAPI client interceptors
     * want to do some logging or request capturing.
     *
     * @return request body as string
     */
    @Override
    public String getRequestBodyFromStream() {
        return request.bodyPublisher().map(this::wrapPublisher).orElse(null);
    }

    private String wrapPublisher(HttpRequest.BodyPublisher requestPublisher) {
        // Subscribe to the request as string
        var bodySubscriber = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);
        requestPublisher.subscribe(new StringSubscriber(bodySubscriber));
        return bodySubscriber.getBody().toCompletableFuture().join();
    }

    @Override
    public String getUri() {
        return request.uri().toString();
    }

    @Override
    public void setUri(String uri) {
        request.uri(uri);
    }

    @Override
    public String getHttpVerbName() {
        return request.method();
    }

    @Override
    public void removeHeaders(String headerName) {
        request.removeHeader(headerName);
    }

    public static class Sync extends MethanolHttpRequest {

        public Sync(Methanol httpClient, MutableRequest request) {
            super(httpClient, request);
        }

        @Override
        public IHttpResponse execute() {
            try {
                var responseStopWatch = new StopWatch();
                var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
                return new MethanolHttpResponse(response, responseStopWatch);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static class Async extends MethanolHttpRequest {

        public Async(Methanol httpClient, MutableRequest request) {
            super(httpClient, request);
        }

        @Override
        public IHttpResponse execute() {
            try {
                var responseStopWatch = new StopWatch();
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                        .thenApply(response -> new MethanolHttpResponse(response, responseStopWatch))
                        .get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static final class StringSubscriber implements Flow.Subscriber<ByteBuffer> {
        final HttpResponse.BodySubscriber<String> wrapped;

        StringSubscriber(HttpResponse.BodySubscriber<String> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            wrapped.onSubscribe(subscription);
        }

        @Override
        public void onNext(ByteBuffer item) {
            wrapped.onNext(List.of(item));
        }

        @Override
        public void onError(Throwable throwable) {
            wrapped.onError(throwable);
        }

        @Override
        public void onComplete() {
            wrapped.onComplete();
        }
    }
}
