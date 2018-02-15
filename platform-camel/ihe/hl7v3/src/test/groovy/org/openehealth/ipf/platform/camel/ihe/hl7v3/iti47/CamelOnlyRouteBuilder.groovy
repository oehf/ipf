/*
 * Copyright 2012 the original author or authors.
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

import org.apache.camel.Exchange
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message.QBP_Q21
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqRequest3to2Translator
import org.openehealth.ipf.commons.ihe.hl7v3.translation.PdqResponse2to3Translator

import static java.util.Objects.requireNonNull
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.staticResponse
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.validatingProcessor
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.translatorHL7v2toHL7v3
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.translatorHL7v3toHL7v2
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47ResponseValidator

class CamelOnlyRouteBuilder extends RouteBuilder {

    private static final PdqRequest3to2Translator REQUEST_TRANSLATOR = new PdqRequest3to2Translator()
    private static final PdqResponse2to3Translator RESPONSE_TRANSLATOR = new PdqResponse2to3Translator()


    @Override
    public void configure() throws Exception {
        from("pdqv3-iti47:iti47Service")
                .onException(Exception.class)
                .maximumRedeliveries(0)
                .end()
                .process(iti47RequestValidator())
                .setHeader("myHeader", constant("content-1"))
                .convertBodyTo(byte[].class)
                .process(translatorHL7v3toHL7v2(REQUEST_TRANSLATOR))
                .process(typeAndHeaderChecker(QBP_Q21, "content-1"))
                .process(validatingProcessor())
                .transform(staticResponse(Testiti47CamelOnly.getResponseMessage()))
                .process(validatingProcessor())
                .setHeader("myHeader", constant("content-2"))
                .process(translatorHL7v2toHL7v3(RESPONSE_TRANSLATOR))
                .process(typeAndHeaderChecker(String.class, "content-2"))
                .process(iti47ResponseValidator())
    }


    private static Processor typeAndHeaderChecker(
            final Class<?> expectedClass,
            final String expectedHeaderContent) {
        requireNonNull(expectedClass)
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                Class<?> actualClass = exchange.getIn().getBody().getClass()
                if (!expectedClass.equals(actualClass)) {
                    throw new RuntimeException("Wrong body class: expected " +
                            expectedClass + ", got " + actualClass)
                }

                if (expectedHeaderContent != null) {
                    String actualHeaderContent = exchange.getIn().getHeader("myHeader", String.class)
                    if (!expectedHeaderContent.equals(actualHeaderContent)) {
                        throw new RuntimeException("wrong headers")
                    }
                }
            }
        }
    }

}
