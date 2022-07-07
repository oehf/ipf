/*
 * Copyright 2016 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.translation;

import org.openehealth.ipf.commons.ihe.core.SecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @param <T> HttpBuilder class
 */
public abstract class FhirSecurityInformation<T> extends SecurityInformation {

    public FhirSecurityInformation(boolean secure, SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
        super(secure, sslContext, hostnameVerifier, username, password);
    }

    public abstract void configureHttpClientBuilder(T builder);

    public SSLContext getSslContext() {
        try {
            return super.getSslContext() == null ? SSLContext.getDefault() : super.getSslContext();
        } catch (NoSuchAlgorithmException e) {
            // Should never happen
            throw new RuntimeException("Could not create SSL Context", e);
        }
    }
}
