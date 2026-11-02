/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Operation;
import org.easymock.EasyMock;
import org.hl7.fhir.r4.model.Parameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockServletConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IpfFhirServletTest {

    private static final String SERVLET_NAME = "IpfFhirServletTest";

    @AfterEach
    public void cleanup() {
        DefaultFhirRegistry.removeFhirRegistry(SERVLET_NAME);
    }

    @Test
    public void testReinitializedServletKeepsProviders() throws Exception {
        var registry = DefaultFhirRegistry.getFhirRegistry(SERVLET_NAME);
        var provider = registry.resolveProvider(new TestOperationProvider());
        RequestConsumer consumer = EasyMock.niceMock(RequestConsumer.class);
        EasyMock.replay(consumer);
        provider.connect(consumer, registry);

        var servlet = new IpfFhirServlet(FhirVersionEnum.R4);
        servlet.init(new MockServletConfig(SERVLET_NAME));
        assertTrue(servlet.getPlainProviders().contains(provider));
        servlet.destroy();
        assertFalse(registry.hasServlet(SERVLET_NAME));

        // e.g. redeployment while the Camel context keeps running
        var reinitialized = new IpfFhirServlet(FhirVersionEnum.R4);
        reinitialized.init(new MockServletConfig(SERVLET_NAME));
        assertTrue(reinitialized.getPlainProviders().contains(provider));
        reinitialized.destroy();
    }

    public static class TestOperationProvider extends AbstractPlainProvider {

        @Operation(name = "$test", idempotent = true)
        public Parameters test() {
            return new Parameters();
        }
    }
}
