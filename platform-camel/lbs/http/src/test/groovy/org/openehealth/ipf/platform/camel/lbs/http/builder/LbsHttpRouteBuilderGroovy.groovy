/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.platform.camel.lbs.http.builder

import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.net.URI

import javax.activation.DataHandler

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder

import org.openehealth.ipf.commons.lbs.resource.ResourceFactory
import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource
import org.openehealth.ipf.commons.lbs.resource.LargeBinaryStoreDataSource
import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore
import org.openehealth.ipf.platform.camel.lbs.http.process.ResourceList

/**
 * @author Jens Riemschneider
 * @author Martin Krasser
 */
class LbsHttpRouteBuilderGroovy extends SpringRouteBuilder {
    
    void configure() {
        
        errorHandler(defaultErrorHandler().maximumRedeliveries(0).redeliverDelay(0));

        // --------------------------------------------------------------
        //  LBS routes
        // --------------------------------------------------------------
        from('jetty:http://localhost:9452/lbstest_no_extract?disableStreamCache=true').noStreamCaching()
            .to('mock:mock')

        from('jetty:http://localhost:9452/lbstest_extract?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .to('mock:mock')

        from('jetty:http://localhost:9452/lbstest_ping?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .process { Exchange exchange ->
                def dataSource = exchange.in.getBody(ResourceDataSource.class)
                exchange.out.setBody(dataSource)
            }
            .to('mock:mock');
            
        from('jetty:http://localhost:9452/lbstest_extract_factory_via_bean?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .to('mock:mock')

        from('jetty:http://localhost:9452/lbstest_extract_router?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .setHeader('tag').constant('I was here')
            .fetch().with('resourceHandlers')
            .removeHeader(Exchange.HTTP_PATH)
            .removeHeader(Exchange.HTTP_URI)
            .to('http://localhost:9452/lbstest_receiver')

        from('direct:lbstest_send_only')
            .fetch().with('resourceHandlers')
            .to('http://localhost:9452/lbstest_receiver')
            
        from('direct:lbstest_non_http')
            .store().with('resourceHandlers')
            .to('mock:mock')
            
        from('jetty:http://localhost:9452/lbstest_receiver?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .to('mock:mock')
            
        from('jetty:http://localhost:9452/lbstest_jms?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .to('jms:temp:queue:lbstest')
            
        from('direct:lbstest_download')
            .to('http://localhost:9452/lbstest_download')
            .transform {
                def result = it.in.getBody(InputStream.class)
                org.openehealth.ipf.platform.camel.lbs.http.process.GroovyLbsHttpTest.getLength(result)
            }
            
        from('jetty:http://localhost:9452/lbstest_download?disableStreamCache=true').noStreamCaching()
            .transform().constant(new org.openehealth.ipf.platform.camel.lbs.http.process.GroovyLbsHttpTest.HugeContentInputStream())
            
        from('jms:temp:queue:lbstest')
            .to('mock:mock')
            
        // Example routes only tested with groovy
        from('jetty:http://localhost:9452/lbstest_example1?disableStreamCache=true').noStreamCaching()
            // Replace the message content with a data source
            .store().with('resourceHandlers') 
            // Custom processing to find a token
            .process { Exchange exchange ->
                // Get the stream from the data source and read it
                def inputStream = exchange.in.getBody(InputStream.class)                
                def reader = new BufferedReader(new InputStreamReader(inputStream))
                try {
                    def line = reader.readLine()
                    // Look for the token
                    while (line != null && !line.contains('blu')) {
                        line = reader.readLine()
                    }
                    // If found set the header
                    if (line != null) {
                        exchange.in.setHeader('tokenfound', 'yes')
                    }
                }
                finally {
                    reader.close()
                }
            }
            .to('mock:mock')
            
        from('jetty:http://localhost:9452/lbstest_example2?disableStreamCache=true').noStreamCaching()
            // Replace the message content with data sources
            .store().with('resourceHandlers')
            // Custom processing to look for text resources
            .process { Exchange exchange ->
                // Run through all resources and check the content type
                exchange.in.getBody(ResourceList.class).each {
                    if (it.contentType.startsWith('text/plain')) {
                        exchange.in.setHeader('textfound', 'yes')
                    }
                }
            }
            .to('mock:mock')
            
        from('jetty:http://localhost:9452/lbstest_example3?disableStreamCache=true').noStreamCaching()
            .store().with('resourceHandlers')
            .process { Exchange exchange ->
                // The resource factory can be used to create resources manually
                def resourceFactory = lookup('resourceFactory', ResourceFactory.class) 
                def inputStream = new ByteArrayInputStream('hello world'.bytes)
                
                // Using the unit of work from the original exchange we can ensure that the
                // resource is removed once the message has been processed by the route
                def resource = resourceFactory.createResource(exchange.unitOfWork.id, 'text/xml', null, 'hello', inputStream)
                def resourceList = exchange.in.getBody(ResourceList.class)
                resourceList.add(resource)
            }
            // Create a POST request with the resources
            .fetch().with('resourceHandlers')
            .removeHeader(Exchange.HTTP_PATH)
            .removeHeader(Exchange.HTTP_URI)
            .to('http://localhost:9452/lbstest_receiver')
    }    
}
