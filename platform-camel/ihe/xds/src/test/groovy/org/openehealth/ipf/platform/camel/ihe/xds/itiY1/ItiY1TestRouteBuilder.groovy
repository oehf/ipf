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
package org.openehealth.ipf.platform.camel.ihe.xds.itiY1

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocuments
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity
import org.openehealth.ipf.platform.camel.core.util.Exchanges

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.itiY1RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.itiY1ResponseValidator

/**
 * @author Dmytro Rud
 */
public class ItiY1TestRouteBuilder extends SpringRouteBuilder {

    @Override
    void configure() throws Exception {
        from('xds-itiY1:xds-itiY1-service2')
            .process(itiY1RequestValidator())
            .process { checkValue(it) }
            .process(itiY1ResponseValidator())
    }

    void checkValue(Exchange exchange) {
        RemoveDocuments request = exchange.in.getBody(RemoveDocuments.class)
        String firstDocumentUid = request.documents[0].documentUniqueId
        Response response = null
        switch (firstDocumentUid) {
            case 'success':
                response = new Response(SUCCESS)
                break
            case 'failure':
                response = new Response(FAILURE)
                break
            case 'partial':
                response = new Response(PARTIAL_SUCCESS)
                response.errors << new ErrorInfo(ErrorCode.DOCUMENT_UNIQUE_ID_ERROR, "blabla ${request.documents[1].documentUniqueId} foobar", Severity.WARNING, null, null)
                response.errors << new ErrorInfo(ErrorCode.DOCUMENT_UNIQUE_ID_ERROR, "foobar ${request.documents[2].documentUniqueId} blabla", Severity.ERROR, null, null)
                break
        }
        Exchanges.resultMessage(exchange).body = response
    }
}
