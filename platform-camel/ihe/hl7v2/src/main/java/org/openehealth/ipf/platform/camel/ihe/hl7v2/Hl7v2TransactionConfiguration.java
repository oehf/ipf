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
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import ca.uhn.hl7v2.parser.Parser;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static org.apache.commons.lang3.ArrayUtils.INDEX_NOT_FOUND;
import static org.apache.commons.lang3.ArrayUtils.contains;
import static org.apache.commons.lang3.ArrayUtils.indexOf;
import static org.apache.commons.lang3.Validate.*;

/**
 * Endpoint-agnostic parameters of an HL7v2-based transaction.
 * @author Dmytro Rud
 */
public class Hl7v2TransactionConfiguration {
    
    private final String hl7Version;
    private final String sendingApplication;
    private final String sendingFacility;
    
    private final int requestErrorDefaultErrorCode;
    private final int responseErrorDefaultErrorCode;

    private final String[] allowedRequestMessageTypes;
    private final String[] allowedRequestTriggerEvents;

    private final String[] allowedResponseMessageTypes;
    private final String[] allowedResponseTriggerEvents;

    private final boolean[] auditabilityFlags;
    private final boolean[] responseContinuabilityFlags;

    private final Parser parser;


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
     * @param responseErrorDefaultErrorCode
     *      default error code for response-related NAKs.
     * @param allowedRequestMessageTypes
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
     *      If <code>null</code>, the transaction will not perform any auditing.
     * @param responseContinuabilityFlags
     *      flags of whether the messages of corresponding
     *      type should support HL7 response continuations.
     *      If <code>null</code>, no continuations will be supported.
     * @param parser
     *      transaction-specific HL7v2 NAK parser.
     */
    public Hl7v2TransactionConfiguration(
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
            Parser parser)
    {
        notNull(hl7Version);
        notNull(sendingApplication);
        notNull(sendingFacility);

        noNullElements(allowedRequestMessageTypes);
        noNullElements(allowedRequestTriggerEvents);
        noNullElements(allowedResponseMessageTypes);
        noNullElements(allowedResponseTriggerEvents);
        notNull(parser);

        notEmpty(allowedRequestMessageTypes);
        isTrue(allowedRequestMessageTypes.length == allowedRequestTriggerEvents.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseMessageTypes.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseTriggerEvents.length);
        if (auditabilityFlags != null) {
            isTrue(allowedRequestMessageTypes.length == auditabilityFlags.length);
        }
        if (responseContinuabilityFlags != null){
            isTrue(allowedRequestMessageTypes.length == responseContinuabilityFlags.length);
        }
        
        // QC passed ;)
        
        this.hl7Version = hl7Version;
        this.sendingApplication = sendingApplication;
        this.sendingFacility = sendingFacility;
        
        this.requestErrorDefaultErrorCode = requestErrorDefaultErrorCode;
        this.responseErrorDefaultErrorCode = responseErrorDefaultErrorCode;

        this.allowedRequestMessageTypes = allowedRequestMessageTypes;
        this.allowedRequestTriggerEvents = allowedRequestTriggerEvents;
        this.allowedResponseMessageTypes = allowedResponseMessageTypes;
        this.allowedResponseTriggerEvents = allowedResponseTriggerEvents;

        this.auditabilityFlags = auditabilityFlags;
        this.responseContinuabilityFlags = responseContinuabilityFlags;

        this.parser = parser;
    }

    
    /**
     * Returns <code>true</code> when request messages  
     * of the given type belong to this transaction. 
     */
    public boolean isSupportedRequestMessageType(String messageType) {
        return contains(allowedRequestMessageTypes, messageType);
    }

    
    /**
     * Returns <code>true</code> when the given trigger event  
     * is valid for request messages of the given type. 
     */
    public boolean isSupportedRequestTriggerEvent(String messageType, String triggerEvent) {
        int index = indexOf(allowedRequestMessageTypes, messageType);
        if (index == INDEX_NOT_FOUND) {
            throw new IllegalArgumentException("Unknown message type " + messageType);
        }
        String triggerEvents = allowedRequestTriggerEvents[index];
        return contains(StringUtils.split(triggerEvents, ' '), triggerEvent);
    }

    
    /**
     * Returns <code>true</code> when response messages  
     * of the given type belong to this transaction. 
     * "ACK" is always supported.
     */
    public boolean isSupportedResponseMessageType(String messageType) {
        return "ACK".equals(messageType) || contains(allowedResponseMessageTypes, messageType);
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
        int index = indexOf(allowedResponseMessageTypes, messageType);
        if (index == INDEX_NOT_FOUND) {
            throw new IllegalArgumentException("Unknown message type " + messageType);
        }
        String triggerEvents = allowedResponseTriggerEvents[index];
        return contains(StringUtils.split(triggerEvents, ' '), triggerEvent);
    }
    
    
    /**
     * Returns <code>true</code> when messages of the given type are auditable.
     */
    public boolean isAuditable(String messageType) {
        if (auditabilityFlags == null) {
            return false;
        }
        int index = indexOf(allowedRequestMessageTypes, messageType);
        if (index != INDEX_NOT_FOUND) {
            return auditabilityFlags[index];
        }
        throw new IllegalArgumentException("Unknown message type " + messageType);
    }


    /**
     * Returns <code>true</code> when messages of the given [request] type 
     * can be split by means of interactive continuation.
     * <p>
     * When this method returns true, the request message structure must 
     * be able to contain segments RCP, QPD, DSC; and the response message
     * structure -- segments DSC, QAK.
     */
    public boolean isContinuable(String messageType) {
        if (responseContinuabilityFlags == null) {
            return false;
        }
        int index = indexOf(allowedRequestMessageTypes, messageType);
        if (index != INDEX_NOT_FOUND) {
            return responseContinuabilityFlags[index];
        }
        throw new IllegalArgumentException("Unknown message type " + messageType);
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
     * contains a segment which belongs to segments following the data 
     * records ("footer"). 
     */
    public boolean isFooterStartSegment(List<String> segments, int index) {
        return false;
    }
    
    
    // ----- automatically generated getters -----

    public String getHl7Version() {
        return hl7Version;
    }
    
    public int getRequestErrorDefaultErrorCode() {
        return requestErrorDefaultErrorCode;
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
    
    public String[] getAllowedRequestMessageTypes() {
        return allowedRequestMessageTypes;
    }

    public String[] getAllowedResponseMessageTypes() {
        return allowedResponseMessageTypes;
    }

    public Parser getParser() {
        return parser;
    }
}

