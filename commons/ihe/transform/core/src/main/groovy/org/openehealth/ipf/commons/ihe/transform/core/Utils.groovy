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
package org.openehealth.ipf.commons.ihe.transform.core

import groovy.xml.MarkupBuilderimport groovy.util.XmlSlurper
import groovy.util.slurpersupport.GPathResultimport groovy.util.slurpersupport.Nodeimport javax.annotation.PreDestroy
import org.openehealth.ipf.modules.hl7dsl.SelectorClosureimport org.openehealth.ipf.modules.hl7dsl.MessageAdapter

/*
 * Generic routines vor HL7 v2-to-v3 transformation.
 * @author Dmytro Rud, Marek Václavík
 */
class Utils {

    /**
     * Creates and configures an XML builder based on the given {@link StringWriter}.
     */
    static MarkupBuilder getBuilder(StringWriter stringWriter) {
        MarkupBuilder builder = new MarkupBuilder(stringWriter)
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
    
    
    static String senderOrReceiverIdentification(GPathResult senderOrReceiver, boolean useDeviceName) {
        def device = senderOrReceiver.device
        if (useDeviceName) {
            def deviceName = device.name?.text()
            if (deviceName) {
                return deviceName
            }
        }
        return device.id.@extension.text() + '@' + device.id.@root.text()
    }    


    /**
     * Returns the next peretition of the given HL7 v2 field/segment/etc.
     */
    static def nextRepetition(SelectorClosure closure) {
        return closure(closure().size())
    }


    /**
     * Fills generic MSH fields (v2) from XML (v3). 
     */
    static void fillMshFromSlurper(
            GPathResult xml, 
            MessageAdapter msg,
            boolean useSenderDeviceName,
            boolean useReceiverDeviceName) 
    {
        msg.MSH[3][1] = senderOrReceiverIdentification(xml.sender, useSenderDeviceName)
        msg.MSH[4][1] = ''
        msg.MSH[5][1] = senderOrReceiverIdentification(xml.receiver, useReceiverDeviceName)
        msg.MSH[6][1] = ''
        msg.MSH[7][1] = xml.creationTime.@value.text().replaceFirst('[.+-].*$', '')
        msg.MSH[10]   = xml.id.@extension?.text() ?: System.currentTimeMillis()
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
     * Inserts an XML element with the given name if the corresponding content is present.
     */
    static void conditional(MarkupBuilder builder, String elementName, String content) {
        if (content) {
            builder."${elementName}"(content)
        }
    }
    

    /**
     * Determines whether CX-4-3 should be filled.
     * <p>
     * The parameter is expected to be an XML element able to contain attributes
     * "root" and "extension".
     */
    static String getIso(GPathResult source) {
        return getIso(source.@root?.text(), source.@extension?.text())
    }


    /**
     * Determines whether CX-4-3 should be filled.
     */
    static String getIso(String root, String extension) {
        return (root && extension) ? 'ISO' : ''
    }


    /**
     * Fills an HL7 v2 CX data struture.
     */
    static void fillCx(cx, String root, String extension, String assigningAuthority) {
        cx[1]    = extension
        cx[4][1] = assigningAuthority
        cx[4][2] = root
        cx[4][3] = getIso(root, extension)
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
    
}

