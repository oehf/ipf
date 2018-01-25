/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.audit.handler;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example implementation of an audit exception handler
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class LoggingAuditExceptionHandler implements AuditExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingAuditExceptionHandler.class);

    @Override
    public void handleException(AuditContext auditContext, Throwable t, String... auditMessages) {
        LOG.warn(String.format("Failed to send ATNA audit event to destination [%s:%d]",
                auditContext.getAuditRepositoryAddress().getHostAddress(),
                auditContext.getAuditRepositoryPort()), t);
    }
}
