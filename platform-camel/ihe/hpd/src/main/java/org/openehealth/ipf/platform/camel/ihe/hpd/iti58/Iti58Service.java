/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hpd.iti58;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hpd.iti58.Iti58PortType;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;
import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchResponse;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

import javax.jws.WebParam;

public class Iti58Service extends AbstractWebService implements Iti58PortType {

    @Override
    public BatchResponse providerInformationQueryRequest(@WebParam(partName = "body", name = "batchRequest", targetNamespace = "urn:oasis:names:tc:DSML:2:0:core") BatchRequest body) {
        Exchange result = process(body);
        Exception exception = Exchanges.extractException(result);
        if (exception != null) {
            // TODO
        }
        return Exchanges.resultMessage(result).getBody(BatchResponse.class);

    }
}
