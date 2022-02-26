/*
 * Copyright 2022 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hpd.controls;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortControl2;
import org.openehealth.ipf.commons.ihe.hpd.controls.sorting.SortResponseControl2;

import javax.naming.ldap.SortKey;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@Slf4j
public class Asn1Test {

    @Test
    public void testSortControlParsing() throws IOException {
        doTestSortControlParsing(new SortControl2(
                new SortKey[]{
                        new SortKey("a", false, "rule1"),
                        new SortKey("b", false, null),
                        new SortKey("c", true, "rule2"),
                        new SortKey("d", true, null),
                        },
                true));

        doTestSortControlParsing(new SortControl2(new SortKey[0], true));
    }

    private static void doTestSortControlParsing(SortControl2 original) throws IOException {
        byte[] berBytes = original.getEncodedValue();
        SortControl2 restored = new SortControl2(berBytes, true);
        log.debug(restored.toString());
        assertEquals(original, restored);
    }

    @Test
    public void testSortResponseControlRendering() throws IOException {
        doTestSortResponseControlRendering(new SortResponseControl2(10, "abcd", true));
        doTestSortResponseControlRendering(new SortResponseControl2(10, "", true));
        doTestSortResponseControlRendering(new SortResponseControl2(10, null, true));
    }

    private static void doTestSortResponseControlRendering(SortResponseControl2 original) throws IOException {
        byte[] berBytes = original.getEncodedValue();
        SortResponseControl2 restored = new SortResponseControl2(berBytes, true);
        log.debug(restored.toString());
        assertEquals(original, restored);
    }
}
