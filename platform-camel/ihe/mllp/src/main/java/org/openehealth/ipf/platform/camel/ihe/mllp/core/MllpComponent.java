/*
 * Copyright 2009 the original author or authors.
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

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.hl7.HL7MLLPNettyDecoderFactory;
import org.apache.camel.component.netty.NettyComponent;
import org.apache.camel.component.netty.NettyConfiguration;
import org.apache.camel.component.netty.NettyEndpoint;
import org.apache.camel.component.netty.NettyEndpointConfigurer;
import org.apache.camel.spi.PropertyConfigurer;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.ssl.CamelTlsParameters;
import org.openehealth.ipf.platform.camel.ihe.core.ssl.StaticSSLContextParameters;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerAdaptingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;


/**
 * Generic Camel component for MLLP.
 *
 * @author Dmytro Rud
 */
public abstract class MllpComponent<ConfigType extends MllpEndpointConfiguration, AuditDatasetType extends MllpAuditDataset>
        extends NettyComponent implements InterceptableComponent, Hl7v2ConfigurationHolder<AuditDatasetType> {

    private static final transient Logger LOG = LoggerFactory.getLogger(MllpComponent.class);

    public static final String ACK_TYPE_CODE_HEADER = ConsumerAdaptingInterceptor.ACK_TYPE_CODE_HEADER;

    private static final String DEFAULT_HL7_DECODER_FACTORY_BEAN_NAME = "#hl7decoder";
    private static final String DEFAULT_HL7_ENCODER_FACTORY_BEAN_NAME = "#hl7encoder";
    private static final String MLLP_ENDPOINT_CONFIG = "mllpEndpointConfig";

    protected MllpComponent() {
        super();
    }

    /**
     * Camel context-based constructor.
     *
     * @param camelContext camel context
     */
    protected MllpComponent(CamelContext camelContext) {
        super(camelContext);
    }


    /**
     * Creates a configuration object.
     *
     * @param uri endpoint URI
     * @param parameters URL parameters.
     * @return configuration object filled with values from the provided parameter map.
     */
    protected abstract ConfigType createConfig(String uri, Map<String, Object> parameters) throws Exception;


    /**
     * Creates an endpoint object.
     *
     * @param wrappedEndpoint standard Camel Netty endpoint instance.
     * @param config          endpoint configuration.
     * @return configured MLLP endpoint instance which wraps the Netty one.
     */
    protected abstract MllpEndpoint<?, ?, ?> createEndpoint(NettyEndpoint wrappedEndpoint, ConfigType config);


    /**
     * Called when the NettyEndpoint is created. We overwrite a few parameters.
     */
    @Override
    protected NettyConfiguration parseConfiguration(NettyConfiguration configuration, String remaining, Map<String, Object> parameters) throws Exception {
        // Explicitly overwrite or set some standard camel-netty parameters
        var nettyParameters = new HashMap<>(parameters);
        nettyParameters.put("sync", true);
        nettyParameters.put("lazyChannelCreation", true);
        nettyParameters.put("transferExchange", false);
        nettyParameters.put("serverInitializerFactory",
                new CustomServerInitializerFactory(getAndRemoveParameter(parameters, MLLP_ENDPOINT_CONFIG, MllpEndpointConfiguration.class)));

        if (!nettyParameters.containsKey("decoders")) {
            nettyParameters.put("decoders", DEFAULT_HL7_DECODER_FACTORY_BEAN_NAME);
        }
        if (!nettyParameters.containsKey("encoders")) {
            nettyParameters.put("encoders", DEFAULT_HL7_ENCODER_FACTORY_BEAN_NAME);
        }
        // Backwards compatibility
        if (!nettyParameters.containsKey("requestTimeout")) {
            nettyParameters.put("requestTimeout", getAndRemoveParameter(parameters, "timeout", Long.class, 30000L));
        }
        if (!nettyParameters.containsKey("ssl")) {
            nettyParameters.put("ssl", getAndRemoveParameter(parameters, "secure", boolean.class, nettyParameters.containsKey("sslContextParameters")));
        }
        if (nettyParameters.containsKey("sslContext")) {
            nettyParameters.put("sslContextParameters", new StaticSSLContextParameters(getAndRemoveOrResolveReferenceParameter(parameters, "sslContext", SSLContext.class)));
        }
        var nettyConfiguration = super.parseConfiguration(configuration, remaining, nettyParameters);

        // Postprocess the configuration
        var charset = getCharset(nettyConfiguration);
        nettyConfiguration.setEncoding(charset.name());

        if (nettyConfiguration.isSsl() && nettyConfiguration.getSslContextParameters() == null ) {
            nettyConfiguration.setSslContextParameters(CamelTlsParameters.SYSTEM.getSSLContextParameters());
            // nettyConfiguration.setSslContextParameters(new StaticSSLContextParameters());
        }

        return nettyConfiguration;
    }

    private Charset getCharset(NettyConfiguration nettyConfiguration) {
        Charset charset = null;
        var decoder = new HL7MLLPNettyDecoderFactory();
        try {
            var decoders = nettyConfiguration.getDecoders();
            if (decoders.isEmpty()) {
                decoders.add(decoder);
                LOG.warn("No HL7 decoder factory found, creating new default instance {}", decoder);
            } else {
                decoder = (HL7MLLPNettyDecoderFactory)decoders.iterator().next();
            }
            charset = decoder.getCharset();
        } catch (ClassCastException cce) {
            LOG.warn("Unsupported HL7 decoder factory type {}, using default character set", decoder.getClass().getName());
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        return charset;
    }

    /**
     * Creates and configures the endpoint.
     */
    @Override
    protected Endpoint createEndpoint(
            String uri,
            String remaining,
            Map<String, Object> parameters) throws Exception {

        // construct IPF-specific config and pass in
        var config = createConfig(uri, parameters);
        parameters.put(MLLP_ENDPOINT_CONFIG, config);

        // construct the Netty endpoint
        var endpoint = super.createEndpoint(uri, "tcp://" + remaining, parameters);
        var nettyEndpoint = (NettyEndpoint) endpoint;

        // wrap and return
        return createEndpoint(nettyEndpoint, config);
    }

    @Override
    public PropertyConfigurer getEndpointPropertyConfigurer() {
        return new NettyEndpointConfigurer();
    }
}
