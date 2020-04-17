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
package org.openehealth.ipf.commons.audit;

import org.openehealth.ipf.commons.audit.utils.AuditUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class DefaultAuditMetadataProvider implements AuditMetadataProvider {

    private String hostName;
    private String processID;
    private String sendingApplication;
    private String timestamp;
    private TemporalUnit precision = ChronoUnit.MILLIS;

    public DefaultAuditMetadataProvider() {
        this(AuditUtils.getLocalHostName(), AuditUtils.getProcessId(), "IPF", null);
    }

    public DefaultAuditMetadataProvider(String hostName, String processID, String sendingApplication, String timestamp) {
        this.hostName = hostName;
        this.processID = processID;
        this.sendingApplication = sendingApplication;
        this.timestamp = timestamp;
    }

    @Override
    public void setSendingApplication(String sendingApplication) {
        this.sendingApplication = sendingApplication;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    public void setPrecision(TemporalUnit precision) {
        this.precision = precision;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getTimestamp() {
        return timestamp != null ? timestamp : Instant.now().truncatedTo(precision).toString();
    }

    @Override
    public String getHostname() {
        return hostName;
    }

    @Override
    public String getSendingApplication() {
        return sendingApplication;
    }

    @Override
    public String getProcessID() {
        return processID;
    }
}
