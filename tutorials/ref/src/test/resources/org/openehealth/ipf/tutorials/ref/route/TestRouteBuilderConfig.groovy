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
package org.openehealth.ipf.tutorials.ref.route

import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.builder.xml.Namespaces
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

/**
 * @author Martin Krasser
 */
class TransmogrifierRouteBuilderConfig implements RouteBuilderConfig {
    
    void apply(RouteBuilder builder) {

        Namespaces ns = new Namespaces('oehf', 'http://www.openehealth.org/tutorial');
        
        // ------------------------------------------------------------
        //  Global error handling strategy (still experimental)
        // ------------------------------------------------------------
        
        builder
            .onException(ValidationException.class)
            .to('mock:error-app')
        
        builder
            .onException(Exception.class)
            .to('mock:error-sys')

        // ------------------------------------------------------------
        //  Receive and validate order
        // ------------------------------------------------------------
        
        builder
            .from('direct:order').convertBodyTo(String.class)
            .validation('direct:validation')
            .to('seda:validated')
        
        builder
            .from('direct:validation').unhandled()
            // a tutorial-specific language extension
            .writeValidationResponse('message valid')
            .to('validator:order/order.xsd')
        
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        builder
            .from('seda:validated')
            .choice()
                .when()
                    .xpath('/oehf:order/oehf:category = \'animals\'', ns)
                    .to('direct:transform-animal-orders')
                .when()
                    .xpath('/oehf:order/oehf:category = \'books\'', ns)
                    .to('direct:transform-book-orders')
                .otherwise()
                    .to('direct:error-sys')
                    
         builder.from('direct:transform-animal-orders')
             .transmogrify('animalOrderTransformer')
             .to('mock:animals')
        
         builder.from('direct:transform-book-orders')
             .to('xslt:order/order.xslt')
             .to('mock:books')

    }
    
}
