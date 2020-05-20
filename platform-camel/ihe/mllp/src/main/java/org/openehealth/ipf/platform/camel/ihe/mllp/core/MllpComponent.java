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
import org.apache.camel.component.hl7.HL7MLLPCodec;
import org.apache.camel.component.mina.MinaComponent;
import org.apache.camel.component.mina.MinaEndpoint;
import org.apache.camel.component.mina.MinaEndpointConfigurer;
import org.apache.camel.spi.PropertyConfigurer;
import org.openehealth.ipf.commons.ihe.hl7v2.audit.MllpAuditDataset;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptableComponent;
import org.openehealth.ipf.platform.camel.ihe.core.Interceptor;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer.ConsumerAdaptingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Generic Camel component for MLLP.
 *
 * @author Dmytro Rud
 */
public abstract class MllpComponent<ConfigType extends MllpEndpointConfiguration, AuditDatasetType extends MllpAuditDataset>
        extends MinaComponent implements InterceptableComponent, Hl7v2ConfigurationHolder<AuditDatasetType> {

    private static final transient Logger LOG = LoggerFactory.getLogger(MllpComponent.class);

    public static final String ACK_TYPE_CODE_HEADER = ConsumerAdaptingInterceptor.ACK_TYPE_CODE_HEADER;

    private static final String DEFAULT_HL7_CODEC_FACTORY_BEAN_NAME = "#hl7codec";

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
     * @param wrappedEndpoint standard Camel MINA endpoint instance.
     * @param config          endpoint configuration.
     * @return configured MLLP endpoint instance which wraps the MINA one.
     */
    protected abstract MllpEndpoint<?, ?, ?> createEndpoint(MinaEndpoint wrappedEndpoint, ConfigType config);


    /**
     * Creates and configures the endpoint.
     */
    @Override
    protected Endpoint createEndpoint(
            String uri,
            String remaining,
            Map<String, Object> parameters) throws Exception {
        // explicitly overwrite some standard camel-mina parameters
        if (parameters.isEmpty()) {
            parameters = new HashMap<>();
        }
        parameters.put("sync", true);
        parameters.put("lazySessionCreation", true);
        parameters.put("transferExchange", false);
        if (!parameters.containsKey("codec")) {
            parameters.put("codec", DEFAULT_HL7_CODEC_FACTORY_BEAN_NAME);
        }

        var config = createConfig(uri, parameters);

        Charset charset = null;
        try {
            var codecFactory = (HL7MLLPCodec) config.getCodecFactory();
            if (codecFactory == null) {
                codecFactory = new HL7MLLPCodec();
                LOG.warn("No HL7 codec factory found, creating new default instance {}", codecFactory);
            }
            charset = codecFactory.getCharset();
        } catch (ClassCastException cce) {
            LOG.warn("Unsupported HL7 codec factory type {}, using default character set", config.getCodecFactory().getClass().getName());
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        parameters.put("encoding", charset.name());

        // construct the endpoint
        var endpoint = super.createEndpoint(uri, "tcp://" + remaining, parameters);
        var minaEndpoint = (MinaEndpoint) endpoint;

        // wrap and return
        return createEndpoint(minaEndpoint, config);
    }

    @Override
    public List<Interceptor<?>> getAdditionalConsumerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public List<Interceptor<?>> getAdditionalProducerInterceptors() {
        return Collections.emptyList();
    }

    @Override
    public PropertyConfigurer getEndpointPropertyConfigurer() {
        return new MinaEndpointConfigurer();
    }
}
