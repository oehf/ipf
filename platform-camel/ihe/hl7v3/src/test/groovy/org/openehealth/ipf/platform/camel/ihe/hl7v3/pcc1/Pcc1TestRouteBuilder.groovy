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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.pcc1

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.pcc1RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.pcc1ResponseValidator

/**
 * @author Dmytro Rud
 */
class Pcc1TestRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('qed-pcc1:qed-pcc1-service1')
            .process(pcc1RequestValidator())
            .process { 
                Exchanges.resultMessage(it).body = StandardTestContainer.readFile('pcc1/pcc1-sample-response.xml')
            }
            .process(pcc1ResponseValidator())

    }
}
