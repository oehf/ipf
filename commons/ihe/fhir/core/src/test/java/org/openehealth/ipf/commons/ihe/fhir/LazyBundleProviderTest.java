/*
 * Copyright 2016 the original author or authors.
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

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import io.undertow.servlet.spec.HttpServletResponseImpl;
import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.easymock.EasyMock.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 *
 */
public class LazyBundleProviderTest {

    private static final int MAX_SIZE = 50;
    private RequestConsumer requestConsumer;
    private AbstractBundleProvider bundleProvider;
    private List<IBaseResource> response;

    @BeforeEach
    public void setup() {
        requestConsumer = EasyMock.createMock(RequestConsumer.class);
        response = new ArrayList<>();
        for (var i = 0; i < MAX_SIZE; i++) {
            response.add(new Patient().setId(Integer.toString(i)));
        }
        var payload = new Object();
        var headers = new HashMap<String, Object>();
        bundleProvider = new LazyBundleProvider(requestConsumer, true, payload, headers, new HttpServletResponseImpl(null, null));
    }

    @Test
    public void testGetSize() {
        EasyMock.expect(requestConsumer.handleSizeRequest(eq(bundleProvider.getPayload()), hasRequestSizeParameter())).andReturn(MAX_SIZE);
        EasyMock.replay(requestConsumer);
        assertEquals(response.size(), Objects.requireNonNull(bundleProvider.size()).intValue());
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResources() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30), eq(new HashMap<>())))
                .andReturn(response.subList(10, 30));
        EasyMock.replay(requestConsumer);
        var result = bundleProvider.getResources(10, 30);
        assertEquals(20, result.size());
        assertEquals(response.subList(10, 30), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesAlreadyCached() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30), eq(new HashMap<>())))
                .andReturn(response.subList(10, 30));
        EasyMock.replay(requestConsumer);

        // Now get a subset of what was already retrieved. No further calls to handleBundleRequest
        bundleProvider.getResources(10, 30);
        var result = bundleProvider.getResources(15, 20);

        assertEquals(5, result.size());
        assertEquals(response.subList(15, 20), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached1() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30), eq(new HashMap<>())))
                .andReturn(response.subList(10, 30));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 40), eq(new HashMap<>())))
                .andReturn(response.subList(30, 40));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 30, afterwards 20 to 40. The expectation is that with the second call only
        // elements 30 to 40 are returned.
        bundleProvider.getResources(10, 30);
        var result = bundleProvider.getResources(20, 40);

        assertEquals(20, result.size());
        assertEquals(response.subList(20, 40), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached2() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30), eq(new HashMap<>())))
                .andReturn(response.subList(10, 30));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(5, 10), eq(new HashMap<>())))
                .andReturn(response.subList(5, 10));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 35), eq(new HashMap<>())))
                .andReturn(response.subList(30, 35));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 30, afterwards 5 to 35. The expectation is that with the second call si split into
        // getting elements 5 to 10 and 30 to 35.
        bundleProvider.getResources(10, 30);
        var result = bundleProvider.getResources(5, 35);

        assertEquals(30, result.size());
        assertEquals(response.subList(5, 35), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached3() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 20), eq(new HashMap<>())))
                .andReturn(response.subList(10, 20));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 40), eq(new HashMap<>())))
                .andReturn(response.subList(30, 40));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(20, 30), eq(new HashMap<>())))
                .andReturn(response.subList(20, 30));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 20, then 30 to 40, then 15 to 35. The expectation is that with the third call
        // only elements 20 to 30 are fetched
        bundleProvider.getResources(10, 20);
        bundleProvider.getResources(30, 40);
        var result = bundleProvider.getResources(15, 35);

        assertEquals(20, result.size());
        assertEquals(response.subList(15, 35), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testBoundToLaterRequest() {
        var originalResponse = new MockHttpServletResponse();
        var originalRequest = requestDetails(new MockHttpServletRequest("GET", "/fhir/Patient"), originalResponse);
        originalRequest.setRestOperationType(RestOperationTypeEnum.SEARCH_TYPE);
        originalRequest.setResourceName("Patient");
        originalRequest.setRequestPath("Patient");
        originalRequest.setParameters(Map.of("family", new String[]{"Miller"}));
        originalRequest.getUserData().put("original", "value");
        var headers = new HashMap<String, Object>();
        headers.put(Constants.FHIR_REQUEST_DETAILS, originalRequest);
        var payload = new Object();
        var original = new LazyBundleProvider(requestConsumer, true, payload, headers, originalResponse);

        var currentServletRequest = new MockHttpServletRequest("GET", "/fhir");
        var currentResponse = new MockHttpServletResponse();
        var currentRequest = requestDetails(currentServletRequest, currentResponse);
        currentRequest.setRestOperationType(RestOperationTypeEnum.GET_PAGE);

        var seenRequestDetails = new ArrayList<ServletRequestDetails>();
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(payload), hasRequestSublistParameters(10, 20), EasyMock.anyObject()))
                .andAnswer(() -> {
                    Map<String, Object> inHeaders = EasyMock.getCurrentArgument(1);
                    Map<String, Object> outHeaders = EasyMock.getCurrentArgument(2);
                    seenRequestDetails.add((ServletRequestDetails) inHeaders.get(Constants.FHIR_REQUEST_DETAILS));
                    outHeaders.put(Constants.HTTP_OUTGOING_HEADERS, Map.of("X-Test", List.of("page")));
                    return response.subList(10, 20);
                });
        EasyMock.replay(requestConsumer);

        var bound = original.boundTo(currentRequest);
        assertEquals(response.subList(10, 20), bound.getResources(10, 20));

        // Describes the original search, but on the servlet request and response of the current request
        var rebound = seenRequestDetails.get(0);
        assertNotSame(originalRequest, rebound);
        assertSame(currentServletRequest, rebound.getServletRequest());
        assertSame(currentResponse, rebound.getServletResponse());
        assertEquals(RestOperationTypeEnum.SEARCH_TYPE, rebound.getRestOperationType());
        assertEquals("Patient", rebound.getResourceName());
        assertEquals("Patient", rebound.getRequestPath());
        assertEquals("Miller", rebound.getParameters().get("family")[0]);
        assertEquals("value", rebound.getUserData().get("original"));
        assertEquals("page", currentResponse.getHeader("X-Test"));
        assertNull(originalResponse.getHeader("X-Test"));

        // The copy shares the cached results with the original, so no further consumer call is needed
        assertEquals(response.subList(12, 18), original.getResources(12, 18));
        assertEquals(response.subList(12, 18), original.boundTo(currentRequest).getResources(12, 18));
        EasyMock.verify(requestConsumer);
    }

    private static ServletRequestDetails requestDetails(MockHttpServletRequest request, MockHttpServletResponse response) {
        var requestDetails = new ServletRequestDetails();
        requestDetails.setServletRequest(request);
        requestDetails.setServletResponse(response);
        return requestDetails;
    }


    private static Map<String, Object> hasRequestSizeParameter() {
        EasyMock.reportMatcher(new HasRequestSizeParameter());
        return null;
    }

    private static class HasRequestSizeParameter implements IArgumentMatcher {

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append("hasSizeParameter()");
        }

        @Override
        public boolean matches(Object argument) {
            var headers = (Map<String, Object>)argument;
            return headers.containsKey(Constants.FHIR_REQUEST_SIZE_ONLY);
        }
    }

    private static Map<String, Object> hasRequestSublistParameters(int expectedFrom, int expectedTo) {
        EasyMock.reportMatcher(new RequestSublistParameters(expectedFrom, expectedTo));
        return null;
    }

    private static class RequestSublistParameters implements IArgumentMatcher {

        private final int expectedFromIndex;
        private final int expectedToIndex;

        public RequestSublistParameters(int expectedFromIndex, int expectedToIndex) {
            this.expectedFromIndex = expectedFromIndex;
            this.expectedToIndex = expectedToIndex;
        }

        @Override
        public void appendTo(StringBuffer buffer) {
            buffer.append(String.format("hasRequestSublistParameter(%d,%d)", expectedFromIndex, expectedToIndex));
        }

        @Override
        public boolean matches(Object argument) {
            var headers = (Map<String, Object>)argument;
            return headers.get(Constants.FHIR_FROM_INDEX).equals(expectedFromIndex) &&
                    headers.get(Constants.FHIR_TO_INDEX).equals(expectedToIndex);
        }
    }
}
