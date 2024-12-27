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
import org.openehealth.ipf.commons.audit.protocol.providers.NettyTLSSyslogSenderProvider;
import org.openehealth.ipf.commons.audit.protocol.providers.ReactorNettyTLSSyslogSenderProvider;
import org.openehealth.ipf.commons.audit.protocol.providers.RecordingAuditMessageTransmissionProvider;
import org.openehealth.ipf.commons.audit.protocol.providers.TLSSyslogSenderProvider;
import org.openehealth.ipf.commons.audit.protocol.providers.UDPSyslogSenderProvider;

import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

/**
 * Maps AuditTransmissionProtocol names to service providers which should provide a concrete instance of an
 * {@link AuditTransmissionProtocol} over a {@link ServiceLoader} mechanism.
 *
 * @author Christian Ohr
 */
public enum AuditTransmissionChannel {

    UDP("UDP", UDPSyslogSenderProvider.class.getName()),
    @Deprecated(forRemoval = true, since = "5.0.0")
    NIO_UDP("NIO-UDP", UDPSyslogSenderProvider.class.getName()),
    @Deprecated(forRemoval = true, since = "5.0.0")
    VERTX_UDP("VERTX-UDP", UDPSyslogSenderProvider.class.getName()),
    TLS("TLS", TLSSyslogSenderProvider.class.getName()),
    @Deprecated(forRemoval = true, since = "5.0.0")
    NIO_TLS("NIO-TLS", NettyTLSSyslogSenderProvider.class.getName()),
    NETTY_TLS("NETTY-TLS", NettyTLSSyslogSenderProvider.class.getName()),
    REACTOR_NETTY_TLS("REACTOR-NETTY-TLS", ReactorNettyTLSSyslogSenderProvider.class.getName()),
    @Deprecated(forRemoval = true, since = "5.0.0")
    FHIR_REST_TLS("FHIR-REST-TLS", "org.openehealth.ipf.commons.ihe.fhir.audit.protocol.FhirRestTLSAuditRecordApacheSenderProvider"),
    FHIR_REST_APACHE5_TLS("FHIR-REST-APACHE5-TLS", "org.openehealth.ipf.commons.ihe.fhir.audit.protocol.FhirRestTLSAuditRecordApache5SenderProvider"),
    FHIR_REST_METHANOL_TLS("FHIR-REST-METHANOL-TLS", "org.openehealth.ipf.commons.ihe.fhir.audit.protocol.FhirRestTLSAuditRecordMethanolSenderProvider"),
    RECORDING("RECORDING", RecordingAuditMessageTransmissionProvider.class.getName());

    private final String protocolName;
    private final String protocolClass;

    AuditTransmissionChannel(String protocolName, String protocolClass) {
        this.protocolName = protocolName;
        this.protocolClass = protocolClass;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public AuditTransmissionProtocol makeInstance(TlsParameters tlsParameters) {
        var loader = ServiceLoader.load(AuditTransmissionProtocolProvider.class);
        for (var provider : loader) {
            if (protocolClass.equals(provider.getClass().getName())) {
                return provider.createAuditTransmissionProtocol(tlsParameters);
            }
        }
        throw new AuditException("Could not instantiate AuditTransmissionProtocolProvider for name " + protocolName);
    }

    public static AuditTransmissionChannel fromProtocolName(String protocolName) {
        for (var channel : AuditTransmissionChannel.values()) {
            if (channel.protocolName.equalsIgnoreCase(protocolName)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unknown audit protocol name: " + protocolName +
            ". Choose one of: " +
            Arrays.stream(AuditTransmissionChannel.values())
                .map(AuditTransmissionChannel::getProtocolName)
                .collect(Collectors.joining(",")));
    }

}
