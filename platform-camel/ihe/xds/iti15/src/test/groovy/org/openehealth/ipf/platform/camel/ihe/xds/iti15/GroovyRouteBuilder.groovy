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
package org.openehealth.ipf.platform.camel.ihe.xds.iti15

import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.*

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.apache.commons.io.IOUtils
import org.openehealth.ipf.commons.ihe.xds.core.utils.LargeDataSource
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet


/**
 * @author Jens Riemschneider
 */
public class GroovyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti15:xds-iti15-service1')
            .validate().iti15Request()
            .process { checkValue(it, 'service 1') }
            .validate().iti15Response()
    
        from('xds-iti15:xds-iti15-service2')
            .process { checkValue(it, 'service 2') }
   }

    def checkValue(exchange, expected) {
        def doc = exchange.in.getBody(ProvideAndRegisterDocumentSet.class).documents[0]
        def value = doc.documentEntry.comments.value
        def status = FAILURE
        if (expected == value && doc.dataHandler != null) {
            def length = 0
            def inputStream = doc.dataHandler.inputStream
            while (inputStream.read() != -1) {
                ++length
            }
            IOUtils.closeQuietly(inputStream)
            if (length == LargeDataSource.STREAM_SIZE) {
                status = SUCCESS
            }
        }
        
        def response = new Response(status)
        Exchanges.resultMessage(exchange).body = response
    }
}

