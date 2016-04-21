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
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class EagerBundleProviderTest {

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
        bundleProvider = new EagerBundleProvider(requestConsumer, payload, headers, FhirValidator.NO_VALIDATION);
    }

    @Test
    public void testGetSize() {
        EasyMock.expect(requestConsumer.handleBundleRequest(bundleProvider.getPayload(), bundleProvider.getHeaders())).andReturn(response);
        EasyMock.replay(requestConsumer);
        assertEquals(response.size(), bundleProvider.size());
        EasyMock.verify(requestConsumer);
    }

    @Test
    public void testGetResources() {
        EasyMock.expect(requestConsumer.handleBundleRequest(bundleProvider.getPayload(), bundleProvider.getHeaders())).andReturn(response);
        EasyMock.replay(requestConsumer);
        List<IBaseResource> result = bundleProvider.getResources(10, 30);
        Assert.assertEquals(response.subList(10, 30), result);
        EasyMock.verify(requestConsumer);
    }
}
