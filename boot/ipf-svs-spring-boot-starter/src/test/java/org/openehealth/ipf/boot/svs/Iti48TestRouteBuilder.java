/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.boot.svs;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.platform.camel.ihe.svs.core.converters.SvsConverters;
import org.springframework.stereotype.Component;

/**
 * An ITI-48 consumer, i.e. a route as a user of this starter would write it. Whether it can be started
 * at all is what the test is about: it requires the SVS Camel component to be on the classpath, the CXF
 * bus of the Spring Boot application to publish the web service, and the audit context contributed by
 * the ATNA starter to be present.
 */
@Component
public class Iti48TestRouteBuilder extends RouteBuilder {

    public static final String SERVICE = "svs-iti48-service";

    public static final String VALUE_SET_ID = "1.2.840.10008.6.1.308";

    @Override
    public void configure() {
        from("svs-iti48:" + SERVICE)
                .process(exchange -> exchange.getMessage().setBody(SvsConverters.xmlToSvsResponse("""
                        <RetrieveValueSetResponse xmlns="urn:ihe:iti:svs:2008">
                            <ValueSet id="%s" displayName="Common Anatomic Regions Context ID 4031" version="20061023">
                                <ConceptList xml:lang="en-US">
                                    <Concept code="T-D4000" displayName="Abdomen" codeSystem="2.16.840.1.113883.6.5"/>
                                </ConceptList>
                            </ValueSet>
                        </RetrieveValueSetResponse>
                        """.formatted(VALUE_SET_ID))));
    }
}
