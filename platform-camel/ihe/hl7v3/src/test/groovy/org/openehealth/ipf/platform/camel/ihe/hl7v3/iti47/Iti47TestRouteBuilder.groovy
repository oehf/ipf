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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti47

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Exception
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqRequest3to2Translator
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqResponse2to3Translator
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.*

/**
 * @author Dmytro Rud
 */
class Iti47TestRouteBuilder extends SpringRouteBuilder {
    private static final PdqRequest3to2Translator REQUEST_TRANSLATOR =
        new PdqRequest3to2Translator(translateInitialQuantity: true)

    private static final PdqResponse2to3Translator RESPONSE_TRANSLATOR =
        new PdqResponse2to3Translator()

    private static final String V3_RESPONSE = StandardTestContainer.readFile('iti47/02_PDQQuery1Response.xml')

    private static final String V2_RESPONSE =
        'MSH|^~\\&|MESA_PD_SUPPLIER|PIM|MESA_PD_CONSUMER|MESA_DEPARTMENT|' +
            '20090901140929||RSP^K22^RSP_K21|356757|P|2.5|||\r' +
        'MSA|AA|12345\r' +
        'QAK|1486133081|OK\n' +
        'QPD|IHE PDQ Query|1486133081|@PID.11.3^KÃ¶ln\n' +
        'PID|1||79471^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&' +
            'ISO^PI~78912^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI|' +
            '|MÃ¼ller^Hans||19400101|M|||Am Domplatz 1^^KÃ¶ln^^57000|' +
            '|022/235715~022/874491~01732356265||||' +
            '|GLD-1-1^^^ANGID1&2.16.840.1.113883.3.37.4.1.5.2&ISO^AN|123-11-1234\r' +
        'PID|2||78001^^^PKLN&2.16.840.1.113883.3.37.4.1.1.2.511.1&ISO^PI|' +
            '|Mueller^Hans||19400101|M|||Teststr. 1^^KÃ¶ln^^57000||022/235715\r' +
        'PID|3||79653^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI|' +
            '|MÃ¼ller^Hannes||19400101|M|||Am Domplatz 14^^KÃ¶ln^^57000||022/843274\r' +
        'PID|4||79233^^^HZLN&2.16.840.1.113883.3.37.4.1.1.2.411.1&ISO^PI|' +
            '|MÃ¼ller^Joachim||19400101|M|||Am Domplatz 112^^KÃ¶ln^^57000||022/844275\r';


    @Override
    public void configure() throws Exception {

        from('pdqv3-iti47:pdqv3-iti47-service1')
            .process(PixPdqV3CamelValidators.iti47RequestValidator())
            .setBody(constant(V3_RESPONSE))
            .process(PixPdqV3CamelValidators.iti47ResponseValidator())


        from('pdqv3-iti47:pdqv3-iti47-serviceConti' +
             '?supportContinuation=true' +
             '&defaultContinuationThreshold=1' +
             '&continuationStorage=#hl7v3ContinuationStorage' +
             '&validationOnContinuation=true')
            .process(PixPdqV3CamelValidators.iti47RequestValidator())
            .streamCaching()
            .setBody(constant(V3_RESPONSE.bytes))
            .process(PixPdqV3CamelValidators.iti47ResponseValidator())


        from('pdqv3-iti47:pdqv3-iti47-serviceIntercept')
            .process {
                Exchanges.resultMessage(it).body = '<PRPA_IN201306UV02 xmlns="urn:hl7-org:v3" from="PDSupplier"/>'
            }


        // check Hl7v3 exception handling (NAK with issue management code)
        from('pdqv3-iti47:pdqv3-iti47-serviceNak1')
            .throwException(new Hl7v3Exception(
                    message: 'message1',
                    detectedIssueEventCode: 'ISSUE',
                    detectedIssueManagementCode: 'ABCD',
                    typeCode: 'XX',
                    statusCode: 'revised',
                    queryResponseCode: 'YY',
                    acknowledgementDetailCode: 'FEHLER'))


        // check Hl7v3 exception handling (NAK without issue management code)
        from('pdqv3-iti47:pdqv3-iti47-serviceNak2')
            .throwException(new Hl7v3Exception(
                    message: 'message1',
                    detectedIssueEventCode: 'ISSUE',
                    typeCode: 'XX',
                    statusCode: 'revised',
                    queryResponseCode: 'YY',
                    acknowledgementDetailCode: 'FEHLER'))


        // check validation exception handling
        from('pdqv3-iti47:pdqv3-iti47-serviceNakValidate')
            .process {
                throw new ValidationException('message2')
            }


        // routes for v3-v2 continuation interoperability test
        from('pdqv3-iti47:pdqv3-iti47-serviceV2Conti')
            .process(PixPdqV3CamelValidators.iti47RequestValidator())
            .process(translatorHL7v3toHL7v2(REQUEST_TRANSLATOR))
            .to('pdq-iti21://localhost:8888?supportInteractiveContinuation=true')
            .process(translatorHL7v2toHL7v3(RESPONSE_TRANSLATOR))
            .process(PixPdqV3CamelValidators.iti47ResponseValidator())


        from('pdq-iti21://localhost:8888?supportInteractiveContinuation=true' +
             '&interactiveContinuationStorage=#hl7v2ContinuationStorage')
            .process {
                Exchanges.resultMessage(it).body = V2_RESPONSE
            }

    }
}
