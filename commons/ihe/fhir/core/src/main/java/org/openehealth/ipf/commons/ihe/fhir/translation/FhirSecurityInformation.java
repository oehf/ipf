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

import org.apache.http.impl.client.HttpClientBuilder;
import org.openehealth.ipf.commons.ihe.core.SecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class FhirSecurityInformation extends SecurityInformation {

    public FhirSecurityInformation(boolean secure, SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
        super(secure, sslContext, hostnameVerifier, username, password);
    }

    public void configureHttpClientBuilder(HttpClientBuilder builder)  {
        if (isSecure() && getSslContext() == null) {
            try {
                builder.setSSLContext(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException e) {
                // Should never happen
                throw new RuntimeException("Could not create SSL Context", e);
            }
        } else {
            builder.setSSLContext(getSslContext());
        }
        if (getHostnameVerifier() != null) {
            builder.setSSLHostnameVerifier(getHostnameVerifier());
        }

        // This is currently done by the FhirProducer#getClient by attaching a ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor
        // I wonder if the following wouldn't be better

        /*
        if (getUsername() != null) {
            CredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
            UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(getUsername(), getPassword());
            provider.setCredentials(scope, credentials);
            builder.setDefaultCredentialsProvider(provider);
        }
        */
    }
}
