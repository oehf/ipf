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

import static org.apache.camel.component.cxf.CxfConstants.*
import static org.apache.camel.Exchange.*

import javax.activation.DataHandler
import javax.activation.DataSource

import org.apache.camel.spring.SpringRouteBuilder
import org.apache.cxf.message.MessageContentsList

import org.openehealth.ipf.platform.camel.lbs.http.process.ResourceList


class SampleRouteBuilder extends SpringRouteBuilder {
    
    void configure() {
        // The request method in the header is used to find out if we have a POST 
        // or GET request.
        // Depending on the request, we route the message to a "direct" endpoint.
        from('jetty:http://localhost:8412/imagebin')
            .choice()  
            .when(header(HTTP_METHOD).isEqualTo('POST')).to('direct:upload')
            .when(header(HTTP_METHOD).isEqualTo('GET')).to('direct:download')
            .otherwise().end()
        
        // Handle uploads
        from('direct:upload')
            .store().with('resourceHandlers')       // ensure we can upload large files                
            .transform {                            // transform the message into a CXF call
                it.in.headers = [(OPERATION_NAME): 'upload']  // operation
                [new DataHandler(it.in.body[0])]              // parameters
            }
            .to('cxf:bean:imageBinServer')          // webservice.upload() call
            .transform { it.in.body[0] }            // back to http using result param 0
        
        // Handle downloads
        from('direct:download')                
            .transform {                            // transform the message into a CXF call 
                it.out.headers = [(OPERATION_NAME): 'download']  // operation
                [it.in.headers.handle]                           // parameters
            }
            .to('cxf:bean:imageBinServer')          // webservice.download() call
            .store().with('resourceHandlers')       // ensure we can download large files
            .transform { it.in.body[0].dataSource } // back to http using data source in param 0
    }    
}
