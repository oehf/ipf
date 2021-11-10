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
package org.openehealth.ipf.platform.camel.ihe.xds.iti86

import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocuments
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti86RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti86ResponseValidator

/**
 * @author Dmytro Rud
 */
public class Iti86TestRouteBuilder extends RouteBuilder {

    @Override
    void configure() throws Exception {
        from('rmd-iti86:rmd-iti86-service2')
            .process(iti86RequestValidator())
            .process { checkValue(it) }
            .process(iti86ResponseValidator())
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
        exchange.message.body = response
    }
}
