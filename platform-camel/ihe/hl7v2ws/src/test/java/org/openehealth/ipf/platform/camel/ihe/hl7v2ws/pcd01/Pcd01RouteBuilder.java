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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.pcd.PcdTransactions;

import java.util.Scanner;

import static org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsCamelValidators.pcdValidator;

/**
 * @author Mitko Kolev
 */
public class Pcd01RouteBuilder extends RouteBuilder {

    public static final Message PCD_01_SPEC_RESPONSE = load(
            HapiContextFactory.createHapiContext(PcdTransactions.PCD1),
            "pcd01/pcd01-response.hl7");
    public static final Message PCD_01_SPEC_RESPONSE_INVALID = load(
            HapiContextFactory.createHapiContext(PcdTransactions.PCD1),
            "pcd01/pcd01-response-invalid.hl7");

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() {

        from("pcd-pcd01:devicedata?rejectionHandlingStrategy=#rejectionHandlingStrategy")
                .onException(Exception.class)
                .maximumRedeliveries(0)
                .end()
                .transform(constant(PCD_01_SPEC_RESPONSE));

        from("pcd-pcd01:route_throws_exception?rejectionHandlingStrategy=#rejectionHandlingStrategy")
                .throwException(new RuntimeException())
                .transform(constant(PCD_01_SPEC_RESPONSE));

        from("pcd-pcd01:route_unacceptable_response?rejectionHandlingStrategy=#rejectionHandlingStrategy")
                .transform(constant(PCD_01_SPEC_RESPONSE_INVALID));

        from("pcd-pcd01:route_inbound_validation")
                .onException(ValidationException.class)
                .maximumRedeliveries(0)
                .end()
                .process(pcdValidator())
                .transform(constant(PCD_01_SPEC_RESPONSE));

        from("pcd-pcd01:route_inbound_and_outbound_validation")
                .onException(ValidationException.class)
                .maximumRedeliveries(0)
                .end()
                .process(pcdValidator())
                .transform(constant(PCD_01_SPEC_RESPONSE))
                .process(pcdValidator());
    }

    private static <T extends Message> T load(HapiContext context, String fileName) {
        try {
            return (T) context.getPipeParser().parse(
                    new Scanner(Pcd01RouteBuilder.class.getResourceAsStream("/" + fileName)).useDelimiter("\\A").next());
        } catch (HL7Exception e) {
            return null;
        }
    }

}
