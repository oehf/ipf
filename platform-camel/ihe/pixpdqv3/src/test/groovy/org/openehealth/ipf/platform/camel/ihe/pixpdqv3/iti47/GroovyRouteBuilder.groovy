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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti47

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.apache.commons.io.IOUtils
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Exception
import org.openehealth.ipf.commons.core.modules.api.ValidationException

/**
 * @author Dmytro Rud
 */
class GroovyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {

        from('pdqv3-iti47:pdqv3-iti47-service1' +
             '?supportContinuation=true' +
             '&defaultContinuationThreshold=1' +
             '&continuationStorage=#continuationStorage' +
             '&validationOnContinuation=true')
            .process {
                Exchanges.resultMessage(it).body = IOUtils.toString(
                        GroovyRouteBuilder.class.classLoader.getResourceAsStream('iti47/02_PDQQuery1Response.xml'))
            }

        from('pdqv3-iti47:pdqv3-iti47-service2')
            .process {
                Exchanges.resultMessage(it).body = '<response from="PDSupplier"/>'
            }

        // check Hl7v3 exception handling
        from('pdqv3-iti47:pdqv3-iti47-service3')
            .process {
                throw new Hl7v3Exception('message1', [
                    detectedIssueEventCode: 'ISSUE',
                    detectedIssueManagementCode: 'ABCD',
                    typeCode: 'XX',
                    statusCode: 'revised',
                    queryResponseCode: 'YY',
                    acknowledgementDetailCode: 'FEHLER'
                ])
            }

        // check validation exception handling
        from('pdqv3-iti47:pdqv3-iti47-service4')
            .process {
                throw new ValidationException('message2')
            }

    }
}
