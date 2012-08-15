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

import org.junit.Assert;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;

/**
 * @author Dmytro Rud
 */
public class XdsHl7v2RenderingTest {

    @Test
    public void testCodeRendering() {
        check("a",  "b",  "c",  "a^^c");
        check("a",  "b",  null, "a");
        check("a", null,  "c",  "a^^c");
        check("a", null,  null, "a");
        check(null, "b",  "c",  "^^c");
        check(null, "b",  null, null);
        check(null, null, "c",  "^^c");
        check(null, null, null, null);
    }

    private static void check(String id, String displayName, String schemeName, String expected) {
        Code code = new Code(id, new LocalizedString(displayName), schemeName);
        String rendered = Hl7v2Based.render(code);
        Assert.assertEquals(expected, rendered);
    }
}
