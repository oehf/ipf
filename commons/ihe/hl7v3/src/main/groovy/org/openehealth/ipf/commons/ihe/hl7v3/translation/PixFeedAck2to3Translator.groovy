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

import groovy.util.slurpersupport.GPathResult;

import org.openehealth.ipf.modules.hl7.message.MessageUtils;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * Translator for PIX Feed Responses v2 to v3.
 * @author Marek Václavík, Dmytro Rud
 */
class PixFeedAck2to3Translator implements Hl7TranslatorV2toV3 {

	/**
	 * First character of the acknowledgment code.  Possible values are 'A' and 'C'.
	 * <p>
	 * Declared as String from technical reasons.
	 */
	String ackCodeFirstCharacter = 'A'

	/**
	 * <tt>root</tt> attribute of message's <tt>id</tt> element.
	 */
	String messageIdRoot = '1.2.3'
	
    /**
     * Predefined fix value of <code>//acknowledgementDetail/code/@codeSystem</code>.
     */
    String errorCodeSystem = '2.16.840.1.113883.5.1100'
	    
	    
    /**
     * Translates an HL7 v2 <tt>ACK</tt> response message 
     * into HL7 v3 <tt>MCCI_IN000002UV01</tt> message.
     */
    String translateV2toV3(MessageAdapter rsp, String originalMessage, String charset) {
        def xml = slurp(originalMessage)
        
        def output = new ByteArrayOutputStream()
        def builder = getBuilder(output, charset)
        def status = getStatusInformation(rsp, xml)
        
        builder.MCCI_IN000002UV01(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : HL7V3_NSURI,
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema') 
        {         
            buildInstanceIdentifier(builder, 'id', false, this.messageIdRoot, rsp.MSH[10].value)    
            creationTime(value: MessageUtils.hl7Now())      
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'MCCI_IN000002UV01')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, xml, HL7V3_NSURI)
            createQueryAcknowledgementElement(builder, xml, status, this.errorCodeSystem, this.ackCodeFirstCharacter)
        }

        return output.toString(charset)
   }


    private Map getStatusInformation(MessageAdapter rsp, GPathResult xml) {
        def errorText      = ''
        def errorCode      = ''
        def errorLocations = []
        def ackCode        = rsp.MSA[1].value
        
        if (ackCode[1] != 'A') {
            errorCode = rsp.ERR[3][1].value ?: ''
            errorText = "PIXv2 Interface Reported [${collectErrorInfo(rsp)}]"
        } 

        return [ackCode:        ackCode, 
                errorText:      errorText, 
                errorCode:      errorCode, 
                errorLocations: errorLocations]
    }
}
