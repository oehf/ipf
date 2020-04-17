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
package org.openehealth.ipf.platform.camel.core.camel.transaction;

import org.apache.camel.spring.SpringRouteBuilder;
import org.openehealth.ipf.platform.camel.core.support.processor.FailureProcessor;

/**
 * @author Martin Krasser
 */
public class TransactionalMessagingRouteBuilder extends SpringRouteBuilder {

    @Override
    public void configure() throws Exception {

        /*
         * We use two JMS components here:
         * 
         * - amqProcess for transactional message processing between JMS queues
         * - amqDelivery for transactional delivery of messages to destinations
         * 
         * Transacted delivery is useful if destination system are unavailable
         * or a message transport error occured. In this case the JMS provider
         * should be configured to attempt a redelivery (1 redelivery attempt 
         * in our case). We don't want to have redeliveries for normal message 
         * processing between JMS queues. Therefore the amqProcess JMS
         * component is configured to do no redelivery at all.
         * 
         * - Configuration of the amqProcess JMS component is done in 
         *   context-transaction-process.xml
         * - Configuration of the amqDelivery JMS component is done in 
         *   context-transaction-delivery.xml
         */
        

        // -----------------------------------------------------------------
        // Read from transacted input JMS queue and multicast to internal
        // destinations
        // -----------------------------------------------------------------
        
        from("amqProcess:queue:txm-input")
        // on exception rollback transaction and send to txm-error
        .onException(Exception.class).to("mock:txm-error").end()
        .multicast()
        .to("direct:intern-1")
        .to("direct:intern-2");
        
        // -----------------------------------------------------------------
        // Read from first internal endpoint and route to output JMS queue 1
        // without further message processing.
        // -----------------------------------------------------------------
        
        from("direct:intern-1")
        .to("amqProcess:queue:txm-output-1");
        
        // -----------------------------------------------------------------
        // Read from second internal endpoint and route to output JMS queue 2
        // and throw an exception if body equals "blah". If an exception is
        // thrown the transaction is rolled back which is
        //
        // - reading from input queue
        // - writing to output queue 1
        //
        // If no exception is thrown then the transaction is committed which
        // is
        //
        // - reading from input queue
        // - writing to output queue 1
        // - writing to output queue 2
        //
        // WHAT WE ACHIEVED HERE IS THAT WE READ AND WROTE MESSAGES FROM
        // AND TO SEVERAL DESTINATIONS IN A SINGLE TRANSACTION WITHOUT
        // USING XA!
        // 
        // -----------------------------------------------------------------
        
        from("direct:intern-2")
        // the noErrorHandler() can be omitted here if an explicit
        // transactionErrorHandler(...) is configured for endpoint
        // amqProcess:queue:txm-input
        .errorHandler(noErrorHandler())
        // throw exception if body equals "blah"
        .process(new FailureProcessor("blah"))
        // otherwise send to output queue 2
        .to("amqProcess:queue:txm-output-2"); 

        
        // -----------------------------------------------------------------
        // Read from transacted output queue and throw exception if body
        // equals "blub". The amqDelivery JMS component is configured to
        // attempt a single redelivery on rollback before giving up. 
        // without further message processing. 
        // -----------------------------------------------------------------
        
        from("amqDelivery:queue:txm-output-1")
        // on exception rollback transaction and send to txm-error
        .onException(Exception.class).to("mock:txm-error").end()
        // throw exception if body equals "blub"
        .process(new FailureProcessor("blub"))
        .to("mock:txm-mock");
        
        // -----------------------------------------------------------------
        // Read from transacted output queue and send to txm-mock.
        // -----------------------------------------------------------------
        
        from("amqDelivery:queue:txm-output-2")
        .to("mock:txm-mock");
        
    }

}
