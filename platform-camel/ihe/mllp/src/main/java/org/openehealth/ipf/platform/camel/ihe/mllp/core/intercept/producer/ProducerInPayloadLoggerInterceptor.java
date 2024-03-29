/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer;

import lombok.experimental.Delegate;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.core.payload.ExpressionResolver;
import org.openehealth.ipf.commons.ihe.core.payload.SpringExpressionResolver;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorFactorySupport;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.HL7v2Endpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpPayloadLoggerBase;

/**
 * Producer-side MLLP interceptor which stores incoming payload
 * into files with user-defined name patterns.
 * <p>
 * Members of {@link MllpPayloadLoggerBase} are mixed into this class.
 *
 * @author Dmytro Rud
 */
public class ProducerInPayloadLoggerInterceptor extends InterceptorSupport {
    @Delegate private final MllpPayloadLoggerBase base = new MllpPayloadLoggerBase();

    /**
     * Instantiation, implicitly using a {@link SpringExpressionResolver}
     *
     * @param fileNamePattern file name pattern
     */
    public ProducerInPayloadLoggerInterceptor(String fileNamePattern) {
        this(new SpringExpressionResolver(fileNamePattern));
    }

    /**
     * Instantiation, explicitly using a ExpressionResolver instance
     *
     * @param resolver ExpressionResolver instance
     * @since 3.1
     */
    public ProducerInPayloadLoggerInterceptor(ExpressionResolver resolver) {
        super();
        addBefore(ProducerStringProcessingInterceptor.class.getName());
        setExpressionResolver(resolver);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        getWrappedProcessor().process(exchange);
        if (canProcess()) {
            logPayload(exchange, getEndpoint(HL7v2Endpoint.class).getInteractionId());
        }
    }

    public static class Factory extends InterceptorFactorySupport<ProducerInPayloadLoggerInterceptor> {

        private final ExpressionResolver resolver;
        private boolean enabled = true;

        public Factory(String fileNamePattern) {
            this(new SpringExpressionResolver(fileNamePattern));
        }

        public Factory(ExpressionResolver resolver) {
            super(ProducerInPayloadLoggerInterceptor.class);
            this.resolver = resolver;
        }

        @Override
        public ProducerInPayloadLoggerInterceptor getNewInstance() {
            var interceptor = new ProducerInPayloadLoggerInterceptor(resolver);
            interceptor.setEnabled(enabled);
            return interceptor;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
