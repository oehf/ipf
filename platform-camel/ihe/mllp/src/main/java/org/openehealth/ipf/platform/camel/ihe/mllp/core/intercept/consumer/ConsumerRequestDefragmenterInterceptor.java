/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v2.storage.UnsolicitedFragmentationStorage;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.keyString;


/**
 * Consumer-side interceptor for receiving unsolicited request fragments
 * as described in paragraph 2.10.2.2 of the HL7 v.2.5 specification.
 * 
 * @author Dmytro Rud
 */
public class ConsumerRequestDefragmenterInterceptor extends InterceptorSupport {
    private static final Logger log = LoggerFactory.getLogger(ConsumerRequestDefragmenterInterceptor.class);
    
    // keys consist of: continuation pointer, MSH-3-1, MSH-3-2, and MSH-3-3  
    private UnsolicitedFragmentationStorage storage;


    @Override
    public void setEndpoint(Endpoint endpoint) {
        super.setEndpoint(endpoint);
        this.storage = getEndpoint(MllpTransactionEndpoint.class).getUnsolicitedFragmentationStorage();
        requireNonNull(storage);
    }

    /**
     * Accumulates fragments and passes the "big" message to the processing route. 
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        var requestString = exchange.getIn().getBody(String.class);
        var parser = getEndpoint(MllpTransactionEndpoint.class).getHl7v2TransactionConfiguration().getParser();
        var requestMessage = parser.parse(requestString);
        var requestTerser = new Terser(requestMessage);
        var msh14 = requestTerser.get("MSH-14");
        String dsc1 = null;
        try {
            if (! "I".equals(requestTerser.get("DSC-2"))) {
                dsc1 = requestTerser.get("DSC-1");
            }
        } catch (HL7Exception e) {
            // segment DSC does not exist in cancel requests
        }

        // pass when the message is not fragmented
        if (isEmpty(msh14) && isEmpty(dsc1)) {
            getWrappedProcessor().process(exchange);
            return;
        }

        // get pieces of the accumulator's key
        var msh31 = requestTerser.get("MSH-3-1");
        var msh32 = requestTerser.get("MSH-3-2");
        var msh33 = requestTerser.get("MSH-3-3");

        // create an accumulator (on the arrival of the first fragment) 
        // or get an existing one (on the arrival of fragments 2..n)
        StringBuilder accumulator; 
        if (isEmpty(msh14)) {
            accumulator = new StringBuilder();
        } else {
            accumulator = storage.getAndRemove(keyString(msh14, msh31, msh32, msh33));
            if (accumulator == null) {
                log.warn("Pass unknown fragment with MSH-14=={} to the route", msh14);
                getWrappedProcessor().process(exchange);
                return;
            }
        }

        // append current fragment to the accumulator
        var beginIndex = isEmpty(msh14) ? 0 : requestString.indexOf('\r');
        var endIndex = isEmpty(dsc1) ? requestString.length() : (requestString.indexOf("\rDSC") + 1);
        accumulator.append(requestString, beginIndex, endIndex);
        
        // DSC-1 is empty -- finish accumulation, pass message to the marshaller
        if (isEmpty(dsc1)) {
            log.debug("Finished fragment chain {}", msh14);
            exchange.getIn().setBody(accumulator.toString());
            getWrappedProcessor().process(exchange);
            return;
        } 

        // DSC-1 is not empty -- update accumulators map, request the next fragment
        log.debug("Processed fragment {} requesting {}", msh14, dsc1);
            
        storage.put(keyString(dsc1, msh31, msh32, msh33), accumulator);
        var ack = MessageUtils.response(
                requestMessage, "ACK", 
                requestTerser.get("MSH-9-2"));
        var ackTerser = new Terser(ack);
        ackTerser.set("MSA-1", "CA");
        ackTerser.set("MSA-2", requestTerser.get("MSH-10"));
        exchange.getMessage().setBody(parser.encode(ack));
    }
    
}
