/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.iti48;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.platform.camel.ihe.svs.core.SvsCamelValidators;
import org.openehealth.ipf.platform.camel.ihe.svs.core.converters.SvsConverters;
import org.openehealth.ipf.platform.camel.ihe.svs.iti48.exceptions.ChUnknownLanguageException;

/**
 * @author Quentin Ligier
 **/
public class Iti48TestRouteBuilder extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            from("svs-iti48:service1-ok")
                .process(SvsCamelValidators.iti48RequestValidator())
                .process(exchange -> {
                    var response = SvsConverters.xmlToSvsResponse("""
                        <RetrieveValueSetResponse xmlns="urn:ihe:iti:svs:2008" cacheExpirationHint="2008-08-15T00:00:00-05:00">
                            <ValueSet id="1.2.840.10008.6.1.308" displayName="Common Anatomic Regions Context ID 4031" version="20061023">
                                <ConceptList xml:lang="en-US">
                                    <Concept code="T-D4000" displayName="Abdomen" codeSystem="2.16.840.1.113883.6.5"/>
                                    <Concept code="R-FAB57" displayName="Abdomen and Pelvis" codeSystem="2.16.840.1.113883.6.5"/>
                                </ConceptList>
                            </ValueSet>
                        </RetrieveValueSetResponse>
                        """);
                    exchange.getMessage().setBody(response);
                })
                .process(SvsCamelValidators.iti48ResponseValidator());

            from("svs-iti48:service2-exception")
                .process(SvsCamelValidators.iti48RequestValidator())
                .process(exchange -> {
                    throw new ChUnknownLanguageException("en-EN");
                })
                .process(SvsCamelValidators.iti48ResponseValidator());
        }
}
