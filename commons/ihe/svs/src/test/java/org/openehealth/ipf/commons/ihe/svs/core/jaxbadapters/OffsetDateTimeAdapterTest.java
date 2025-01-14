/*
 * Copyright 2025 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.svs.core.jaxbadapters;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class OffsetDateTimeAdapterTest {

    private final OffsetDateTimeAdapter adapter = new OffsetDateTimeAdapter();

    /**
     * Simple pattern for an XSD:dateTime
     */
    private final Pattern dateTimePattern = Pattern.compile("-?\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d+)?([+-]\\d{2}:\\d{2}|Z)?");

    @Test
    public void testUnmarshal() {
        assertNotNull(adapter.unmarshal("2007-12-03T10:15:30+01:00"));
        assertNotNull(adapter.unmarshal("2002-10-10T17:00:00Z"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52.12679"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52.1267946-00:00"));
        assertNotNull(adapter.unmarshal("2001-10-26T21:32:52"));
        assertNotNull(adapter.unmarshal("-2001-10-26T21:32:52"));
    }

    @Test
    public void testMarshal() {
        assertTrue(dateTimePattern.matcher(adapter.marshal(OffsetDateTime.now())).matches());
        assertNotNull(adapter.unmarshal(adapter.marshal(OffsetDateTime.now())));
    }
}