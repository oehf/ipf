/*
 * Copyright 2025 the original author or authors.
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
package org.openehealth.ipf.boot;

import org.openehealth.ipf.commons.core.ssl.TlsParameters;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;

import javax.net.ssl.SSLContext;

/**
 * Implementation of TlsParameters that uses Spring Boot's SSL bundle configuration
 * to provide SSL/TLS parameters for secure connections.
 */
public class SslBundleTlsParameters implements TlsParameters {

    private final SslBundle bundle;

    /**
     * Creates a new SslBundleTlsParameters instance using the specified SSL bundle.
     *
     * @param sslBundles the SSL bundles registry containing configured SSL bundles
     * @param bundleName the name of the SSL bundle to retrieve and use for TLS configuration
     * @throws org.springframework.boot.ssl.NoSuchSslBundleException if bundle is unknown
     */
    public SslBundleTlsParameters(SslBundles sslBundles, String bundleName) {
        this.bundle = sslBundles.getBundle(bundleName);
    }

    @Override
    public SSLContext getSSLContext(boolean serverSide) {
        return bundle.createSslContext();
    }
}
