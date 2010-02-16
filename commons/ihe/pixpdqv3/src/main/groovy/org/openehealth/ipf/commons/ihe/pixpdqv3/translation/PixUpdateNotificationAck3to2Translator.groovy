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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*

/**
 * PIX Update Notification Acknowledgement translator v3 to v2.
 * @author Marek Václavík, Dmytro Rud
 */
class PixUpdateNotificationAck3to2Translator implements Hl7TranslatorV3toV2 {
    
    /**
     * If true, MSH-9-3 of the output message will be filled. 
     * Otherwise, MSH-9-3 will remain empty.
     */
    boolean outputMessageStructure = true 

    /**
     * First character of the acknowledgment code.  Possible values are 'A' and 'C'.
     * <p>
     * Declared as String from technical reasons.
     */
    String ackCodeFirstCharacter = 'C'

        
    /**
     * Translates HL7 v3 acknowledgement message <tt>MCCI_IN000002UV01</tt> 
     * into HL7 v2 message </tt>ACK</tt>.
     */
    MessageAdapter translateV3toV2(String xmlText, MessageAdapter originalNotification) {
        def xml = slurp(xmlText)

        MessageAdapter rsp = originalNotification.ack()
        rsp.MSH[7][1] = xml.creationTime.@value.text()
        rsp.MSH[9][3] = this.outputMessageStructure ? 'ACK' : ''
        rsp.MSH[10]   = xml.id.@extension.text()
        rsp.MSA[1]    = ackCodeFirstCharacter[0] + xml.acknowledgement.typeCode.@code.text()[1]
        rsp.MSA[2]    = xml.acknowledgement.targetMessage.id.@extension.text()
        
        return rsp
	}
}
