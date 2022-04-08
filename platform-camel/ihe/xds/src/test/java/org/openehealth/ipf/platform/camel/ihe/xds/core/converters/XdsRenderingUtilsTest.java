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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;

import java.util.Collections;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XdsRenderingUtilsTest {

    @Test
    public void testRenderProvideAndRegisterDocumentSet30() {
        ProvideAndRegisterDocumentSet pnr = SampleData.createProvideAndRegisterDocumentSet();
        for (Document document : pnr.getDocuments()) {
            document.getDocumentEntry().setExtraMetadata(Collections.singletonMap(
                    "urn:e-health-suisse:2020:originalProviderRole",
                    Collections.singletonList("HCP^^^&2.16.756.5.30.1.127.3.10.6&ISO")));
        }

        ProvideAndRegisterDocumentSetRequestType converted = EbXML30Converters.convert(pnr);
        String renderedPnr = XdsRenderingUtils.renderEbxml(converted);
        assertNotNull(renderedPnr);
        assertTrue(renderedPnr.contains("ProvideAndRegisterDocumentSetRequest"));
        assertTrue(renderedPnr.contains("urn:e-health-suisse:2020:originalProviderRole"));
    }

}
