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
import groovy.util.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.hl7v2.PDQ

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*

/**
 * PDQ Query request translator HL7 v3 to v2.
 * @author Marek Vaclavik, Dmytro Rud
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


	/**
	 * Translates HL7 v3 request message <tt>PRPA_IN201305UV01</tt>
     * into HL7 v2 message <tt>QBP^Q22</tt>.
     * <p>
     * Continuation and Cancel requests are not supported.
     * Continuation support in the IPF ITI-21 or ITI-47 Camel components
     * should be used instead.
	 */
    Message translateV3toV2(String v3requestString, Message dummy = null) {
	    def v3request = slurp(v3requestString)
        Message v2request = PDQ.Interactions.ITI_21.hl7v2TransactionConfiguration.request()
        
        // Segment MSH
        fillMshFromSlurper(v3request, v2request, useSenderDeviceName, useReceiverDeviceName)
        if (! outputMessageStructure) {
            v2request.MSH[9][3] = ''
        }

        // determine data containers
        def queryByParameter  = v3request.controlActProcess.queryByParameter
        def parameterList     = queryByParameter.parameterList

        def queryParams = []

        addIdentifierParameters(parameterList.livingSubjectId*.value, queryParams)
        addNameParameters(parameterList.livingSubjectName*.value, queryParams)
        addOtherParameters(parameterList, queryParams)
        addAddressParameters(parameterList.patientAddress*.value, queryParams)

        
        // Segment QPD
        v2request.QPD[1] = queryName
        v2request.QPD[2] = idString(queryByParameter.queryId)
        
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


    protected void addIdentifierParameters(identifierValues, queryParams) {
        identifierValues.findAll{ it.@root != accountNumberRoot }.each {
            queryParams.add([
                    '@PID.3.1'  : it.@extension.text(),
                    '@PID.3.4.1': it.@assigningAuthorityName.text(),
                    '@PID.3.4.2': it.@root.text(),
                    '@PID.3.4.3': getIso(it),
            ])
        }
        identifierValues.findAll{ it.@root == accountNumberRoot }.each {
            queryParams.add([
                    '@PID.18.1'  : it.@extension.text(),
                    '@PID.18.4.2': it.@root.text(),
                    '@PID.18.4.3': getIso(it)
            ])
        }
    }

    protected void addNameParameters(nameValues, queryParams) {
        nameValues.each {
            boolean needWildcard = (it.@use == 'SRCH')
            def usableGivenNames = it.given.findAll { it.@qualifier.text() in ['', 'CL', 'IN'] }
            queryParams.add([
                    '@PID.5.1': wildcardize(it.family.find { !it.@qualifier.text() }.text(), needWildcard),
                    '@PID.5.2': wildcardize(usableGivenNames[0].text(), needWildcard),
                    '@PID.5.3': wildcardize(usableGivenNames[1].text(), needWildcard),
            ])
        }
    }

    protected void addOtherParameters(parameterList, queryParams) {
        queryParams.add([
                '@PID.7': parameterList.livingSubjectBirthTime.value.@value.text(),
                '@PID.8': parameterList.livingSubjectAdministrativeGender.value.@code.text(),
        ])
    }

    protected void addAddressParameters(addressValues, queryParams) {
        addressValues.each {
            queryParams.add([
                    '@PID.11.1': it.streetAddressLine.text(),
                    '@PID.11.3': it.city.text(),
                    '@PID.11.4': it.state.text(),
                    '@PID.11.5': it.postalCode.text(),
                    '@PID.11.6': it.country.text(),
            ])
        }
    }

    private String wildcardize(String value, boolean needWildcard) {
        return (value && needWildcard) ? "*${value}*" : value 
    }

    @Override
    void postprocess(Message qry, GPathResult xml) {
    }

}
