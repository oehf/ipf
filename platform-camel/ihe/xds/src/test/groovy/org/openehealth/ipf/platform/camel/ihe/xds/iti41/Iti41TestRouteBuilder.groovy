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
package org.openehealth.ipf.platform.camel.ihe.xds.iti41

import org.apache.cxf.binding.soap.SoapFault
import org.apache.cxf.headers.Header
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.xds.XdsSubmissionProducer

import javax.xml.namespace.QName
import javax.activation.DataHandler
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.io.IOUtils
import org.openehealth.ipf.commons.ihe.ws.utils.LargeDataSource
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.FAILURE
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.PARTIAL_SUCCESS
import static org.openehealth.ipf.commons.ihe.xds.core.responses.Status.SUCCESS
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41ResponseValidator

/**
 * @author Jens Riemschneider
 */
public class Iti41TestRouteBuilder extends SpringRouteBuilder {

    String soapFaultUnhandledEndpoint

    @Override
    public void configure() throws Exception {
        from('xds-iti41:xds-iti41-service1?rejectionHandlingStrategy=#rejectionHandlingStrategy')
            .process(iti41RequestValidator())
            .process { checkValue(it, 'service 1', 'urn:oid:1.2.1') }
            .process(iti41ResponseValidator())
    
        from('xds-iti41:xds-iti41-service2')
            .process { checkValue(it, 'service 2', 'urn:oid:1.2.2') }

        from('xds-iti41:xds-iti41-service3')
            .process {
                boolean hasExtraMetadata = it.in.getHeader(XdsJaxbDataBinding.SUBMISSION_SET_HAS_EXTRA_METADATA, Boolean.class)
                def response = new Response(hasExtraMetadata ? SUCCESS : FAILURE)
                Exchanges.resultMessage(it).body = response
            }

        // route which ends with a SOAP Fault
        from('xds-iti41:soap-fault-unhandled')
                .throwException(new SoapFault('SOAP fault in the test route', new QName('http://openehealth.org/ipf', 'soapfault')))

        // route which handles a SOAP fault internally and returns a normal response
        from('xds-iti41:soap-fault-handled')
            .onException(SoapFault)
                .process { Exchanges.extractException(it) }     // clean exchange
                .end()
            .setHeader('soapFaultUnhandledEndpoint', constant(soapFaultUnhandledEndpoint))
            .recipientList(header('soapFaultUnhandledEndpoint'))
            .setBody(constant(new Response(SUCCESS)))
    }


    void checkValue(exchange, expected, expectedTargetHomeCommunityId) {
        ProvideAndRegisterDocumentSet request = exchange.in.getBody(ProvideAndRegisterDocumentSet.class)
        def doc = request.documents[0]
        def value = doc.documentEntry.comments.value        
        def status = FAILURE;
        def dataHandler = doc.getContent(DataHandler)
        if (expected == value && dataHandler != null) {
            Collection attachments = dataHandler.dataSource.attachments
            def inputStream = dataHandler.inputStream
            try {
                if (attachments.size() == 1 && attachments.iterator().next().xop) {
                    def length = 0
                    while (inputStream.read() != -1) {
                        ++length
                    }
                    if (length == LargeDataSource.STREAM_SIZE) {
                        status = SUCCESS
                    }
                }
            }
            finally {
                IOUtils.closeQuietly(inputStream)
            }
        }

        if (request.targetHomeCommunityId != expectedTargetHomeCommunityId) {
            status = PARTIAL_SUCCESS
        }

        Header header = exchange.in.headers[AbstractWsEndpoint.INCOMING_SOAP_HEADERS][XdsSubmissionProducer.TARGET_HCID_HEADER_NAME]
        if (header.object.firstChild.textContent != expectedTargetHomeCommunityId) {
            status = PARTIAL_SUCCESS
        }

        def response = new Response(status)
        Exchanges.resultMessage(exchange).body = response
    }
}
