/*
 * Copyright 2016 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8

import ca.uhn.hl7v2.AcknowledgmentCode
import ca.uhn.hl7v2.HL7Exception
import org.apache.camel.CamelContext
import org.apache.camel.Exchange
import org.apache.camel.impl.DefaultExchange
import org.apache.camel.spi.ExceptionHandler
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.springframework.beans.factory.annotation.Autowired

/**
 *
 */
class Iti8MllpExceptionHandler implements ExceptionHandler {

    @Autowired CamelContext camelContext

    @Override
    public void handleException(Throwable exception) {
        handleException("", exception)
    }

    @Override
    public void handleException(String message, Throwable exception) {
        handleException(message, null, exception)
    }

    @Override
    public void handleException(String message, Exchange exchange, Throwable exception) {
        if (exchange == null) {
            exchange = new DefaultExchange(camelContext)
        }
        message = MessageUtils.defaultNak(new HL7Exception(exception), AcknowledgmentCode.CE, "2.3.1").toString()
        exchange.getIn().setBody(message)
    }
}
