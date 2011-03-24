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
package org.openehealth.ipf.platform.camel.ihe.pixpdq.pdqcore;

import java.util.List;

import ca.uhn.hl7v2.parser.Parser;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionConfiguration;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.NakFactory;

/**
 * A MLLP transaction configuration with PDQ-specific methods for continuation support.
 * @author Dmytro Rud
 */
public class PdqTransactionConfiguration extends MllpTransactionConfiguration {
   
    public PdqTransactionConfiguration(
            String hl7Version,
            String sendingApplication, 
            String sendingFacility,
            int requestErrorDefaultErrorCode,
            int responseErrorDefaultErrorCode,
            String[] allowedRequestMessageTypes,
            String[] allowedRequestTriggerEvents,
            String[] allowedResponseMessageTypes,
            String[] allowedResponseTriggerEvents, 
            boolean[] auditabilityFlags,
            boolean[] responseContinuabilityFlags,
            Parser parser,
            NakFactory nakFactory)
    {
        super(hl7Version, sendingApplication, sendingFacility,
                requestErrorDefaultErrorCode,
                responseErrorDefaultErrorCode, allowedRequestMessageTypes,
                allowedRequestTriggerEvents, allowedResponseMessageTypes,
                allowedResponseTriggerEvents, auditabilityFlags,
                responseContinuabilityFlags, parser, nakFactory);
    }
    
    @Override
    public boolean isDataStartSegment(List<String> segments, int index) {
        return segments.get(index).startsWith("PID");
    }

    @Override
    public boolean isFooterStartSegment(List<String> segments, int index) {
        return segments.get(index).startsWith("DSC");
    }
}
