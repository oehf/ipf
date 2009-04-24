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

import static org.openehealth.ipf.platform.camel.core.util.Expressions.builderExpression;
import static org.openehealth.ipf.platform.camel.core.util.Expressions.exceptionMessageExpression;

import org.apache.camel.ValidationException;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.adapter.ProcessorAdapter;
import org.openehealth.ipf.platform.camel.core.builder.RouteHelper;
import org.openehealth.ipf.platform.camel.core.dataformat.GnodeDataFormat;

/**
 * @author Martin Krasser
 */
public class TestRouteBuilderJava extends SpringRouteBuilder {

    private RouteHelper coreHelper;
    
    public TestRouteBuilderJava() {
        super();
        this.coreHelper = new RouteHelper(this);
    }
    
    @Override
    public void configure() throws Exception {
        
        Namespaces ns = new Namespaces("oehf", "http://www.openehealth.org/tutorial");
        DataFormat df = new GnodeDataFormat(false);
        
        ProcessorAdapter orderTransmogrifier = coreHelper.transmogrifier("animalOrderTransformer");
        ProcessorAdapter bookTransmogrifier = coreHelper.transmogrifier("bookOrderTransformer").params(builderExpression());
        
        // ------------------------------------------------------------
        //  Global error handling strategy
        // ------------------------------------------------------------
        
        onException(Exception.class)
            .transform(exceptionMessageExpression()).to("mock:error-sys");
        
        // ------------------------------------------------------------
        //  Receive and validate order
        // ------------------------------------------------------------
        
        from("direct:order")
            .convertBodyTo(String.class)
            .intercept(coreHelper.validation("direct:validation"))
            .to("seda:validated");
        
        from("direct:validation")
            .onException(ValidationException.class)
                .transform(exceptionMessageExpression()).to("mock:error-app").end()
            .to("validator:order/order.xsd")
            .transform(constant("message valid"));
        
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        from("seda:validated")
        .choice()
            .when()
                .xpath("/oehf:order/oehf:category = 'animals'", ns)
                .unmarshal(df)
                .process(orderTransmogrifier)
                .to("mock:animals")
            .when()
                .xpath("/oehf:order/oehf:category = 'books'", ns)
                .unmarshal(df)
                .process(bookTransmogrifier)
                .to("mock:books")
            .otherwise()
                .to("direct:error-sys");

    }

}
