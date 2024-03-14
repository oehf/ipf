/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xacml20.chadr;

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.platform.camel.ihe.core.HomeCommunityUtils;

import jakarta.xml.bind.JAXBElement;

import static org.openehealth.ipf.platform.camel.ihe.xacml20.Xacml20CamelValidators.*;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class ChAdrTestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // sends a correct response with status "success"
        from("ch-adr:ch-adr-success")
                .process(chAdrRequestValidator())
                .process(exchange -> {
                    var stream = ChAdrTestRouteBuilder.class.getClassLoader().getResourceAsStream("messages/chadr/chadr-response-1.xml");
                    var unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
                    JAXBElement<?> jaxbElement = (JAXBElement<?>) unmarshaller.unmarshal(stream);
                    exchange.getMessage().setBody(jaxbElement.getValue());
                })
                .process(chAdrResponseValidator());

        // sends a correct response with status "failure"
        from("ch-adr:ch-adr-failure?homeCommunityId=urn:oid:1.2.777")
                .process(chAdrRequestValidator())
                .setProperty(HomeCommunityUtils.HOME_COMMUNITY_ID_NAME, constant("urn:oid:1.2.4"))
                .throwException(new RuntimeException("Alles schlimm..."));

    }
}
