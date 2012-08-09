/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.rad75;


import java.util.concurrent.atomic.AtomicInteger
import javax.activation.DataHandler
import org.apache.camel.ExchangePattern
import org.apache.camel.Message
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.openehealth.ipf.platform.camel.core.util.Exchanges
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWsEndpoint
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.rad75RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.rad75ResponseValidator

/**
 * Test routes for RAD-75.
 * @author Clay Sebourn
 */
class Rad75TestRouteBuilder extends SpringRouteBuilder {
    private static final transient LOG = LogFactory.getLog(Rad75TestRouteBuilder.class)

    static final AtomicInteger responseCount = new AtomicInteger()  
    static final AtomicInteger asyncResponseCount = new AtomicInteger()
    
    static final long ASYNC_DELAY = 10 * 1000L

    static boolean errorOccurred = false
    
    @Override
    public void configure() throws Exception {

        // receiver of asynchronous responses
        from('xcai-rad75-async-response:rad75service-response' +
             '?correlator=#correlator' +
             '&inInterceptors=#clientAsyncInLogger' +
             '&inFaultInterceptors=#clientAsyncInLogger' +
             '&outInterceptors=#clientAsyncOutLogger' +
             '&outFaultInterceptors=#clientAsyncOutLogger'
        )
            .process(rad75ResponseValidator())
            .process {
                try {
                    def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]
                    assert inHttpHeaders['MyResponseHeader'].startsWith('Re: Number')

                    assert it.pattern == ExchangePattern.InOnly
                    assert it.in.headers[AbstractWsEndpoint.CORRELATION_KEY_HEADER_NAME] ==
                        "corr ${asyncResponseCount.getAndIncrement() * 2}"

                    assert it.in.getBody(RetrievedDocumentSet.class).status == Status.SUCCESS
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }
            }
            .delay(ASYNC_DELAY)


        // responding route
        from('xcai-rad75:rad75service' +
             '?inInterceptors=#serverInLogger' +
             '&inFaultInterceptors=#serverInLogger' +
             '&outInterceptors=#serverOutLogger' +
             '&outFaultInterceptors=#serverOutLogger'
        )
            .process(rad75RequestValidator())
            .process {
                // check incoming SOAP and HTTP headers
                def inHttpHeaders = it.in.headers[AbstractWsEndpoint.INCOMING_HTTP_HEADERS]

                try {
                    assert inHttpHeaders['MyRequestHeader'].startsWith('Number')
                } catch (Exception e) {
                    errorOccurred = true
                    LOG.error(e)
                }

                // create response, inclusive SOAP and HTTP headers
                Message message = Exchanges.resultMessage(it)
                message.body = createRetrievedDocumentSet()
                message.headers[AbstractWsEndpoint.OUTGOING_HTTP_HEADERS] =
                    ['MyResponseHeader' : ('Re: ' + inHttpHeaders['MyRequestHeader'])]
                
                responseCount.incrementAndGet()
            }
            .process(rad75ResponseValidator())
    }


    private static RetrievedDocumentSet createRetrievedDocumentSet() {
        RetrieveDocument requestData1 = new RetrieveDocument();
        requestData1.setDocumentUniqueId("doc1");
        requestData1.setHomeCommunityId("urn:oid:1.2.3");
        requestData1.setRepositoryUniqueId("repo1");

        DataHandler dataHandler1 = new DataHandler('Hund ' * 1500, "text/plain");
        RetrievedDocument doc1 = new RetrievedDocument();
        doc1.setRequestData(requestData1);
        doc1.setDataHandler(dataHandler1);
        doc1.setMimeType("animal/dog");

        RetrieveDocument requestData2 = new RetrieveDocument();
        requestData2.setDocumentUniqueId("doc2");
        requestData2.setHomeCommunityId("urn:oid:1.2.4");
        requestData2.setRepositoryUniqueId("repo2");

        DataHandler dataHandler2 = new DataHandler('Katz ' * 1500, "text/plain");
        RetrievedDocument doc2 = new RetrievedDocument();
        doc2.setRequestData(requestData2);
        doc2.setDataHandler(dataHandler2);
        doc2.setMimeType("animal/cat");

        RetrievedDocumentSet response = new RetrievedDocumentSet();
        response.getDocuments().add(doc1);
        response.getDocuments().add(doc2);
        response.setStatus(Status.SUCCESS);

        return response;
    }

}
