/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.core.converters;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class XdsRenderingUtilsTest {

    @Test
    public void testRenderProvideAndRegisterDocumentSet30() {
        ProvideAndRegisterDocumentSet pnr = SampleData.createProvideAndRegisterDocumentSet();
        org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType converted =
                EbXML30Converters.convert(pnr);
        String renderedPnr = XdsRenderingUtils.renderEbxml(converted);
        assertNotNull(renderedPnr);
        assertTrue(renderedPnr.contains("ProvideAndRegisterDocumentSetRequest"));
    }

    @Test
    public void testRenderProvideAndRegisterDocumentSet21() {
        ProvideAndRegisterDocumentSet pnr = SampleData.createProvideAndRegisterDocumentSet();
        org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType converted =
                EbXML21Converters.convert(pnr);
        String renderedPnr = XdsRenderingUtils.renderEbxml(converted);
        assertNotNull(renderedPnr);
        assertTrue(renderedPnr.contains("ProvideAndRegisterDocumentSetRequest"));
    }
}
