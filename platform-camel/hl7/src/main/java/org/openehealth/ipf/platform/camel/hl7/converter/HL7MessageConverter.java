/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.platform.camel.hl7.converter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import jakarta.jms.*;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * HL7MessageConverter helps serializing a HL7 message to and from a string for the purpose of
 * routing it through a JMS queue. The same converter should be used on consumer and producer side.
 * <p/>
 * HAPI HL7 message objects have a reference to a transient parser, which in return has the reference
 * to the valid HapiContext. After deserialization, the context and parser references have to be
 * re-established.
 * <p/>
 * Currently, this class accepts String and HAPI message objects only. Deserialization will always
 * produce a HAPI Message object.
 */
public class HL7MessageConverter implements MessageConverter {

    private final HapiContext hapiContext;

    public HL7MessageConverter(HapiContext hapiContext) {
        this.hapiContext = hapiContext;
    }

    /**
     * Returns a {@link jakarta.jms.TextMessage} instance containing the HL7 message as string
     *
     * @param o       incoming object, can be either String or {@link ca.uhn.hl7v2.model.Message}
     * @param session JMS session
     * @return a {@link jakarta.jms.TextMessage} instance containing the HL7 message as string
     * @throws JMSException
     * @throws MessageConversionException
     */
    @Override
    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
        if (o instanceof String) {
            return session.createTextMessage((String)o);
        }
        if (o instanceof ca.uhn.hl7v2.model.Message) {
            try {
                return session.createTextMessage(((ca.uhn.hl7v2.model.Message) o).encode());
            } catch (HL7Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new MessageConversionException("Unexpected class : " + o.getClass());
    }

    /**
     * Returns a {@link ca.uhn.hl7v2.model.Message} built from the JMS message type, using the specified HapiContext
     *
     * @param message JMS {@link jakarta.jms.ObjectMessage} or {@link jakarta.jms.TextMessage}
     * @return a {@link ca.uhn.hl7v2.model.Message} built from the JMS message type
     * @throws JMSException
     * @throws MessageConversionException
     */
    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        if (message instanceof ObjectMessage) {
            var msg = (ca.uhn.hl7v2.model.Message) ((ObjectMessage) message).getObject();
            msg.setParser(hapiContext.getGenericParser());
            return msg;
        }
        if (message instanceof TextMessage) {
            var msg = ((TextMessage) message).getText();
            try {
                return hapiContext.getGenericParser().parse(msg);
            } catch (HL7Exception e) {
                throw new RuntimeException(e);
            }
        }
        throw new MessageConversionException("Unexpected class : " + message.getClass());
    }
}

