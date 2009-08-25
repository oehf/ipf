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
package org.openehealth.ipf.platform.camel.ihe.mllp.commons;

import org.openehealth.ipf.modules.hl7.AckTypeCode;

/**
 * Holder for transaction-specific endpoint parameters.
 * 
 * @author Dmytro Rud
 */
public class MllpEndpointConfiguration {
    private final String hl7Version;
    private final String sendingApplication;
    private final String sendingFacility;
    
    private final int requestErrorDefaultErrorCode;
    private final AckTypeCode requestErrorDefaultAckTypeCode;
    
    private final int responseErrorDefaultErrorCode;
    
    private final String allowedMessageType;
    private final String[] allowedTriggerEvents;
    private final String[] allowedStructureMaps;
    

    /**
     * Constructor.
     * 
     * @param hl7Version
     *      HL7 version for acceptance checks and default NAKs (MSH-12).
     * @param sendingApplication
     *      Sending application for default NAKs (MSH-3).
     * @param sendingFacility
     *      Sending application for default NAKs (MSH-4).
     * @param requestErrorDefaultErrorCode
     *      Default error code for request-related NAKs.
     * @param requestErrorDefaultAckTypeCode
     *      Default ack type code for request-related NAKs.
     * @param responseErrorDefaultErrorCode
     *      Default error code for response-related NAKs.
     * @param allowedMesasgeType
     *      Valid value of MSH-9-1 (for acceptance checks).
     * @param allowedTriggerEvents
     *      Array of valid values of MSH-9-2 (for acceptance checks).
     * @param allowedStructureNames
     *      Array of valid values of MSH-9-3 (for acceptance checks).
     */
    public MllpEndpointConfiguration(
            String hl7Version,
            String sendingApplication,
            String sendingFacility,
            int requestErrorDefaultErrorCode,
            AckTypeCode requestErrorDefaultAckTypeCode,
            int responseErrorDefaultErrorCode,
            String allowedMesasgeType,
            String[] allowedTriggerEvents,
            String[] allowedStructureMaps)
    {
        this.hl7Version = hl7Version;
        this.sendingApplication = sendingApplication;
        this.sendingFacility = sendingFacility;
        
        this.requestErrorDefaultErrorCode = requestErrorDefaultErrorCode;
        this.requestErrorDefaultAckTypeCode = requestErrorDefaultAckTypeCode;
        this.responseErrorDefaultErrorCode = responseErrorDefaultErrorCode;
        
        this.allowedMessageType = allowedMesasgeType;
        this.allowedTriggerEvents = allowedTriggerEvents;
        this.allowedStructureMaps = allowedStructureMaps;
    }

    
    // ----- automatically generated getters -----

    public String getHl7Version() {
        return hl7Version;
    }
    
    public int getRequestErrorDefaultErrorCode() {
        return requestErrorDefaultErrorCode;
    }

    public AckTypeCode getRequestErrorDefaultAckTypeCode() {
        return requestErrorDefaultAckTypeCode;
    }

    public int getResponseErrorDefaultErrorCode() {
        return responseErrorDefaultErrorCode;
    }

    public String getSendingApplication() {
        return sendingApplication;
    }

    public String getSendingFacility() {
        return sendingFacility;
    }

    public String getAllowedMessageType() {
        return allowedMessageType;
    }

    public String[] getAllowedTriggerEvents() {
        return allowedTriggerEvents;
    }

    public String[] getAllowedStructureMaps() {
        return allowedStructureMaps;
    }
}
