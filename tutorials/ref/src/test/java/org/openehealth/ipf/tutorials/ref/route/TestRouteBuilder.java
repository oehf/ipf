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
package org.openehealth.ipf.tutorials.ref.route;

import static org.openehealth.ipf.tutorials.ref.util.Processors.responseMessageWriter;

import org.apache.camel.ValidationException;
import org.apache.camel.builder.xml.Namespaces;
import org.openehealth.ipf.platform.camel.flow.builder.RouteBuilder;

/**
 * @author Martin Krasser
 */
public class TestRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        Namespaces ns = new Namespaces("oehf", "http://www.openehealth.org/tutorial");
        
        // ------------------------------------------------------------
        //  Global error handling strategy (still experimental)
        // ------------------------------------------------------------
        
        onException(ValidationException.class)
            .to("mock:error-app");
        onException(Exception.class)
            .to("mock:error-sys");
        
        // ------------------------------------------------------------
        //  Receive and validate order
        // ------------------------------------------------------------
        
        from("direct:order")
            .convertBodyTo(String.class)
            .intercept(validation("direct:validation"))
            .to("seda:validated");
        
        from("direct:validation")
            .errorHandler(noErrorHandler())
            .intercept(responseMessageWriter())
            .to("validator:order/order.xsd");
        
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        from("seda:validated")
        .choice()
            .when()
                .xpath("/oehf:order/oehf:category = 'animals'", ns)
                .process(transmogrifier("animalOrderTransformer"))
                .to("mock:animals")
            .when()
                .xpath("/oehf:order/oehf:category = 'books'", ns)
                .to("xslt:order/order.xslt")
                .to("mock:books")
            .otherwise()
                .to("direct:error-sys");
        

    }

}
