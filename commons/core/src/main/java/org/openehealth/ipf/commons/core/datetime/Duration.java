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

import java.util.Date;

/**
 * @author Martin Krasser
 */
public class Duration {

    private static final String EMPTY = "";
    private static final String PATTERN = "^[0-9]+[smh]?$";
    private static final String PATTERN_UNIT = "[smh]";
    private static final String PATTERN_NUMBER = "[0-9]+";
    
    private static final String SECOND = "s";
    private static final String MINUTE = "m";
    private static final String HOUR = "h";
    
    
    private long milliseconds;
    
    public Duration(long milliseconds) {
        this.milliseconds = milliseconds;
    }
    
    public long getValue() {
        return milliseconds;
    }
    
    public Date since() {
        return since(new Date());
    }
    
    public Date since(Date date) {
        return new Date(date.getTime() - milliseconds);
    }
    
    /**
     * Parses the string representation of a duration. The string representation
     * must follow the format &lt;number&gt;[&lt;unit&gt;] where &lt;number&gt;
     * is a valid <code>long</code> number and &lt;unit&gt; is one of the
     * following
     * 
     * <ul>
     * <li><code>s</code>: second</li>
     * <li><code>m</code>: minute</li>
     * <li><code>h</code>: hour</li>
     * </ul>
     * 
     * If no unit is given it defaults to milliseconds.
     * 
     * @param duration
     * @return
     */
    public static Duration parse(String duration) {
        String d = duration.trim();
        if (!d.matches(PATTERN)) {
            throw new DurationFormatException(
                    "Duration string " + duration + " doesn't match pattern " + PATTERN);
        }
        String u = d.replaceFirst(PATTERN_NUMBER, EMPTY);
        long v = Long.parseLong(d.replaceFirst(PATTERN_UNIT, EMPTY));
        if (u.length() == 0) {
            // v already in units of milliseconds
        } else if (u.equals(SECOND)) {
            v = v * 1000L;
        } else if (u.equals(MINUTE)) {
            v = v * 1000L * 60L;
        } else if (u.equals(HOUR)) {
            v = v * 1000L * 60L * 60L;
        } 
        return new Duration(v);
    }

}
