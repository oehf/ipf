/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq1;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.model.EprConstants.StatusCode;
import org.openehealth.ipf.commons.ihe.xacml20.stub.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.UnknownPolicySetId;

import jakarta.xml.bind.Marshaller;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint;

import java.io.StringWriter;
import java.util.Map;

import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpq1RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpq1ResponseValidator;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class ChPpq1TestRouteBuilder extends RouteBuilder {

    public static final String TRACE_CONTEXT_ID = "00-0af7651916cd43dd8448eb211c80319c-1111111111111111-01";

    @Override
    public void configure() {

        // sends a correct response with status "success"
        from("ch-ppq1:ch-ppq-success")
                .process(chPpq1RequestValidator())
                .process(exchange -> {
                    var request = exchange.getIn().getBody();
                    if (request instanceof AddPolicyRequest) {
                        exchange.getMessage().setHeader(AbstractWsEndpoint.OUTGOING_HTTP_HEADERS, Map.of("TRACEPARENT", TRACE_CONTEXT_ID));
                    }

                    var response = new EprPolicyRepositoryResponse();
                    response.setStatus(StatusCode.SUCCESS);
                    exchange.getMessage().setBody(response);
                    var marshaller = Xacml20Utils.JAXB_CONTEXT.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    var writer = new StringWriter();
                    marshaller.marshal(exchange.getMessage().getBody(), writer);
                    log.debug("PPQ output message:\n{}", writer);
                })
                .process(chPpq1ResponseValidator());

        // sends a correct response with status "failure"
        from("ch-ppq1:ch-ppq-failure")
                .process(chPpq1RequestValidator())
                .throwException(new RuntimeException("Alles schlimm..."));

        // throws an UnknownPolicySetId SOAP Fault (for UPDATE and DELETE)
        from("ch-ppq1:ch-ppq-exception")
                .process(chPpq1RequestValidator())
                .process(exchange -> {
                    var unknownSetId = new UnknownPolicySetId();
                    unknownSetId.setMessage("Policy set ID '12345' is unknown");
                    throw new UnknownPolicySetIdFaultMessage("An error is occurred", unknownSetId);
                });

    }
}
