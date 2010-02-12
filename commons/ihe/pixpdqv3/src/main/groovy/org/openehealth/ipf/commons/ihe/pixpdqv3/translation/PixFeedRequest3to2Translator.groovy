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

import org.openehealth.ipf.modules.hl7.AbstractHL7v2Exception
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.GroupAdapterimport groovy.util.slurpersupport.GPathResultimport ca.uhn.hl7v2.model.Message
import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*

/**
 * PIX Feed Requests translator v3 to v2.
 * @author Marek Václavík, Dmytro Rud
 */
class PixFeedRequest3to2Translator implements Hl7TranslatorV3toV2 {
    
    /**
     * If true, the translator will use //sender/device/name/ for MSH-3. 
     * If the value of the name is empty or the false parameter is set to false, 
     * the translator will use a concatenation of //sender/device/id/@root and 
     * //sender/device/id/@extension instead. 
     */
	boolean useSenderDeviceName = true
	
	/**
	 * If true, the translator will use //receiver/device/name/ for MSH-3. 
	 * If the value of the name is empty or the false parameter is set to false, 
	 * the translator will use a concatenation of //receiver/device/id/@root and 
	 * //receiver/device/id/@extension instead.
	 */
	boolean useReceiverDeviceName = true 

	/**
	 * Name of the field (a String) for storing e-mail addresses.
	 * <p>
	 * On 'PID-13-1' e-mails are copied to PID-13-1 of the output.
	 * On 'PID-13-4' e-mails are copied to PID-13-4 of the output.
	 * Otherwise e-mail information is not copied to output.
	 */
	String copyEmailAs = 'PID-13-1' 
	
	/**
     * Name of the field (a String) for storing the account number.
     * <p>
	 * On 'PID-18' account number (recognized as such by the means of 
	 * accountNumberRoot) will be copied to PID-18 of the output.
	 * On other values account number will not be copied.
	 */
	String copyAccountNumberAs = 'PID-18' 
	
	/**
	 * Patient ID with this root (a String )found either under  
	 * //patient/id or //patient/asOtherIDs will be handled as  
	 * assigning authority for account numbers.
	 */
	String accountNumberRoot = '1.2.3' 

	/**
	 * Name of the filed (a String) to store 
	 * On 'PID-19' account number (recognized as such by the means of 
	 * nationalIdentifierRoot) will be copied to PID-19 of the output.
	 * On other values account number will not be copied. 
	 */
	String copyNationalIdentifierAs = 'PID-19'
	
	/**
	 * Patient ID with this root found either under //patient/id or 
	 * //patient/asOtherIDs will be handled as assigning authority 
	 * for a national patient identifier (SSN).
	 */
	String nationalIdentifierRoot = '2.16.840.1.113883.4.1'  
    
	/**
	 * Name of the field (as String) to store the birth name.
	 * <p>
	 * On 'PID.5' birth name will be copied into the output  
	 * as a repetition of PID.5 with PID.5.7 set to 'B'.
	 * On 'PID.6' birth name will be copied into the output 
	 * as the only repetition of PID-6.
	 * Otherwise birth name will be ignored. 
	 */
	String birthNameCopyTo = 'PID-5'  
    
	/**
	 * If true, patient IDs listed under "asOtherIDs" will be copied  
	 * to output. Otherwise "asOtherIDs" will be ignored.
	 */
	boolean useOtherIds = true
	
	/**
	 * If true, donor card will be assumed to be on file (for PD1-8).
	 */
	boolean donorCardOnFile = true
	
	
	/**
	 * Adds patient identifiers from the given GPath source.
	 */
	private void addPatientIdentifiers(GPathResult source, GroupAdapter grp) {
	    for (id in source.id) {
	        def root               = id.@root.text()
	        def extension          = id.@extension.text()
	        def assigningAuthority = id.@assigningAuthorityName.text()
	        
            switch (root) {
            case this.accountNumberRoot:
                if (this.copyAccountNumberAs == 'PID-18') {
                    if (grp.PID[18].value) {
                        throw new IllegalStateException('PID-18 already filled')
                    }
                    fillCx(grp.PID[18], root, extension, assigningAuthority)
                }
                break

            case this.nationalIdentifierRoot:
                if (this.copyNationalIdentifierAs == 'PID-19') {
                    if (grp.PID[19].value) {
                        throw new IllegalStateException('PID-19 already filled')
                    }
                    grp.PID[19] = extension
                }
                break

            default:
                fillCx(nextRepetition(grp.PID[3]), root, extension, assigningAuthority)
            }
	    }
	}
	
    /**
     * Translates HL7 v3 request messages <tt>PRPA_IN201301UV02</tt>, 
     * </tt>PRPA_IN201302UV02</tt> or </tt>PRPA_IN201304UV02</tt>
     * into HL7 v2 messages </tt>ADT_A01</tt> or </tt>ADT_A39</tt>.
     */
    MessageAdapter translateV3toV2(String xmlText, MessageAdapter dummy = null) {
	    def xml           = slurp(xmlText)
	    def interactionId = xml.interactionId.@extension.text()
        def triggerEvent  = interactionId.map('map-interactionId-eventStructure')[0]
	    
        def hapiMessage   = Message."ADT_${triggerEvent}"('2.3.1')        
        def adt           = new MessageAdapter(hapiMessage);          
        def grp           = (triggerEvent == 'A40') ? adt.PIDPD1MRGPV1(0) : adt

        // Segment MSH
        fillMshFromSlurper(xml, adt, this.useSenderDeviceName, this.useReceiverDeviceName)                       
        
        // Segment EVN
        adt.EVN[1] = adt.MSH[9][2]
        
        def time = xml.controlActProcess.subject.registrationEvent.effectiveTime.@value.text()
        if (!time) {
            time = xml.controlActProcess.effectiveTime.@value.text()
        }
        if (!time) {
            time = adt.MSH[7][1].value
        }
        adt.EVN[2] = time 
                
        // Segment PID
        // extract patient indentifiers
        def patient = xml.controlActProcess.subject.registrationEvent.subject1.patient
        def person = patient.patientPerson
                
        // PID-3 = identifiers
	    addPatientIdentifiers(patient, grp)
	    if (this.useOtherIds) {
            for (otherIds in person.asOtherIDs) {
                addPatientIdentifiers(otherIds, grp)
	        }
        }
        
        // PID-5 = names
        boolean birthNameProcessed = false
        for(name in person.name) {
            def family = name.family
            def given  = name.given

            def pid5 = nextRepetition(grp.PID[5])
            pid5[1][1].value = family?.find { it.@qualifier.text() != 'BR' }?.text()    
            pid5[2].value    = given[0].text()
            pid5[3].value    = given[1].text()
            pid5[4].value    = name.suffix.text()
            pid5[5].value    = name.prefix.text()

            def birthName = family?.find { it.@qualifier.text() == 'BR' }?.text()
            if (birthName) {
                if (birthNameProcessed) {
                    throw new IllegalStateException('A person can have only one birth name')
                }
                birthNameProcessed = true
                if (this.birthNameCopyTo == 'PID-5') {
                    pid5 = nextRepetition(grp.PID[5])
                    pid5[1][1] = birthName
                    pid5[7] = 'B'
                } else if (this.birthNameCopyTo == 'PID-6') {
                    grp.PID[6](0)[1] = birthName
                }
            }
        }

        // education level --> into the first repetition of PID-5
        grp.PID[5](0)[6].value = person.educationLevelCode.@code.text()
        
        // PID-7..8
        grp.PID[7][1].value = dropTimeZone(person.birthTime.@value.text())
        grp.PID[8].value    = person.administrativeGenderCode.@code.text().map('bidi-administrativeGender-administrativeGender')
        
        // PID-11 = addresses
        for (address in person.addr) {
            def pid11 = nextRepetition(grp.PID[11])
            pid11[1].value = address.streetAddressLine.text()
            pid11[3].value = address.city.text()
            pid11[4].value = address.state.text()
            pid11[5].value = address.postalCode.text()
            pid11[6].value = address.country.text()
        }
        
        // PID-13 = telecom        
        for (telecom in person.telecom) {            
            String value = telecom.@value.text()
            int pos      = value.indexOf(':')
            
            String type   = value.substring(0, pos)
            String number = value.substring(pos + 1)

            def pid13 = nextRepetition(grp.PID[13])

            switch (type) {
            case 'tel':
                pid13[3].value = (telecom.@use.text() == 'MC') ? 'MP' : 'PH'
                pid13[1].value = number
                break

            case 'mailto':
                pid13[3].value = 'Internet'
                if (this.copyEmailAs == 'PID-13-1') {                           
                    pid13[1].value = number
                } else if (this.copyEmailAs == 'PID-13-4'){
                    pid13[4].value = number
                }
                break
            
            case 'fax':
                pid13[3].value = 'FX'
                pid13[1].value = number
                break
            
            default:
                throw new IllegalStateException("Unknown telecom type ${type}")
            }
        }        
        
        // PID-16..                
        grp.PID[16] = person.maritalStatusCode.@code.text().map('patient-maritalStatus')
        grp.PID[17] = person.religiousAffiliationCode.@code.text().map('patient-religiousAffiliation')
        /*
        for (race in person.raceCode) {
            nextRepetition(grp.PID[10])[1] = race.@code.text().map('patient-race')
        }
        for (ethnicGroup in person.ethnicGroupCode) {
            nextRepetition(grp.PID[22])[1] = ethnicGroup.@code.text().map('patient-ethnic-groups')
        }
        
        // siblings
        grp.PID[24] = person.multipleBirthInd.@value.text().map('boolean')
        grp.PID[25] = person.multipleBirthOrderNumber.@value.text()

        // citizenshipd
        for (citizen in person.asCitizen) {
            nextRepetition(grp.PID[26])[1] = citizen.politicalNation.code.@code.text()
        }
        
        // poor Yorick...
        grp.PID[29] = dropTimeZone(person.deceasedTime.@value.text())
        grp.PID[30] = person.deceasedInd.@value.text().map('boolean')
        
        
        // Segment PD1
        if (person.organDonorId.@value.text() == 'true') {
            grp.PD1[8] = this.donorCardOnFile ? 'Y' : 'F'
        } else (person.organDonorId.@value.text() == 'false') { 
            grp.PD1[8] = 'N'
        }
        
        grp.PD1[2] = person.livingArrangementCode.@code.text()
        */
        
        
        // Segment NK1
        
        
        // Segment PV1
        grp.PV1[2] = 'O'
        
        // Segment MRG (for A40 only): extract merged patient ID
        if (triggerEvent == 'A40') {
            def priorId = xml.controlActProcess.subject.registrationEvent.replacementOf.priorRegistration.subject1.priorRegisteredRole.id
            fillCx(grp.MRG[1](0), priorId)
        }
        
        // return filled MessageAdapter
        return adt
	}
}
