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

import ca.uhn.hl7v2.ErrorCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.Version;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.Terser;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static org.apache.commons.lang3.Validate.*;

/**
 * Endpoint-agnostic parameters of an HL7v2-based transaction.
 *
 * @author Dmytro Rud
 */
public class Hl7v2TransactionConfiguration {

    private static class Definition {
        private final Set<String> triggerEvents;
        private final boolean auditable;
        private final boolean responseContinuable;

        Definition(String triggerEventsString, boolean auditable, boolean responseContinuable) {
            this.triggerEvents = new HashSet<>(Arrays.asList(StringUtils.split(triggerEventsString, ' ')));
            this.auditable = auditable;
            this.responseContinuable = responseContinuable;
        }
        
        boolean isAllowedTriggerEvent(String triggerEvent) {
            return triggerEvents.contains("*") || triggerEvents.contains(triggerEvent);
        }
    }


    @Getter private final String sendingApplication;
    @Getter private final String sendingFacility;

    @Getter private final ErrorCode requestErrorDefaultErrorCode;
    @Getter private final ErrorCode responseErrorDefaultErrorCode;

    @Getter private final HapiContext hapiContext;
    @Getter private final Version[] hl7Versions;

    // true = request, false = response
    private final Map<Boolean, Map<String, Definition>> definitions;


    /**
     * Constructor.
     *
     * @param hl7Versions                   HL7 versions for acceptance checks (MSH-12). The first version of this array is used for default NAKs.
     * @param sendingApplication            sending application for default NAKs (MSH-3).
     * @param sendingFacility               sending application for default NAKs (MSH-4).
     * @param requestErrorDefaultErrorCode  default error code for request-related NAKs.
     * @param responseErrorDefaultErrorCode default error code for response-related NAKs.
     * @param allowedRequestMessageTypes    array of allowed request message types,
     *                                      e.g. <code>{"ADT", "MDM"}</code>.
     * @param allowedRequestTriggerEvents   array of allowed request trigger events
     *                                      for each request message type,
     *                                      e.g. <code>{"A01 A02 A03", "T06 T07 T08"}</code>.
     * @param allowedResponseMessageTypes   array of allowed response message types, e.g. <code>{"ACK", "RSP"}</code>.
     * @param allowedResponseTriggerEvents  array of allowed response trigger events for each message type,
     *                                      ignored for messages of type "ACK".
     * @param auditabilityFlags             flags of whether the messages of corresponding
     *                                      message type should be audited.
     *                                      If <code>null</code>, the transaction will not perform any auditing.
     * @param responseContinuabilityFlags   flags of whether the messages of corresponding
     *                                      message type should support HL7 response continuations.
     *                                      If <code>null</code>, no continuations will be supported.
     * @param hapiContext                   transaction-specific HAPI Context
     */
    public Hl7v2TransactionConfiguration(
            Version[] hl7Versions,
            String sendingApplication,
            String sendingFacility,
            ErrorCode requestErrorDefaultErrorCode,
            ErrorCode responseErrorDefaultErrorCode,
            String[] allowedRequestMessageTypes,
            String[] allowedRequestTriggerEvents,
            String[] allowedResponseMessageTypes,
            String[] allowedResponseTriggerEvents,
            boolean[] auditabilityFlags,
            boolean[] responseContinuabilityFlags,
            HapiContext hapiContext)
    {
        notNull(hl7Versions);
        notNull(sendingApplication);
        notNull(sendingFacility);

        noNullElements(allowedRequestMessageTypes);
        noNullElements(allowedRequestTriggerEvents);
        noNullElements(allowedResponseMessageTypes);
        noNullElements(allowedResponseTriggerEvents);
        notNull(hapiContext);

        notEmpty(allowedRequestMessageTypes);
        isTrue(allowedRequestMessageTypes.length == allowedRequestTriggerEvents.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseMessageTypes.length);
        isTrue(allowedRequestMessageTypes.length == allowedResponseTriggerEvents.length);
        if (auditabilityFlags != null) {
            isTrue(allowedRequestMessageTypes.length == auditabilityFlags.length);
        }
        if (responseContinuabilityFlags != null) {
            isTrue(allowedRequestMessageTypes.length == responseContinuabilityFlags.length);
        }

        // QC passed ;)

        this.hl7Versions = hl7Versions;
        this.sendingApplication = sendingApplication;
        this.sendingFacility = sendingFacility;

        this.requestErrorDefaultErrorCode = requestErrorDefaultErrorCode;
        this.responseErrorDefaultErrorCode = responseErrorDefaultErrorCode;

        this.hapiContext = hapiContext;

        this.definitions = new HashMap<>();
        definitions.put(true, createDefinitionsMap(allowedRequestMessageTypes, allowedRequestTriggerEvents,
                auditabilityFlags, responseContinuabilityFlags));
        definitions.put(false, createDefinitionsMap(allowedResponseMessageTypes, allowedResponseTriggerEvents,
                auditabilityFlags, responseContinuabilityFlags));

        if (! definitions.get(false).containsKey("ACK")) {
            definitions.get(false).put("ACK", new Definition("*", false, false));
        }
    }

    private static Map<String, Definition> createDefinitionsMap(
            String[] allowedMessageTypes,
            String[] allowedTriggerEvents,
            boolean[] auditabilityFlags,
            boolean[] responseContinuabilityFlags)
    {
        Map<String, Definition> result = new HashMap<>();
        for (int i = 0; i < allowedMessageTypes.length; ++i) {
            Definition definition = new Definition(
                    allowedTriggerEvents[i],
                    (auditabilityFlags != null) && auditabilityFlags[i],
                    (responseContinuabilityFlags != null) && responseContinuabilityFlags[i]);
            result.put(allowedMessageTypes[i], definition);
        }
        return result;
    }


    /**
     * Returns <code>true</code> when request messages of the given type are auditable.
     */
    public boolean isAuditable(String messageType) {
        Definition definition = definitions.get(true).get(messageType);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown message type " + messageType);
        }
        return definition.auditable;
    }


    /**
     * Returns <code>true</code> when request messages of the given type
     * can be split by means of interactive continuation.
     * <p/>
     * When this method returns true, the request message structure must
     * be able to contain segments RCP, QPD, DSC; and the response message
     * structure -- segments DSC, QAK.
     */
    public boolean isContinuable(String messageType) {
        Definition definition = definitions.get(true).get(messageType);
        if (definition == null) {
            throw new IllegalArgumentException("Unknown message type " + messageType);
        }
        return definition.responseContinuable;
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


    /**
     * Performs transaction-specific acceptance test of the given request message.
     *
     * @param message {@link Message} object.
     */
    public void checkRequestAcceptance(Message message) throws Hl7v2AcceptanceException {
        checkMessageAcceptance(message, true);
    }


    /**
     * Performs transaction-specific acceptance test of the given response message.
     *
     * @param message {@link Message} object.
     */
    public void checkResponseAcceptance(Message message) throws Hl7v2AcceptanceException {
        checkMessageAcceptance(message, false);

        try {
            if (!ArrayUtils.contains(
                    new String[]{"AA", "AR", "AE", "CA", "CR", "CE"},
                    new Terser(message).get("MSA-1")))
            {
                throw new Hl7v2AcceptanceException("Bad response: missing or invalid MSA segment", ErrorCode.REQUIRED_FIELD_MISSING);
            }
        } catch (HL7Exception e) {
            throw new Hl7v2AcceptanceException("Bad response: missing or invalid MSA segment", ErrorCode.APPLICATION_INTERNAL_ERROR);
        }
    }


    /**
     * Performs acceptance test of the given message.
     *
     * @param message   HAPI {@link Message} object.
     * @param isRequest <code>true</code> iff the message is a request.
     * @throws Hl7v2AcceptanceException when the message is not acceptable.
     */
    public void checkMessageAcceptance(
            Message message,
            boolean isRequest) throws Hl7v2AcceptanceException {
        try {
            Segment msh = (Segment) message.get("MSH");
            checkMessageAcceptance(
                    Terser.get(msh, 9, 0, 1, 1),
                    Terser.get(msh, 9, 0, 2, 1),
                    Terser.get(msh, 9, 0, 3, 1),
                    Terser.get(msh, 12, 0, 1, 1),
                    isRequest);
        } catch (Hl7v2AcceptanceException e) {
            throw e;
        } catch (HL7Exception e) {
            throw new Hl7v2AcceptanceException("Missing or invalid MSH segment: " + e.getMessage(), ErrorCode.APPLICATION_INTERNAL_ERROR);
        }
    }


    /**
     * Performs acceptance test of the message with the given attributes.
     *
     * @param messageType      value from MSH-9-1, can be empty or <code>null</code>.
     * @param triggerEvent     value from MSH-9-PAI2, can be empty or <code>null</code>.
     * @param messageStructure value from MSH-9-3, can be empty or <code>null</code>.
     * @param version          value from MSH-12, can be empty or <code>null</code>.
     * @param isRequest        <code>true</code> iff the message under consideration is a request.
     * @throws Hl7v2AcceptanceException when the message is not acceptable.
     */
    public void checkMessageAcceptance(
            String messageType,
            String triggerEvent,
            String messageStructure,
            String version,
            boolean isRequest) throws Hl7v2AcceptanceException
    {
        checkMessageVersion(version);

        Definition definition = definitions.get(isRequest).get(messageType);

        if (definition == null) {
            throw new Hl7v2AcceptanceException("Invalid message type " + messageType + ", must be one of " +
                    join(definitions.get(isRequest).keySet()), ErrorCode.UNSUPPORTED_MESSAGE_TYPE);
        }

        if (! definition.isAllowedTriggerEvent(triggerEvent)) {
            throw new Hl7v2AcceptanceException("Invalid trigger event " + triggerEvent + ", must be one of " +
                    join(definition.triggerEvents), ErrorCode.UNSUPPORTED_EVENT_CODE);
        }

        if (!StringUtils.isEmpty(messageStructure)) {
            // This may not work as the custom event map cannot be distinguished from the
            // default one! This needs to be fixed for HAPI 2.1
            String event = messageType + "_" + triggerEvent;
            String expectedMessageStructure;
            try {
                expectedMessageStructure = hapiContext.getModelClassFactory().getMessageStructureForEvent(event, Version.versionOf(version));
            } catch (HL7Exception e) {
                throw new Hl7v2AcceptanceException("Acceptance check failed", ErrorCode.UNKNOWN_KEY_IDENTIFIER);
            }

            // TODO when upgrading to HAPI 2.1 remove the constant IF statements
            if ("QBP_ZV1".equals(event)) {
                expectedMessageStructure = "QBP_Q21";
            } else if ("RSP_ZV2".equals(event)) {
                expectedMessageStructure = "RSP_ZV2";
            }

            // the expected structure must be equal to the actual one,
            // but second components may be omitted in acknowledgements
            boolean bothAreEqual = messageStructure.equals(expectedMessageStructure);
            boolean bothAreAcks = (messageStructure.startsWith("ACK") && expectedMessageStructure.startsWith("ACK"));
            if (!(bothAreEqual || bothAreAcks)) {
                throw new Hl7v2AcceptanceException("Invalid message structure " + messageStructure + 
                        ", must be " + expectedMessageStructure, ErrorCode.APPLICATION_INTERNAL_ERROR);
            }
        }
    }

    private void checkMessageVersion(String version) throws Hl7v2AcceptanceException {
        Version messageVersion = Version.versionOf(version);
        if (! ArrayUtils.contains(hl7Versions, messageVersion)) {
            throw new Hl7v2AcceptanceException("Invalid HL7 version " + version + ", must be one of " + supportedVersions(hl7Versions),
                    ErrorCode.UNSUPPORTED_VERSION_ID);
        }
    }

    private String supportedVersions(Version[] hl7versions) {
        StringBuilder builder = new StringBuilder();
        for (Version v : hl7versions) {
            builder.append(v.getVersion()).append(' ');
        }
        return builder.toString();
    }

    public Parser getParser() {
        return getHapiContext().getPipeParser();
    }

    private static String join(Collection<String> collection) {
        return StringUtils.join(collection, ' ');
    }

}

