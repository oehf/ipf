/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EqualityCheckTest {

    @Test
    public void testCeEquality() {
        var ce1 = new CE("code1", "1.2.3.4.5", "codeSystemName1", "displayName1");
        var ce2 = new CE("code1", "1.2.3.4.5", "codeSystemName2", "displayName2");
        var ce3 = new CE("XXXXX", "1.2.3.4.5", "codeSystemName1", "displayName1");
        var ce4 = new CE("code1", "2.3.4.5.6", "codeSystemName1", "displayName1");
        assertEquals(ce1, ce2);
        assertNotEquals(ce1, ce3);
        assertNotEquals(ce1, ce4);
    }

}
