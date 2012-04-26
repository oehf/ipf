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

import org.apache.camel.spring.SpringRouteBuilder

/**
 * @author Martin Krasser
 */
class TutorialRouteBuilder extends SpringRouteBuilder {

    private String appErrorUri = 'direct:error-app'
    private String sysErrorUri = 'direct:error-sys'
        
    int httpPort
    
    void configure() {
        // ------------------------------------------------------------
        //  Global error handling
        // ------------------------------------------------------------
        
        onException(Exception.class)
            .transform().exceptionMessage()
            .responseCode().constant(500)
            .to(sysErrorUri)
                
        // ------------------------------------------------------------
        //  Receive order
        // ------------------------------------------------------------

        from('jetty:http://localhost:' + httpPort + '/tutorial')
            .initFlow('http', sysErrorUri)
            .to('direct:received')
        
        from('file:order/input?delete=true')
            .initFlow('file', sysErrorUri)
            .to('direct:received')
        
        // ------------------------------------------------------------
        //  Validate order
        // ------------------------------------------------------------
            
        //  The 'validation' DSL intercepts the route, and returns as output 
        //  the output of the 'direct:validation' route.
        //  More details in class ValidationDefinition
        from('direct:received')
            .convertBodyTo(String.class)
            .validation('direct:validation')
            .to('jms:queue:validated')
        
        from('direct:validation')
            .onException(ValidationException.class)
                .handled(true)
                .transform().exceptionMessage()
                .responseCode().constant(400)
                .to(appErrorUri)
                .end()
            .to('validator:order/order.xsd')
            .transmogrify {'message valid'}
           
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        from('jms:queue:validated')
            .setBody(constant())
            .unmarshal().gnode(false)
            .choice()
                .when { it.in.body.category.text() == 'animals' }
                    .to('direct:transform-animal-orders')
                .when { it.in.body.category.text() == 'books' }
                    .to('direct:transform-book-orders')
                .otherwise()
                    .to('direct:error-sys')
                    
         from('direct:transform-animal-orders')
             .transmogrify('animalOrderTransformer')
             .to('jms:queue:transformed-animals')
        
         from('direct:transform-book-orders')
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
        
        from('jmsDeliver:queue:transformed-animals')
            .dedupeFlow()
            .toFile('order/out', 'order', 'txt')
            .ackFlow()
        
        from('jmsDeliver:queue:transformed-books')
            .dedupeFlow()
            .toFile('order/out', 'order', 'xml')
            .ackFlow()

        // ------------------------------------------------------------
        //  Error handling routes
        // ------------------------------------------------------------
            
        from(sysErrorUri)
            .toFile('order/err/sys', 'error', 'txt')
            .nakFlow()
        
        from(appErrorUri)
            .toFile('order/err/app', 'error', 'txt')
            .nakFlow()
            
    }

}
