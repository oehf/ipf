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
package org.openehealth.ipf.platform.camel.ihe.mllp.intercept.consumer;

import static org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils.appendSegments;
import static org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils.isEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils.isPresent;
import static org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils.joinSegments;
import static org.openehealth.ipf.platform.camel.ihe.mllp.MllpMarshalUtils.splitString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.ContinuationStorage;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.mllp.intercept.AbstractMllpInterceptor;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;


/**
 * Consumer-side interceptor for interactive continuation support 
 * as described in paragraph 5.6.3 of the HL7 v2.5 specification.
 * @author Dmytro Rud
 */
public class ConsumerInteractiveResponseSenderInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerInteractiveResponseSenderInterceptor.class);
    private final ContinuationStorage storage;
    
    public ConsumerInteractiveResponseSenderInterceptor(
            MllpEndpoint endpoint, 
            Processor wrappedProcessor,
            ContinuationStorage storage) 
    {
        super(endpoint, wrappedProcessor);
        this.storage = storage;
    }


    public void process(Exchange exchange) throws Exception {
        Parser parser = getMllpEndpoint().getParser();
        MessageAdapter request = (MessageAdapter) exchange.getIn().getHeader(ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME);
        Message requestMessage = (Message) request.getTarget();
        Terser requestTerser = new Terser(requestMessage);
        String requestMessageType = requestTerser.get("MSH-9-1");

        // get pieces of fragments' keys
        String msh31 = requestTerser.get("MSH-3-1");
        String msh32 = requestTerser.get("MSH-3-2");
        String msh33 = requestTerser.get("MSH-3-3");

        // handle cancel messages; if there is nothing to cancel -- pass to the route
        if ("QCN".equals(requestMessageType)) {
            String queryTag = requestTerser.get("QID-1");
            if (storage.deleteFragments(queryTag, msh31, msh32, msh33)) {
                LOG.debug("Dropped response chain for query ID " + queryTag);
                Message ack = (Message) MessageUtils.ack(parser.getFactory(), requestMessage);
                Exchanges.resultMessage(exchange).setBody(parser.encode(ack));
            } else {
                getWrappedProcessor().process(exchange);
            }
            return;
        }
        
        // check whether responses to messages of this type can even be splitted
        if (! getMllpEndpoint().getTransactionConfiguration().isContinuable(requestMessageType)) {
            getWrappedProcessor().process(exchange);
            return;
        }
        
        // determine the threshold
        int threshold = -1;
        if ("RD".equals(requestTerser.get("RCP-2-2"))) {
            try {
                threshold = new Integer(requestTerser.get("RCP-2-1"));
            } catch (NumberFormatException nfe) {
                LOG.warn("Cannot parse RCP-2-1, use default threshold, if any", nfe);
            }
        }
        if (threshold < 1) {
            threshold = getMllpEndpoint().getInteractiveContinuationDefaultThreshold();
        }
        if (threshold < 1) {
            getWrappedProcessor().process(exchange);
            return;
        }
        
        // check whether the request is acceptable; if not -- pass it to the route, let the user decide 
        if (isPresent(requestTerser.get("DSC-1")) && ! "I".equals(requestTerser.get("DSC-2"))) {
            LOG.warn("Only interactive continuations are supported");
            getWrappedProcessor().process(exchange);
            return;
        }
        
        String queryTag = requestTerser.get("QPD-2");
        if (isEmpty(queryTag)) {
            LOG.warn("Cannot handle empty query tag");
            getWrappedProcessor().process(exchange);
            return;
        }

        // handle query
        String continuationPointer = requestTerser.get("DSC-1");
        if (isEmpty(continuationPointer)) {
            continuationPointer = null;
        }
        Message responseMessage = storage.getFragment(continuationPointer, queryTag, msh31, msh32, msh33);
        if (responseMessage != null) {
            // a prepared response fragment found -- perform some post-processing and send it to the user
            synchronized (responseMessage) {
                Terser responseTerser = new Terser(responseMessage);
                responseTerser.set("MSH-7", MessageUtils.hl7Now());
                responseTerser.set("MSH-10", UUID.randomUUID().toString());
                responseTerser.set("MSA-2", requestTerser.get("MSH-10"));
            }
            LOG.debug("Successfully handled interactive fragment request for " + continuationPointer);
        } else {
            // no fragment found --> run the route and create fragments if necessary
            getWrappedProcessor().process(exchange);
            MessageAdapter response = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
            responseMessage = considerFragmentingResponse(response, threshold, queryTag, msh31, msh32, msh33);
        }
        Exchanges.resultMessage(exchange).setBody(getMllpEndpoint().getParser().encode(responseMessage));
    }
     
    
    /**
     * Checks whether the given response message should and can be fragmented.
     * <br>
     * If yes -- stores the fragments into the storage and returns the first fragment,
     * i.e. the one that must be sent immediately.
     * <br>
     * If no -- simply returns the response message back.
     */
    private Message considerFragmentingResponse(
            MessageAdapter response, 
            int threshold,
            String queryTag, 
            String msh31, String msh32, String msh33) throws Exception 
    {
        Message responseMessage = (Message) response.getTarget();
        Terser responseTerser = new Terser(responseMessage);  
        if (isPresent(responseTerser.get("DSC-1")) || isPresent(responseTerser.get("MSH-14"))) {
            LOG.warn("DSC-1 and/or MSH-14 already present in the response message " +
            		 "returned from the route, interactive continuation not possible");
            return responseMessage;
        }
        
        // determine data record boundaries in the response
        List<String> segments = splitString(response.toString(), '\r');
        List<Integer> recordBoundaries = getRecordBoundaries(segments);
        if (recordBoundaries.size() - 1 <= threshold) {
            return responseMessage;
        }
        
        // prepare header and footer segment groups
        CharSequence headerSegments = joinSegments(segments, 0, recordBoundaries.get(0));
        CharSequence footerSegments = joinSegments(
                segments, recordBoundaries.get(recordBoundaries.size() - 1), segments.size());

        // determine count of resulting fragments
        final int fragmentsCount = (recordBoundaries.size() + threshold - 2) / threshold; 
        
        // create a new chain of fragments
        Parser parser = getMllpEndpoint().getParser();
        String continuationPointer = null;
        for (int currentFragmentIndex = 0; currentFragmentIndex < fragmentsCount; ++currentFragmentIndex) {
            // create the current fragment as String 
            int startRecordIndex = currentFragmentIndex * threshold;
            int endRecordIndex = Math.min(startRecordIndex + threshold, recordBoundaries.size());
            int startSegmentIndex = recordBoundaries.get(startRecordIndex);
            int endSegmentIndex = recordBoundaries.get(endRecordIndex);

            StringBuilder sb = new StringBuilder(headerSegments);
            appendSegments(sb, segments, startSegmentIndex, endSegmentIndex);
            sb.append(footerSegments);

            // parse, post-process and register the current fragment
            Message fragment = parser.parse(sb.toString());
            Terser fragmentTerser = new Terser(fragment);
            String nextContinuationPointer = UUID.randomUUID().toString();
            if (CONSIDER_MSH_14) {
                fragmentTerser.set("MSH-14", continuationPointer);
            }
            if (currentFragmentIndex != fragmentsCount - 1) {
                fragmentTerser.set("DSC-1", nextContinuationPointer);
                fragmentTerser.set("DSC-2", "I");
            }
            fragmentTerser.set("QAK-4", Integer.toString(recordBoundaries.size() - 1));
            fragmentTerser.set("QAK-5", Integer.toString(endSegmentIndex - startSegmentIndex));
            fragmentTerser.set("QAK-6", Integer.toString(
                    recordBoundaries.get(recordBoundaries.size() - 1) - endSegmentIndex));

            storage.putFragment(continuationPointer, queryTag, msh31, msh32, msh33, fragment);
            continuationPointer = nextContinuationPointer;

            // remember the first fragment in order to return it
            if (currentFragmentIndex == 0) {
                responseMessage = fragment;
            }
        }
        return responseMessage;
    }

    
    /**
     * Determines boundaries for data records among the given segments' list.
     * For N data records there will be N+1 boundaries.
     */
    private List<Integer> getRecordBoundaries(List<String> segments) {
        MllpTransactionConfiguration config = getMllpEndpoint().getTransactionConfiguration();
        List<Integer> recordBoundaries = new ArrayList<Integer>(); 
        boolean foundFooter = false;
        for (int i = 1; i < segments.size(); ++i) {
            if (config.isDataStartSegment(segments, i)) {
                recordBoundaries.add(i);
            } else {
                foundFooter = (recordBoundaries.size() > 0) && config.isNotDataSegment(segments, i);
                if (foundFooter) {
                    recordBoundaries.add(i);
                    break;
                }
            }
        }
        if (! foundFooter) {
            recordBoundaries.add(segments.size());
        }
        return recordBoundaries;
    }
    
}
