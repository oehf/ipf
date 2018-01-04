/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v2;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.MllpAuditDataset;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;


/**
 * Basic ACK and NAK factory for HL7v2-based transactions.
 *
 * @author Dmytro Rud
 */
public class NakFactory<T extends MllpAuditDataset> {

    private final Hl7v2TransactionConfiguration<T> config;
    private final boolean useCAckTypeCodes;
    private final String defaultNakMsh9;


    /**
     * Generic constructor.
     *
     * @param config           Configuration of the transaction served by this factory.
     * @param useCAckTypeCodes if <code>true</code>, HL7v2 acknowledgement codes
     *                         <tt>CA</tt>, <tt>CE</tt>, <tt>CR</tt> will be used instead of the default
     *                         <tt>AA</tt>, <tt>AE</tt>, <tt>AR</tt>.
     * @param defaultNakMsh9   desired contents of MSH-9 in this transaction's default NAKs.
     */
    public NakFactory(Hl7v2TransactionConfiguration<T> config, boolean useCAckTypeCodes, String defaultNakMsh9) {
        this.config = requireNonNull(config);
        this.useCAckTypeCodes = useCAckTypeCodes;
        this.defaultNakMsh9 = requireNonNull(defaultNakMsh9);
    }


    /**
     * Short constructor which corresponds to <code>NakFactory(config, false, "ACK")</code>.
     *
     * @param config Configuration of the transaction served by this factory.
     */
    public NakFactory(Hl7v2TransactionConfiguration<T> config) {
        this(config, false, "ACK");
    }


    /**
     * Generates a transaction-specific HL7v2 ACK response message on the basis
     * of the original HAPI request message.
     *
     * @param originalMessage original HAPI request message.
     */
    public Message createAck(Message originalMessage) throws HL7Exception, IOException {
        return originalMessage.generateACK(useCAckTypeCodes ? AcknowledgmentCode.CA : AcknowledgmentCode.AA, null);
    }


    /**
     * Generates an HL7v2 NAK response message on the basis
     * of the thrown exception and the original HAPI request message.
     *
     * @param exception       thrown exception.
     * @param originalMessage original HAPI request message.
     * @param ackTypeCode     HL7v2 acknowledgement type code.
     */
    public Message createNak(Message originalMessage, HL7Exception exception, AcknowledgmentCode ackTypeCode) throws HL7Exception, IOException {
        return originalMessage.generateACK(ackTypeCode, exception);
    }


    /**
     * Generates an HL7v2 NAK response message on the basis
     * of the thrown exception and the original HAPI request message.
     *
     * @param t               thrown exception.
     * @param originalMessage original HAPI request message.
     */
    public Message createNak(Message originalMessage, Throwable t) throws HL7Exception, IOException {
        HL7Exception hl7Exception = getHl7Exception(t);
        return createNak(originalMessage, hl7Exception, getAckTypeCode(hl7Exception));
    }


    /**
     * Generates a "default" HL7v2 NAK message on the basis
     * of the thrown exception.
     *
     * @param e thrown exception.
     */
    public Message createDefaultNak(HL7Exception e) {
        HL7Exception hl7Exception = new HL7Exception(
                formatErrorMessage(e),
                config.getRequestErrorDefaultErrorCode(),
                e);

        return MessageUtils.defaultNak(
                hl7Exception,
                useCAckTypeCodes ? AcknowledgmentCode.CR : AcknowledgmentCode.AR,
                config.getHl7Versions()[0].getVersion(),
                config.getSendingApplication(),
                config.getSendingFacility(),
                defaultNakMsh9);
    }


    /**
     * Returns a HL7v2 exception that corresponds
     * to the given instance of {@link Throwable}.
     */
    protected HL7Exception getHl7Exception(Throwable t) {
        return getException(HL7Exception.class, t).orElseGet(() ->
                new HL7Exception(
                        formatErrorMessage(t),
                        config.getRequestErrorDefaultErrorCode(),
                        t));
    }


    /**
     * Returns a HL7v2 acknowledgement type code that corresponds
     * to the given instance of {@link Throwable}.
     */
    protected AcknowledgmentCode getAckTypeCode(Throwable t) {
        AcknowledgmentCode ackTypeCode = (t instanceof Hl7v2AcceptanceException) ? AcknowledgmentCode.AR : AcknowledgmentCode.AE;
        if (useCAckTypeCodes) {
            ackTypeCode = (ackTypeCode == AcknowledgmentCode.AR) ? AcknowledgmentCode.CR : AcknowledgmentCode.CE;
        }
        return ackTypeCode;
    }


    /**
     * Formats and returns error message of an exception.
     * <p>
     * In particular, all line break characters must be removed,
     * otherwise they will break the structure of an HL7 NAK.
     *
     * @param t thrown exception.
     * @return formatted error message from the given exception.
     */
    public static String formatErrorMessage(Throwable t) {
        String s = t.getMessage();
        if (s == null) {
            s = t.getClass().getName();
        }
        s = s.replace('\n', ';');
        s = s.replace('\r', ';');
        return s;
    }


    /**
     * Returns configuration of the transaction served by this factory.
     */
    protected Hl7v2TransactionConfiguration getConfig() {
        return config;
    }

    /**
     * Retrieves the given exception type from the exception.
     * <p/>
     * Is used to get the caused exception that typically have been wrapped in some sort
     * of Camel wrapper exception
     * <p/>
     * The strategy is to look in the exception hierarchy to find the first given cause that matches the type.
     * Will start from the bottom (the real cause) and walk upwards.
     *
     * @param type      the exception type wanted to retrieve
     * @param exception the caused exception
     * @return the exception found (or <tt>null</tt> if not found in the exception hierarchy)
     */
    private static <T extends Throwable> Optional<T> getException(Class<T> type, Throwable exception) {
        if (exception == null) {
            return Optional.empty();
        }

        //check the suppressed exception first
        Optional<T> result = identifyException(Stream.of(exception.getSuppressed()), type);
        if (result.isPresent()) return result;
        return createExceptionStream(exception)
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    private static <T extends Throwable> Optional<T> identifyException(Stream<Throwable> stream, Class<T> type) {
        return stream
                .filter(type::isInstance)
                .map(type::cast)
                .findFirst();
    }

    private static Stream<Throwable> createExceptionStream(Throwable exception) {
        List<Throwable> throwables = new ArrayList<>();
        Throwable current = exception;
        while (current != null) {
            throwables.add(current);
            current = current.getCause();
        }
        Collections.reverse(throwables);
        return throwables.stream();
    }

}
