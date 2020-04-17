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

/**
 * Providing meta information about the audit record that is e.g. used for populating
 * the RFC 5424 header. A different provider may be configured for relays that have
 * to preserve the original header information that may come from a different host.
 *
 * @see DefaultAuditMetadataProvider
 * @see org.openehealth.ipf.commons.audit.protocol.AuditTransmissionProtocol#send(AuditContext, AuditMetadataProvider, String)
 */
public interface AuditMetadataProvider {

    String getTimestamp();

    String getHostname();

    String getProcessID();

    String getSendingApplication();

    void setSendingApplication(String sendingApplication);

    /**
     * @return the default metadata provider
     */
    static AuditMetadataProvider getDefault() {
        return DEFAULT;
    }

    AuditMetadataProvider DEFAULT = new DefaultAuditMetadataProvider();
}
