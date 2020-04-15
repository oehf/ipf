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

import static java.util.Objects.requireNonNull;

/**
 * Default implementation, using the current host name, process ID, timestamp and "IPF" and sending application.
 * Using the setters you can assign custom values.
 *
 * @see {@link AuditUtils#getLocalHostName()}
 * @see {@link AuditUtils#getProcessId()} ()}
 */
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
        this.sendingApplication = requireNonNull(sendingApplication);
        this.timestamp = timestamp;
    }

    @Override
    public void setSendingApplication(String sendingApplication) {
        this.sendingApplication = requireNonNull(sendingApplication);
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
    }

    /**
     * Sets the timestamp precision. Note that RFC 5424 Syslog Format does not allow more than 6 subsecond
     * digits, so {@link ChronoUnit#NANOS} will be rejected.
     *
     * @param precision precision, as offered by {@link ChronoUnit}
     */
    public void setPrecision(TemporalUnit precision) {
        if (ChronoUnit.NANOS == precision) {
            throw new IllegalArgumentException("RFC 5424 only allows 6 subsecond digits");
        }
        this.precision = requireNonNull(precision);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the timestamp or if null the current timestamp
     */
    @Override
    public String getTimestamp() {
        return timestamp != null ? timestamp : Instant.now().truncatedTo(precision).toString();
    }

    /**
     * @return the hostName or if null the current hostName
     */
    @Override
    public String getHostname() {
        return hostName != null ? hostName : AuditUtils.getLocalHostName();
    }

    @Override
    public String getSendingApplication() {
        return sendingApplication;
    }

    /**
     * @return the processID or if null the current processID
     */
    @Override
    public String getProcessID() {
        return processID != null ? processID : AuditUtils.getProcessId();
    }
}
