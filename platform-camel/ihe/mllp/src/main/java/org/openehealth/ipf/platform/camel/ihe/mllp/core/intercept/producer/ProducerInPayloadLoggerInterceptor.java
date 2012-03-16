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

import lombok.Delegate;
import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.MllpPayloadLoggerBase;

/**
 * Producer-side MLLP interceptor which stores incoming payload
 * into files with user-defined name patterns.
 * <p>
 * Members of {@link MllpPayloadLoggerBase} are mixed into this class.
 *
 * @author Dmytro Rud
 */
public class ProducerInPayloadLoggerInterceptor extends AbstractMllpInterceptor {
    @Delegate private final MllpPayloadLoggerBase base = new MllpPayloadLoggerBase();

    public ProducerInPayloadLoggerInterceptor(String fileNamePattern) {
        addBefore(ProducerStringProcessingInterceptor.class.getName());
        setFileNamePattern(fileNamePattern);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        getWrappedProcessor().process(exchange);
        if (canProcess()) {
            logPayload(exchange, true);
        }
    }

}
