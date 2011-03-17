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
package org.openehealth.ipf.platform.camel.ihe.pixpdq;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.*;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.util.Terser;
import org.apache.commons.lang.Validate;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAcceptanceException;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.NakFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * NAK factory for PDQ, PDVQ and PIX Query.
 * @author Dmytro Rud
 */
public class QpdAwareNakFactory extends BasicNakFactory implements NakFactory {

    private final String messageType, triggerEvent;


    public QpdAwareNakFactory(String messageType, String triggerEvent) {
        Validate.notEmpty(messageType);
        Validate.notEmpty(triggerEvent);

        this.messageType = messageType;
        this.triggerEvent = triggerEvent;
    }


    @Override
    public Message createNak(
            ModelClassFactory classFactory,
            Message originalMessage,
            AbstractHL7v2Exception exception,
            AckTypeCode ackTypeCode)
    {
        try {
            return (exception instanceof MllpAcceptanceException)
                    ? super.createNak(classFactory, originalMessage, exception, ackTypeCode)
                    : createNak0(classFactory, originalMessage, exception, ackTypeCode);

        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }


    @SuppressWarnings("unchecked")
    public Message createNak0(
            ModelClassFactory classFactory,
            Message originalMessage,
            AbstractHL7v2Exception exception,
            AckTypeCode ackTypeCode)
        throws HL7Exception, IllegalAccessException, NoSuchFieldException
    {
        AbstractMessage ack = (AbstractMessage) MessageUtils.response(
                classFactory,
                originalMessage,
                messageType,
                triggerEvent);
        exception.populateMessage(ack, ackTypeCode);

        Segment ackQak = (Segment) ack.get("QAK");
        Terser.set(ackQak, 2, 0, 1, 1, "AE");

        // try to copy QPD from the original request
        for (String name : originalMessage.getNames()) {
            if ("QPD".equals(name)) {
                Segment origQpd = (Segment) originalMessage.get("QPD");
                ack.insertRepetition("QPD", 0);

                Field field = AbstractGroup.class.getDeclaredField("structures");
                field.setAccessible(true);
                HashMap<String, ArrayList<Structure>> structures =
                        (HashMap<String, ArrayList<Structure>>) field.get(ack);
                structures.get("QPD").set(0, origQpd);

                Terser.set(ackQak, 1, 0, 1, 1, ((ST) origQpd.getField(2, 0)).getValue());

                break;
            }
        }

        return ack;
    }


    @Override
    public Message createAck(ModelClassFactory classFactory, Message originalMessage, AckTypeCode ackTypeCode) {
        throw new IllegalStateException("This transaction cannot return a simple ACK");
    }
}
