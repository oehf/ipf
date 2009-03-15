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
package org.openehealth.ipf.platform.camel.core.camel.exception;

import org.apache.camel.Processor;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.support.processor.ExceptionProcessor;


/**
 * @author Martin Krasser
 */
public class ExceptionHandlingRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {
        
        from("direct:input")
        // Enable the following statement if error handling
        // has been globally turned off via noErrorHandler(),
        // otherwise exception() statements have no effect.
        //.errorHandler(deadLetterChannel())
        .onException(Exception1.class).to("mock:error1").end()
        .onException(Exception2.class).to("mock:error2").end()
        .onException(Exception.class).to("mock:error3").end()
        .process(exceptionProcessor())
        .to("mock:success");        
    }
    
    private Processor exceptionProcessor() {
        ExceptionProcessor result = new ExceptionProcessor();
        result.getExceptions().put("blah", new Exception1());
        result.getExceptions().put("blub", new Exception2());
        result.getExceptions().put("oink", new Exception3());
        return result;
    }
}
