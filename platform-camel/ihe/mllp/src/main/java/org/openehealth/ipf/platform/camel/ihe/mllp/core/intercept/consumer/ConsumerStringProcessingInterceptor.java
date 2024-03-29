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

import org.apache.camel.Exchange;
import org.openehealth.ipf.platform.camel.ihe.core.InterceptorSupport;
import org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2MarshalUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.FragmentationUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpEndpoint;


/**
 * Consumer-side MLLP interceptor that sets character encoding configured
 * for the given endpoint, and handles segment fragmentation (\rADD|...).
 * @author Dmytro Rud
 */
public class ConsumerStringProcessingInterceptor extends InterceptorSupport {

    @Override
    public void process(Exchange exchange) throws Exception {
        final var charsetName = getEndpoint(MllpEndpoint.class).getCharsetName();
        exchange.setProperty(Exchange.CHARSET_NAME, charsetName);

        var supportSegmentFragmentation = getEndpoint(MllpEndpoint.class).isSupportSegmentFragmentation();
        var segmentFragmentationThreshold = getEndpoint(MllpEndpoint.class).getSegmentFragmentationThreshold();
        
        // read in the request
        var message = exchange.getIn();
        message.setBody(Hl7v2MarshalUtils.convertBodyToString(
                message,
                charsetName,
                supportSegmentFragmentation));
        
        // run the route
        getWrappedProcessor().process(exchange);

        // preprocess output
        if (supportSegmentFragmentation && (segmentFragmentationThreshold >= 5)) {
            message = exchange.getMessage();
            var s = message.getBody(String.class);
            s = FragmentationUtils.ensureMaximalSegmentsLength(s, segmentFragmentationThreshold);
            message.setBody(s);
        }

    }
}
