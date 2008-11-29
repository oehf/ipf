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
package org.openehealth.ipf.platform.manager.jmxexplorer.ui.util;

import junit.framework.TestCase;

/**
 * Tests if the primitive arrays are converted correctly.
 * <p>
 * Copyright (c) 2008 <a
 * href="http://www.intercomponentware.com">InterComponentWare AG</a>
 * 
 * @author Mitko Kolev i000174
 */
public class MBeanUtilsPrimitiveConversionTest extends TestCase {

    public void testConvertArrayOfIntsToArrayOfObjects() {
        int[] primitives = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE, 1,
                2, 3, 3, 12, 123, 15, 6, 7, 434, 6234, 26, 0, -1, -12341234 };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfBooleansToArrayOfObjects() {
        boolean[] primitives = new boolean[] { true, false, false, false };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfLongsToArrayOfObjects() {
        long[] primitives = new long[] { 1L, 5L, 123412341L, -12341234123L,
                Long.MAX_VALUE, Long.MIN_VALUE };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfCharsToArrayOfObjects() {
        char[] primitives = new char[] { Character.MAX_VALUE,
                Character.MIN_VALUE, Character.MAX_HIGH_SURROGATE,
                Character.MIN_SURROGATE, Character.MIN_LOW_SURROGATE, 'a', 'b',
                'c', 'd', '\u1234', '\u6619' };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfShortsOfObjects() {
        short[] primitives = new short[] { Short.MAX_VALUE, Short.MIN_VALUE, 1,
                2, 34, -6, 1234, 5345, 3467, -4566, 75 };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfFloatsToArrayOfObjects() {
        float[] primitives = new float[] { 1.1f, 5.0f, 12341f, -123.0f,
                Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfDoublesToArrayOfObjects() {
        double[] primitives = new double[] { Double.MAX_VALUE,
                Double.MIN_VALUE, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, 1.1d, 5.0d, 12341d, -123.0d, 0d };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

    public void testConvertArrayOfBytesToArrayOfObjects() {
        byte[] primitives = new byte[] { Byte.MAX_VALUE, Byte.MIN_VALUE, 1, 23,
                123, -123, 0 };
        Object[] objects = MBeanUtils
                .convertPrimitiveArrayToObjectArray(primitives);
        assertTrue(objects.length == primitives.length);
        for (int t = 0; t < primitives.length; t++) {
            assertTrue(objects[t].equals(primitives[t]));
        }
    }

}
