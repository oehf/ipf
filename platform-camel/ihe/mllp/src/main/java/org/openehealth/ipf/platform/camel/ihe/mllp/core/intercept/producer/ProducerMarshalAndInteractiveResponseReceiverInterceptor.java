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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.v25.message.QCN_J01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapters;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.AbstractMllpInterceptor;

import java.util.List;

import static org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils.isEmpty;
import static org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils.isPresent;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.*;

/**
 * Producer-side Hl7 marshalling/unmarshalling interceptor 
 * with support for interactive continuation.
 * <p>
 * Note that this interceptor has the same ID as {@link ProducerMarshalInterceptor}.
 *
 * @author Dmytro Rud
 */
public class ProducerMarshalAndInteractiveResponseReceiverInterceptor extends AbstractMllpInterceptor {
    private static final transient Log LOG = LogFactory.getLog(ProducerMarshalAndInteractiveResponseReceiverInterceptor.class);

    public ProducerMarshalAndInteractiveResponseReceiverInterceptor() {
        setId(ProducerMarshalInterceptor.class.getName());
    }


    /**
     * Marshals the request, sends it to the route, and unmarshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        Hl7v2TransactionConfiguration config = getHl7v2TransactionConfiguration();
        MessageAdapter<?> request = exchange.getIn().getBody(MessageAdapter.class);
        
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
            requestTerser = new Terser(request.getHapiMessage());
            if (config.isContinuable(requestTerser.get("MSH-9-1")) && isEmpty(requestTerser.get("DSC-1"))) {
                supportContinuations = true;
                fragmentAccumulator = new StringBuilder();
            }
        }
        
        // communication with optional continuation handling
        boolean mustSend = true;
        int fragmentsCount = 0;
        int recordsCount = 0;
        String continuationPointer; 
        while (mustSend) {
            mustSend = false;
    
            // marshal, send and wait for response
            exchange.getIn().setBody(request.toString());
            getWrappedProcessor().process(exchange);
            responseString = Exchanges.resultMessage(exchange).getBody(String.class);

            // continuations handling 
            if (supportContinuations) {
                List<String> segments = splitString(responseString, '\r');

                // analyse whether this fragment is a positive response
                boolean positiveResponse = false;
                for (String segment : segments) {
                    if (segment.startsWith("MSA")) {
                        positiveResponse = (segment.length() >= 7) && (segment.charAt(5) == 'A');
                        break;
                    }
                }
                if (! positiveResponse) {
                    // ignore all collected fragments, pass this response as is to the route
                    LOG.debug("Not a positive response, cannot perform continuation");
                    fragmentsCount = 0;
                    recordsCount = 0;
                    break;
                }

                // analyse whether we should request the next fragment   
                if (segments.get(segments.size() - 1).startsWith("DSC")) {
                    List<String> dscFields = splitString(segments.get(segments.size() - 1), responseString.charAt(3));
                    
                    if ((dscFields.size() >= 3) 
                            && "I".equals(dscFields.get(2)) 
                            && isPresent(dscFields.get(1))) 
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
                int startFooterSegmentIndex = segments.size();
                for (int i = 1; i < segments.size(); ++i) {
                    if(config.isDataStartSegment(segments, i)) {
                        ++recordsCount;
                        if (startDataSegmentIndex == -1) {
                            startDataSegmentIndex = i;
                        }
                    } 
                    else if (config.isFooterStartSegment(segments, i)) {
                        startFooterSegmentIndex = i;
                        break;
                    }
                }
                
                if (startDataSegmentIndex == -1) {
                    // no data records in this message
                    startDataSegmentIndex = segments.size();
                }
                
                if (++fragmentsCount == 1) {
                    appendSegments(fragmentAccumulator, segments, 0, startDataSegmentIndex);
                }
                appendSegments(fragmentAccumulator, segments, startDataSegmentIndex, startFooterSegmentIndex);
                if (! mustSend) {
                    appendSegments(fragmentAccumulator, segments, startFooterSegmentIndex, segments.size());
                }
            }
        }

        // get accumulated response
        if (fragmentsCount > 1) {
            responseString = fragmentAccumulator.toString();

            // prepare and send automatic cancel request, if necessary.
            // All errors will be ignored
            if (getMllpEndpoint().isAutoCancel()) {
                try {
                    String cancel = createCancelMessage(request.getHapiMessage(), config.getParser());
                    exchange.getIn().setBody(cancel);
                    getWrappedProcessor().process(exchange);
                } catch (Exception e) {
                    LOG.warn("Error while preparing and sending automatic cancel message", e);
                }
            }
        }

        // unmarshal and return
        MessageAdapter<?> rsp = MessageAdapters.make(config.getParser(), responseString);
        if (recordsCount != 0) {
            Terser responseTerser = new Terser(rsp.getHapiMessage());
            String recordsCountString = Integer.toString(recordsCount);
            responseTerser.set("QAK-4", recordsCountString);
            responseTerser.set("QAK-5", recordsCountString);
            responseTerser.set("QAK-6", "0");
        }
        Exchanges.resultMessage(exchange).setBody(rsp);
    }


    /**
     * Creates a continuation cancel message on the basis of the given request.
     * <p>
     * For requests with HL7 version (MSH-12) prior to 2.4, a <tt>CNQ</tt>
     * message will be created.  For requests with version 2.4 and above,
     * a <tt>QCN^J01</tt> message will be created.
     * See paragraph 5.6.3 in HL7 v2.5 specification.
     */
    private static String createCancelMessage(Message request, Parser parser) throws HL7Exception {
        return (request.getVersion().charAt(2) < '4') ?
            createCnqMessage(request, parser) :
            createQcnJ01Message(request, parser);
    }


    private static String createQcnJ01Message(Message request, Parser parser) throws HL7Exception {
        Message cancel = new QCN_J01();

        // ===== Segment MSH =====
        Segment requestMsh = (Segment) request.get("MSH");
        Segment cancelMsh = (Segment) cancel.get("MSH");

        Terser.set(cancelMsh, 1, 0, 1, 1, Terser.get(requestMsh, 1, 0, 1, 1));
        Terser.set(cancelMsh, 2, 0, 1, 1, Terser.get(requestMsh, 2, 0, 1, 1));

        // sender & receiver
        for (int field = 3; field <= 6; ++field) {
            for (int component = 1; component <= 3; ++component) {
               Terser.set(cancelMsh,  field, 0, component, 1,
               Terser.get(requestMsh, field, 0, component, 1));
            }
        }
        Terser.set(cancelMsh,  7, 0, 1, 1, MessageUtils.hl7Now());
        Terser.set(cancelMsh,  9, 0, 1, 1, "QCN");
        Terser.set(cancelMsh,  9, 0, 2, 1, "J01");
        Terser.set(cancelMsh,  9, 0, 3, 1, "QCN_J01");
        Terser.set(cancelMsh, 10, 0, 1, 1, uniqueId());
        Terser.set(cancelMsh, 11, 0, 1, 1, "P");

        // version
        for (int component = 1; component <= 3; ++component) {
           Terser.set(cancelMsh,  12, 0, component, 1,
           Terser.get(requestMsh, 12, 0, component, 1));
        }

        // ===== Segment QID =====
        Segment requestQpd = (Segment) request.get("QPD");
        Segment cancelQid = (Segment) cancel.get("QID");

        // query tag: QPD-2 --> QID-1
        Terser.set(cancelQid, 1, 0, 1, 1, Terser.get(requestQpd, 2, 0, 1, 1));

        // message query name: QPD-1 --> QID-2, 6 components
        for (int component = 1; component <= 6; ++component) {
            Terser.set(cancelQid,  2, 0, component, 1,
            Terser.get(requestQpd, 1, 0, component, 1));
        }

        // return
        return parser.encode(cancel);
    }


    private static String createCnqMessage(Message request, Parser parser) throws HL7Exception {
        Message cancel = parser.parse(parser.encode(request));
        Segment cancelMsh = (Segment) cancel.get("MSH");

        Terser.set(cancelMsh,  7, 0, 1, 1, MessageUtils.hl7Now());
        Terser.set(cancelMsh,  9, 0, 2, 1, "CNQ");
        Terser.set(cancelMsh,  9, 0, 3, 1, "");
        Terser.set(cancelMsh, 10, 0, 1, 1, uniqueId());

        return parser.encode(cancel);
    }
}
