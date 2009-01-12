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

import javax.xml.ws.Holder;

import javax.activation.DataHandler

import org.apache.camel.Exchange
import org.apache.cxf.message.MessageContentsList
import org.apache.camel.component.cxf.CxfConstants

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

import org.openehealth.ipf.commons.lbs.attachment.AttachmentFactory

import org.openehealth.ipf.platform.camel.lbs.process.cxf.AbstractLbsCxfTest

import org.apache.camel.builder.RouteBuilder

/**
 * @author Jens Riemschneider
 */
class LbsCxfRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {
        
        builder.errorHandler(builder.noErrorHandler())
        
        builder.from('cxf:bean:soapEndpointNoExtract') 
            .to('bean:serviceBean?methodName=processSOAP')
        
        builder.from('cxf:bean:soapEndpointExtract?dataFormat=POJO')
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .store().with('attachmentHandlers')
            .to('bean:serviceBean?methodName=processSOAP')
            .store().with('attachmentHandlers')
        
        builder.from('cxf:bean:soapEndpointExtractRouter?dataFormat=POJO')
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .store().with('attachmentHandlers')
            .to('cxf:bean:soapEndpointExtract?dataFormat=POJO')
            .store().with('attachmentHandlers')

        builder.from('cxf:bean:soapEndpointExtractRouterRealServer?dataFormat=POJO')
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .store().with('attachmentHandlers')
            .to('cxf:bean:soapEndpointRealServer?dataFormat=POJO')
            .store().with('attachmentHandlers')
        
        builder.from('direct:cxflbs')
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .store().with('attachmentHandlers')
            .to('mock:mock')
            .store().with('attachmentHandlers')

        builder.from('cxf:bean:soapEndpointExtractSwA?dataFormat=POJO')
            .intercept(new AbstractLbsCxfTest.CheckOutputDataSource())
            .store().with('attachmentHandlers')
            .to("bean:serviceBean?methodName=processSOAP")
            .store().with('attachmentHandlers')
            
        builder.from('cxf:bean:soapEndpointExample1')
            // Store the binaries of the operation paremeters
            .store().with('attachmentHandlers')
            // Custom processing to find a token in a binary
            .process { Exchange exchange ->
                // Operation parameters are contained in a list
                def params = exchange.in.getBody(List.class)
                // In this example, the parameter with index 1 contains a binary
                def inputStream = params.get(1).value.inputStream
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
            .to('bean:serviceBean?methodName=processSOAP')

        builder.from('direct:start')
            // Custom processor to manually create a SOAP call
            .process { Exchange exchange ->
                // The attachment factory can be used to create attachments manually
                def attachmentFactory = builder.bean(AttachmentFactory.class, 'attachmentFactory') 
                def inputStream1 = new ByteArrayInputStream('hello world'.bytes)
            
                // Using the unit of work from the original exchange we can ensure that the
                // attachment is removed once the message has been processed by the route
                def attachment1 = attachmentFactory.createAttachment(exchange.unitOfWork.id, 'text/plain', null, null, inputStream1)
                def inputStream2 = new ByteArrayInputStream('this is me'.bytes)
                def attachment2 = attachmentFactory.createAttachment(exchange.unitOfWork.id, 'text/plain', null, null, inputStream2)
                
                // The list of parameters for the operation call
                def params = new MessageContentsList()                
                params.set(0, new Holder<String>('Hello world'))
                params.set(1, new Holder<DataHandler>(new DataHandler(attachment1)))
                params.set(2, new DataHandler(attachment2))
                
                // postMe is the operation being called
                exchange.in.setHeader(CxfConstants.OPERATION_NAME, "postMe")
                exchange.in.body = params
            }
            .to('cxf:bean:soapEndpointExtract')
    }    
}
