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

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.NoSuchAlgorithmException;

/**
 * Abstraction for setting TLS parameters to be used by ATNA sender implementations.
 * Use {@link DefaultAuditContext#setTlsParameters(TlsParameters)} to configure the
 * audit context.
 *
 * The default uses the default as defined by javax.net.ssl.* system properties.
 *
 * @author Christian Ohr
 */
public interface TlsParameters {

    static TlsParameters getDefault() {
        return () -> {
            try {
                return SSLContext.getDefault();
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        };
    }

    /**
     * @return the {@link SSLContext}
     */
    SSLContext getSSLContext();

    /**
     * @return the {@link SSLSocketFactory}
     */
    default SSLSocketFactory getSSLSocketFactory() {
        return getSSLContext().getSocketFactory();
    }

}
