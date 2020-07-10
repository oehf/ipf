/*
 * Copyright 2020 the original author or authors.
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
package org.openehealth.ipf.modules.hl7;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * @author Dmytro Rud
 */
public class SegmentFinderTest {

    public static Message loadFile(String fn) throws Exception {
        var url =  SegmentFinderTest.class.getClassLoader().getResource(fn);
        var s = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new PipeParser().parse(s);
    }

    @Test
    public void testSegmentFinder() throws Exception {
        var iti21Response = loadFile("msg-11.hl7");

        // valid index
        var pid = SegmentFinder.find(iti21Response, "PID", 2);
        assertNotNull(pid);
        assertEquals("3", Terser.get(pid, 1, 0, 1, 1));

        // invalid index
        assertNull(SegmentFinder.find(iti21Response, "PID", 20));

        // missing segment
        assertNull(SegmentFinder.find(iti21Response, "SFT", 0));

        // not a segment -- shall not fail
        assertNull(SegmentFinder.find(iti21Response, "QUERY_RESPONSE", 2));
    }

}
