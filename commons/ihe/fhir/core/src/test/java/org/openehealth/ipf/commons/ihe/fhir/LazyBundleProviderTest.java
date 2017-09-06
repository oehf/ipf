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

import org.easymock.EasyMock;
import org.easymock.IArgumentMatcher;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.eq;
import static org.junit.Assert.assertEquals;

/**
 *
 */
public class LazyBundleProviderTest {

    private static final int MAX_SIZE = 50;
    private RequestConsumer requestConsumer;
    private AbstractBundleProvider bundleProvider;
    private List<IBaseResource> response;

    @Before
    public void setup() {
        requestConsumer = EasyMock.createMock(RequestConsumer.class);
        response = new ArrayList<>();
        for (int i = 0; i < MAX_SIZE; i++) {
            response.add(new Patient().setId(Integer.toString(i)));
        }
        Object payload = new Object();
        Map<String, Object> headers = new HashMap<>();
        bundleProvider = new LazyBundleProvider(requestConsumer, true, payload, headers);
    }

    @Test
    public void testGetSize() {
        EasyMock.expect(requestConsumer.handleSizeRequest(eq(bundleProvider.getPayload()), hasRequestSizeParameter())).andReturn(MAX_SIZE);
        EasyMock.replay(requestConsumer);
        assertEquals(response.size(), bundleProvider.size().intValue());
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResources() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30)))
                .andReturn(response.subList(10, 30));
        EasyMock.replay(requestConsumer);
        List<IBaseResource> result = bundleProvider.getResources(10, 30);
        Assert.assertEquals(20, result.size());
        Assert.assertEquals(response.subList(10, 30), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesAlreadyCached() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30)))
                .andReturn(response.subList(10, 30));
        EasyMock.replay(requestConsumer);

        // Now get a subset of what was already retrieved. No further calls to handleBundleRequest
        bundleProvider.getResources(10, 30);
        List<IBaseResource> result = bundleProvider.getResources(15, 20);

        Assert.assertEquals(5, result.size());
        Assert.assertEquals(response.subList(15, 20), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached1() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30)))
                .andReturn(response.subList(10, 30));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 40)))
                .andReturn(response.subList(30, 40));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 30, afterwards 20 to 40. The expectation is that with the second call only
        // elements 30 to 40 are returned.
        bundleProvider.getResources(10, 30);
        List<IBaseResource> result = bundleProvider.getResources(20, 40);

        Assert.assertEquals(20, result.size());
        Assert.assertEquals(response.subList(20, 40), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached2() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 30)))
                .andReturn(response.subList(10, 30));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(5, 10)))
                .andReturn(response.subList(5, 10));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 35)))
                .andReturn(response.subList(30, 35));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 30, afterwards 5 to 35. The expectation is that with the second call si split into
        // getting elements 5 to 10 and 30 to 35.
        bundleProvider.getResources(10, 30);
        List<IBaseResource> result = bundleProvider.getResources(5, 35);

        Assert.assertEquals(30, result.size());
        Assert.assertEquals(response.subList(5, 35), result);
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResourcesPartlyCached3() {
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(10, 20)))
                .andReturn(response.subList(10, 20));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(30, 40)))
                .andReturn(response.subList(30, 40));
        EasyMock.expect(requestConsumer.handleBundleRequest(eq(bundleProvider.getPayload()), hasRequestSublistParameters(20, 30)))
                .andReturn(response.subList(20, 30));
        EasyMock.replay(requestConsumer);

        // First get elements 10 to 20, then 30 to 40, then 15 to 35. The expectation is that with the third call
        // only elements 20 to 30 are fetched
        bundleProvider.getResources(10, 20);
        bundleProvider.getResources(30, 40);
        List<IBaseResource> result = bundleProvider.getResources(15, 35);

        Assert.assertEquals(20, result.size());
        Assert.assertEquals(response.subList(15, 35), result);
        EasyMock.verify(requestConsumer);
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
            Map<String, Object> headers = (Map<String, Object>)argument;
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
            Map<String, Object> headers = (Map<String, Object>)argument;
            return headers.get(Constants.FHIR_FROM_INDEX).equals(expectedFromIndex) &&
                    headers.get(Constants.FHIR_TO_INDEX).equals(expectedToIndex);
        }
    }
}
