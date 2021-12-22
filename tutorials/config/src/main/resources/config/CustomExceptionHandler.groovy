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
package config

import ca.uhn.hl7v2.HL7Exception
import java.util.function.Function
import org.apache.camel.Exchange
import org.apache.camel.Expression
import org.openehealth.ipf.platform.camel.core.config.CustomRouteBuilder

/**
 * 
 * @author Boris Stanojevic
 */
class CustomExceptionHandler extends CustomRouteBuilder {

    void configure() {

        onException(HL7Exception.class)
          .maximumRedeliveries(0)
          .handled(true)
          .transform().exceptionMessage()
          .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
          .setHeader(Exchange.FILE_NAME).exchange({exhg ->
              "error-${System.currentTimeMillis()}.txt"
           } as Function)
          .to('file:target/hl7-error')
    }
}
