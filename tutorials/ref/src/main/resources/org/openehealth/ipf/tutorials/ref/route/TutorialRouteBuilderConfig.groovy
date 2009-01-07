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

import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig

/**
 * @author Martin Krasser
 */
class TutorialRouteBuilderConfig implements RouteBuilderConfig {

    private String appErrorUri = 'direct:error-app'
    private String sysErrorUri = 'direct:error-sys'
        
    int httpPort
    
    void apply(RouteBuilder builder) {
                
        // ------------------------------------------------------------
        //  Global error handling
        // ------------------------------------------------------------
        
        builder
            .onException(Exception.class)
                .transform().exceptionMessage()
                .responseCode().constant(500)
                .to(sysErrorUri)
                
        // ------------------------------------------------------------
        //  Receive order
        // ------------------------------------------------------------

        builder.from('jetty:http://localhost:' + httpPort + '/tutorial')
            .initFlow('http', sysErrorUri)
            .to('direct:received')
        
        builder.from('file:order/input?delete=true')
            .initFlow('file', sysErrorUri)
            .to('direct:received')
        
        // ------------------------------------------------------------
        //  Validate order
        // ------------------------------------------------------------
        
        builder.from('direct:received')
            .convertBodyTo(String.class)
            .validation('direct:validation')
            .to('jms:queue:validated')
        
        builder.from('direct:validation')
            .onException(ValidationException.class)
                .transform().exceptionMessage()
                .responseCode().constant(400)
                .to(appErrorUri).end()
            .to('validator:order/order.xsd')
            .transmogrify {'message valid'}
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        builder.from('jms:queue:validated')
            .unmarshal().gnode(false)
            .choice()
                .when { it.in.body.category.text() == 'animals' }
                    .to('direct:transform-animal-orders')
                .when { it.in.body.category.text() == 'books' }
                    .to('direct:transform-book-orders')
                .otherwise()
                    .to('direct:error-sys')
                    
         builder.from('direct:transform-animal-orders')
             .transmogrify('animalOrderTransformer')
             .to('jms:queue:transformed-animals')
        
         builder.from('direct:transform-book-orders')
             .transmogrify('bookOrderTransformer')
             .params().builder()
             .to('jms:queue:transformed-books')

        // ------------------------------------------------------------
        //  Deliver order
        // ------------------------------------------------------------
        
        // We use a different JMS component here (jmsDeliver) that has special
        // redelivery settings (3 redelivery attempts). This is useful when
        // sending the transformation results over HTTP, for example (not done
        // here).
        
        builder.from('jmsDeliver:queue:transformed-animals')
            .dedupeFlow()
            .toFile('order/out', 'order', 'txt')
            .ackFlow()
            .renderer('defaultRenderer')
            
        
        builder.from('jmsDeliver:queue:transformed-books')
            .dedupeFlow()
            .toFile('order/out', 'order', 'xml')
            .ackFlow()
            .renderer('defaultRenderer')

        // ------------------------------------------------------------
        //  Error handling routes
        // ------------------------------------------------------------
            
        builder.from(sysErrorUri)
            .toFile('order/err/sys', 'error', 'txt')
            .nakFlow()
            .renderer('defaultRenderer')
        
        builder.from(appErrorUri)
            .toFile('order/err/app', 'error', 'txt')
            .nakFlow()
            .renderer('defaultRenderer')
            
    }

}
