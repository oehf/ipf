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
import org.openehealth.ipf.commons.ihe.core.chain.ChainableImpl;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.NakFactory;

import java.nio.charset.Charset;

/**
 * Abstract interceptor for Hl7v2-based transactions.
 * @author Dmytro Rud
 */
abstract public class AbstractHl7v2Interceptor extends ChainableImpl implements Hl7v2Interceptor {

    private Hl7v2ConfigurationHolder configurationHolder;
    private Processor wrappedProcessor;


    @Override
    public Hl7v2ConfigurationHolder getConfigurationHolder() {
        return configurationHolder;
    }

    @Override
    public void setConfigurationHolder(Hl7v2ConfigurationHolder configurationHolder) {
        this.configurationHolder = configurationHolder;
    }

    @Override
    public Processor getWrappedProcessor() {
        return wrappedProcessor;
    }

    @Override
    public void setWrappedProcessor(Processor wrappedProcessor) {
        this.wrappedProcessor = wrappedProcessor;
    }

    /**
     * Shortcut to access HL7v2 transaction configuration.
     * @return HL7v2 transaction configuration.
     */
    public Hl7v2TransactionConfiguration getHl7v2TransactionConfiguration() {
        return configurationHolder.getHl7v2TransactionConfiguration();
    }

    /**
     * Shortcut to access HL7v2 NAK factory.
     * @return HL7v2 NAK factory.
     */
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
