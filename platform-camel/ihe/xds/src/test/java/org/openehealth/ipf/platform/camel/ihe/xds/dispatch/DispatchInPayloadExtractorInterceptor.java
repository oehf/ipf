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

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadExtractorInterceptor;
import org.openehealth.ipf.commons.ihe.ws.cxf.payload.StringPayloadHolder;

import javax.xml.namespace.QName;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmytro Rud
 */
public class DispatchInPayloadExtractorInterceptor extends InPayloadExtractorInterceptor {
    private final static Set<QName> SET = new HashSet<>();
    static {
        SET.add(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_RegistryStoredQuery"));
        SET.add(new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_CrossGatewayQuery"));
        SET.add(new QName("urn:ihe:iti:xds-b:2007", "DocumentRegistry_MultiPatientStoredQuery"));
        SET.add(new QName("urn:ihe:iti:xds-b:2007", "RespondingGateway_CrossGatewayFetch"));
    }


    public DispatchInPayloadExtractorInterceptor() {
        super(StringPayloadHolder.PayloadType.SOAP_BODY);
    }

    @Override
    public void handleMessage(Message message) throws Fault {

        // TODO: find a way for choosing whether payload collecting is necessary or not
        /*
        if (! message.containsKey(MessageContext.WSDL_OPERATION)) {
            return;
        }

        QName operationName = (QName) message.get(MessageContext.WSDL_OPERATION);
        if (SET.contains(operationName)) {
            super.handleMessage(message);
        }
        */

        super.handleMessage(message);
    }
}
