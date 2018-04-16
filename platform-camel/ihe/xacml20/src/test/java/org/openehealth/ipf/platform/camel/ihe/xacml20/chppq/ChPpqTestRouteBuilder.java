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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.chppq.UnknownPolicySetIdFaultMessage;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AssertionBasedRequestType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.EpdPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.UnknownPolicySetId;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpqRequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpqResponseValidator;

/**
 * @author Dmytro Rud
 */
public class ChPpqTestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // sends a correct response with status "success"
        from("ch-ppq:ch-ppq-success")
                .process(chPpqRequestValidator())
                .process(exchange -> {
                    Object request = exchange.getIn().getBody();
                    if (request instanceof XACMLPolicyQueryType) {
                        InputStream stream = ChPpqTestRouteBuilder.class.getClassLoader().getResourceAsStream("messages/chppq/ppq-query-backend-response.xml");
                        Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
                        JAXBElement<?> jaxbElement = (JAXBElement) unmarshaller.unmarshal(stream);
                        exchange.getOut().setBody(jaxbElement.getValue());
                    } else if (request instanceof AssertionBasedRequestType) {
                        AssertionBasedRequestType assertionRequest = (AssertionBasedRequestType) request;
                        EpdPolicyRepositoryResponse response = new EpdPolicyRepositoryResponse();
                        response.setStatus(Xacml20Utils.PPQ_STATUS_SUCCESS);
                        exchange.getOut().setBody(response);
                    }
                    Marshaller marshaller = Xacml20Utils.JAXB_CONTEXT.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    StringWriter writer = new StringWriter();
                    marshaller.marshal(exchange.getOut().getBody(), writer);
                    log.debug("PPQ output message:\n{}", writer.toString());
                })
                .process(chPpqResponseValidator());

        // sends a correct response with status "failure"
        from("ch-ppq:ch-ppq-failure")
                .process(chPpqRequestValidator())
                .throwException(new RuntimeException("Alles schlimm..."));

        // throws an UnknownPolicySetId SOAP Fault (for UPDATE and DELETE)
        from("ch-ppq:ch-ppq-exception")
                .process(chPpqRequestValidator())
                .process(exchange -> {
                    UnknownPolicySetId unknownSetId = new UnknownPolicySetId();
                    unknownSetId.setMessage("Policy set ID '12345' is unknown");
                    throw new UnknownPolicySetIdFaultMessage("An error is occurred", unknownSetId);
                });

    }
}
