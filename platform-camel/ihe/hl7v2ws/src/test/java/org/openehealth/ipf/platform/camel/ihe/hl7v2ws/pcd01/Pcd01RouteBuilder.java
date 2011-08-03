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
package org.openehealth.ipf.platform.camel.ihe.hl7v2ws.pcd01;

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;
import static org.openehealth.ipf.platform.camel.core.util.Exchanges.resultMessage;
import static org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsCamelValidators.pcd01RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsCamelValidators.pcd01ResponseValidator;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
/**
 * @author Mitko Kolev
 *
 */
public class Pcd01RouteBuilder extends SpringRouteBuilder {
   
    public static final String PCD_01_SPEC_RESPONSE = load("pcd01/pcd01-response.hl7").toString();
    public static final String PCD_01_SPEC_RESPONSE_INVALID = load(
            "pcd01/pcd01-response-invalid.hl7").toString();

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        
    from("pcd-pcd01:devicedata?failureHandler=#failureHandler")
        .onException(Exception.class)
            .maximumRedeliveries(0)
            .end()
        .process(setOutBody(PCD_01_SPEC_RESPONSE));
    
    from("pcd-pcd01:route_throws_exception?failureHandler=#failureHandler")
        .throwException(new RuntimeException())
        .process(setOutBody(PCD_01_SPEC_RESPONSE));
    
    from("pcd-pcd01:route_unacceptable_response?failureHandler=#failureHandler")
        .process(setOutBody(PCD_01_SPEC_RESPONSE_INVALID));

    from("pcd-pcd01:route_inbound_validation")
        .onException(ValidationException.class)
            .maximumRedeliveries(0)
            .end()
        .process(pcd01RequestValidator())
        .process(setOutBody(PCD_01_SPEC_RESPONSE));
    
    from("pcd-pcd01:route_inbound_and_outbound_validation")
        .onException(ValidationException.class)
            .maximumRedeliveries(0)
            .end()
        .process(pcd01RequestValidator())
        .process(setOutBody(PCD_01_SPEC_RESPONSE))
        .process(pcd01ResponseValidator());
    }

    /**
     * Helper DSL method to set the out body.
     * 
     * @param body
     *            an Object
     * @return a processor that sets the output body.
     */
    protected <T> Processor setOutBody(final T body) {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                resultMessage(exchange).setBody(body);
            }
        };
    }

    
}
