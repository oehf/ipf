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

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.EXCEPTIONS;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.RAW_MESSAGE;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasAppName;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasHostname;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasMessage;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasMsgId;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasPriority;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasProcId;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasStructuredDataEntry;
import static org.openehealth.ipf.commons.audit.server.support.SyslogMatchers.hasTimestamp;

class SyslogParserTest {

    @Test
    void shouldParseSimpleSyslogMessage() {
        var msg = "<34>1 2023-04-18T14:32:52Z host app1 1234 ID47 - Hello World";
        var result = SyslogParser.parse(msg);

        assertThat(result, hasPriority(34));
        assertThat(result, hasHostname("host"));
        assertThat(result, hasAppName("app1"));
        assertThat(result, hasProcId("1234"));
        assertThat(result, hasMsgId("ID47"));
        assertThat(result, hasMessage("Hello World"));
        assertThat(result, hasTimestamp(Instant.parse("2023-04-18T14:32:52Z")));
    }

    @Test
    void shouldParseStructuredDataCorrectly() {
        var msg = "<165>1 2023-04-18T14:32:52Z testhost app2 8888 MSGID99 [sdID@123 event=\"test\" user=\"admin\"] Done.";
        var result = SyslogParser.parse(msg);

        assertThat(result, hasAppName("app2"));
        assertThat(result, hasStructuredDataEntry("sdID@123", Map.of("event", "test", "user", "admin")));
        assertThat(result, hasMessage("Done."));
        assertThat(result, hasTimestamp(Instant.parse("2023-04-18T14:32:52Z")));
    }

    @Test
    void shouldParseMessageWithoutStructuredData() {
        var msg = "<13>1 2023-04-18T14:32:52Z localhost logger 321 ID10 - Just a log message";
        var result = SyslogParser.parse(msg);

        assertThat(result, hasAppName("logger"));
        assertThat(result, hasMessage("Just a log message"));
        assertThat(result, hasTimestamp(Instant.parse("2023-04-18T14:32:52Z")));
    }

    // --- Invalid cases using helper ---

    @Test
    void shouldThrowWhenVersionIsInvalid() {
        assertSyslogParseFails("<13>0 2023-04-18T14:32:52Z localhost logger 321 ID10 - Just a log message");
    }

    @Test
    void shouldThrowWhenPriorityIsMissing() {
        assertSyslogParseFails("1 2023-04-18T14:32:52Z localhost logger 321 ID10 - Missing PRI");
    }

    @Test
    void shouldThrowForInvalidTimestamp() {
        assertSyslogParseFails("<34>1 2023-04-18 14:32:52 localhost logger 321 ID10 - Invalid timestamp format");
    }

    @Test
    void shouldThrowForInconsistentTimestamp() {
        assertSyslogParseFails("<34>1 2023-37-12T14:32:52Z localhost logger 321 ID10 - Invalid timestamp format");
    }

    @Test
    void shouldThrowForMalformedStructuredData() {
        assertSyslogParseFails("<165>1 2023-04-18T14:32:52Z host app 123 MSG [id broken=\"value\" missingEnd");
    }

    @Test
    void shouldThrowWhenTooFewFieldsPresent() {
        assertSyslogParseFails("<34>1 2023-04-18T14:32:52Z host app1");
    }

    @Test
    void shouldThrowWhenPriorityFormatIsInvalid() {
        assertSyslogParseFails("<999>1 2023-04-18T14:32:52Z host app1 1234 ID47 - Message");
    }

    // --- Helper ---

    private void assertSyslogParseFails(String msg) {
        var result = SyslogParser.parse(msg);
        assertThat(result.containsKey(EXCEPTIONS), is(true));
        assertThat(result.get(RAW_MESSAGE), equalTo(msg));
    }
}
