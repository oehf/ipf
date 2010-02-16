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
package org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer;

import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.appendSegments;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.isEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.splitString;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpMarshalUtils.uniqueId;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Producer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;


/**
 * Producer-side Hl7 marshalling/unmarshalling interceptor 
 * with support for interactive continuation.
 * @author Dmytro Rud
 */
public class ProducerMarshalAndInteractiveResponseReceiverInterceptor extends AbstractProducerInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ProducerMarshalAndInteractiveResponseReceiverInterceptor.class);
    
    public ProducerMarshalAndInteractiveResponseReceiverInterceptor(MllpEndpoint endpoint, Producer wrappedProducer) {
        super(endpoint, wrappedProducer);
    }

    
    /**
     * Marshals the request, sends it to the route, and unmarshals the response. 
     */
    public void process(Exchange exchange) throws Exception {
        MllpTransactionConfiguration config = getMllpEndpoint().getTransactionConfiguration();
        String charset = getMllpEndpoint().getConfiguration().getCharsetName();
        MessageAdapter request = exchange.getIn().getBody(MessageAdapter.class);
        
        Terser requestTerser = null;
        String responseString = null;
        StringBuilder fragmentAccumulator = null;
        
        // Determine whether we should automatically handle message continuations.
        // Conditions:
        //     1. It must be allowed for the endpoint.
        //     2. It must be allowed for the given request message type.
        //     3. The user must not have already filled the DSC segment.
        boolean supportContinuations = false;
        if (getMllpEndpoint().isSupportInteractiveContinuation()) {
            Message requestMessage = (Message) request.getTarget();
            requestTerser = new Terser(requestMessage);
            if (config.isContinuable(requestTerser.get("MSH-9-1")) && isEmpty(requestTerser.get("DSC-1"))) {
                supportContinuations = true;
                fragmentAccumulator = new StringBuilder();
            }
        }
        
        // communication with optional continuation handling
        boolean mustSend = true;
        int fragmentsCount = 0;
        int recordsCount = 0;
        String continuationPointer = null; 
        while (mustSend) {
            mustSend = false;
    
            // marshal, send and wait for response
            exchange.getIn().setBody(request.toString());
            getWrappedProcessor().process(exchange);
            responseString = Exchanges.resultMessage(exchange).getBody(String.class);

            // continuations handling 
            if (supportContinuations) {
                char fieldDelimiter = responseString.charAt(3);
                List<String> segments = splitString(responseString, '\r');
                
                // check whether this fragment is a correct one, 
                // i.e. whether its MSH-14 corresponds to DSC-1 of the request
                if (getMllpEndpoint().isUseMsh14() && (continuationPointer != null)) {
                    List<String> mshFields = splitString(segments.get(0), fieldDelimiter);
                    String msh14 = (mshFields.size() >= 14) ? mshFields.get(13) : null;
                    if (! continuationPointer.equals(msh14)) {
                        throw new RuntimeException(new StringBuilder()
                            .append("Expected ")
                            .append(continuationPointer)
                            .append(" in MSH-14, but got ")
                            .append(msh14)
                            .toString());
                    }
                }
                
                // analyse whether we should request the next fragment   
                if (segments.get(segments.size() - 1).startsWith("DSC")) {
                    List<String> dscFields = splitString(segments.get(segments.size() - 1), fieldDelimiter);
                    
                    if ((dscFields.size() >= 3) 
                            && "I".equals(dscFields.get(2)) 
                            && ! isEmpty(dscFields.get(1))) 
                    {
                        continuationPointer = dscFields.get(1);
                        LOG.debug("Automatically query interactive fragment " + continuationPointer);
                        requestTerser.set("DSC-1", continuationPointer);
                        requestTerser.set("DSC-2", "I");
                        requestTerser.set("MSH-7", MessageUtils.hl7Now());
                        requestTerser.set("MSH-10", uniqueId());
                        mustSend = true;
                    }
                }
                
                // accumulate response fragments:
                //      - header segments from the first one,
                //      - data records from all
                //      - footer segments from the last one
                int startDataSegmentIndex = -1;
                int endDataSegmentIndex = segments.size();
                for (int i = 1; i < segments.size(); ++i) {
                    if(config.isDataStartSegment(segments, i)) {
                        ++recordsCount;
                        if (startDataSegmentIndex == -1) {
                            startDataSegmentIndex = i;
                        }
                    } else {
                        if (startDataSegmentIndex != -1) {
                            endDataSegmentIndex = i;
                            break;
                        }
                    }
                }
                
                if (startDataSegmentIndex == -1) {
                    LOG.warn("No data segments found, thus the resulting message structure can be wrong");
                    startDataSegmentIndex = segments.size();
                }
                
                if (++fragmentsCount == 1) {
                    appendSegments(fragmentAccumulator, segments, 0, startDataSegmentIndex);
                }
                appendSegments(fragmentAccumulator, segments, startDataSegmentIndex, endDataSegmentIndex);
                if (! mustSend) {
                    appendSegments(fragmentAccumulator, segments, endDataSegmentIndex, segments.size());
                }
            }
        }

        // get accumulated response
        if (fragmentsCount > 1) {
            responseString = fragmentAccumulator.toString();
        }

        // unmarshal and return
        MessageAdapter rsp = MessageAdapters.make(getMllpEndpoint().getParser(), responseString);
        if (recordsCount != 0) {
            Terser responseTerser = new Terser((Message) rsp.getTarget());
            String recordsCountString = Integer.toString(recordsCount);
            responseTerser.set("QAK-4", recordsCountString);
            responseTerser.set("QAK-5", recordsCountString);
            responseTerser.set("QAK-6", "0");
        }
        Exchanges.resultMessage(exchange).setBody(rsp);
        exchange.setProperty(Exchange.CHARSET_NAME, charset);
    }


}
