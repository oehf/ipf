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

import java.util.Formatter;

/**
 * @author Mitko Kolev
 */
public class NumberUtils {

    /**
     * Format the given <code>value</code> into a representation with 4 digits
     * after the comma.
     * 
     * @param value
     *            the value to be formatted.
     * @return formatted number.
     */
    public static String format(double value) {
        return format(value, 4);
    }

    /**
     * Format the given <code>value</code> into a representation with
     * <code>precision</code> digits after the comma.
     * 
     * @param value
     *            the value to be formatted.
     * @param precision
     *            number of digits after the comma.
     * @return formatted number.
     */
    public static String format(double value, int precision) {
        return new Formatter().format("%1." + precision + "f", value)
                .toString();
    }

}
