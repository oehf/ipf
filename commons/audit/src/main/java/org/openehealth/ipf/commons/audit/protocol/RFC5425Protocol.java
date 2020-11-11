/*
 * Copyright 2017 the original author or authors.
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
package org.openehealth.ipf.commons.audit.protocol;


import org.openehealth.ipf.commons.audit.AuditMetadataProvider;

/**
 * Base client implementation of RFC 5425 syslog for sending audit messages to an Audit Record Repository
 * that implements RFC 5425 (SYSLOG via TLS).
 *
 * @author Christian Ohr
 * @since 4.0
 */
public class RFC5425Protocol extends RFC5424Protocol {

    @Override
    protected byte[] getTransportPayload(AuditMetadataProvider auditMetadataProvider, String auditMessage) {
        var msgBytes = super.getTransportPayload(auditMetadataProvider, auditMessage);
        var length = String.format("%d ", msgBytes.length).getBytes();
        var output = new byte[length.length + msgBytes.length];
        System.arraycopy(length, 0, output, 0, length.length);
        System.arraycopy(msgBytes, 0, output, length.length, msgBytes.length);
        return output;
    }

}
