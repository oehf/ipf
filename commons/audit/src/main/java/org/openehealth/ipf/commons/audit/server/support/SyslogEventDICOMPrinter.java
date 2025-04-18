/*
 * Copyright 2020 the original author or authors.
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

import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.unmarshal.AuditParser;
import org.openehealth.ipf.commons.audit.unmarshal.dicom.DICOMAuditParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_APP_NAME;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_HOST_NAME;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.HEADER_TIMESTAMP;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.MESSAGE;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.REMOTE_HOST;
import static org.openehealth.ipf.commons.audit.server.support.SyslogConstants.REMOTE_PORT;

/**
 * A simple collector of Syslog events
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class SyslogEventDICOMPrinter {

    private static final Logger log = LoggerFactory.getLogger(SyslogEventDICOMPrinter.class);
    private static final AuditParser PARSER = new DICOMAuditParser();

    public static EventConsumer newEventConsumer(String channel) {
        return new EventConsumer(channel);
    }

    public static ErrorConsumer newErrorConsumer() {
        return new ErrorConsumer();
    }

    public static class ErrorConsumer implements Consumer<Throwable> {

        private ErrorConsumer() {
        }

        @Override
        public void accept(Throwable throwable) {
            log.error("Error occurred while receiving a syslog event: ", throwable);
        }
    }

    public static class EventConsumer implements Consumer<Map<String, Object>> {

        private final String channel;

        private EventConsumer(String channel) {
            this.channel = channel;
        }

        @Override
        public void accept(Map<String, Object> syslogMap) {
            log.info("Received event on {} from {}:{}",
                    channel,
                    syslogMap.get(REMOTE_HOST),
                    syslogMap.get(REMOTE_PORT));
            log.info("Syslog Metadata: AppName: {}, HostName: {}, Timestamp: {}",
                    syslogMap.get(HEADER_APP_NAME),
                    syslogMap.get(HEADER_HOST_NAME),
                    syslogMap.get(HEADER_TIMESTAMP));
            try {
                var auditMessage = parse(syslogMap);
                log.info("DICOM Payload is");
                log.info("{}", auditMessage);
            } catch (Exception e) {
                log.warn("Could not parse payload:", e);
                log.info("{}", syslogMap.get(MESSAGE));
            }
        }

        private static AuditMessage parse(Map<String, Object> syslogMap) {
            return PARSER.parse(syslogMap.get(MESSAGE).toString(), false);
        }
    }

}
