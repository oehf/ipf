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
package org.openehealth.ipf.commons.flow.transfer;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author Martin Krasser
 */
public class FlowInfoUtils {

    public static final String NEWLINE = "\n";

    public static String durationString(long duration) {
        if (duration  < 0L) {
            return "N/A";
        }
        return Long.toString(duration); 
    }
    
    public static String dateString(Date date) {
        if (date == null) {
            return "";
        }
        return DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM)
                .format(date);
    }
    
    public static String textString(String text) {
        if (text == null) {
            return "N/A";
        } else {
            return text;
        }
    }
}
