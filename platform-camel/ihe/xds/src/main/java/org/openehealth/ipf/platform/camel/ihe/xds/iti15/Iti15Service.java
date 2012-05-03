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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15;

import java.util.Map;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.iti15.Iti15PortType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.ProvideAndRegisterDocumentSetRequestType.Document;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.RegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rs.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;
import org.openehealth.ipf.platform.camel.ihe.xds.core.converters.EbXML21Converters;

/**
 * Service implementation for the IHE ITI-15 transaction (Provide and Register Document Set).
 * <p>
 * This implementation delegates to a Camel consumer by creating an exchange.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class Iti15Service extends AbstractWebService implements Iti15PortType {
    @Resource
    private WebServiceContext wsc;

    @Override
    public RegistryResponse documentRepositoryProvideAndRegisterDocumentSet(SubmitObjectsRequest body) {
        // We need to put together a structure similar to what is done in ITI-41
        // This means we have to put any message attachments into a ProvideAndRegisterDocumentSet
        MessageContext messageContext = wsc.getMessageContext();
        Map<?, ?> dataHandlers = (Map<?, ?>) messageContext.get(MessageContext.INBOUND_MESSAGE_ATTACHMENTS);
        
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        request.setSubmitObjectsRequest(body);
        for (Map.Entry<?, ?> entry : dataHandlers.entrySet()) {
            Document doc = new Document();
            doc.setId((String) entry.getKey());
            doc.setValue((DataHandler) entry.getValue());
            request.getDocument().add(doc);
        }
        
        Exchange result = process(request);
        if (result.getException() != null) {
            log.debug("ITI-15 service failed", result.getException());
            Response errorResponse = new Response(result.getException(), ErrorCode.REPOSITORY_METADATA_ERROR, ErrorCode.REPOSITORY_ERROR, null);
            return EbXML21Converters.convert(errorResponse);
        }
        
        return Exchanges.resultMessage(result).getBody(RegistryResponse.class);            
    }
}