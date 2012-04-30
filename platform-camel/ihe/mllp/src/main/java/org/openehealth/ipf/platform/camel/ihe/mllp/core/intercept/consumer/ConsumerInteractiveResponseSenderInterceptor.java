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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2ConfigurationHolder;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.InteractiveContinuationStorage;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.*;


/**
 * Consumer-side interceptor for interactive continuation support 
 * as described in paragraph 5.6.3 of the HL7 v2.5 specification.
 * @author Dmytro Rud
 */
public class ConsumerInteractiveResponseSenderInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ConsumerInteractiveResponseSenderInterceptor.class);
    private InteractiveContinuationStorage storage;


    @Override
    public void setConfigurationHolder(Hl7v2ConfigurationHolder configurationHolder) {
        super.setConfigurationHolder(configurationHolder);
        this.storage = getMllpEndpoint().getInteractiveContinuationStorage();
        Validate.notNull(storage);
    }


    @Override
    public void process(Exchange exchange) throws Exception {
        Parser parser = getHl7v2TransactionConfiguration().getParser();
        MessageAdapter<?> request = (MessageAdapter<?>) exchange.getIn().getHeader(ORIGINAL_MESSAGE_ADAPTER_HEADER_NAME);
        Message requestMessage = request.getHapiMessage();
        Terser requestTerser = new Terser(requestMessage);
        String requestMessageType = requestTerser.get("MSH-9-1");

        // get pieces of fragments' keys
        final String msh31 = requestTerser.get("MSH-3-1");
        final String msh32 = requestTerser.get("MSH-3-2");
        final String msh33 = requestTerser.get("MSH-3-3");

        // handle cancel messages; if there is nothing to cancel -- pass to the route
        if ("QCN".equals(requestMessageType) || "CNQ".equals(requestTerser.get("MSH-9-2"))) {
            String queryTag = "QCN".equals(requestMessageType) ? 
                    requestTerser.get("QID-1") :
                    requestTerser.get("QPD-2");
            if (storage.delete(keyString(queryTag, msh31, msh32, msh33))) {
                LOG.debug("Dropped response chain for query tag " + queryTag);
                Message ack = MessageUtils.ack(parser.getFactory(), requestMessage);
                Exchanges.resultMessage(exchange).setBody(parser.encode(ack));
            } else {
                getWrappedProcessor().process(exchange);
            }
            return;
        }

        // check whether responses to messages of this type can even be splitted
        if (! getHl7v2TransactionConfiguration().isContinuable(requestMessageType)) {
            getWrappedProcessor().process(exchange);
            return;
        }

        // check whether requested unit type is supported
        String rcp22 = requestTerser.get("RCP-2-2");
        if (! "RD".equals(rcp22)) {
            LOG.warn("Unit '" + rcp22 + "' in RCP-2-2 is not supported");
            getWrappedProcessor().process(exchange);
            return;
        }

        // determine the threshold (maximal records count per message)
        int threshold = -1;
        try {
            threshold = Integer.parseInt(requestTerser.get("RCP-2-1"));
        } catch (NumberFormatException nfe) {
            LOG.warn("Cannot parse RCP-2-1, try to use default threshold", nfe);
        }
        if (threshold < 1) {
            threshold = getMllpEndpoint().getInteractiveContinuationDefaultThreshold();
        }
        if (threshold < 1) {
            LOG.debug("Cannot perform interactive continuation: invalid or missing threshold");
            getWrappedProcessor().process(exchange);
            return;
        }
        
        // check whether the request is acceptable; if not -- pass it to the route, let the user decide 
        String continuationPointer = requestTerser.get("DSC-1");
        if (isEmpty(continuationPointer)) {
            continuationPointer = null;
        }

        if ((continuationPointer != null) && ! "I".equals(requestTerser.get("DSC-2"))) {
            LOG.warn("Cannot perform interactive continuation: DSC-1 is not empty and DSC-2 is not 'I'");
            getWrappedProcessor().process(exchange);
            return;
        }
        
        final String queryTag = requestTerser.get("QPD-2");
        if (isEmpty(queryTag)) {
            LOG.warn("Cannot perform interactive continuation: empty query tag in QPD-2");
            getWrappedProcessor().process(exchange);
            return;
        }

        // handle query
        final String chainId = keyString(queryTag, msh31, msh32, msh33);
        Message responseMessage = storage.get(continuationPointer, chainId);
        if (responseMessage != null) {
            // a prepared response fragment found -- perform some post-processing and send it to the user
            LOG.debug("Use prepared fragment for " + continuationPointer);
            synchronized (responseMessage) {
                Terser responseTerser = new Terser(responseMessage);
                responseTerser.set("MSH-7", MessageUtils.hl7Now());
                responseTerser.set("MSH-10", uniqueId());
                responseTerser.set("MSA-2", requestTerser.get("MSH-10"));
            }
        } else {
            // no fragment found --> run the route and create fragments if necessary
            getWrappedProcessor().process(exchange);
            MessageAdapter<?> response = Exchanges.resultMessage(exchange).getBody(MessageAdapter.class);
            responseMessage = considerFragmentingResponse(response, threshold, queryTag, chainId);
        }
        Exchanges.resultMessage(exchange).setBody(parser.encode(responseMessage));
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
            MessageAdapter<?> response, 
            int threshold,
            String queryTag,
            String chainId) throws Exception
    {
        Message responseMessage = response.getHapiMessage();
        Terser responseTerser = new Terser(responseMessage);  
        if (isNotEmpty(responseTerser.get("DSC-1"))) {
            LOG.warn("Cannot perform interactive continuation: DSC-1 already " +
            		 "present in the response message returned from the route");
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
        Parser parser = getHl7v2TransactionConfiguration().getParser();
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
            String nextContinuationPointer = uniqueId();
            if (currentFragmentIndex != fragmentsCount - 1) {
                fragmentTerser.set("DSC-1", nextContinuationPointer);
                fragmentTerser.set("DSC-2", "I");
            }
            fragmentTerser.set("QAK-4", Integer.toString(recordBoundaries.size() - 1));
            fragmentTerser.set("QAK-5", Integer.toString(endSegmentIndex - startSegmentIndex));
            fragmentTerser.set("QAK-6", Integer.toString(
                    recordBoundaries.get(recordBoundaries.size() - 1) - endSegmentIndex));

            storage.put(continuationPointer, chainId, fragment);
            continuationPointer = nextContinuationPointer;

            // remember the first fragment in order to return it
            if (currentFragmentIndex == 0) {
                responseMessage = fragment;
            }
        }
        LOG.debug(new StringBuilder("Prepared ")
                        .append(fragmentsCount)
                        .append(" interactive fragments for query tag ")
                        .append(queryTag)
                        .toString());
        return responseMessage;
    }

    
    /**
     * Determines boundaries for data records among the given segments' list.
     * For N data records there will be N+1 boundaries.
     */
    private List<Integer> getRecordBoundaries(List<String> segments) {
        Hl7v2TransactionConfiguration config = getHl7v2TransactionConfiguration();
        List<Integer> recordBoundaries = new ArrayList<Integer>(); 
        boolean foundFooter = false;
        for (int i = 1; i < segments.size(); ++i) {
            if (config.isDataStartSegment(segments, i)) {
                recordBoundaries.add(i);
            } else if ((recordBoundaries.size() > 0) && config.isFooterStartSegment(segments, i)) {
                foundFooter = true;
                recordBoundaries.add(i);
                break;
            }
        }
        if (! foundFooter) {
            recordBoundaries.add(segments.size());
        }
        return recordBoundaries;
    }
    
}
