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

import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.PfxOptions;

import java.util.stream.Stream;

/**
 * Extension interface for {@link TlsParameters} for Vert.x based
 * ATNA clients.
 *
 * @deprecated 
 */
@Deprecated
public class VertxTlsParameters extends CustomTlsParameters {

    public static VertxTlsParameters getDefault() {
        return new VertxTlsParameters();
    }

    /**
     * Sets the Vert.x {@link NetClientOptions} according to te defined Tls Parameters
     *
     * @param options Vert.x {@link NetClientOptions}
     */
    public void initNetClientOptions(NetClientOptions options) {
        if ("JKS".equalsIgnoreCase(keyStoreType)) {
            options.setKeyStoreOptions(new JksOptions()
                    .setPath(keyStoreFile)
                    .setPassword(keyStorePassword));
        } else {
            options.setPfxKeyCertOptions(new PfxOptions()
                    .setPath(keyStoreFile)
                    .setPassword(keyStorePassword));
        }
        if ("JKS".equalsIgnoreCase(trustStoreType)) {
            options.setTrustStoreOptions(new JksOptions()
                    .setPath(trustStoreFile)
                    .setPassword(trustStorePassword));
        } else {
            options.setPfxTrustOptions(new PfxOptions()
                    .setPath(trustStoreFile)
                    .setPassword(trustStorePassword));
        }
        Stream.of(split(enabledProtocols))
                .forEach(options::addEnabledSecureTransportProtocol);

        if (enabledCipherSuites != null) {
            Stream.of(split(enabledCipherSuites))
                    .forEach(options::addEnabledCipherSuite);
        }
    }
}
