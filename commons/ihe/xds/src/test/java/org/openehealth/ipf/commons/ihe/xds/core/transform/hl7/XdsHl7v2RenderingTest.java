/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.hl7;

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ReferenceId;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Telecom;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Dmytro Rud
 */
public class XdsHl7v2RenderingTest {

    private static final String XDS_VALIDATION_CP_1292 = "XDS_VALIDATION_CP_1292";

    static {
        if (isBlank(System.getProperty(XDS_VALIDATION_CP_1292))) {
            System.setProperty(XDS_VALIDATION_CP_1292, "true");
        }
    }

    @Test
    public void testCodeRendering() {
        doCheckCodeRendering("a", "b", "c", "a^^c");
        doCheckCodeRendering("a", "b", null, "a");
        doCheckCodeRendering("a", null, "c", "a^^c");
        doCheckCodeRendering("a", null, null, "a");
        doCheckCodeRendering(null, "b", "c", "^^c");
        doCheckCodeRendering(null, "b", null, null);
        doCheckCodeRendering(null, null, "c", "^^c");
        doCheckCodeRendering(null, null, null, null);
    }

    private static void doCheckCodeRendering(String id, String displayName, String schemeName, String expected) {
        var code = new Code(id, new LocalizedString(displayName), schemeName);
        var rendered = Hl7v2Based.render(code);
        assertEquals(expected, rendered);
    }

    @Test
    public void testTelecomRendering() {
        var xtn = "1^2^3^4^5^6^7^8^9^10^11^12^13^14^^^";
        var telecom = Hl7v2Based.parse(xtn, Telecom.class);
        assertEquals("^2^3^4^5^6^7^8^^^^12", Hl7v2Based.render(telecom));
        assertEquals("1^2^3^4^5^6^7^8^9^10^11^12^13^14", Hl7v2Based.rawRender(telecom));
    }

    @Test
    public void testIdentifiableRendering() {
        var cx = "1^2^3^41&42&43&44&45&46^51&52^6^^&&^^";
        var identifiable = Hl7v2Based.parse(cx, Identifiable.class);
        assertEquals("1^^^&42&43", Hl7v2Based.render(identifiable));
        assertEquals("1^2^3^41&42&43&44&45&46^51&52^6", Hl7v2Based.rawRender(identifiable));
    }

    @Test
    public void testReferenceIdRendering() {
        try {
            var cx1 = "1^2^3^41&42&43&44&45&46^51&52^^^&&^^";
            var referenceId1 = Hl7v2Based.parse(cx1, ReferenceId.class);
            assertEquals("1^^^41&42&43^51", Hl7v2Based.render(referenceId1));
            var cx2 = "1^2^3^41&42&43&44&45&46^51&52^&1.2.40.0.34.3.1.2.99.1000001&ISO^2015^&&^^";
            var referenceId2 = Hl7v2Based.parse(cx2, ReferenceId.class);
            String cx2Rendered = Hl7v2Based.render(referenceId2);
            // some other tests classes may have already initialized the Hl7Rendering static rules based on non-existing
            // 'XDS_VALIDATION_CP_1292' property therefore this 'or' check
            assertTrue(
                "1^^^41&42&43^51^&1.2.40.0.34.3.1.2.99.1000001&ISO".equals(cx2Rendered) ||
                "1^^^41&42&43^51".equals(cx2Rendered));
            assertEquals("1^2^3^41&42&43&44&45&46^51&52^&1.2.40.0.34.3.1.2.99.1000001&ISO^2015",
                Hl7v2Based.rawRender(referenceId2));
        } finally {
            System.clearProperty(XDS_VALIDATION_CP_1292);
        }
    }

    @Test
    public void testEmptyAssignignAuthority() {
        var cx = "1^^^ABCD^^^^";
        var identifiable = Hl7v2Based.parse(cx, Identifiable.class);
        assertNotNull(identifiable);
        assertNull(identifiable.getAssigningAuthority());
        var referenceId = Hl7v2Based.parse(cx, ReferenceId.class);
        assertNotNull(referenceId);
        assertNotNull(referenceId.getAssigningAuthority());
    }

    @Test
    public void testNonEmptyAssignignAuthority() {
        var cx = "1^^^ABCD&1.2.3&ISO^^^";
        var identifiable = Hl7v2Based.parse(cx, Identifiable.class);
        assertNotNull(identifiable);
        assertNotNull(identifiable.getAssigningAuthority());
        var referenceId = Hl7v2Based.parse(cx, ReferenceId.class);
        assertNotNull(referenceId);
        assertNotNull(referenceId.getAssigningAuthority());
    }

}
