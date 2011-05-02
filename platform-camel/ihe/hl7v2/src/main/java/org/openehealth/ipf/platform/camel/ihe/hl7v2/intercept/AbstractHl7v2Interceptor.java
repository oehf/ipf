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
package org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;

import java.nio.charset.Charset;

/**
 * Abstract Camel interceptor for Hl7v2-based transactions.
 * @author Dmytro Rud
 */
public abstract class AbstractHl7v2Interceptor implements Hl7v2Interceptor {

    private final Hl7v2ConfigurationHolder configurationHolder;
    private final Processor wrappedProcessor;

    /**
     * Constructor.
     * @param configurationHolder
     *      The Camel endpoint to which this interceptor belongs.
     * @param wrappedProcessor
     *      Original camel-mina processor.
     */
    public AbstractHl7v2Interceptor(
            Hl7v2ConfigurationHolder configurationHolder,
            Processor wrappedProcessor)
    {
        Validate.notNull(wrappedProcessor);
        Validate.notNull(configurationHolder);

        this.wrappedProcessor = wrappedProcessor;
        this.configurationHolder = configurationHolder;
    }

    protected Hl7v2ConfigurationHolder getConfigurationHolder() {
        return configurationHolder;
    }

    @Override
    public Processor getWrappedProcessor() {
        return wrappedProcessor;
    }

    @Override
    public Hl7v2TransactionConfiguration getTransactionConfiguration() {
        return configurationHolder.getTransactionConfiguration();
    }

    @Override
    public NakFactory getNakFactory() {
        return configurationHolder.getNakFactory();
    }

    /**
     * Returns character set configured in the given Camel exchange,
     * or, when none found, the system default character set.
     * @param exchange
     *      Camel exchange.
     * @return
     *      character set name.
     */
    protected static String characterSet(Exchange exchange) {
        return exchange.getProperty(Exchange.CHARSET_NAME, Charset.defaultCharset().name(), String.class);
    }
}
