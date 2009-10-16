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
package org.openehealth.ipf.commons.ihe.xds.core.hl7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.util.List;

/**
 * Tests for {@link HL7}.
 * @author Jens Riemschneider
 */
public class TestHL7 {
    @Test
    public void testEscape() {
        assertEquals("abc", HL7.escape("abc"));
        assertEquals("a\\F\\b\\S\\c\\R\\d\\T\\e\\E\\f", HL7.escape("a|b^c~d&e\\f"));        
    }
    
    public void testEscapeWithNull() {
        assertNull(HL7.escape(null));
    }
    
    @Test
    public void testUnescape() {
        assertEquals("abc", HL7.unescape("abc"));
        assertEquals("a|b^c~d&e\\f", HL7.unescape("a\\F\\b\\S\\c\\R\\d\\T\\e\\E\\f"));
    }
    
    public void testUnescapeIsNullSafe() {
        assertNull(HL7.unescape(null));
    }
    
    @Test
    public void testRender() {
        String rendered = HL7.render(HL7Delimiter.COMPONENT, 
                "part1", "part2", "part3");
        
        assertEquals("part1^part2^part3", rendered);
    }

    /**
     * This test is important because it ensures that the render method does
     * not escape the strings. When using render to render nested parts, the
     * outer render should not change the string that were rendered by the
     * inner render call. If render does escape strings itself, the result
     * would always represent a flat list, instead of the nested construct.
     */
    @Test
    public void testRenderDoesNotEscape() {
        String rendered = HL7.render(HL7Delimiter.COMPONENT, 
                "outer1",
                HL7.render(HL7Delimiter.SUBCOMPONENT, 
                        "inner1", "inner2"),
                "outer2");
        
        assertEquals("outer1^inner1&inner2^outer2", rendered);
    }
    
    @Test
    public void testRenderRemoveTrailingDelimiters() {
        String rendered = HL7.render(HL7Delimiter.COMPONENT, 
                "part1", "part2");
        
        assertEquals("part1^part2", rendered);
    }
    
    @Test
    public void testRenderConvertsNullToEmptyString() {
        String rendered = HL7.render(HL7Delimiter.COMPONENT, 
                "part1", null, "part3");
        
        assertEquals("part1^^part3", rendered);
    }
    
    @Test
    public void testRenderAlsoWorksWithNothing() {
        assertNull(HL7.render(HL7Delimiter.COMPONENT));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testRenderIsNullSafeParam1() {
        HL7.render(null, "fail");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenderIsNullSafeParam2() {
        HL7.render(HL7Delimiter.COMPONENT, (String[])null);
    }

    @Test
    public void testParse() {
        List<String> result = HL7.parse(HL7Delimiter.COMPONENT, "part1^part2^part3");
        assertEquals(3, result.size());
        assertEquals("part1", result.get(0));
        assertEquals("part2", result.get(1));
        assertEquals("part3", result.get(2));
    }
    
    /**
     * See comments for {@link #testRenderDoesNotEscape()} for reasons why this
     * test is important.
     */
    @Test
    public void testParseDoesNotUnescape() {
        List<String> outer = HL7.parse(HL7Delimiter.COMPONENT, "outer1^inner1&inner2^outer2");
        assertEquals(3, outer.size());
        List<String> inner = HL7.parse(HL7Delimiter.SUBCOMPONENT, outer.get(1));
        assertEquals(2, inner.size());
        assertEquals("outer1", outer.get(0));
        assertEquals("inner1", inner.get(0));
        assertEquals("inner2", inner.get(1));
        assertEquals("outer2", outer.get(2));
    }

    @Test
    public void testParseEmptyPartsResultsInNullValues() {
        List<String> result = HL7.parse(HL7Delimiter.COMPONENT, "part1^^part3");
        assertEquals(3, result.size());
        assertEquals("part1", result.get(0));
        assertNull(result.get(1));
        assertEquals("part3", result.get(2));
    }

    @Test
    public void testParseWorksWithNothing() {
        List<String> result = HL7.parse(HL7Delimiter.COMPONENT, "");
        assertEquals(0, result.size());
    }

    @Test
    public void testParseWithNullStringBehavesLikeEmptyString() {
        List<String> result = HL7.parse(HL7Delimiter.COMPONENT, null);
        assertEquals(0, result.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseIsNullSafeParam1() {
        HL7.parse(null, "fail");
    }
    
    @Test
    public void testRemoveTrailingDelimiters() {
        assertEquals("^hallo", HL7.removeTrailingDelimiters("^hallo^"));
        assertEquals("^hallo\\", HL7.removeTrailingDelimiters("^hallo\\^~|&"));
        assertNull(HL7.removeTrailingDelimiters(null));
        assertEquals("", HL7.removeTrailingDelimiters(""));
    }
}
