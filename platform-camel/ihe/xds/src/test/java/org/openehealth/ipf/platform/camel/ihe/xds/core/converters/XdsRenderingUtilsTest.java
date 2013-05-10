package org.openehealth.ipf.platform.camel.ihe.xds.core.converters;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

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
