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

import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfConstants;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * @author Dmytro Rud
 */
public class DispatchRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("cxf:bean:xdsRegistryEndpoint")
            .choice()
                .when(header(CxfConstants.OPERATION_NAME).isEqualTo("DocumentRegistry_RegistryStoredQuery"))
                    .to("direct:handle-iti18")
                .when(header(CxfConstants.OPERATION_NAME).isEqualTo("DocumentRegistry_RegisterDocumentSet-b"))
                    .to("direct:handle-iti42")
                .otherwise()
                    .to("direct:unsupportedOperation");

        from("direct:handle-iti18")
                .process(XdsCamelValidators.iti18RequestValidator())
                .transform(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        return (T) SampleData.createQueryResponseWithObjRef();
                    }
                })
                .convertBodyTo(AdhocQueryResponse.class)
                .process(XdsCamelValidators.iti18ResponseValidator());

        from("direct:handle-iti42")
                .process(XdsCamelValidators.iti42RequestValidator())
                .transform(new Expression() {
                    @Override
                    public <T> T evaluate(Exchange exchange, Class<T> type) {
                        return (T) SampleData.createResponse();
                    }
                })
                .convertBodyTo(RegistryResponseType.class)
                .process(XdsCamelValidators.iti42ResponseValidator());

        from("direct:unsupportedOperation")
                .throwException(new SOAPFaultException(SOAPFactory.newInstance().createFault(
                        "Unknown WSDL operation",
                        new QName("http://www.openehealth.org/ipf", "UNK"))));

    }
}
