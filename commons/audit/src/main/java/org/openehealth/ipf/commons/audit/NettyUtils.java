/*
 * Copyright 2020 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.audit;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.ssl.ApplicationProtocolConfig;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.JdkSslContext;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SupportedCipherSuiteFilter;
import org.openehealth.ipf.commons.core.ssl.TlsParameters;

import java.util.stream.Stream;

public class NettyUtils {

    private static final boolean MICROMETER_AVAILABLE = isClassPresent("io.micrometer.core.instrument.Metrics");

    public static SslContext initSslContext(TlsParameters tlsParameters, boolean serverSide) {
        var allowedProtocols = System.getProperty(
                serverSide ? "jdk.tls.server.protocols" : "jdk.tls.client.protocols",
                "TLSv1.2,TLSv1.3");
        var protocols = Stream.of(allowedProtocols.split("\\s*,\\s*")).toArray(String[]::new);
        return new JdkSslContext(
                tlsParameters.getSSLContext(serverSide),
                !serverSide,
                null, // use default
                SupportedCipherSuiteFilter.INSTANCE,
                ApplicationProtocolConfig.DISABLED,
                ClientAuth.REQUIRE, // require mutual authentication
                protocols,
                false
        );
    }

    /**
     * Writes the bytes to the channel and waits until they have been written. Other than a plain
     * {@code writeAndFlush(...).await(...)}, which only tells whether the write has completed, a write
     * that failed is reported as well, so that an audit message cannot get lost unnoticed.
     *
     * @param channel       channel to write to
     * @param bytes         bytes to write
     * @param timeoutMillis maximum time to wait for the write to complete
     * @param host          host of the Audit Record Repository, for error messages
     * @param port          port of the Audit Record Repository, for error messages
     * @throws AuditException if writing has timed out, failed, or was interrupted
     * @since 6.0
     */
    public static void writeAndAwait(Channel channel, byte[] bytes, long timeoutMillis, String host, int port) {
        var future = channel.writeAndFlush(Unpooled.wrappedBuffer(bytes));
        try {
            if (!future.await(timeoutMillis)) {
                throw new AuditException("Timed out sending audit message to " + host + ":" + port);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AuditException("Interrupted during sending audit message to " + host + ":" + port, e);
        }
        if (!future.isSuccess()) {
            throw new AuditException("Could not send audit message to " + host + ":" + port, future.cause());
        }
    }

    /**
     * @return whether Micrometer is on the classpath, so that Reactor Netty metrics can be enabled
     * @since 6.0
     */
    public static boolean isMicrometerAvailable() {
        return MICROMETER_AVAILABLE;
    }

    private static boolean isClassPresent(String className) {
        try {
            Class.forName(className, false, NettyUtils.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }
}
