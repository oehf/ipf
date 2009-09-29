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
package org.openehealth.ipf.commons.test.performance.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static org.openehealth.ipf.commons.test.performance.utils.NumberUtils.format;

/**
 * Test the functionality of {@link NumberUtils#format(double, int)} and
 * {@link NumberUtils#format(double)}
 * 
 * @author Mitko Kolev
 */
public class NumberUtilsTest {

    double d = 12.3456789;

    @Test
    public void testFormatTrimsPrecision() {
        String formatted = format(d, 0);
        assertEquals("12", formatted);
    }

    @Test
    public void testFormatTrimsPrecision1Sign() {
        String formatted = format(d, 1);
        String localeInvariant = formatted.replace(',', '.');
        assertEquals(12.3, Double.parseDouble(localeInvariant), 0.000001);
    }

    @Test
    public void testFormatTrimsPrecision2SignAndRounds() {
        String formatted = format(d, 2);
        String localeInvariant = formatted.replace(',', '.');
        // must round after the second digit
        assertEquals(12.35, Double.parseDouble(localeInvariant), 0.000001);
    }

    @Test
    public void testFormat() {
        String formatted = format(d);
        String localeInvariant = formatted.replace(',', '.');
        // must round after the fourth digit
        assertEquals(12.3457, Double.parseDouble(localeInvariant), 0.000001);
    }
   
}
