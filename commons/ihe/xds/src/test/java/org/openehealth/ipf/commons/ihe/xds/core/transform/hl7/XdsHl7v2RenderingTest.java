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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Telecom;
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
        Code code = new Code(id, new LocalizedString(displayName), schemeName);
        String rendered = Hl7v2Based.render(code);
        assertEquals(expected, rendered);
    }

    @Test
    public void testTelecomRendering() {
        String xtn = "1^2^3^4^5^6^7^8^9^0^a^b^c^d^^^";
        Telecom telecom = Hl7v2Based.parse(xtn, Telecom.class);
        assertEquals("^^3^4", Hl7v2Based.render(telecom));
        assertEquals("1^2^3^4^5^6^7^8^9^0^a^b^c^d", Hl7v2Based.rawRender(telecom));
    }
}
