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

import org.openehealth.ipf.commons.audit.AuditContext;

/**
 * Implementations of this interface transmit the serialized {@link org.openehealth.ipf.commons.audit.model.AuditMessage AuditMessage}
 * to an AuditRepository using IP protocols like TCP or UDP, usually wrapped into a carrier protocol (such as SYSLOG).
 *
 * @author Christian Ohr
 * @since 3.5
 */
public interface AuditTransmissionProtocol {

    String JAVAX_NET_DEBUG = "javax.net.debug";
    String JAVAX_NET_SSL_TRUSTSTORE = "javax.net.ssl.trustStore";
    String JAVAX_NET_SSL_TRUSTSTORE_TYPE = "javax.net.ssl.trustStoretype";
    String JAVAX_NET_SSL_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    String JAVAX_NET_SSL_KEYSTORE = "javax.net.ssl.keyStore";
    String JAVAX_NET_SSL_KEYSTORE_TYPE = "javax.net.ssl.keyStoreType";
    String JAVAX_NET_SSL_KEYSTORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    String HTTPS_CIPHERSUITES = "https.ciphersuites";
    String JDK_TLS_CLIENT_PROTOCOLS = "jdk.tls.client.protocols";

    /**
     * Transmits the message
     *
     * @param auditContext audit context that e.g. contains the destination
     * @param auditMessage audit message strings
     * @throws Exception thrown if sending the messages has failed
     */
    void send(AuditContext auditContext, String... auditMessage) throws Exception;

    /**
     * May be imüplemented to clean up instances on shut down
     */
    void shutdown();

    /**
     * @return name of the AuditTransmissionProtocol
     */
    String getTransportName();

}
