/*
 * Copyright 2019 the original author or authors.
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

package org.openehealth.ipf.commons.audit.queue;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Message Queue that logs the serialized plain audit messages with (by default)
 * pretty formatting using a configurable logger. Log level is INFO.
 *
 * @author Christian Ohr
 * @since 3.6
 */
public class LoggingAuditMessageQueue extends AbstractAuditMessageQueue {

    private static final String DEFAULT_LOGGER_NAME = "AUDIT";
    private Logger log;

    public LoggingAuditMessageQueue() {
        setPretty(true);
        setLoggerName(DEFAULT_LOGGER_NAME);
    }

    public void setLoggerName(String loggerName) {
        this.log = LoggerFactory.getLogger(Objects.requireNonNull(loggerName));
    }

    @Override
    protected synchronized void handle(AuditContext auditContext, String auditRecord) {
        if (auditRecord != null) {
            log.info(auditRecord);
        }
    }
}
