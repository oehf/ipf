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
package org.openehealth.ipf.commons.ihe.hl7v3;

import groovy.util.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.openehealth.ipf.commons.xml.XmlYielder

/*
 * Generic routines for HL7 v3 processing.
 * @author Dmytro Rud, Marek Václavík
 */
class Hl7v3Utils {

    public static final String HL7V3_NSURI = 'urn:hl7-org:v3'
    private static final DateTimeFormatter TIME_FORMAT = ISODateTimeFormat.basicDateTimeNoMillis()
    
    
    /**
     * Returns current timestamp in the format prescribed by HL7. 
     */
    static String hl7timestamp() {
        return TIME_FORMAT.print(new DateTime())[0..7, 9..14]
    }

    
    /**
     * Creates and configures an XML builder based on the given output stream.
     */
    static MarkupBuilder getBuilder(OutputStream output, String charset = 'UTF-8') {
        Writer writer = new OutputStreamWriter(output, charset)
        MarkupBuilder builder = new MarkupBuilder(writer)
        builder.setDoubleQuotes(true)
        builder.setOmitNullAttributes(true)
        builder.setOmitEmptyAttributes(true)
        return builder
    }
    
    
    /**
     * Parses the given XML document and performs some post-configuration 
     * of the generated {@link GPathResult} object. 
     */
    static GPathResult slurp(String document) {
        GPathResult xml = new XmlSlurper(false, true).parseText(document)
        xml.declareNamespace(
            '*'   : 'urn:hl7-org:v3',              
            'xsi' : 'http://www.w3.org/2001/XMLSchema-instance',
            'xsd' : 'http://www.w3.org/2001/XMLSchema')
        return xml
    }
    
    
    /**
     * Creates an XML element that represents an instance identifier with given contents.
     * <p>
     * Both root and extension can be empty or <code>null</code>.
     */
    static void buildInstanceIdentifier(
            MarkupBuilder builder, 
            String elementName, 
            boolean useNullFlavor, 
            String root, 
            String extension,
            String assigningAuthorityName = null) 
    {    
        if (root || extension) {
            def map = [root: root, 
                       extension: extension, 
                       assigningAuthorityName: assigningAuthorityName].findAll { it.value } 
            builder."$elementName"(map)
        } else if (useNullFlavor){
            builder."$elementName"(nullFlavor: 'UNK')
        }
    }         


    /**
     * Yields crossed sender and receiver elements from original message.
     */
    static void buildReceiverAndSender(
            MarkupBuilder builder, 
            GPathResult originalXml, 
            String defaultNamespaceUri) 
    {
        builder.receiver(typeCode: 'RCV') {
            XmlYielder.yieldChildren(originalXml.sender, builder, defaultNamespaceUri)
        }
        builder.sender(typeCode: 'SND') {
            XmlYielder.yieldChildren(originalXml.receiver, builder, defaultNamespaceUri)
        }
    }

    
    /**
     * Inserts an XML element with the given name and given attributes from the map.
     */
    static void conditional(
    		MarkupBuilder builder,
    		String elementName,
    		String content,
    		Map attributes = [:]) {
        if (content) {
            builder."${elementName}"(attributes, content)
        }
    }
    
    
    /**
     * Removes time zone from date/time representation (i.e. suffix "[+-].*").
     */
    static String dropTimeZone(String s) {
        int pos = s.indexOf('+')
        if (pos == -1) {
            pos = s.indexOf('-')
        }
        return (pos > 0) ? s.substring(0, pos) : s
    }
    

    /**
     * Creates an HL7 v3 "custodian" element.
     */
    static createCustodian(MarkupBuilder builder, String mpiSystemIdRoot, mpiSystemIdExtension) {
        builder.custodian(typeCode: 'CST') {
            assignedEntity(classCode: 'ASSIGNED') {
                buildInstanceIdentifier(builder, 'id', false, 
                        mpiSystemIdRoot, mpiSystemIdExtension)
            }
        }
    }
    
    
    /**
     * Some schemas prescribe the existence of an patientPerson element,
     * but we do not have any data to fill in there.
     */
    static void fakePatientPerson(MarkupBuilder builder) {
        builder.patientPerson(classCode: 'PSN', determinerCode: 'INSTANCE') {
            name(nullFlavor: 'NA')
        }
    }

    
    /**
     * Creates string representation of an HL7v2 CX field from the given HL7v3 id element.
     * When the element is not well-formed, returns empty string.
     */
    static String iiToCx(GPathResult xmlIdNode) {
        String root = xmlIdNode.@root.text()
        String extension = xmlIdNode.@extension.text()
        String assigningAuthority = xmlIdNode.@assigningAuthorityName.text()
        
        if (root || extension || assigningAuthority) {
            return new StringBuilder()
                .append(extension)
                .append('^^^')
                .append(assigningAuthority)
                .append('&')
                .append(root)
                .append((root || extension) ? '&ISO' : '')
                .toString()
        }
        
        return ''
    }

    /**
     * Takes an element which contains an ID (II data type)
     * and returns its extension and root parts concatenated with '@'.
     * @param element
     *      HL7v3 II element.
     * @return
     *      "extension @ root".
     */
    static String idString(GPathResult id) {
        return "${id.@extension.text()}@${id.@root.text()}"
    }


    /**
     * Renders the given GPath source to String.
     */
    static String render(GPathResult source) {
        OutputStream output = new ByteArrayOutputStream()
        MarkupBuilder builder = getBuilder(output)
        XmlYielder.yieldElement(source, builder, HL7V3_NSURI)
        return output.toString()
    }
}
