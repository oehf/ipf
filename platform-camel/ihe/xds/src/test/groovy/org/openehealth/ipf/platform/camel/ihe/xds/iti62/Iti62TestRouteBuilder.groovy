/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.iti62

import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveMetadata
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorInfo
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti62RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti62ResponseValidator

/**
 * @author Boris Stanojevic
 */
public class Iti62TestRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti62:xds-iti62-service1')
            .process(iti62RequestValidator())
            .process { checkValue(it)}
            .process(iti62ResponseValidator())

        from('xds-iti62:xds-iti62-service2')
            .process { checkValue(it)}
    }

    void checkValue(exchange) {
        RemoveMetadata value = exchange.in.getBody(RemoveMetadata.class)
        def doCheck = value.references.find {
            it.id == 'wrong-id'
        }
        def response = new Response(doCheck? FAILURE : SUCCESS)
        if (response.status == FAILURE){
            ErrorInfo errorInfo = new ErrorInfo()
            // the codeContext attribute of the RegistryError element shall
            // contain the id attribute of the metadata object causing the error.
            errorInfo.setCodeContext('wrong-id')
            errorInfo.setErrorCode(ErrorCode.UNRESOLVED_REFERENCE_EXCEPTION)
            errorInfo.setSeverity(Severity.ERROR)
            response.errors.add(errorInfo)
        }
        exchange.message.body = response
    }
}
