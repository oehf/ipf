/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import lombok.Getter;
import org.apache.camel.util.EndpointHelper;
import org.apache.camel.util.jsse.ClientAuthentication;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.openehealth.ipf.platform.camel.ihe.atna.AuditableEndpointConfiguration;
import org.openehealth.ipf.platform.camel.ihe.core.AmbiguousBeanException;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.util.Map;

/**
 * Configuration of an MLLP endpoint.
 *
 * @author Dmytro Rud
 */
public class MllpEndpointConfiguration extends AuditableEndpointConfiguration {

    private static final long serialVersionUID = -3604219045768985192L;
    private static final Logger LOG = LoggerFactory.getLogger(MllpEndpointConfiguration.class);
    protected static final String UNKNOWN_URI = "unknown";

    @Getter
    private final ProtocolCodecFactory codecFactory;
    @Getter
    private final SSLContext sslContext;
    @Getter
    private final ClientAuthType clientAuthType;
    @Getter
    private final String[] sslProtocols;
    @Getter
    private final String[] sslCiphers;

    @Getter
    private final boolean supportSegmentFragmentation;
    @Getter
    private final int segmentFragmentationThreshold;

    @Getter
    private final ConsumerDispatchingInterceptor dispatcher;

    /**
     * @deprecated
     */
    protected MllpEndpointConfiguration(MllpComponent<?, ?> component, Map<String, Object> parameters) throws Exception {
        this(component, UNKNOWN_URI, parameters);
    }

    protected MllpEndpointConfiguration(MllpComponent<?, ?> component, String uri, Map<String, Object> parameters) throws Exception {
        super(component, parameters);
        codecFactory = EndpointHelper.resolveReferenceParameter(component.getCamelContext(), (String)parameters.get("codec"), ProtocolCodecFactory.class);

        // Will only be effective if sslContext is set and overrides
        String sslProtocolsString = component.getAndRemoveParameter(parameters, "sslProtocols", String.class, null);
        String sslCiphersString = component.getAndRemoveParameter(parameters, "sslCiphers", String.class, null);
        this.sslProtocols = sslProtocolsString != null ? sslProtocolsString.split("\\s*,\\s*") : null;
        this.sslCiphers = sslCiphersString != null ? sslCiphersString.split("\\s*,\\s*") : null;

        ClientAuthType configuredClientAuthType = component.getAndRemoveParameter(parameters, "clientAuth", ClientAuthType.class, ClientAuthType.NONE);
        boolean secure = component.getAndRemoveParameter(parameters, "secure", boolean.class, false);
        SSLContextParameters configuredSslContextParameters = component.resolveAndRemoveReferenceParameter(parameters, "sslContextParameters", SSLContextParameters.class);
        SSLContext configuredSslContext = component.resolveAndRemoveReferenceParameter(parameters, "sslContext", SSLContext.class);

        if (secure || configuredSslContextParameters != null || configuredSslContext != null) {
            LOG.debug("Setting up TLS security for MLLP endpoint {}", uri);
            if (configuredSslContext == null) {
                if (configuredSslContextParameters == null) {
                    Map<String, SSLContextParameters> sslContextParameterMap = component.getCamelContext().getRegistry().findByTypeWithName(SSLContextParameters.class);
                    if (sslContextParameterMap.size() == 1) {
                        Map.Entry<String, SSLContextParameters> entry = sslContextParameterMap.entrySet().iterator().next();
                        configuredSslContextParameters = entry.getValue();
                        LOG.debug("Setting up SSLContext from SSLContextParameters bean with name {}", entry.getKey());
                    } else if (sslContextParameterMap.size() > 1) {
                        throw new AmbiguousBeanException(SSLContextParameters.class);
                    }
                } else {
                    LOG.debug("Setting up SSLContext from SSLContextParameters provided in endpoint URI");
                }
                if (configuredSslContextParameters == null) {
                    LOG.debug("Setting up default SSLContext");
                    configuredSslContext = SSLContext.getDefault();
                } else {
                    configuredSslContext = configuredSslContextParameters.createSSLContext(component.getCamelContext());
                    // If not already specified, extract client authentication
                    if (configuredClientAuthType == null) {
                        String clientAuthenticationString = configuredSslContextParameters.getServerParameters().getClientAuthentication();
                        if (clientAuthenticationString != null) {
                            ClientAuthentication clientAuthentication = ClientAuthentication.valueOf(clientAuthenticationString.toUpperCase());
                            switch (clientAuthentication) {
                                case WANT: configuredClientAuthType = ClientAuthType.WANT; break;
                                case REQUIRE: configuredClientAuthType = ClientAuthType.MUST; break;
                                case NONE: configuredClientAuthType = ClientAuthType.NONE;
                            }
                        }
                    }

                }
            }
            this.sslContext = configuredSslContext;
        } else {
            this.sslContext = null;
        }

        clientAuthType = configuredClientAuthType;
        supportSegmentFragmentation = component.getAndRemoveParameter(
                parameters, "supportSegmentFragmentation", boolean.class, false);
        segmentFragmentationThreshold = component.getAndRemoveParameter(
                parameters, "segmentFragmentationThreshold", int.class, -1);                // >= 5 characters

        dispatcher = component.resolveAndRemoveReferenceParameter(parameters, "dispatcher", ConsumerDispatchingInterceptor.class);

    }

}
