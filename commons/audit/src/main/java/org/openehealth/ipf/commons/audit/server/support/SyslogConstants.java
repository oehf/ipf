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

public interface SyslogConstants {

    String HEADER_PRI = "syslog.header.pri";
    String HEADER_SEVERITY = "syslog.header.severity";
    String HEADER_FACILITY = "syslog.header.facility";
    String HEADER_VERSION = "syslog.header.version";
    String HEADER_TIMESTAMP = "syslog.header.timestamp";
    String HEADER_HOST_NAME = "syslog.header.hostName";
    String HEADER_APP_NAME = "syslog.header.appName";
    String HEADER_PROC_ID = "syslog.header.procId";
    String HEADER_MSG_ID = "syslog.header.msgId";
    String STRUCTURED_DATA = "syslog.structuredData";
    String STRUCTURED_DATA_ID = "syslog.structuredData.id";
    String STRUCTURED_DATA_PARAMS = "syslog.structuredData.params";
    String MESSAGE = "syslog.message";
    String RAW_MESSAGE = "syslog.raw.message";
    String EXCEPTIONS = "syslog.exceptions";
    String REMOTE_HOST = "syslog.remote.host";
    String REMOTE_PORT = "syslog.remote.port";
    String REMOTE_IP = "syslog.remote.ip";
}
