/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.rad69

import org.apache.camel.spring.SpringRouteBuilder
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveImagingDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.ws.utils.LargeDataSource

import javax.activation.DataHandler;

/**
 * @author Clay Sebourn
 */
public class Rad69TestRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xdsi-rad69:xdsi-rad69-service1')
            .process(rad69RequestValidator())
            .process { checkValue(it, 'service 1') }
            .process(rad69ResponseValidator())
    
        from('xdsi-rad69:xdsi-rad69-service2')
            .process { checkValue(it, 'service 2') } 
    }

    void checkValue(exchange, expected) {
        def request = exchange.in.getBody(RetrieveImagingDocumentSet.class)
        def retrieveDocument = request.getRetrieveStudies().get(0).getRetrieveSerieses().get(0).getDocuments().get(0)
        def value = retrieveDocument.documentUniqueId
        def response = new RetrievedDocumentSet(SUCCESS)
        if (expected != value) {
            response.setStatus(FAILURE)
        }
        else {
            def doc = new RetrievedDocument()
            doc.dataHandler = new DataHandler(new LargeDataSource())
            doc.requestData = retrieveDocument
            doc.mimeType = 'application/octet-cream'
            response.documents.add(doc)
        }
        
        Exchanges.resultMessage(exchange).body = response
    }
}
