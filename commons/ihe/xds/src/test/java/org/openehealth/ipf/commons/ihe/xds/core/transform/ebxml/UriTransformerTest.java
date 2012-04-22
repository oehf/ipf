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
package org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link UriTransformer}.
 * @author Jens Riemschneider
 */
public class UriTransformerTest {
    private UriTransformer transformer;
    private StringBuilder veryLongUriString;

    @Before
    public void setUp() {
        transformer = new UriTransformer();
        
        veryLongUriString = new StringBuilder();
        for (int idx = 0; idx < 1000; ++idx) {
            veryLongUriString.append("1234567890");
        }        
    }

    @Test
    public void testURIThatFitsExactlyIntoOneSlot() {
        String uri = veryLongUriString.substring(0, 126);
        String[] ebXML = transformer.toEbXML(uri);
        assertEquals(1, ebXML.length);
        assertEquals("1|" + uri, ebXML[0]);
    }

    @Test
    public void testToEbXMLWithShortestUriUsingTwoSlots() {
        String uri = veryLongUriString.substring(0, 127);
        String[] ebXML = transformer.toEbXML(uri);
        String uriPart1 = uri.substring(0, 126);
        String uriPart2 = uri.substring(126, 127);
        assertEquals(2, ebXML.length);
        assertEquals("1|" + uriPart1, ebXML[0]);
        assertEquals("2|" + uriPart2, ebXML[1]);
    }

    @Test
    public void testFromEbXMLWithSimpleUri() {
        String uri = "http://localhost:8080";        
        assertEquals(uri, transformer.fromEbXML(Collections.singletonList(uri)));
    }

    @Test
    public void testFromEbXMLWithUriInMultipleSlots() {
        String uri = veryLongUriString.substring(0, 1000);
        
        String[] ebXML = transformer.toEbXML(uri);
        String result = transformer.fromEbXML(Arrays.asList(ebXML));

        assertNotNull(result);
        assertEquals(uri, result);
    }
}
