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

import static org.openehealth.ipf.platform.camel.ihe.mllp.core.ContinuationUtils.keyString;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.isEmpty;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;


/**
 * Consumer-side interceptor for receiving unsolicited request fragments
 * as described in § 2.10.2.2 of the HL7 v.2.5 specification.
 * @author Dmytro Rud
 */
public class ConsumerRequestDefragmenterInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerRequestDefragmenterInterceptor.class);

    // keys consist of: continuation pointer, MSH-3-1, MSH-3-2, and MSH-3-3  
    private static final Map<String, StringBuilder> accumulators = 
        Collections.synchronizedMap(new HashMap<String, StringBuilder>());

    
    public ConsumerRequestDefragmenterInterceptor(MllpEndpoint endpoint, Processor wrappedProcessor) {
        super(endpoint, wrappedProcessor);
    }


    /**
     * Accumulates fragments and passes the "big" message to the processing route. 
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        String requestString = exchange.getIn().getBody(String.class);
        Parser parser = getMllpEndpoint().getParser();
        Message requestMessage = parser.parse(requestString);
        Terser requestTerser = new Terser(requestMessage);
        String msh14 = requestTerser.get("MSH-14");
        String dsc1 = "I".equals(requestTerser.get("DSC-2")) ? null : requestTerser.get("DSC-1"); 
        
        // pass when the message is not fragmented
        if (isEmpty(msh14) && isEmpty(dsc1)) {
            getWrappedProcessor().process(exchange);
            return;
        }

        // get pieces of the accumulator's key
        String msh31 = requestTerser.get("MSH-3-1");
        String msh32 = requestTerser.get("MSH-3-2");
        String msh33 = requestTerser.get("MSH-3-3");

        // create the accumulator (in the case of first fragment) 
        // or get an existing one (for fragments 2..n)
        StringBuilder accumulator; 
        if (isEmpty(msh14)) {
            accumulator = new StringBuilder();
        } else {
            accumulator = accumulators.remove(keyString(msh14, msh31, msh32, msh33));
            if (accumulator == null) {
                throw new RuntimeException("Got unwanted non-interactive fragment " + msh14);
            }
        }

        /*
        // handle cross-message segment fragmentation
        if (getMllpEndpoint().isSupportSegmentFragmentation() 
                && "\rADD\r".equals(accumulator.substring(accumulator.length() - 5))) 
        {
            accumulator.setLength(accumulator.length() - 6);
        }
        */
        
        // append the current fragment to the accumulator
        int beginIndex = isEmpty(msh14) ? 0 : requestString.indexOf('\r');
        int endIndex = isEmpty(dsc1) ? requestString.length() : requestString.indexOf("\rDSC") + 1;
        accumulator.append(requestString, beginIndex, endIndex);
        
        // if DSC-1 empty -- finish accumulation, put message to the marshaller
        if (isEmpty(dsc1)) {
            LOG.debug("Finished fragment chain " + msh14);
            exchange.getIn().setBody(accumulator.toString());
            getWrappedProcessor().process(exchange);
            return;
        } 

        // if DSC-1 not empty -- update accumulators map, request the next fragment
        LOG.debug(new StringBuilder()
            .append("Processed fragment ")
            .append(msh14)
            .append(", requesting ")
            .append(dsc1)
            .toString());
            
        accumulators.put(keyString(dsc1, msh31, msh32, msh33), accumulator);
        Message ack = (Message) MessageUtils.response(
                getMllpEndpoint().getParser().getFactory(), 
                requestMessage, "ACK", 
                requestTerser.get("MSH-9-2"));
        Terser t1 = new Terser(ack);
        t1.set("MSA-1", "CA");
        t1.set("MSA-2", requestTerser.get("MSH-10"));
        Exchanges.resultMessage(exchange).setBody(ack);
        
        /*
         * Unhandled errors in this method:
         *    1. Multiple fragments with the same MSH-14
         *    2. Mixed interactive + non-interactive styles
         *    3. Timeout
         */
    }
    
}
