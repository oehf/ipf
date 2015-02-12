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

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLContext;

import lombok.Getter;
import org.apache.camel.spring.GenericBeansException;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.openehealth.ipf.commons.ihe.core.ClientAuthType;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2InterceptorFactory;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerDispatchingInterceptor;
import org.springframework.context.ApplicationContext;

/**
 * Configuration of an MLLP endpoint.
 * @author Dmytro Rud
 */
public class MllpEndpointConfiguration implements Serializable {
    private static final long serialVersionUID = -3604219045768985192L;

    @Getter private final ProtocolCodecFactory codecFactory;

    @Getter private final boolean audit;
    @Getter private final SSLContext sslContext;
    @Getter private final String[] customInterceptorBeans;
    @Getter private final List<Hl7v2InterceptorFactory> customInterceptorFactories;
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
                parameters, "interceptorFactories", Hl7v2InterceptorFactory.class);

        customInterceptorBeans = extractInterceptorBeanNames(component, parameters);

        dispatcher = component.resolveAndRemoveReferenceParameter(parameters, "dispatcher", ConsumerDispatchingInterceptor.class);

    }


    private static String extractBeanName(String originalBeanName) {
        return originalBeanName.startsWith("#") ? originalBeanName.substring(1) : originalBeanName;
    }


    private String[] extractInterceptorBeanNames(MllpComponent component, Map<String, Object> parameters) {
        SpringCamelContext camelContext = (SpringCamelContext) component.getCamelContext();
        ApplicationContext applicationContext = camelContext.getApplicationContext();

        String paramValue = component.getAndRemoveParameter(parameters, "interceptors", String.class);
        if (StringUtils.isEmpty(paramValue)) {
            return new String[0];
        }

        String[] beanNames = paramValue.split(",");
        for (int i = 0; i < beanNames.length; ++i) {
            beanNames[i] = extractBeanName(beanNames[i]);
            if (! applicationContext.isPrototype(beanNames[i])) {
                throw new GenericBeansException("Custom HL7v2 interceptor bean '" +
                        beanNames[i] + "' shall have scope=\"prototype\"");
            }
        }

        return beanNames;
    }

}
