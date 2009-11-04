/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3ws;

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ws.DefaultItiWebService;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 * @author Dmytro Rud
 */
public class DefaultHL7v3WebService extends DefaultItiWebService {

    /**
     * The proper message processing method.
     * @param request
     *      XML payload of HL7 v3 request message.
     * @return
     *      XML payload of HL7 v3 response message.
     */
    protected String doProcess(String request) {
        Exchange exchange = process(request);
        return Exchanges.resultMessage(exchange).getBody(String.class);
    }

}
