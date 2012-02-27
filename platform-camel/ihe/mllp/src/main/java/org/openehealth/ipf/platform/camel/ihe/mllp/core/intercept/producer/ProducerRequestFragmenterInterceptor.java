/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer;

import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import java.util.List;

import static org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils.isPresent;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.*;

/**
 * A producer-side interceptor which implements non-interactive request 
 * fragmentation as described in paragraph 2.10.2.2 of the HL7 v.2.5 specification.
 * @author Dmytro Rud
 */
public class ProducerRequestFragmenterInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ProducerRequestFragmenterInterceptor.class);
    

    @Override
    public void process(Exchange exchange) throws Exception {
        int threshold = getMllpEndpoint().getUnsolicitedFragmentationThreshold();
        if (threshold < 3) {
            getWrappedProcessor().process(exchange);
            return;
        }
        
        String request = exchange.getIn().getBody(String.class);
        char fieldSeparator = request.charAt(3);
        List<String> segments = splitString(request, '\r');

        // short message --> send unmodified and return
        if (segments.size() <= threshold) {
            getWrappedProcessor().process(exchange);
            return;
        }

        // parse MSH segment
        List<String> mshFields = splitString(segments.get(0), request.charAt(3));
        
        // when MSH-14 is already present -- send the message unmodified and return
        if ((mshFields.size() >= 14) && isPresent(mshFields.get(13))) {
            LOG.warn("MSH-14 is not empty, cannot perform automatic message fragmentation");
            getWrappedProcessor().process(exchange);
            return;
        }
        
        // when DSC is present and already filled -- send the message unmodified 
        // and return; otherwise -- delete the DSC segment, if present 
        if (segments.get(segments.size() - 1).startsWith("DSC")) {
            List<String> dscFields = splitString(segments.get(segments.size() - 1), request.charAt(3));
            String dsc1 = (dscFields.size() >= 2) ? dscFields.get(1) : null;
            if (isPresent(dsc1)) {
                LOG.warn("DSC-1 is not empty, cannot perform automatic message fragmentation");
                getWrappedProcessor().process(exchange);
                return;
            }
            segments.remove(segments.size() - 1);
        }

        while (mshFields.size() < 14) {
            mshFields.add("");
        }
        
        // main loop
        int currentSegmentIndex = 1;
        String continuationPointer = "";
        while (currentSegmentIndex < segments.size()) {
            int currentSegmentsCount = 1;
            StringBuilder sb = new StringBuilder();
            
            // add MSH (position 1)
            appendSplittedSegment(sb, mshFields, fieldSeparator);

            // add data segments (positions 2..MAX-1)
            do {
                sb.append(segments.get(currentSegmentIndex)).append('\r');
            } while ((++currentSegmentIndex  < segments.size()) 
                  && (++currentSegmentsCount < threshold - 1));

            // one position free, one segment left -> bring them together
            if (currentSegmentIndex == segments.size() - 1) {
                sb.append(segments.get(currentSegmentIndex++)).append('\r');
            }
            
            // one or more segments left -> add DSC (position MAX)
            if(currentSegmentIndex < segments.size()) {
                continuationPointer = uniqueId();
                sb.append("DSC")
                  .append(fieldSeparator)
                  .append(continuationPointer)
                  .append(fieldSeparator)
                  .append("F\r");

                LOG.debug("Send next fragment, continuation pointer = " + continuationPointer);
            }
            
            // send the generated fragment to the receiver
            exchange.getIn().setBody(sb.toString());
            getWrappedProcessor().process(exchange);

            // catch and analyse the response, if this was not the last fragment
            if(currentSegmentIndex < segments.size()) {
                String responseString = Exchanges.resultMessage(exchange).getBody(String.class);
                Terser responseTerser = new Terser(getHl7v2TransactionConfiguration().getParser().parse(responseString));
                
                String messageType = responseTerser.get("MSH-9-1");
                String acknowledgementCode = responseTerser.get("MSA-1");
                String controlId = mshFields.get(9);
                
                if (! "ACK".equals(messageType)) {
                    throw new RuntimeException(new StringBuilder() 
                        .append("Server responded with ")
                        .append(messageType)
                        .append(" instead of ACK to the fragment with control ID ")
                        .append(mshFields.get(9))
                        .toString());
                }
                if ("AA".equals(acknowledgementCode) || "CA".equals(acknowledgementCode)) {
                    if (! controlId.equals(responseTerser.get("MSA-2"))) {
                        throw new RuntimeException(new StringBuilder()
                            .append("Expected ")
                            .append(controlId)
                            .append(" in MSA-2, but got ")
                            .append(responseTerser.get("MSA-2"))
                            .toString());
                    }
                } else {
                    // NAKs will go to the route
                    LOG.debug("Got NAK response for fragment with control ID" + controlId); 
                    break;
                }
    
                // update fields for next fragment
                mshFields.set(6, MessageUtils.hl7Now());
                mshFields.set(9, uniqueId());
                mshFields.set(13, continuationPointer);
            }
        }
    }

}
