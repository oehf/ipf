/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3

import groovy.util.slurpersupport.GPathResult;
import groovy.xml.MarkupBuilder;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.DateTime;

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * Factory class for HL7 v3 NAK messages.
 * @author Dmytro Rud
 */
class Hl7v3NakFactory {

    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.basicDateTimeNoMillis()

    /**
     * <tt>root</tt> attribute of message's <tt>id</tt> element.
     */
    static String messageIdRoot = '1.2.3'
    

    static String createNak(String originalMessage, Throwable t) {
        GPathResult xml
        try {
            xml = slurp(originalMessage)
        } catch (Exception e) {
            xml = slurp('<dummy/>')
        }
        
        OutputStream output = new ByteArrayOutputStream()
        MarkupBuilder builder = getBuilder(output)
        
        builder.MCCI_IN000002UV01(
            'ITSVersion' : 'XML_1.0',
            'xmlns'      : 'urn:hl7-org:v3',
            'xmlns:xsi'  : 'http://www.w3.org/2001/XMLSchema-instance',
            'xmlns:xsd'  : 'http://www.w3.org/2001/XMLSchema') 
        {
            buildInstanceIdentifier(builder, 'id', false, messageIdRoot, UUID.randomUUID().toString())
            creationTime(value: TIME_FORMAT.print(new DateTime())[0..7, 9..14])      
            interactionId(root: '2.16.840.1.113883.1.6', extension: 'MCCI_IN000002UV01')
            processingCode(code: 'P')
            processingModeCode(code: 'T')
            acceptAckCode(code: 'NE')
            buildReceiverAndSender(builder, xml, 'urn:hl7-org:v3')
            
            builder.acknowledgement {
                typeCode(code: 'AE')
                targetMessage {
                    def quid = xml.id
                    buildInstanceIdentifier(builder, 'id', false, quid.@root.text(), quid.@extension.text())
                }
                acknowledgementDetail(typeCode: 'E') {
                    code(code: 'INTERR', codeSystem: '2.16.840.1.113883.5.1100')
                    text(t.getMessage())
                }
            }
        }

        return output.toString()
    }
}
