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

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.keyString;

import ca.uhn.hl7v2.HL7Exception;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.UnsolicitedFragmentationStorage;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;


/**
 * Consumer-side interceptor for receiving unsolicited request fragments
 * as described in paragraph 2.10.2.2 of the HL7 v.2.5 specification.
 * 
 * @author Dmytro Rud
 */
public class ConsumerRequestDefragmenterInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerRequestDefragmenterInterceptor.class);
    
    // keys consist of: continuation pointer, MSH-3-1, MSH-3-2, and MSH-3-3  
    private UnsolicitedFragmentationStorage storage;
    

    @Override
    public void setConfigurationHolder(Hl7v2ConfigurationHolder configurationHolder) {
        super.setConfigurationHolder(configurationHolder);
        this.storage = getMllpEndpoint().getUnsolicitedFragmentationStorage();
        Validate.notNull(storage);
    }


    /**
     * Accumulates fragments and passes the "big" message to the processing route. 
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        String requestString = exchange.getIn().getBody(String.class);
        Parser parser = getHl7v2TransactionConfiguration().getParser();
        Message requestMessage = parser.parse(requestString);
        Terser requestTerser = new Terser(requestMessage);
        String msh14 = requestTerser.get("MSH-14");
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
        String msh31 = requestTerser.get("MSH-3-1");
        String msh32 = requestTerser.get("MSH-3-2");
        String msh33 = requestTerser.get("MSH-3-3");

        // create an accumulator (on the arrival of the first fragment) 
        // or get an existing one (on the arrival of fragments 2..n)
        StringBuilder accumulator; 
        if (isEmpty(msh14)) {
            accumulator = new StringBuilder();
        } else {
            accumulator = storage.getAndRemove(keyString(msh14, msh31, msh32, msh33));
            if (accumulator == null) {
                LOG.warn("Pass unknown fragment with MSH-14==" + msh14 + " to the route");
                getWrappedProcessor().process(exchange);
                return;
            }
        }

        // append current fragment to the accumulator
        int beginIndex = isEmpty(msh14) ? 0 : requestString.indexOf('\r');
        int endIndex = isEmpty(dsc1) ? requestString.length() : (requestString.indexOf("\rDSC") + 1);
        accumulator.append(requestString, beginIndex, endIndex);
        
        // DSC-1 is empty -- finish accumulation, pass message to the marshaller
        if (isEmpty(dsc1)) {
            LOG.debug("Finished fragment chain " + msh14);
            exchange.getIn().setBody(accumulator.toString());
            getWrappedProcessor().process(exchange);
            return;
        } 

        // DSC-1 is not empty -- update accumulators map, request the next fragment
        LOG.debug(new StringBuilder()
            .append("Processed fragment ")
            .append(msh14)
            .append(", requesting ")
            .append(dsc1)
            .toString());
            
        storage.put(keyString(dsc1, msh31, msh32, msh33), accumulator);
        Message ack = MessageUtils.response(
                parser.getFactory(),
                requestMessage, "ACK", 
                requestTerser.get("MSH-9-2"));
        Terser ackTerser = new Terser(ack);
        ackTerser.set("MSA-1", "CA");
        ackTerser.set("MSA-2", requestTerser.get("MSH-10"));
        Exchanges.resultMessage(exchange).setBody(parser.encode(ack));
    }
    
}
