/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.hl7v3.translation.Hl7TranslatorV2toV3;
import org.openehealth.ipf.commons.ihe.hl7v3.translation.Hl7TranslatorV3toV2;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;

/**
 * Camel processors for translation of HL7 messages between versions 2 and 3.
 * @author Dmytro Rud
 */
abstract public class PixPdqV3CamelTranslators {

    /**
     * Name of the Camel exchange property in which original 
     * request messages will be saved before translation.
     */
    public static final String HL7V3_ORIGINAL_REQUEST_PROPERTY = "hl7v3.original.request";

    
    /**
     * Returns a processor for translating HL7v3 messages to Hl7v2 
     * using the given translator instance. 
     */
    public static Processor translatorHL7v3toHL7v2(final Hl7TranslatorV3toV2 translator) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                MessageAdapter<?> initial = exchange.getProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, MessageAdapter.class);
                String xmlText = exchange.getIn().getMandatoryBody(String.class);
                exchange.setProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, xmlText);
                Message resultMessage = Exchanges.resultMessage(exchange);
                resultMessage.getHeaders().putAll(exchange.getIn().getHeaders());
                resultMessage.setBody(translator.translateV3toV2(xmlText, initial));
            }
        };
    }
    
    
    /**
     * Returns a processor for translating HL7v2 messages to HL7v3 
     * using the given translator instance. 
     */
    public static Processor translatorHL7v2toHL7v3(final Hl7TranslatorV2toV3 translator) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                String initial = exchange.getProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, String.class);
                MessageAdapter<?> msg = exchange.getIn().getMandatoryBody(MessageAdapter.class);
                exchange.setProperty(HL7V3_ORIGINAL_REQUEST_PROPERTY, msg);
                Message resultMessage = Exchanges.resultMessage(exchange);
                String charset = exchange.getProperty(Exchange.CHARSET_NAME, "UTF-8", String.class);
                resultMessage.getHeaders().putAll(exchange.getIn().getHeaders());
                resultMessage.setBody(translator.translateV2toV3(msg, initial, charset));
            }
        };
    }
    
}
