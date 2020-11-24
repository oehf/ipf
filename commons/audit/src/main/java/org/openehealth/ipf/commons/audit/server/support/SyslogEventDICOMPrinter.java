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

import com.github.palindromicity.syslog.dsl.SyslogFieldKeys;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.audit.unmarshal.AuditParser;
import org.openehealth.ipf.commons.audit.unmarshal.dicom.DICOMAuditParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple collector of Syslog events
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class SyslogEventDICOMPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(SyslogEventDICOMPrinter.class);
    private static final AuditParser PARSER = new DICOMAuditParser();

    public static EventConsumer newEventConsumer(String channel) {
        return new EventConsumer(channel);
    }

    public static ErrorConsumer newErrorConsumer() {
        return new ErrorConsumer();
    }

    private static class ErrorConsumer implements Consumer<Throwable> {

        private ErrorConsumer() {
        }

        @Override
        public void accept(Throwable throwable) {
            LOG.error("Error occurred while receiving a syslog event: ", throwable);
        }
    }

    private static class EventConsumer implements Consumer<Map<String, Object>> {

        private final String channel;

        private EventConsumer(String channel) {
            this.channel = channel;
        }

        @Override
        public void accept(Map<String, Object> syslogMap) {
            LOG.info("Received event on {} from {}:{}",
                    channel,
                    syslogMap.get("syslog.remote.host"),
                    syslogMap.get("syslog.remote.port"));
            LOG.info("Syslog Metadata: AppName: {}, HostName: {}, Timestamp: {}",
                    syslogMap.get(SyslogFieldKeys.HEADER_APPNAME.getField()),
                    syslogMap.get(SyslogFieldKeys.HEADER_HOSTNAME.getField()),
                    syslogMap.get(SyslogFieldKeys.HEADER_TIMESTAMP.getField()));
            try {
                var auditMessage = parse(syslogMap);
                LOG.info("DICOM Payload is");
                LOG.info("{}", auditMessage.toString());
            } catch (Exception e) {
                LOG.warn("Could not parse payload:", e);
                LOG.info("{}", syslogMap.get(SyslogFieldKeys.MESSAGE.getField()));
            }
        }

        private static AuditMessage parse(Map<String, Object> syslogMap) {
            return PARSER.parse(syslogMap.get(SyslogFieldKeys.MESSAGE.getField()).toString(), false);
        }
    }

}
