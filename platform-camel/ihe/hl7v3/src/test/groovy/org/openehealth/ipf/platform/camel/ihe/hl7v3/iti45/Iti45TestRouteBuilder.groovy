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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti45

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import org.openehealth.ipf.platform.camel.core.util.Exchanges

/**
 * @author Dmytro Rud
 */
class Iti45TestRouteBuilder extends SpringRouteBuilder {

    private static final String RESPONSE =
        StandardTestContainer.readFile('translation/pixquery/v3/07_PIXQuery1Response.xml')


    @Override
    public void configure() throws Exception {
        from('pixv3-iti45:pixv3-iti45-service1')
            .process(PixPdqV3CamelValidators.iti45RequestValidator())
            .setBody(constant(RESPONSE))
            .process(PixPdqV3CamelValidators.iti45ResponseValidator())


        from('pixv3-iti45:pixv3-iti45-service2?audit=false')
            .process {
                Exchanges.resultMessage(it).body = '''
                    <!-- comment 1 -->
                    <!-- comment 2 -->
                    <PRPA_IN201310UV02 xmlns="urn:hl7-org:v3" from="PIX Manager"/>
                    <!-- comment 3 -->
                '''
            }

    }
}
