/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.ech.ech0214;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.cxf.interceptor.Fault;
import org.openehealth.ipf.commons.ihe.ech.ech0214.Ech0214PortType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Request;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2.Response;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

@Slf4j
public class Ech0214Service extends AbstractWebService implements Ech0214PortType {

    @Override
    public Response querySpid(Request request) {
        Exchange exchange = process(request);
        Exception exception = Exchanges.extractException(exchange);
        if (exception != null) {
            log.debug("eCH-0214 request failed", exception);
            throw new Fault(exception);
        }
        return exchange.getMessage().getBody(Response.class);
    }

}
