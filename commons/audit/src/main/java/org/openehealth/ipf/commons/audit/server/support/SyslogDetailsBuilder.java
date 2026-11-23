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

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.*;

class SyslogDetailsBuilder {

    private static final Logger log = LoggerFactory.getLogger(SyslogDetailsBuilder.class);

    private static final int MAX_PRIORITY = 191;
    private static final int MIN_PRIORITY = 0;
    private static final int SEVERITY_MASK = 0x7;
    private static final String NIL = "-";

    private final Map<String, Object> result = new LinkedHashMap<>();

    public SyslogDetailsBuilder addPriority(String priorityString) {
        int priority = Integer.parseInt(priorityString);
        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new IllegalArgumentException("Invalid priority value: " + priority);
        }
        result.put(HEADER_PRI, priority);
        result.put(HEADER_SEVERITY, priority & SEVERITY_MASK);
        result.put(HEADER_FACILITY, priority >> 3);
        return this;
    }

    public SyslogDetailsBuilder addVersion(String versionString) {
        int version = Integer.parseInt(versionString);
        if (version <= 0) {
            throw new IllegalArgumentException("Invalid version value: " + version);
        }
        result.put(HEADER_VERSION, version);
        return this;
    }

    public SyslogDetailsBuilder addTimestamp(String timestampString) {
        try {
            var timestamp = Instant.parse(timestampString);
            result.put(HEADER_TIMESTAMP, timestamp);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid timestamp format: " + timestampString, e);
        }
        return this;
    }

    public SyslogDetailsBuilder addHostName(String hostName) {
        return addField(hostName, HEADER_HOST_NAME);
    }

    public SyslogDetailsBuilder addAppName(String appName) {
        return addField(appName, HEADER_APP_NAME);
    }

    public SyslogDetailsBuilder addProcId(String procId) {
        return addField(procId, HEADER_PROC_ID);
    }

    public SyslogDetailsBuilder addMsgId(String msgId) {
        return addField(msgId, HEADER_MSG_ID);
    }

    public SyslogDetailsBuilder addStructuredData(List<Map<String, Object>> structuredData) {
        result.put(STRUCTURED_DATA, structuredData);
        return this;
    }

    public SyslogDetailsBuilder addMessage(String message) {
        result.put(MESSAGE, message != null ? message : "");
        return this;
    }

    public SyslogDetailsBuilder failure(String syslogMessage, Exception e) {
        log.warn("Failed to match syslog format in message {}", syslogMessage, e);
        result.put(EXCEPTIONS, e);
        result.put(RAW_MESSAGE, syslogMessage);
        return this;
    }

    private SyslogDetailsBuilder addField(String value, String header) {
        if (!NIL.equals(value)) {
            result.put(header, value);
        }
        return this;
    }

    public Map<String, Object> build() {
        log.debug("Return syslog parser result: {}", result);
        return new LinkedHashMap<>(result);
    }
}
