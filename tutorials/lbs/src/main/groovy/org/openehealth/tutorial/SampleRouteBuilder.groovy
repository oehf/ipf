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
package org.openehealth.tutorial

import javax.activation.DataHandler
import javax.activation.DataSource

import org.apache.camel.Exchange
import org.apache.camel.spring.SpringRouteBuilder
import org.apache.camel.component.cxf.CxfConstants
import org.apache.cxf.message.MessageContentsList

import org.openehealth.ipf.platform.camel.lbs.process.ResourceList


class SampleRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
        // When the jetty endpoint receives a message the route checks the header 
        // for the request method.
        // The request method in the header is used to find out if we have a POST 
        // or GET request.
        // Depending on the request, we route the message to a "direct" endpoint.
        from('jetty:http://localhost:8412/imagebin')
                .disableStreamCaching()              // tell Camel to not read the stream in memory
                .choice()   
                .when(header('http.requestMethod').isEqualTo('POST')).to('direct:upload')
                .when(header('http.requestMethod').isEqualTo('GET')).to('direct:download')
                .otherwise().end()
        
        // Deal with uploads
        from('direct:upload')
                .disableStreamCaching()              // tell Camel to not read the stream in memory
                .store().with('resourceHandlers')    // ensure we can upload large files
                .process { Exchange exchange ->
                    // Transform the message into the CXF format
                    def params = new MessageContentsList()                    
                    def resourceList = exchange.in.getBody(ResourceList.class) 
                    params[0] = new DataHandler(resourceList.get(0))
                    exchange.in.setHeader(CxfConstants.OPERATION_NAME, "upload")
                    exchange.in.body = params
                }
                .to('cxf:bean:imageBinServer')       // webservice.upload() call
                .process { Exchange exchange ->
                    // Transform the message back to HTTP
                    def params = exchange.in.getBody(MessageContentsList.class)
                    exchange.in.body = params[0]
                }
        
        // Deal with downloads
        from('direct:download')
                .disableStreamCaching()              // tell Camel to not read the stream in memory
                .process { Exchange exchange ->
                    // Transform the message into the CXF format
                    def params = new MessageContentsList()
                    params[0] = exchange.in.getHeader("handle")
                    exchange.in.setHeader(CxfConstants.OPERATION_NAME, "download")
                    exchange.in.body = params
                }
                .to('cxf:bean:imageBinServer')       // webservice.download() call
                .store().with('resourceHandlers')    // ensure we can download large files
                .process { Exchange exchange ->
                    // Transform the message back to HTTP
                    def params = exchange.in.getBody(MessageContentsList.class)
                    def resourceList = new ResourceList()
                    resourceList.add(params[0].dataSource)
                    exchange.in.body = resourceList
                }
    }    
}
