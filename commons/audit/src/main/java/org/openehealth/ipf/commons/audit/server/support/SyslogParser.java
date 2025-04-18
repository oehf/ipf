/*
 * Copyright 2025 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.server.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

public class SyslogParser {

    private static final Logger log = LoggerFactory.getLogger(SyslogParser.class);
    private static final Pattern syslogPattern = Pattern.compile(
            "^<(\\d{1,3})>(\\d)\\s" +    // PRI and VERSION
            "(\\S+)\\s" +                // TIMESTAMP
            "(\\S+)\\s" +                // HOSTNAME
            "(\\S+)\\s" +                // APP-NAME
            "(\\S+)\\s" +                // PROCID
            "(\\S+)\\s" +                // MSGID
            "(\\[.*?]|-)\\s*" +          // STRUCTURED-DATA
            "(.*)$"                      // MESSAGE
    );

    public static Map<String, Object> parse(String syslogMessage) {
        if (syslogMessage == null || syslogMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("Syslog message cannot be null or empty");
        }
        log.debug("Parsing syslog message: {}", syslogMessage);

        try {
            var matcher = syslogPattern.matcher(syslogMessage);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Syslog message does not match RFC 5424 format");
            }
            return new SyslogDetailsBuilder()
                .addPriority(matcher.group(1))
                .addVersion(matcher.group(2))
                .addTimestamp(matcher.group(3))
                .addHostName(matcher.group(4))
                .addAppName(matcher.group(5))
                .addProcId(matcher.group(6))
                .addMsgId(matcher.group(7))
                .addStructuredData(matcher.group(8))
                .addMessage(matcher.group(9))
                .build();

        } catch (Exception e) {
            return new SyslogDetailsBuilder()
                .failure(syslogMessage, e)
                .build();
        }
    }

}