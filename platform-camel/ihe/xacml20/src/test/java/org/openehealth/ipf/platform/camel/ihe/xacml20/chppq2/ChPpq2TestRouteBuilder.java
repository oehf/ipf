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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chppq2;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpq2RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.chPpq2ResponseValidator;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class ChPpq2TestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // sends a correct response with status "success"
        from("ch-ppq2:ch-ppq-success")
                .process(chPpq2RequestValidator())
                .process(exchange -> {
                    InputStream stream = ChPpq2TestRouteBuilder.class.getClassLoader().getResourceAsStream("messages/chppq2/ppq-query-backend-response.xml");
                    Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
                    JAXBElement<?> jaxbElement = (JAXBElement) unmarshaller.unmarshal(stream);
                    exchange.getOut().setBody(jaxbElement.getValue());
                    Marshaller marshaller = Xacml20Utils.JAXB_CONTEXT.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    StringWriter writer = new StringWriter();
                    marshaller.marshal(exchange.getOut().getBody(), writer);
                    log.debug("PPQ output message:\n{}", writer.toString());
                })
                .process(chPpq2ResponseValidator());

        // sends a correct response with status "failure"
        from("ch-ppq2:ch-ppq-failure")
                .process(chPpq2RequestValidator())
                .throwException(new RuntimeException("Alles schlimm..."));

    }
}
