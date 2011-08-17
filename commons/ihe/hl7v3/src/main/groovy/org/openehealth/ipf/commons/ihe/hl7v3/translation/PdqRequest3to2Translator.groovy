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
package org.openehealth.ipf.commons.ihe.hl7v3.translation;

import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*

import ca.uhn.hl7v2.parser.ModelClassFactory
import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp

/**
 * PDQ Query request translator HL7 v3 to v2.
 * @author Marek V�clav�k, Dmytro Rud
 */
class PdqRequest3to2Translator implements Hl7TranslatorV3toV2 {

    /**
     * If true, the translator will use <code>//sender/device/name/</code> for MSH-3. 
     * If the value of the name is empty or the false parameter is set to false, 
     * the translator will use a concatenation of <code>//sender/device/id/@root</code> 
     * and <code>//sender/device/id/@extension</code> instead. 
     */
 	boolean useSenderDeviceName = true
 	
    /**
     * If true, the translator will use <code>//receiver/device/name/</code> for MSH-3. 
     * If the value of the name is empty or the false parameter is set to false, 
     * the translator will use a concatenation of <code>//receiver/device/id/@root</code> 
     * and <code>//receiver/device/id/@extension</code> instead. 
     */
	boolean useReceiverDeviceName = true
	
	/**
	 * If true, MSH-9-3 of the output message will be filled. 
	 * Otherwise, MSH-9-3 will remain empty.
	 */
	boolean outputMessageStructure = true 
    
	/**
	 * Predefined fix value of QPD-1 (as String)
	 */
	String queryName = 'IHE PDQ Query' 

	/**
	 * Patient id having this root will be placed in PID-18 of the output message. 
	 * Otherwise it will be placed in PID-3 of the message.
	 */
	String accountNumberRoot = '1.2.3' 

    /**
     * If true, initial continuation quantity will be translated in to v2.
     * This gives the possibility to use HL7v2 interactive continuation support
     * from the IPF ITI-21 Camel component.
     */
	boolean translateInitialQuantity = false

	private static final ModelClassFactory MODEL_CLASS_FACTORY =
	    CustomModelClassUtils.createFactory('pdq', '2.5')


	/**
	 * Translates HL7 v3 request message <tt>PRPA_IN201305UV01</tt>
     * into HL7 v2 message <tt>QBP^Q22</tt>.
     * <p>
     * Continuation and Cancel requests are not supported.
     * Continuation support in the IPF ITI-21 or ITI-47 Camel components
     * should be used instead.
	 */
    MessageAdapter translateV3toV2(String v3requestString, MessageAdapter dummy = null) {
	    def v3request = slurp(v3requestString)
        def hapiMessage = MessageUtils.makeMessage(MODEL_CLASS_FACTORY, 'QBP', 'Q22', '2.5')
        def v2request = new MessageAdapter(hapiMessage)
        
        // Segment MSH
        fillMshFromSlurper(v3request, v2request, useSenderDeviceName, useReceiverDeviceName)
        if (! outputMessageStructure) {
            v2request.MSH[9][3] = ''
        }

        // determine data containers
        def queryByParameter  = v3request.controlActProcess.queryByParameter
        def parameterList     = queryByParameter.parameterList
	    def livingSubjectName = parameterList.livingSubjectName[0].value[0]
        def livingSubjectIds  = parameterList.livingSubjectId.value
        def patientAddress    = parameterList.patientAddress[0].value[0]
	    
        // find the first id with a root NOT identical to this.accountNumberRoot
	    def patientId = livingSubjectIds?.find { it.@root != accountNumberRoot }

        // find the first id with a root identical to this.accountNumberRoot   
        def accountNumber = livingSubjectIds?.find { it.@root == accountNumberRoot }
        
        // fill query facets
        boolean needWildcard = (livingSubjectName.@use == 'SRCH')
        def usableGivenNames = livingSubjectName.given.findAll { it.@qualifier.text() in ['', 'CL', 'IN'] }
        // TODO: regarding (livingSubjectName.@use == 'SRCH'): consider CP-308
        def queryParams = [
            '@PID.3.1'    : patientId.@extension.text(),
            '@PID.3.4.1'  : patientId.@assigningAuthorityName.text(),
            '@PID.3.4.2'  : patientId.@root.text(), 
            '@PID.3.4.3'  : getIso(patientId),
            '@PID.5.1'    : wildcardize(livingSubjectName.family.find { ! it.@qualifier.text() }.text(), needWildcard),
            '@PID.5.2'    : wildcardize(usableGivenNames[0].text(), needWildcard),
            '@PID.5.3'    : wildcardize(usableGivenNames[1].text(), needWildcard),
            '@PID.7'      : parameterList.livingSubjectBirthTime.value.@value.text(),
            '@PID.8'      : parameterList.livingSubjectAdministrativeGender.value.@code.text(),   
            '@PID.11.1'   : patientAddress.streetAddressLine.text(),
            '@PID.11.3'   : patientAddress.city.text(),
            '@PID.11.4'   : patientAddress.state.text(),
            '@PID.11.5'   : patientAddress.postalCode.text(),
            '@PID.11.6'   : patientAddress.country.text(),
            '@PID.18.1'   : accountNumber.@extension.text(),
            '@PID.18.4.2' : accountNumber.@root.text(), 
            '@PID.18.4.3' : getIso(accountNumber),
        ] 
        
        // Segment QPD
        v2request.QPD[1] = queryName
        v2request.QPD[2] = constructQueryId(queryByParameter)
        
        fillFacets(queryParams, v2request.QPD[3])

        for (scopingOrg in parameterList.otherIDsScopingOrganization) {
            def value = scopingOrg.value.@root.text()
            if (value) {
                def qpd84 = nextRepetition(v2request.QPD[8])[4]
                qpd84[2].value = value
                qpd84[3].value = 'ISO'
            }
	    }

        // Segment RCP
        v2request.RCP[1] = 'I'
        if (translateInitialQuantity) {
            v2request.RCP[2][1] = queryByParameter.initialQuantity.@value.text()
            v2request.RCP[2][2] = 'RD'
        }

        // user-defined enrichment/post-processing of the MessageAdapter
        postprocess(v2request, v3request)
        
        // return MessageAdapter
	    return v2request
	}


    private String wildcardize(String value, boolean needWildcard) {
        return (value && needWildcard) ? "*${value}*" : value 
    }
 

    /**
     * To be customized in derived classes. 
     */
    void postprocess(MessageAdapter v2request, GPathResult v3request) {
        // empty per default
    }
}
