/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.core.datetime;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Martin Krasser
 */
public class DurationTest {

    @Test
    public void testParse() {
        assertEquals(4L, Duration.parse("4").getValue());
        assertEquals(4 * 1000L, Duration.parse("4s").getValue());
        assertEquals(4 * 60 * 1000L, Duration.parse("4m").getValue());
        assertEquals(4 * 60 * 60 * 1000L, Duration.parse("4h").getValue());
        assertEquals(4 * 60 * 60 * 1000L * 24L, Duration.parse("4d").getValue());
    }
    
    @Test(expected=DurationFormatException.class)
    public void testParseFail1() {
        Duration.parse("s");
    }
    
    @Test(expected=DurationFormatException.class)
    public void testParseFail2() {
        Duration.parse("x");
    }
    
    @Test(expected=DurationFormatException.class)
    public void testParseFail3() {
        Duration.parse("s0");
    }
    
}
