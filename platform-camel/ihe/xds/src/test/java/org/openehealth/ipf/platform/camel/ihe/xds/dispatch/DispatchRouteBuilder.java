/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

import javax.xml.namespace.QName;
import jakarta.xml.soap.SOAPFactory;
import jakarta.xml.ws.soap.SOAPFaultException;

import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class DispatchRouteBuilder extends RouteBuilder {

    private static final String ACTION_PROPERTY = "action";

    @Override
    public void configure() throws Exception {
        from("cxf:bean:xdsRegistryEndpoint")
                .process(exchange -> exchange.setProperty(ACTION_PROPERTY, DispatchInContextCreatorInterceptor
                        .extractWsaAction(exchange.getProperty(CxfConstants.JAXWS_CONTEXT, Map.class)))).choice()
                .when(exchangeProperty(ACTION_PROPERTY).isEqualTo("urn:ihe:iti:2007:RegistryStoredQuery"))
                    .to("direct:handle-iti18")
                .when(exchangeProperty(ACTION_PROPERTY).isEqualTo("urn:ihe:iti:2007:RegisterDocumentSet-b"))
                    .to("direct:handle-iti42")
                .when(exchangeProperty(ACTION_PROPERTY).isEqualTo("urn:ihe:pharm:cmpd:2010:QueryPharmacyDocuments"))
                    .to("direct:handle-pharm1")
                .otherwise()
                    .to("direct:unsupportedOperation");

        from("direct:handle-iti18")
                .process(XdsCamelValidators.iti18RequestValidator())
                .setBody(constant(SampleData.createQueryResponseWithObjRef()))
                .convertBodyTo(AdhocQueryResponse.class)
                .process(XdsCamelValidators.iti18ResponseValidator());

        from("direct:handle-iti42")
                .process(XdsCamelValidators.iti42RequestValidator())
                .setBody(constant(SampleData.createResponse()))
                .convertBodyTo(RegistryResponseType.class)
                .process(XdsCamelValidators.iti42ResponseValidator());

        from("direct:handle-pharm1")
                .process(XdsCamelValidators.pharm1RequestValidator())
                .setBody(constant(SampleData.createQueryResponseWithObjRef()))
                .convertBodyTo(AdhocQueryResponse.class)
                .process(XdsCamelValidators.pharm1ResponseValidator());

        from("direct:unsupportedOperation")
                .throwException(new SOAPFaultException(SOAPFactory.newInstance().createFault(
                        "Unknown WSDL operation",
                        new QName("http://www.openehealth.org/ipf", "UNK"))));

    }
}
