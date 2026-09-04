/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.validation.impl.ValidationContextFactory;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Christian Ohr
 */
public class Hl7v2TraceContextTest {

    private static final String MSH =
            "MSH|^~\\&|SENDER|FACILITY|RECEIVER|FACILITY|20261231235959||QBP^Q22|123456|P|2.5\r";

    private static final HapiContext CONTEXT = new DefaultHapiContext(
            ValidationContextFactory.noValidation());

    private static Map<String, String> traceContext() {
        var map = new LinkedHashMap<String, String>();
        map.put("traceparent", "00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01");
        map.put("tracestate", "rojo=00f067aa0ba902b7");
        return map;
    }

    private static Message parse(String message) throws Exception {
        return CONTEXT.getPipeParser().parse(message);
    }

    private static String writeAndEncode(Map<String, String> traceContext) throws Exception {
        var message = parse(MSH);
        Hl7v2TraceContext.write(message, traceContext);
        return CONTEXT.getPipeParser().encode(message);
    }

    @Test
    public void testRoundTrip() throws Exception {
        assertEquals(traceContext(), Hl7v2TraceContext.read(writeAndEncode(traceContext())));
    }

    @Test
    public void testEncodedLayout() throws Exception {
        assertTrue(writeAndEncode(traceContext()).contains(
                        "ZTR|traceparent^00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01"
                                + "~tracestate^rojo=00f067aa0ba902b7"),
                "unexpected layout: " + writeAndEncode(traceContext()));
    }

    /** Letting HAPI encode the segment is what keeps delimiters in values from breaking the message. */
    @Test
    public void testDelimitersInValuesAreEscaped() throws Exception {
        var traceContext = Map.of("key", "a^b|c~d\\e&f");
        var encoded = writeAndEncode(traceContext);
        assertTrue(encoded.contains("ZTR|key^a\\S\\b\\F\\c\\R\\d\\E\\e\\T\\f"),
                "unexpected layout: " + encoded);
        assertEquals(traceContext, Hl7v2TraceContext.read(encoded));
    }

    @Test
    public void testNothingToWrite() throws Exception {
        assertEquals(MSH, writeAndEncode(null));
        assertEquals(MSH, writeAndEncode(Map.of()));
    }

    /** Writing twice must not add the segment twice. */
    @Test
    public void testWritingTwice() throws Exception {
        var message = parse(MSH);
        Hl7v2TraceContext.write(message, Map.of("traceparent", "abc"));
        Hl7v2TraceContext.write(message, Map.of("tracestate", "def"));
        var encoded = CONTEXT.getPipeParser().encode(message);
        assertEquals(1, encoded.split("ZTR\\|", -1).length - 1, "segment added twice: " + encoded);
        assertEquals(Map.of("traceparent", "abc", "tracestate", "def"), Hl7v2TraceContext.read(encoded));
    }

    @Test
    public void testMessageWithoutTheSegment() {
        assertTrue(Hl7v2TraceContext.read(MSH).isEmpty());
    }

    @Test
    public void testNotAMessageAtAll() {
        assertTrue(Hl7v2TraceContext.read(null).isEmpty());
        assertTrue(Hl7v2TraceContext.read("").isEmpty());
        assertTrue(Hl7v2TraceContext.read("nonsense").isEmpty());
    }

    /** The encoding characters are the sender's choice, and PreParser takes them from the message. */
    @Test
    public void testNonDefaultEncodingCharacters() {
        var msh = "MSH|:+?*|SENDER|FACILITY|RECEIVER|FACILITY|20261231235959||QBP:Q22|123456|P|2.5\r";
        var message = msh + "ZTR|traceparent:abc+tracestate:def\r";
        assertEquals(Map.of("traceparent", "abc", "tracestate", "def"), Hl7v2TraceContext.read(message));
    }

    /** Segments may be separated by a line feed rather than a carriage return. */
    @Test
    public void testLineFeedSeparatedSegments() {
        var message = MSH.replace("\r", "\n") + "ZTR|traceparent^abc\n";
        assertEquals(Map.of("traceparent", "abc"), Hl7v2TraceContext.read(message));
    }

    @Test
    public void testEmptyValueSurvives() {
        assertEquals(Map.of("traceparent", ""), Hl7v2TraceContext.read(MSH + "ZTR|traceparent^\r"));
    }
}
