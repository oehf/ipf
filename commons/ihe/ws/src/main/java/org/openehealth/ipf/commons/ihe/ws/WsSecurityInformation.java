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

package org.openehealth.ipf.commons.ihe.ws;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.openehealth.ipf.commons.ihe.core.SecurityInformation;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

/**
 *
 */
public class WsSecurityInformation extends SecurityInformation {

    public WsSecurityInformation(SSLContext sslContext, HostnameVerifier hostnameVerifier, String username, String password) {
        super(sslContext, hostnameVerifier, username, password);
    }

    protected void configureHttpConduit(HTTPConduit httpConduit) {
        if (getSslContext() != null) {
            TLSClientParameters tlsClientParameters = initializeTLSClientParameters(httpConduit);
            tlsClientParameters.setSSLSocketFactory(getSslContext().getSocketFactory());
            if (getHostnameVerifier() != null) {
                tlsClientParameters.setHostnameVerifier(getHostnameVerifier());
            }
            httpConduit.setTlsClientParameters(tlsClientParameters);
        }
        if (getUsername() != null) {
            AuthorizationPolicy authorizationPolicy = new AuthorizationPolicy();
            authorizationPolicy.setUserName(getUsername());
            authorizationPolicy.setPassword(getPassword());
            httpConduit.setAuthorization(authorizationPolicy);
        }
    }

    private TLSClientParameters initializeTLSClientParameters(HTTPConduit httpConduit) {
        TLSClientParameters tlsClientParameters = httpConduit.getTlsClientParameters();
        return tlsClientParameters != null ? tlsClientParameters : new TLSClientParameters();
    }
}
