/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.core.ssl;

import org.apache.camel.CamelContext;
import org.apache.camel.support.jsse.SSLContextParameters;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * SSLContextParameters that are directly configured with an SSLContext. All configuration
 * setters are without effect.
 */
public class StaticSSLContextParameters extends SSLContextParameters {

    private final SSLContext sslContext;

    public StaticSSLContextParameters() throws NoSuchAlgorithmException {
        this(SSLContext.getDefault());
    }

    /**
     * Sets the SSLContext
     * @param sslContext SSLContext. If null, defaults to
     * @throws NoSuchAlgorithmException
     */
    public StaticSSLContextParameters(SSLContext sslContext) {
        if (sslContext == null) throw new IllegalArgumentException("SSLContext must not be null");
        this.sslContext = sslContext;
    }

    @Override
    public SSLContext createSSLContext(CamelContext camelContext)  {
        return sslContext;
    }

    @Override
    protected void configureSSLContext(SSLContext context) {
        throw new UnsupportedOperationException("SSLContext must be defined explicitly");
    }
}
