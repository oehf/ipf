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
package org.openehealth.ipf.platform.camel.ihe.xds.iti42

import org.apache.camel.spring.SpringRouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti42RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti42ResponseValidator

/**
 * @author Jens Riemschneider
 */
public class Iti42TestRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from('xds-iti42:xds-iti42-service1')
            .process(iti42RequestValidator())
            .process {
                RegisterDocumentSet request = it.in.getBody(RegisterDocumentSet)
                assert request.documentEntries[0].extraMetadata == [
                    'urn:oehf:docEntry1' : ['a', 'b', 'c'],
                    'urn:oehf:docEntry2' : ['g', 'h']]
                assert request.submissionSet.extraMetadata == [
                    'urn:oehf:submission1' : ['1', '2', '3'],
                    'urn:oehf:submission2' : ['7', '8']]
                assert request.associations[1].extraMetadata == [
                    'urn:oehf:association1' : ['A', 'B', 'C'],
                    'urn:oehf:association2' : ['G', 'H']]
                assert request.folders[0].extraMetadata == [
                    'urn:oehf:folder1' : ['i', 'ii', 'iii'],
                    'urn:oehf:folder2' : ['vii', 'viii']]
            }
            .process { checkValue(it, 'service 1') }
            .process(iti42ResponseValidator())
    
        from('xds-iti42:xds-iti42-service2')
            .process { checkValue(it, 'service 2') }

        from('xds-iti42:xds-iti42-service3')
            .process {
                boolean hasExtraMetadata = it.in.getHeader(XdsJaxbDataBinding.SUBMISSION_SET_HAS_EXTRA_METADATA, Boolean.class)
                def response = new Response(hasExtraMetadata ? SUCCESS : FAILURE)
                Exchanges.resultMessage(it).body = response
            }

        from('xds-iti42:xds-iti42-service4?schemaLocations=#schemaLocations&properties=#props')
             .process { checkValue(it, 'service 4') }

    }

    void checkValue(exchange, expected) {
        def value = exchange.in.getBody(RegisterDocumentSet.class).documentEntries[0].comments.value        
        def response = new Response(expected == value ? SUCCESS : FAILURE)
        Exchanges.resultMessage(exchange).body = response
    }
}
