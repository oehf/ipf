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

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import static org.junit.Assert.*;

/**
 * @author Dmytro Rud
 */
public class XdsHl7v2RenderingTest {

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
        var cx = "1^2^3^41&42&43&44&45&46^51&52^6^^&&^^";
        var referenceId = Hl7v2Based.parse(cx, ReferenceId.class);
        assertEquals("1^^^41&42&43^51", Hl7v2Based.render(referenceId));
        assertEquals("1^2^3^41&42&43&44&45&46^51&52^6", Hl7v2Based.rawRender(referenceId));
    }

    @Test
    public void testEmptyAssignignAuthority() {
        var cx = "1^^^ABCD^^^^";
        var identifiable = Hl7v2Based.parse(cx, Identifiable.class);
        assertNull(identifiable.getAssigningAuthority());
        var referenceId = Hl7v2Based.parse(cx, ReferenceId.class);
        assertNotNull(referenceId.getAssigningAuthority());
    }

    @Test
    public void testNonEmptyAssignignAuthority() {
        var cx = "1^^^ABCD&1.2.3&ISO^^^";
        var identifiable = Hl7v2Based.parse(cx, Identifiable.class);
        assertNotNull(identifiable.getAssigningAuthority());
        var referenceId = Hl7v2Based.parse(cx, ReferenceId.class);
        assertNotNull(referenceId.getAssigningAuthority());
    }

}
