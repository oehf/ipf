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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti46

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.apache.camel.Exchange
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators

/**
 * @author Dmytro Rud
 */
class Iti46TestRouteBuilder extends SpringRouteBuilder {

    private static final String ACK =
            StandardTestContainer.readFile('translation/pixfeed/v3/Ack.xml')

    @Override
    public void configure() throws Exception {
        from('pixv3-iti46:pixv3-iti46-service1')
            .process(PixPdqV3CamelValidators.iti46RequestValidator())
            .setBody(constant(ACK))
            .process(PixPdqV3CamelValidators.iti46ResponseValidator())


        from('pixv3-iti46:pixv3-iti46-charset')
            .process {
                if (it.properties[Exchange.CHARSET_NAME] != 'KOI8-R') {
                    throw new RuntimeException('KOI-8 character set expected')
                }
                it.properties[Exchange.CHARSET_NAME] = 'Windows-1251'
                Exchanges.resultMessage(it).body = '<MCCI_IN000002UV01 xmlns="urn:hl7-org:v3" from="PIX Consumer"/>'
            }
    }
}
