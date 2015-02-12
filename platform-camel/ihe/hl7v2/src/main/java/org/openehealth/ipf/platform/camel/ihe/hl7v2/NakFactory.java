/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.hl7v2;

import java.io.IOException;

import ca.uhn.hl7v2.AcknowledgmentCode;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import org.apache.camel.util.ObjectHelper;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;


/**
 * Basic ACK and NAK factory for HL7v2-based transactions.
 * @author Dmytro Rud
 */
public class NakFactory {
    private final Hl7v2TransactionConfiguration config;

    private final boolean useCAckTypeCodes;
    private final String defaultNakMsh9;


    /**
     * Generic constructor.
     * @param config
     *      Configuration of the transaction served by this factory.
     * @param useCAckTypeCodes
     *      if <code>true</code>, HL7v2 acknowledgement codes
     *      <tt>CA</tt>, <tt>CE</tt>, <tt>CR</tt> will be used instead of the default
     *      <tt>AA</tt>, <tt>AE</tt>, <tt>AR</tt>.
     * @param defaultNakMsh9
     *      desired contents of MSH-9 in this transaction's default NAKs.
     */
    public NakFactory(Hl7v2TransactionConfiguration config, boolean useCAckTypeCodes, String defaultNakMsh9) {
        Validate.notNull(config);
        Validate.notEmpty(defaultNakMsh9);

        this.config = config;
        this.useCAckTypeCodes = useCAckTypeCodes;
        this.defaultNakMsh9 = defaultNakMsh9;
    }


    /**
     * Short constructor which corresponds to <code>NakFactory(config, false, "ACK")</code>.
     * @param config
     *      Configuration of the transaction served by this factory.
     */
    public NakFactory(Hl7v2TransactionConfiguration config) {
        this(config, false, "ACK");
    }


    /**
     * Generates a transaction-specific HL7v2 ACK response message on the basis
     * of the original HAPI request message.
     * @param originalMessage
     *      original HAPI request message.
     */
    public Message createAck(Message originalMessage) throws HL7Exception, IOException {
        return originalMessage.generateACK(useCAckTypeCodes ? AcknowledgmentCode.CA : AcknowledgmentCode.AA, null);
    }


    /**
     * Generates an HL7v2 NAK response message on the basis
     * of the thrown exception and the original HAPI request message.
     * @param exception
     *      thrown exception.
     * @param originalMessage
     *      original HAPI request message.
     * @param ackTypeCode
     *      HL7v2 acknowledgement type code.
     */
    public Message createNak(Message originalMessage, HL7Exception exception, AcknowledgmentCode ackTypeCode) throws HL7Exception, IOException {
        return originalMessage.generateACK(ackTypeCode, exception);
    }


    /**
     * Generates an HL7v2 NAK response message on the basis
     * of the thrown exception and the original HAPI request message.
     * @param t
     *      thrown exception.
     * @param originalMessage
     *      original HAPI request message.
     */
    public Message createNak(Message originalMessage, Throwable t) throws HL7Exception, IOException {
        HL7Exception hl7Exception = getHl7Exception(t);
        return createNak(originalMessage, hl7Exception, getAckTypeCode(hl7Exception));
    }


    /**
     * Generates a "default" HL7v2 NAK message on the basis
     * of the thrown exception.
     * @param e
     *      thrown exception.
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
        HL7Exception hl7Exception = ObjectHelper.getException(HL7Exception.class, t);
        if (hl7Exception == null) {
            hl7Exception = new HL7Exception(
                    formatErrorMessage(t),
                    config.getRequestErrorDefaultErrorCode(),
                    t);
        }
        return hl7Exception;
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
     * @param t
     *      thrown exception.
     * @return
     *      formatted error message from the given exception.
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

}
