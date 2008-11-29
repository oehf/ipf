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

import static org.openehealth.ipf.tutorials.ref.util.Expressions.errorFilename;
import static org.openehealth.ipf.tutorials.ref.util.Expressions.orderFilename;
import static org.openehealth.ipf.tutorials.ref.util.Processors.responseCodeWriter;
import static org.openehealth.ipf.tutorials.ref.util.Processors.responseMessageWriter;

import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.builder.xml.Namespaces
import org.apache.camel.model.ProcessorType;
import org.openehealth.ipf.platform.camel.core.builder.RouteBuilderConfig


/**
 * @author Martin Krasser
 */
class TutorialRouteBuilderConfig implements RouteBuilderConfig {

    int httpPort
    
    void apply(RouteBuilder builder) {
        
        def ns = new Namespaces('oehf', 'http://www.openehealth.org/tutorial');
        
        // ------------------------------------------------------------
        //  Receive order
        // ------------------------------------------------------------

        builder.from('jetty:http://localhost:' + httpPort + '/tutorial').handled()
            .writeResponseCode()
            .initFlow('http')
                .handledReplay()
                .application('tutorial')
                .inType(String.class)
                .outType(String.class)
                .outConversion(false)
            .to('direct:received')
        
        builder.from('file:order/input?delete=true').handled()
            .initFlow('file')
                .handledReplay()
                .application('tutorial')
                .inType(String.class)
                .outType(String.class)
                .outConversion(false)
            .to('direct:received')
        
        builder.from('direct:order').handled()
            .initFlow('direct')
                .application('tutorial')
                .outType(String.class)
            .to('direct:received')
        
        // ------------------------------------------------------------
        //  Validate order
        // ------------------------------------------------------------
        
        builder.from('direct:received').handled()
            .convertBodyTo(String.class)
            .validation('direct:validation')
            .to('jms:queue:validated')
        
        builder.from('direct:validation').unhandled()
            .writeValidationResponse('message valid')
            .to('validator:order/order.xsd')
        
        
        // ------------------------------------------------------------
        //  Process order
        // ------------------------------------------------------------
        
        // Any exception causes a redirect to an error endpoint and a rollback
        // on the transacted JMS session. The JMS provider is configured not to
        // do redeliveries on rollback.
        
        builder.from('jms:queue:validated').handled()
            .choice()
                .when()
                    .xpath('/oehf:order/oehf:category = \'animals\'', ns)
                    .to('direct:transform-animal-orders')
                .when()
                    .xpath('/oehf:order/oehf:category = \'books\'', ns)
                    .to('direct:transform-book-orders')
                .otherwise()
                    .fail()
                    
         builder.from('direct:transform-animal-orders')
             .transmogrify('animalOrderTransformer')
             .to('jms:queue:transformed-animals')
        
         builder.from('direct:transform-book-orders')
             .to('xslt:order/order.xslt')
             .to('jms:queue:transformed-books')

        // ------------------------------------------------------------
        //  Deliver order
        // ------------------------------------------------------------
        
        // The error handler installed here tries to make max. 3 redeliveries.
        // If these fail the transacted JMS session is rolled back (without a
        // redelivery on JMS-level)
        
        builder.from('jms:queue:transformed-animals').handledDelivery()
            .dedupeFlow()
            .writeOrderFile('txt') // TODO: send via http
            .ackFlow()
        
        builder.from('jms:queue:transformed-books').unhandled()
            .dedupeFlow()
            .writeOrderFile('xml')
            .ackFlow()

        // ------------------------------------------------------------
        //  Install error handling routes
        // ------------------------------------------------------------
        
        builder.handlers()
        
    }

}
