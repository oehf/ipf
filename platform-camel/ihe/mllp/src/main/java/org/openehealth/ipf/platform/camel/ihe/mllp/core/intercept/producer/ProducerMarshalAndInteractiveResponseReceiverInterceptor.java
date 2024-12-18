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
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer.ProducerMarshalInterceptor;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.appendSegments;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.splitString;
import static org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils.uniqueId;

/**
 * Producer-side Hl7 marshalling/unmarshalling interceptor 
 * with support for interactive continuation.
 * <p>
 * Note that this interceptor has the same ID as {@link ProducerMarshalInterceptor}.
 *
 * @author Dmytro Rud
 */
public class ProducerMarshalAndInteractiveResponseReceiverInterceptor extends InterceptorSupport {
    private static final Logger log = LoggerFactory.getLogger(ProducerMarshalAndInteractiveResponseReceiverInterceptor.class);

    private final String charsetName;

    public ProducerMarshalAndInteractiveResponseReceiverInterceptor(String charsetName) {
        super();
        setId(ProducerMarshalInterceptor.class.getName());
        this.charsetName = charsetName;
    }


    /**
     * Marshals the request, sends it to the route, and unmarshals the response.
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        var config = getEndpoint(MllpTransactionEndpoint.class).getHl7v2TransactionConfiguration();
        var request = exchange.getIn().getBody(Message.class);
        
        Terser requestTerser = null;
        String responseString = null;
        StringBuilder fragmentAccumulator = null;
        
        // Determine whether we should automatically handle message continuations.
        // Conditions:
        //     1. It must be allowed for the endpoint.
        //     2. It must be allowed for the given request message type.
        //     3. The user must not have already filled the DSC segment.
        var supportContinuations = false;
        if (getEndpoint(MllpTransactionEndpoint.class).isSupportInteractiveContinuation()) {
            requestTerser = new Terser(request);
            if (config.isContinuable(requestTerser.get("MSH-9-1")) && isEmpty(requestTerser.get("DSC-1"))) {
                supportContinuations = true;
                fragmentAccumulator = new StringBuilder();
            }
        }
        
        // communication with optional continuation handling
        var mustSend = true;
        var fragmentsCount = 0;
        var recordsCount = 0;
        String continuationPointer; 
        while (mustSend) {
            mustSend = false;


            // marshal, send and wait for response
            exchange.getIn().setBody(Hl7v2MarshalUtils.convertBodyToByteArray(
                    request,
                    exchange.getProperty(Exchange.CHARSET_NAME, charsetName, String.class)));

            getWrappedProcessor().process(exchange);

            responseString = Hl7v2MarshalUtils.convertBodyToString(
                    exchange.getMessage(), charsetName, false);

            // continuations handling 
            if (supportContinuations) {
                var segments = splitString(responseString, '\r');

                // analyse whether this fragment is a positive response
                var positiveResponse = false;
                for (var segment : segments) {
                    if (segment.startsWith("MSA")) {
                        positiveResponse = (segment.length() >= 7) && (segment.charAt(5) == 'A');
                        break;
                    }
                }
                if (! positiveResponse) {
                    // ignore all collected fragments, pass this response as is to the route
                    log.debug("Not a positive response, cannot perform continuation");
                    fragmentsCount = 0;
                    recordsCount = 0;
                    break;
                }

                // analyse whether we should request the next fragment   
                if (segments.get(segments.size() - 1).startsWith("DSC")) {
                    var dscFields = splitString(segments.get(segments.size() - 1), responseString.charAt(3));
                    
                    if ((dscFields.size() >= 3)
                            && "I".equals(dscFields.get(2))
                            && isNotEmpty(dscFields.get(1)))
                    {
                        continuationPointer = dscFields.get(1);
                        log.debug("Automatically query interactive fragment {}", continuationPointer);
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
                var startDataSegmentIndex = -1;
                var startFooterSegmentIndex = segments.size();
                for (var i = 1; i < segments.size(); ++i) {
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
            if (getEndpoint(MllpTransactionEndpoint.class).isAutoCancel()) {
                try {
                    var cancel = createCancelMessage(request, config.getParser());
                    exchange.getIn().setBody(cancel);
                    getWrappedProcessor().process(exchange);
                } catch (Exception e) {
                    log.warn("Error while preparing and sending automatic cancel message", e);
                }
            }
        }

        // unmarshal and return
        var rsp = config.getParser().parse(responseString);
        if (recordsCount != 0) {
            var responseTerser = new Terser(rsp);
            var recordsCountString = Integer.toString(recordsCount);
            responseTerser.set("QAK-4", recordsCountString);
            responseTerser.set("QAK-5", recordsCountString);
            responseTerser.set("QAK-6", "0");
        }
        exchange.getMessage().setBody(rsp);
    }


    /**
     * Creates a continuation cancel message on the basis of the given request.
     * <p>
     * For requests with HL7 version (MSH-12) prior to 2.4, a <tt>CNQ</tt>
     * message will be created.  For requests with version 2.4 and above,
     * a <tt>QCN^J01</tt> message will be created.
     * See paragraph 5.6.3 in HL7 v2.5 specification.
     */
    public static String createCancelMessage(Message request, Parser parser) throws HL7Exception {
        return (request.getVersion().charAt(2) < '4') ?
            createCnqMessage(request, parser) :
            createQcnJ01Message(request, parser);
    }


    private static String createQcnJ01Message(Message request, Parser parser) throws HL7Exception {
        Message cancel = new QCN_J01();

        // ===== Segment MSH =====
        var requestMsh = (Segment) request.get("MSH");
        var cancelMsh = (Segment) cancel.get("MSH");

        Terser.set(cancelMsh, 1, 0, 1, 1, Terser.get(requestMsh, 1, 0, 1, 1));
        Terser.set(cancelMsh, 2, 0, 1, 1, Terser.get(requestMsh, 2, 0, 1, 1));

        // sender & receiver
        for (var field = 3; field <= 6; ++field) {
            for (var component = 1; component <= 3; ++component) {
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
        for (var component = 1; component <= 3; ++component) {
           Terser.set(cancelMsh,  12, 0, component, 1,
           Terser.get(requestMsh, 12, 0, component, 1));
        }

        // ===== Segment QID =====
        var requestQpd = (Segment) request.get("QPD");
        var cancelQid = (Segment) cancel.get("QID");

        // query tag: QPD-2 --> QID-1
        Terser.set(cancelQid, 1, 0, 1, 1, Terser.get(requestQpd, 2, 0, 1, 1));

        // message query name: QPD-1 --> QID-2, 6 components
        for (var component = 1; component <= 6; ++component) {
            Terser.set(cancelQid,  2, 0, component, 1,
            Terser.get(requestQpd, 1, 0, component, 1));
        }

        // return
        return parser.encode(cancel);
    }


    private static String createCnqMessage(Message request, Parser parser) throws HL7Exception {
        var cancel = parser.parse(parser.encode(request));
        var cancelMsh = (Segment) cancel.get("MSH");

        Terser.set(cancelMsh,  7, 0, 1, 1, MessageUtils.hl7Now());
        Terser.set(cancelMsh,  9, 0, 2, 1, "CNQ");
        Terser.set(cancelMsh,  9, 0, 3, 1, "");
        Terser.set(cancelMsh, 10, 0, 1, 1, uniqueId());

        return parser.encode(cancel);
    }
}
