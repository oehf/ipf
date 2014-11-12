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
package org.openehealth.ipf.commons.ihe.hl7v3.translation

import ca.uhn.hl7v2.model.Message

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp

/**
 * Generic PIX acknowledgement translator v3 to v2.
 * @author Marek Václavík, Dmytro Rud
 */
class PixAck3to2Translator implements Hl7TranslatorV3toV2 {
    
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
    String ackCodeFirstCharacter = 'A'

        
    /**
     * Translates HL7 v3 acknowledgement message <tt>MCCI_IN000002UV01</tt> 
     * into HL7 v2 message </tt>ACK</tt>.
     */
    Message translateV3toV2(String xmlText, Message originalNotification) {
        def xml = slurp(xmlText)

        Message rsp = originalNotification.generateACK()
        rsp.MSH[7][1] = xml.creationTime.@value.text()
        rsp.MSH[9][3] = outputMessageStructure ? 'ACK' : ''
        rsp.MSH[10]   = xml.id.@extension.text()
        rsp.MSA[1]    = ackCodeFirstCharacter[0] + xml.acknowledgement.typeCode.@code.text()[1]
        rsp.MSA[2]    = xml.acknowledgement.targetMessage.id.@extension.text()
        
        return rsp
	}
}
