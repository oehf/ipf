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

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_APP_NAME;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_HOST_NAME;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_MSG_ID;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_PRI;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_PROC_ID;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_TIMESTAMP;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.MESSAGE;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.STRUCTURED_DATA;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.STRUCTURED_DATA_ID;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.STRUCTURED_DATA_PARAMS;

public final class SyslogMatchers {

    private SyslogMatchers() {
        // Utility class
    }

    public static Matcher<Map<String, Object>> hasPriority(int expectedPriority) {
        return fieldEquals(HEADER_PRI, expectedPriority);
    }

    public static Matcher<Map<String, Object>> hasAppName(String expectedAppName) {
        return fieldEquals(HEADER_APP_NAME, expectedAppName);
    }

    public static Matcher<Map<String, Object>> hasHostname(String expectedHostname) {
        return fieldEquals(HEADER_HOST_NAME, expectedHostname);
    }

    public static Matcher<Map<String, Object>> hasProcId(String expectedProcId) {
        return fieldEquals(HEADER_PROC_ID, expectedProcId);
    }

    public static Matcher<Map<String, Object>> hasMsgId(String expectedMsgId) {
        return fieldEquals(HEADER_MSG_ID, expectedMsgId);
    }

    public static Matcher<Map<String, Object>> hasMessage(String expectedMessage) {
        return fieldEquals(MESSAGE, expectedMessage);
    }

    public static Matcher<Map<String, Object>> hasTimestamp(Instant expectedTimestamp) {
        return fieldEquals(HEADER_TIMESTAMP, expectedTimestamp);
    }

    public static Matcher<Map<String, Object>> hasStructuredDataEntry(String sdId, Map<String, String> expectedParams) {
        return new TypeSafeMatcher<>() {
            @Override
            @SuppressWarnings("unchecked")
            protected boolean matchesSafely(Map<String, Object> item) {
                Object raw = item.get(STRUCTURED_DATA);
                if (!(raw instanceof List<?> list)) return false;

                for (Object entry : list) {
                    if (!(entry instanceof Map<?, ?> entryMap)) continue;

                    var id = entryMap.get(STRUCTURED_DATA_ID);
                    var params = entryMap.get(STRUCTURED_DATA_PARAMS);

                    if (!(id instanceof String) || !(params instanceof Map<?, ?>)) continue;

                    if (sdId.equals(id)) {
                        try {
                            var paramMap = (Map<String, String>) params;
                            return Objects.equals(paramMap, expectedParams);
                        } catch (ClassCastException e) {
                            return false;
                        }
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("structured data entry with ID ")
                    .appendValue(sdId)
                    .appendText(" and parameters ")
                    .appendValue(expectedParams);
            }

            @Override
            protected void describeMismatchSafely(Map<String, Object> item, Description mismatchDescription) {
                Object raw = item.get(STRUCTURED_DATA);
                mismatchDescription.appendText("was ").appendValue(raw);
            }
        };
    }

    private static <T> Matcher<Map<String, Object>> fieldEquals(String key, T expectedValue) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(Map<String, Object> item) {
                return Objects.equals(item.get(key), expectedValue);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(key).appendText(" = ").appendValue(expectedValue);
            }

            @Override
            protected void describeMismatchSafely(Map<String, Object> item, Description mismatchDescription) {
                mismatchDescription.appendText("was ").appendValue(item.get(key));
            }
        };
    }
}