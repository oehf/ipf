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


import java.nio.charset.StandardCharsets;
import java.time.Instant;

import static java.util.Objects.requireNonNull;

/**
 * Base client implementation of RFC 5424 syslog for sending audit messages to an Audit Record Repository
 * that implements RFC 5424 syslog.
 *
 * @author Christian Ohr
 */
public class RFC5424Protocol {

    /**
     * Default syslog priority for this transport, according to
     * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.6
     */
    private static final int TRANSPORT_PRI = 10 * 8 + 5;

    /**
     * Default syslog MSGID for this transport, according to
     * http://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.6
     */
    private static final String TRANSPORT_MSGID = "IHE+RFC-3881";

    private final String senderHostName;
    private final String senderProcessId;

    public RFC5424Protocol(String senderHostName, String senderProcessId) {
        this.senderHostName = requireNonNull(senderHostName);
        this.senderProcessId = requireNonNull(senderProcessId);
    }

    /**
     * Serialize the syslog message payload body for sending by this transport
     *
     * @param auditMessage Message to prepare
     * @return serialized message
     */
    protected byte[] getTransportPayload(String sendingApplication, String auditMessage) {
        String msg = String.format("<%s>1 %s %s %s %s %s - \uFEFF<?xml version=\"1.0\" encoding=\"UTF-8\"?>%s",
                TRANSPORT_PRI,
                Instant.now(),
                senderHostName,
                sendingApplication,
                senderProcessId,
                TRANSPORT_MSGID,
                auditMessage);
        return msg.trim().getBytes(StandardCharsets.UTF_8);
    }

}
