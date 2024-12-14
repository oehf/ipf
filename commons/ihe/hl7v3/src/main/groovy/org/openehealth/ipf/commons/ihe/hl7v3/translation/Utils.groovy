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

import ca.uhn.hl7v2.Version
import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Message
import groovy.xml.slurpersupport.GPathResult
import groovy.xml.MarkupBuilder
import org.openehealth.ipf.modules.hl7.dsl.Repeatable

import java.util.regex.Pattern

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/*
 * Generic routines for HL7 v2-to-v3 transformation.
 * @author Dmytro Rud, Marek Václavík
 */
class Utils {

    public static final String HL7V2_NULL = '""'
    private static final Pattern OID_PATTERN = Pattern.compile("[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+")

    /**
     * Returns the next repetition of the given HL7 v2 field/segment/etc.
     */
    static def nextRepetition(Repeatable closure) {
        return closure(closure().size())
    }


    static String senderOrReceiverIdentification(GPathResult senderOrReceiver, boolean useDeviceName) {
        def device = senderOrReceiver.device
        if (useDeviceName) {
            def deviceName = device.name?.text()
            if (deviceName) {
                return deviceName
            }
        }
        return idString(device.id)
    }    


    /**
     * Fills generic MSH fields (v2) from XML (v3). 
     */
    static void fillMshFromSlurper(
            GPathResult xml, 
            Message msg,
            boolean useSenderDeviceName,
            boolean useReceiverDeviceName) 
    {
        String sender = senderOrReceiverIdentification(xml.sender, useSenderDeviceName)
        msg.MSH[3][1] = sender
        if (Version.versionOf(msg.version).isGreaterThan(Version.V22) && OID_PATTERN.matcher(sender).matches()) {
            msg.MSH[3][2] = sender
            msg.MSH[3][3] = 'ISO'
        }
        msg.MSH[4][1] = 'unknown'
        String receiver = senderOrReceiverIdentification(xml.receiver, useReceiverDeviceName)
        msg.MSH[5][1] = receiver
        if (Version.versionOf(msg.version).isGreaterThan(Version.V22) && OID_PATTERN.matcher(receiver).matches()) {
            msg.MSH[5][2] = receiver
            msg.MSH[5][3] = 'ISO'
        }
        msg.MSH[6][1] = 'unknown'
        msg.MSH[7][1] = xml.creationTime.@value.text().replaceFirst('[.+-].*$', '')
        
        String idRoot = xml.id.@root?.text()
        msg.MSH[10] = idRoot ? idString(xml.id) : UUID.randomUUID().toString()
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
     * Fills an HL7 v2 CX data structure.
     */
    static void fillCx(Composite cx, String root, String extension, String assigningAuthority) {
        cx[1]    = extension
        cx[4][1] = assigningAuthority
        cx[4][2] = root
        cx[4][3] = getIso(root, extension)
    }

    
    /**
     * Fills an HL7 v2 CX data structure from a corresponding XML element.
     */
    static void fillCx(Composite cx, GPathResult element) {
        fillCx(cx, element.@root.text(), 
                   element.@extension.text(), 
                   element.@assigningAuthorityName.text())
    }

    
    /**
     * Creates a HL7 v3 name element from a v2 XPN.
     */
    static void createName(MarkupBuilder builder, Composite xpn) {
        String use = null, qualifier = null

        switch (xpn[7].value) {
            case 'A':
            case 'S':
                use = 'P'
                break
            case 'B':
            case 'M':
                qualifier = 'BR'
                break
            case 'C':
                use = 'L'
                qualifier = 'AD'
                break
            case 'D':
                // TODO
                break
            case 'I':
                use = 'C'
                break
            case 'L':
                use = 'L'
                break
            case 'N':
                qualifier = 'CL'
                break
            case 'P':
                qualifier = 'SP'
                break
            case 'T':
                use = 'I'
                break
        }

        builder.name(use: use) {
            def family = xpn[1][1].value
            def given  = xpn[2].value
            def middle = xpn[3].value
            def suffix = xpn[4].value
            def prefix = xpn[5].value
            def degree = xpn[6].value
            def profSuffix = (xpn.message.version == '2.5') ? xpn[14].value : null

            Map qualifierAttrs = [qualifier: qualifier]

            conditional(builder, 'family', family,     qualifierAttrs)
            conditional(builder, 'given',  given,      qualifierAttrs)
            if (middle && ! given) {
                builder.given('', qualifier: qualifier)
            }
            conditional(builder, 'given',  middle,     qualifierAttrs)
            conditional(builder, 'prefix', prefix,     qualifierAttrs)
            conditional(builder, 'suffix', suffix,     qualifierAttrs)
            conditional(builder, 'suffix', profSuffix, [qualifier: 'PR'])
            conditional(builder, 'suffix', degree,     [qualifier: 'AC'])
        }
    }

    
    /**
     * Creates an HL7 v3 "acknowledgement" element.
     */
    static void createQueryAcknowledgementElement(
            MarkupBuilder builder, 
            GPathResult xml, 
            Map status,
            String errorCodeSystem,
            String ackCodeFirstCharacter) 
    {
        
        def ackCode = ackCodeFirstCharacter[0] + status.ackCode[1]
        
        builder.acknowledgement {
            typeCode(code: ackCode)
            targetMessage {
                def quid = xml.id
                buildInstanceIdentifier(builder, 'id', false, quid.@root.text(), quid.@extension.text())
            }
            if (ackCode[1] != 'A') {
                acknowledgementDetail(typeCode: 'E') {
                    def errorCode = status.errorCode.map('hl7v2v3-error-codes')
                    code(code: errorCode, codeSystem: errorCodeSystem)
                    text(status.errorText)
                    status.errorLocations.each { location(it) }
                }
            }
        }
    }
    
    
    /**
     * Creates in the given target HL7 v2 data structure a set of repeatable fields
     * which correspond to the items of the given source map.
     * <p>
     * E.g. the source is <code>['abc' : '123', 'cde' : '456']</code> and the 
     * target is <code>msg.QPD[3]</code>.  A call to this function will lead to
     * <code>QPD|...|...|abc^123~cde^456|...</code>.  
     */
    static void fillFacets(List<Map<String, String>> sources, Repeatable target) {
        for (source in sources) {
            for (facet in source.findAll { it.value }) {
                def field = nextRepetition(target)
                field[1].value = facet.key
                field[2].value = facet.value
            }
        }
    }


    /**
     * Collects descriptions of occurred errors from all possible fields
     * of the given HL7 v2 NAK message.
     * <p>
     * Multiple repetitions of the ERR segment are currently not supported. 
     */
    static String collectErrorInfo(Message nak) {
        def fields = [nak.MSA[3], nak.MSA[6], nak.ERR[7], nak.ERR[8]]
        for (err1 in nak.ERR[1]()) {
            [1, 2, 4, 5].each { i -> fields << err1[4][i] }
        }
        [3, 5].each { i ->
            [2, 5, 9].each { j ->
                fields << nak.ERR[i][j]
            }
        }
        fields += nak.ERR[6]()

        return fields*.value.findAll { it }.join('; ')
    }


    /**
     * Creates an XML element that represents an instance identifier with given contents
     * (shortcut for using a single CX parameter instead of three Strings).
     */
    static void buildInstanceIdentifier(
            MarkupBuilder builder,
            String elementName,
            boolean useNullFlavor,
            Composite cx)
    {
        buildInstanceIdentifier(builder, elementName, useNullFlavor, cx[4][2].value, cx[1].value, cx[4][1].value)
    }

}
