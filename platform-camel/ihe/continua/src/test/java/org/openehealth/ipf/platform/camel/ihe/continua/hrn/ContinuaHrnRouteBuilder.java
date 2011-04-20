/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import static org.openehealth.ipf.platform.camel.ihe.continua.hrn.ContinuaHrnCamelValidators.continuaHrnResponseValidator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.continua.hrn.processors.ClinicalDocumentEnricher;

/**
 * Test Continua HRN Route Builder.
 * 
 * @author Stefan Ivanov
 * 
 */
public class ContinuaHrnRouteBuilder extends RouteBuilder {
    
    @Override
    public void configure() throws Exception {
        
        from("xds-iti41:continuaHRNService")
            // .process(continuaHrnRequestValidator())
            .convertBodyTo(ProvideAndRegisterDocumentSet.class)
            .process(new ClinicalDocumentEnricher())
            .to("log:org.openehealth?level=ERROR&showAll=true&multiline=true")
            .process(SUCCESS_RESPONSE_PROCESSOR)
            .process(continuaHrnResponseValidator());
    }
    
    private static Processor SUCCESS_RESPONSE_PROCESSOR = new Processor() {
        
        @Override
        public void process(Exchange exchange) throws Exception {
            Exchanges.resultMessage(exchange).setBody(new Response(Status.SUCCESS), Response.class);
        }
    };
    
    
    
}
