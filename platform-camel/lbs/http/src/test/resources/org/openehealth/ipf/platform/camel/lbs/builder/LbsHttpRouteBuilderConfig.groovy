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
package org.openehealth.ipf.platform.camel.lbs.extend

import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.net.URI

import javax.activation.DataHandler

import org.apache.camel.Exchange

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.openehealth.ipf.commons.lbs.resource.ResourceFactory

import org.openehealth.ipf.commons.lbs.resource.ResourceDataSource
import org.openehealth.ipf.platform.camel.lbs.process.ResourceList

import org.apache.camel.builder.RouteBuilder


import org.openehealth.ipf.commons.lbs.store.LargeBinaryStore
import org.openehealth.ipf.commons.lbs.resource.LargeBinaryStoreDataSource

/**
 * @author Jens Riemschneider
 */
class LbsHttpRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        
        builder.errorHandler(builder.deadLetterChannel().maximumRedeliveries(2).initialRedeliveryDelay(0));

        // --------------------------------------------------------------
        //  LBS routes
        // --------------------------------------------------------------
        builder.from('jetty:http://localhost:9452/lbstest_no_extract')
            .to('mock:mock')

        builder.from('jetty:http://localhost:9452/lbstest_extract')
            .store().with('resourceHandlers')
            .to('mock:mock')

        builder.from('jetty:http://localhost:9452/lbstest_ping')
            .store().with('resourceHandlers')
            .process { Exchange exchange ->
                def dataSource = exchange.in.getBody(ResourceDataSource.class)
                exchange.out.setBody(dataSource)
            }
            .to('mock:mock');
            
        builder.from('jetty:http://localhost:9452/lbstest_extract_factory_via_bean')
            .store().with('resourceHandlers')
            .to('mock:mock')

        builder.from('jetty:http://localhost:9452/lbstest_extract_router')
            .store().with('resourceHandlers')
            .setHeader('tag').constant('I was here')
            .fetch().with('resourceHandlers')
            .to('http://localhost:9452/lbstest_receiver')

        builder.from('direct:lbstest_send_only')
            .fetch().with('resourceHandlers')
            .to('http://localhost:9452/lbstest_receiver')
            
        builder.from('direct:lbstest_non_http')
            .store().with('resourceHandlers')
            .to('mock:mock')
            
        builder.from('jetty:http://localhost:9452/lbstest_receiver')
            .store().with('resourceHandlers')
            .to('mock:mock')
            
        builder.from('jetty:http://localhost:9452/lbstest_jms')
            .store().with('resourceHandlers')
            .process { Exchange exchange ->
                def resourceList = exchange.in.getBody(ResourceList.class)
                def dataSource = resourceList.get(0)
                exchange.in.setHeader('resourceUri', dataSource.resourceUri.toString())
                exchange.in.setHeader('name', dataSource.id)
                exchange.in.setHeader('contentType', dataSource.contentType)
                exchange.in.body = ''
            }
            .to('jms:temp:queue:lbstest')
            
        builder.from('jms:temp:queue:lbstest')
            .process { Exchange exchange ->
                def resourceUri = exchange.in.getHeader('resourceUri')
                def name = exchange.in.getHeader('name')
                def contentType = exchange.in.getHeader('contentType')
                def resourceFactory = builder.bean(ResourceFactory.class, 'resourceFactory')
                def store = builder.bean(LargeBinaryStore.class, 'largeBinaryStore')
                println("in: " + resourceUri + ", " + name + ", " + contentType)
                def largeBinaryDataSource = new LargeBinaryStoreDataSource(store, URI.create(resourceUri), contentType, name)
                def dataSource = new ResourceDataSource(name, largeBinaryDataSource)
                def resourceList = new ResourceList()
                resourceList.add(dataSource)
                exchange.in.body = resourceList
            }
            .to('mock:mock')
            
        // Example routes only tested with groovy
        builder.from('jetty:http://localhost:9452/lbstest_example1')
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
            
        builder.from('jetty:http://localhost:9452/lbstest_example2')
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
            
        builder.from('jetty:http://localhost:9452/lbstest_example3')
            .store().with('resourceHandlers')
            .process { Exchange exchange ->
                // The resource factory can be used to create resources manually
                def resourceFactory = builder.bean(ResourceFactory.class, 'resourceFactory') 
                def inputStream = new ByteArrayInputStream('hello world'.bytes)
                
                // Using the unit of work from the original exchange we can ensure that the
                // resource is removed once the message has been processed by the route
                def resource = resourceFactory.createResource(exchange.unitOfWork.id, 'text/xml', null, 'hello', inputStream)
                def resourceList = exchange.in.getBody(ResourceList.class)
                resourceList.add(resource)
                println(resourceList)
            }
            // Create a POST request with the resources
            .fetch().with('resourceHandlers')
            .to('http://localhost:9452/lbstest_receiver')
    }    
}
