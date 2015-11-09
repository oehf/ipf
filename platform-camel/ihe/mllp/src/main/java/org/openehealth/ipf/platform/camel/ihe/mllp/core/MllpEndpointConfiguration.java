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
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;

import javax.net.ssl.SSLContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Configuration of an MLLP endpoint.
 * @author Dmytro Rud
 */
public class MllpEndpointConfiguration implements Serializable {
    private static final long serialVersionUID = -3604219045768985192L;

    @Getter private final ProtocolCodecFactory codecFactory;

    @Getter private final boolean audit;
    @Getter private final SSLContext sslContext;
    @Getter private final List<InterceptorFactory> customInterceptorFactories;
    @Getter private final ClientAuthType clientAuthType;
    @Getter private final String[] sslProtocols;
    @Getter private final String[] sslCiphers;

    @Getter private final boolean supportSegmentFragmentation;
    @Getter private final int segmentFragmentationThreshold;

    @Getter private ConsumerDispatchingInterceptor dispatcher;


    protected MllpEndpointConfiguration(MllpComponent component, Map<String, Object> parameters) throws Exception {
        codecFactory = component.getCamelContext().getRegistry().lookupByNameAndType(
                extractBeanName((String) parameters.get("codec")),
                ProtocolCodecFactory.class);

        audit = component.getAndRemoveParameter(parameters, "audit", boolean.class, true);

        clientAuthType = component.getAndRemoveParameter(parameters, "clientAuth",
                ClientAuthType.class, ClientAuthType.NONE);

        String sslProtocolsString = component.getAndRemoveParameter(parameters, "sslProtocols", String.class, null);
        String sslCiphersString = component.getAndRemoveParameter(parameters, "sslCiphers", String.class, null);
        sslProtocols = sslProtocolsString != null ? sslProtocolsString.split(",") : null;
        sslCiphers = sslCiphersString != null ? sslCiphersString.split(",") : null;

        boolean secure = component.getAndRemoveParameter(parameters, "secure", boolean.class, false);
        sslContext = secure ? component.resolveAndRemoveReferenceParameter(
                parameters,
                "sslContext",
                SSLContext.class,
                SSLContext.getDefault()) : null;

        supportSegmentFragmentation = component.getAndRemoveParameter(
                parameters, "supportSegmentFragmentation", boolean.class, false);
        segmentFragmentationThreshold = component.getAndRemoveParameter(
                parameters, "segmentFragmentationThreshold", int.class, -1);                // >= 5 characters

        customInterceptorFactories = component.resolveAndRemoveReferenceListParameter(
                parameters, "interceptorFactories", InterceptorFactory.class);

        dispatcher = component.resolveAndRemoveReferenceParameter(parameters, "dispatcher", ConsumerDispatchingInterceptor.class);

    }


    private static String extractBeanName(String originalBeanName) {
        return originalBeanName.startsWith("#") ? originalBeanName.substring(1) : originalBeanName;
    }

}
