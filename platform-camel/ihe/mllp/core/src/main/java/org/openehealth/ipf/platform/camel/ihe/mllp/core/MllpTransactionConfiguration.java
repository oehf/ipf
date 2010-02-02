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
package org.openehealth.ipf.platform.camel.ihe.mllp.core;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.noNullElements;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import java.util.List;

import org.openehealth.ipf.modules.hl7.AckTypeCode;


/**
 * Endpoint-agnostic parameters of an MLLP-based transaction.
 * @author Dmytro Rud
 */
public class MllpTransactionConfiguration {
    private final String hl7Version;
    private final String sendingApplication;
    private final String sendingFacility;
    
    private final int requestErrorDefaultErrorCode;
    private final AckTypeCode requestErrorDefaultAckTypeCode;
    private final int responseErrorDefaultErrorCode;

    private final String[] allowedRequestMessageTypes;
    private final String[] allowedRequestTriggerEvents;

    private final String[] allowedResponseMessageTypes;
    private final String[] allowedResponseTriggerEvents;

    private final boolean[] auditabilityFlags;
    private final boolean[] responseContinuabilityFlags;


    /**
     * Constructor.
     * 
     * @param hl7Version
     *      HL7 version for acceptance checks and default NAKs (MSH-12).
     * @param sendingApplication
     *      sending application for default NAKs (MSH-3).
     * @param sendingFacility
     *      sending application for default NAKs (MSH-4).
     * @param requestErrorDefaultErrorCode
     *      default error code for request-related NAKs.
     * @param requestErrorDefaultAckTypeCode
     *      default ack type code for request-related NAKs.
     * @param responseErrorDefaultErrorCode
     *      default error code for response-related NAKs.
     * @param allowedResponseMessageTypes
     *      array of allowed request message types, 
     *      e.g. <code>{"ADT", "MDM"}</code>.
     * @param allowedRequestTriggerEvents
     *      array of allowed request trigger events  
     *      for each request message type,  
     *      e.g. <code>{"A01 A02 A03", "T06 T07 T08"}</code>.
     * @param allowedResponseMessageTypes
     *      array of allowed response message types, e.g. <code>{"ACK", "RSP"}</code>.
     * @param allowedResponseTriggerEvents
     *      array of allowed response trigger events for each message type,
     *      ignored for messages of type "ACK".  
     * @param auditabilityFlags
     *      flags of whether the messages of corresponding 
     *      type should be audited.
     * @param responseRecordSegmentNames
     *      names of segments which belong to the "data record" of the response.
     */
    public MllpTransactionConfiguration(
            String hl7Version,
            String sendingApplication,
            String sendingFacility,
            int requestErrorDefaultErrorCode,
            AckTypeCode requestErrorDefaultAckTypeCode,
            int responseErrorDefaultErrorCode,
            String[] allowedRequestMessageTypes,
            String[] allowedRequestTriggerEvents,
            String[] allowedResponseMessageTypes,
            String[] allowedResponseTriggerEvents,
            boolean[] auditabilityFlags,
            boolean[] responseContinuabilityFlags)
    {
        notNull(hl7Version);
        notNull(sendingApplication);
        notNull(sendingFacility);
        notNull(requestErrorDefaultAckTypeCode);

        noNullElements(allowedRequestMessageTypes);
        noNullElements(allowedRequestTriggerEvents);
        noNullElements(allowedResponseMessageTypes);
        noNullElements(allowedResponseTriggerEvents);
        notNull(auditabilityFlags);
        notNull(responseContinuabilityFlags);

        notEmpty(allowedRequestMessageTypes);
        isTrue(allowedRequestMessageTypes.length == allowedRequestTriggerEvents.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseMessageTypes.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseTriggerEvents.length);
        isTrue(allowedRequestMessageTypes.length == auditabilityFlags.length);
        isTrue(allowedRequestMessageTypes.length == responseContinuabilityFlags.length);
        
        // QC passed ;)
        
        this.hl7Version = hl7Version;
        this.sendingApplication = sendingApplication;
        this.sendingFacility = sendingFacility;
        
        this.requestErrorDefaultErrorCode = requestErrorDefaultErrorCode;
        this.requestErrorDefaultAckTypeCode = requestErrorDefaultAckTypeCode;
        this.responseErrorDefaultErrorCode = responseErrorDefaultErrorCode;

        this.allowedRequestMessageTypes = allowedRequestMessageTypes;
        this.allowedRequestTriggerEvents = allowedRequestTriggerEvents;
        this.allowedResponseMessageTypes = allowedResponseMessageTypes;
        this.allowedResponseTriggerEvents = allowedResponseTriggerEvents;

        this.auditabilityFlags = auditabilityFlags;
        this.responseContinuabilityFlags = responseContinuabilityFlags;
    }

    
    /**
     * Returns <code>true</code> when request messages  
     * of the given type belong to this transaction. 
     */
    public boolean isSupportedRequestMessageType(String messageType) {
        return indexOf(messageType, allowedRequestMessageTypes) != -1;
    }

    
    /**
     * Returns <code>true</code> when the given trigger event  
     * is valid for request messages of the given type. 
     */
    public boolean isSupportedRequestTriggerEvent(String messageType, String triggerEvent) {
        int index = indexOf(messageType, allowedRequestMessageTypes); 
        if(index != -1) {
            String triggerEvents = allowedRequestTriggerEvents[index]; 
            return indexOf(triggerEvent, triggerEvents.split(" ")) != -1;
        }
        throw new IllegalStateException("Unknown message type " + messageType);
    }

    
    /**
     * Returns <code>true</code> when response messages  
     * of the given type belong to this transaction. 
     * "ACK" is always supported.
     */
    public boolean isSupportedResponseMessageType(String messageType) {
        return "ACK".equals(messageType) || 
               (indexOf(messageType, allowedResponseMessageTypes) != -1);
    }

    
    /**
     * Returns <code>true</code> when the given trigger event  
     * is valid for response messages of the given type. 
     * For "ACK" message type, every trigger event is considered valid.
     */
    public boolean isSupportedResponseTriggerEvent(String messageType, String triggerEvent) {
        if("ACK".equals(messageType)) {
            return true;
        }
        int index = indexOf(messageType, allowedResponseMessageTypes); 
        if(index != -1) {
            String triggerEvents = allowedResponseTriggerEvents[index];
            return indexOf(triggerEvent, triggerEvents.split(" ")) != -1;
        }
        throw new IllegalStateException("Unknown message type " + messageType);
    }
    
    
    /**
     * Returns <code>true</code> when messages of the given type are auditable.
     */
    public boolean isAuditable(String messageType) {
        int index = indexOf(messageType, allowedRequestMessageTypes);
        if(index != -1) {
            return auditabilityFlags[index];
        }
        throw new IllegalStateException("Unknown message type " + messageType);
    }


    /**
     * Returns <code>true</code> when messages of the given [request] type 
     * can be splitted by means of interactive continuation.
     * <p>
     * When this method returns true, the request message structure must 
     * be able to contain segments RCP, QPD, DSC; and the response message
     * structure -- segments DSC, QAK.
     */
    public boolean isContinuable(String messageType) {
        int index = indexOf(messageType, allowedRequestMessageTypes);
        if(index != -1) {
            return responseContinuabilityFlags[index];
        }
        throw new IllegalStateException("Unknown message type " + messageType);
    }
    
    
    /**
     * Helper method that returns the index of the given String 
     * in the given array, or -1 if not found.
     */
    private int indexOf(String s, String[] array) {
        for(int i = 0; i < array.length; ++i) {
            if(s.equals(array[i])) {
                return i;
            }
        }        
        return -1;
    }

    
    /**
     * Returns <code>true</code> if the given element of the given list 
     * contains a start segment of a data record. 
     */
    public boolean isDataStartSegment(List<String> segments, int index) {
        return false;
    }

    
    /**
     * Returns <code>true</code> if the given element of the given list 
     * contains a segment which does not belongs to any data record. 
     */
    public boolean isNotDataSegment(List<String> segments, int index) {
        return false;
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
}

