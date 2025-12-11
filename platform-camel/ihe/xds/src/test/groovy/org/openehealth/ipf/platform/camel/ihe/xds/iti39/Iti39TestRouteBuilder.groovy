/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.iti39

import org.apache.camel.ExchangePattern
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import org.openehealth.ipf.platform.camel.ihe.ws.HeaderUtils
import org.slf4j.LoggerFactory

import jakarta.activation.DataHandler
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicInteger

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti39RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti39ResponseValidator

/**
 * Test routes for ITI-39.
 * @author Dmytro Rud
 */
class Iti39TestRouteBuilder extends RouteBuilder {
    private static final transient log = LoggerFactory.getLogger(Iti39TestRouteBuilder.class)

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static boolean errorOccurred = false

    private final CountDownLatch countDownLatch, asyncCountDownLatch

    static final int TASKS_COUNT = 5

    Iti39TestRouteBuilder(){
        countDownLatch      = new CountDownLatch(TASKS_COUNT)
        asyncCountDownLatch = new CountDownLatch(TASKS_COUNT)
    }

    CountDownLatch getCountDownLatch(){
        this.countDownLatch
    }

    CountDownLatch getAsyncCountDownLatch(){
        this.asyncCountDownLatch
    }

    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xca-iti39-async-response:iti39service-response' +
                '?correlator=#correlator' +
                '&inInterceptors=#clientAsyncInLogger' +
                '&inFaultInterceptors=#clientAsyncInLogger' +
                '&outInterceptors=#clientAsyncOutLogger' +
                '&outFaultInterceptors=#clientAsyncOutLogger'
        )
        .process(iti39ResponseValidator())
        .process {
            try {
                def inHttpHeaders = HeaderUtils.getIncomingHttpHeaders(it)
                assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')

                assert it.pattern == ExchangePattern.InOnly
                assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] ==
                        "corr ${asyncResponseCount.getAndIncrement() * 2}"

                assert it.in.getBody(RetrievedDocumentSet.class).status == Status.SUCCESS
                asyncCountDownLatch.countDown()
            } catch (Exception e) {
                errorOccurred = true
                log.error(e)
            }
        }

        // responding route
        from('xca-iti39:iti39service' +
             '?inInterceptors=#serverInLogger' +
             '&inFaultInterceptors=#serverInLogger' +
             '&outInterceptors=#serverOutLogger' +
             '&outFaultInterceptors=#serverOutLogger'
        )
            .process(iti39RequestValidator())
            .process {
                // check incoming SOAP and HTTP headers
                def inHttpHeaders = HeaderUtils.getIncomingHttpHeaders(it)

                try {
                    assert inHttpHeaders['MyRequestHeader'].startsWith('Number')
                } catch (Exception e) {
                    errorOccurred = true
                    log.error(e)
                }

                // create response, inclusive SOAP and HTTP headers
                it.message.body = createRetrievedDocumentSet()
                HeaderUtils.addOutgoingHttpHeaders(it, 'MyResponseHeader', 'Re: ' + inHttpHeaders['MyRequestHeader'])
                
                responseCount.incrementAndGet()
                countDownLatch.countDown()
            }
            .process(iti39ResponseValidator())

        // route for testing splitting of audit records
        from('xca-iti39:iti39service2-splitAudit')
            .process {
                RetrievedDocumentSet response = createRetrievedDocumentSet()
                response.documents[1].requestData.documentUniqueId = 'WRONG'
                it.message.body = response
            }
    }


    private static RetrievedDocumentSet createRetrievedDocumentSet() {
        DocumentReference requestData1 = new DocumentReference()
        requestData1.setDocumentUniqueId("doc1")
        requestData1.setHomeCommunityId("urn:oid:1.2.3")
        requestData1.setRepositoryUniqueId("repo1")

        DataHandler dataHandler1 = new DataHandler('Hund ' * 1500, "text/plain")
        RetrievedDocument doc1 = new RetrievedDocument()
        doc1.setRequestData(requestData1)
        doc1.setDataHandler(dataHandler1)
        doc1.setMimeType("animal/dog")

        DocumentReference requestData2 = new DocumentReference()
        requestData2.setDocumentUniqueId("doc2")
        requestData2.setHomeCommunityId("urn:oid:1.2.4")
        requestData2.setRepositoryUniqueId("repo2")

        DataHandler dataHandler2 = new DataHandler('Katz ' * 1500, "text/plain")
        RetrievedDocument doc2 = new RetrievedDocument()
        doc2.setRequestData(requestData2)
        doc2.setDataHandler(dataHandler2)
        doc2.setMimeType("animal/cat")

        RetrievedDocumentSet response = new RetrievedDocumentSet()
        response.getDocuments().add(doc1)
        response.getDocuments().add(doc2)
        response.setStatus(Status.SUCCESS)

        return response
    }


}
