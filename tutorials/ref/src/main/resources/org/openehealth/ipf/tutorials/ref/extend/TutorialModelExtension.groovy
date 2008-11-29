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
package org.openehealth.ipf.tutorials.ref.extend

import static org.openehealth.ipf.tutorials.ref.util.Expressions.errorFilename
import static org.openehealth.ipf.tutorials.ref.util.Expressions.orderFilename
import static org.openehealth.ipf.tutorials.ref.util.Processors.responseCodeWriter
import static org.openehealth.ipf.tutorials.ref.util.Processors.responseMessageWriter

import static org.apache.camel.builder.Builder.*

import java.util.Map

import org.apache.camel.Exchange
import org.apache.camel.Message
import org.apache.camel.ValidationException
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.component.http.HttpProducer
import org.apache.camel.model.ProcessorType
import org.apache.camel.processor.DelegateProcessor

/**
 * @author Martin Krasser
 */
class TutorialModelExtension {

    RouteBuilder routeBuilder
    
    def extensions = {

        // ------------------------------------------------------------
        //  Exception handling extensions
        // ------------------------------------------------------------
        
        ProcessorType.metaClass.fail = {
            delegate.to('direct:error-app')
        }

        ProcessorType.metaClass.handled = {
             delegate
                 .onException(ValidationException.class)
                     .to('direct:error-app').end()
                 .onException(Exception.class)
                     .to('direct:error-sys').end()
        }

        ProcessorType.metaClass.handledDelivery = {
            delegate.errorHandler(routeBuilder
                    .deadLetterChannel('direct:error-sys')
                    .maximumRedeliveries(3))
        }
        
        ProcessorType.metaClass.handledReplay = {
            delegate.replayErrorHandler('direct:error-sys')
        }
        
        RouteBuilder.metaClass.handlers = {
            [ 'error-app', 
              'error-sys' ].each { name ->
                delegate
                    .from('direct:' + name).unhandled()
                    .writeErrorFile(name, 'txt')
                    .nakFlow()
            }
            
        }
        
        // ------------------------------------------------------------
        //  File writer extensions
        // ------------------------------------------------------------
        
        ProcessorType.metaClass.writeOrderFile = { String extension ->
            delegate
                .setHeader('org.apache.camel.file.name', orderFilename(extension))
                .to('file:order/output');
        }
        
        ProcessorType.metaClass.writeErrorFile = { String name, String extension ->
            delegate.inOnly()
                .setHeader('org.apache.camel.file.name', errorFilename(extension))
                .to('file:order/' + name);
        }
        
        // ------------------------------------------------------------
        //  Responder extensions
        // ------------------------------------------------------------

        /**
         * Interceptor that writes a default validation response given by 
         * defaultResponse argument. If the invocation of the next processor fails
         * then the exception message is set as response (i.e. out-message) body.
         */
        ProcessorType.metaClass.writeValidationResponse = { String defaultResponse ->
            delegate.intercept { exchange, next ->
                try {
                    next.proceed(exchange)
                } catch (Exception e) {
                    exchange.exception = e
                }
                exchange.out.body = exchange.failed ? exchange.exception.message : defaultResponse            
            }
        }
        
        /**
         * Interceptor that writes an HTTP 400 response code in case of a 
         * ValidationException and and HTTP 500 response code in case of any other
         * exception.
         */
        ProcessorType.metaClass.writeResponseCode = {
            delegate.intercept { exchange, next ->
                next.proceed(exchange)
                if (!exchange.exception) {
                    return
                }
                def out = exchange.getOut(false)
                if (!out) {
                    return
                }
                if (exchange.exception instanceof ValidationException) {
                    out.headers[HttpProducer.HTTP_RESPONSE_CODE] = 400
                } else {
                    out.headers[HttpProducer.HTTP_RESPONSE_CODE] = 500
                }
            }
        }

    }
    
}
