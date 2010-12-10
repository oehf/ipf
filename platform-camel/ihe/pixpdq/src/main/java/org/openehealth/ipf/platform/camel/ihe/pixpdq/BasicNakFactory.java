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

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception;
import org.openehealth.ipf.modules.hl7.AckTypeCode;
import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.NakFactory;

/**
 * Basic NAK factory for PIX Feed and PIX Update Notification.
 * @author Dmytro Rud
 */
public class BasicNakFactory implements NakFactory {

    @Override
    public Message createNak(
            ModelClassFactory classFactory,
            Message originalMessage,
            AbstractHL7v2Exception exception,
            AckTypeCode ackTypeCode) 
    {
        return MessageUtils.nak(
                classFactory,
                originalMessage,
                exception,
                ackTypeCode);
    }
}
