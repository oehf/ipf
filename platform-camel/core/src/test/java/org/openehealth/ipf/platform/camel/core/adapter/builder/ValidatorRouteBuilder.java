/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.core.adapter.builder;

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.platform.camel.core.support.builder.RouteBuilderSupport;

/**
 * @author Martin Krasser
 */
public class ValidatorRouteBuilder extends RouteBuilderSupport {

    @Override
    public void configure() throws Exception {
        onException(ValidationException.class).to("mock:error");

        from("direct:validator-test").process(
                helper.validator("testValidator").staticProfile("correct"));

        from("direct:validator-xml-test").process(
                helper.xsdValidator().staticProfile("/xsd/test.xsd")).setBody()
                .constant("passed");

        from("direct:validator-schematron-test").process(
                helper.schematronValidator().staticProfile(
                        new SchematronProfile(
                                "/schematron/schematron-test-rules.xml")))
                .setBody().constant("passed");
    }

}
