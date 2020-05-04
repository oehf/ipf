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

package org.openehealth.ipf.commons.audit.protocol;

import org.openehealth.ipf.commons.audit.AuditException;
import org.openehealth.ipf.commons.audit.TlsParameters;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Maps AuditTransmissionProtocol names to instances
 *
 * @author Christian Ohr
 */
public enum AuditTransmissionChannel {

    UDP("UDP", UDPSyslogSenderImpl.class),
    NIO_UDP("NIO-UDP", UDPSyslogSenderImpl.class),
    VERTX_UDP("VERTX-UDP", UDPSyslogSenderImpl.class),
    TLS("TLS", TLSSyslogSenderImpl.class),
    NIO_TLS("NIO-TLS", NettyTLSSyslogSenderImpl.class),
    VERTX_TLS("VERTX-TLS", VertxTLSSyslogSenderImpl.class),
    NETTY_TLS("NETTY-TLS", NettyTLSSyslogSenderImpl.class);

    private final String protocolName;
    private final Class<? extends AuditTransmissionProtocol> protocol;

    AuditTransmissionChannel(String protocolName, Class<? extends AuditTransmissionProtocol> protocol) {
        this.protocolName = protocolName;
        this.protocol = protocol;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public AuditTransmissionProtocol makeInstance(TlsParameters tlsParameters) {
        try {
            return protocol.getConstructor(TlsParameters.class).newInstance(tlsParameters);
        } catch (Exception e) {
            throw new AuditException(e);
        }
    }

    public static AuditTransmissionChannel fromProtocolName(String protocolName) {
        for (var channel : AuditTransmissionChannel.values()) {
            if (channel.protocolName.equalsIgnoreCase(protocolName)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unknown audit protocol name: " + protocolName +
                ". Choose on of: " +
                Arrays.stream(AuditTransmissionChannel.values())
                        .map(AuditTransmissionChannel::getProtocolName)
                        .collect(Collectors.joining(",")));
    }
}
