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
package org.openehealth.ipf.commons.ihe.pixpdqv3.translation;

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7.message.MessageUtilsimport org.openehealth.ipf.commons.ihe.pixpdq.definitions.CustomModelClassUtils;import ca.uhn.hl7v2.parser.ModelClassFactory;
import groovy.util.slurpersupport.GPathResult
import static org.openehealth.ipf.commons.ihe.pixpdqv3.translation.Utils.*
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.*

/**
 * PDQ Query request translator HL7 v3 to v2.
 * @author Marek Václavík, Dmytro Rud
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
     * If true, interaction QUQI_IN000003UV01 will be accepted on the input. 
     * Otherwise QUQI_IN000003UV01 on the input will cause an exception. 
     */
	boolean supportQueryContinuation = false 
	
	private static final ModelClassFactory MODEL_CLASS_FACTORY =
	    CustomModelClassUtils.createFactory('pdq', '2.5')


	/**
	 * Translates HL7 v3 request messages <tt>PRPA_IN201305UV01</tt> and 
	 * <tt>QUQI_IN000003UV01</tt> into HL7 v2 message <tt>QBP^Q22</tt>.
	 */
    MessageAdapter translateV3toV2(String xmlText, MessageAdapter dummy = null) {
	    def xml = slurp(xmlText)
	    def interactionId = xml.interactionId.@extension.text()
	    if ((interactionId == 'QUQI_IN000003UV01') && !this.supportQueryContinuation) {
	        throw new UnsupportedOperationException('Query Continuation not supported')
	    }
	    
        def hapiMessage = MessageUtils.makeMessage(MODEL_CLASS_FACTORY, 'QBP', 'Q22', '2.5')
        def qry = new MessageAdapter(hapiMessage)
        
        // Segment MSH
        fillMshFromSlurper(xml, qry, this.useSenderDeviceName, this.useReceiverDeviceName)                       
        if (!this.outputMessageStructure) {
            qry.MSH[9][3] = ''
        }

        // PARSE HL7 V3 MESSAGE
        def queryByParameter  = xml.controlActProcess.queryByParameter
        def queryContinuation = xml.controlActProcess.queryContinuation

        def queryId = constructQueryId(queryByParameter) ?: constructQueryId(queryContinuation)
	    def queryInitialQuantity = queryByParameter.initialQuantity.@value.text()

        // determine data containers
        def parameterList     = queryByParameter.parameterList
	    def livingSubjectName = parameterList.livingSubjectName.value
        def livingSubjectId   = parameterList.livingSubjectId.value
        def patientAddress    = parameterList.patientAddress.value
	    
        // find the first id with a root NOT identical to this.accountNumberRoot
	    def patientId = livingSubjectId?.find { it.@root != this.accountNumberRoot }

        // find the first id with a root identical to this.accountNumberRoot   
        def accountNumber = livingSubjectId?.find { it.@root == this.accountNumberRoot }
        
        // fill query facets
        boolean needWildcard = (livingSubjectName.@use == 'SRCH')
        // TODO: regarding (livingSubjectName.@use == 'SRCH'): consider CP-308 at
        // ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2009/CPs/FinalText/CP-ITI-308-FT.doc
        def queryParams = [
            '@PID.3.1'    : patientId.@extension.text(),
            '@PID.3.4.1'  : patientId.@assigningAuthorityName.text(),
            '@PID.3.4.2'  : patientId.@root.text(), 
            '@PID.3.4.3'  : getIso(patientId),
            '@PID.5.1'    : wildcardize(livingSubjectName.family.text(), needWildcard),
            '@PID.5.2'    : wildcardize(livingSubjectName.given[0].text(), needWildcard),
            '@PID.5.3'    : wildcardize(livingSubjectName.given[1].text(), needWildcard),
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
        qry.QPD[1] = this.queryName
        qry.QPD[2] = queryId
        
        fillFacets(queryParams, qry.QPD[3]) 

        for (scopingOrg in parameterList.otherIDsScopingOrganization) {
            def value = scopingOrg.value.@root.text()
            if (value) {
                def qpd84 = nextRepetition(qry.QPD[8])[4]
                qpd84[2].value = value
                qpd84[3].value = 'ISO'
            }
	    }

        // Other segments: Handle continuations
        def continuation = xml.controlActProcess.queryContinuation
        def continuationQuantityValue = continuation.continuationQuantity.@value.text()
        def resultStart = continuation.startResultNumber.@value.text()    
                
        // Segment RCP
        qry.RCP[1] = 'I'
        qry.RCP[2] = continuationQuantityValue ? continuationQuantityValue : queryInitialQuantity

        // Segment DSC
	    if (resultStart) {
	        qry.DSC[1].value = resultStart 
	    }

        // user-defined enrichment/post-processing of the MessageAdapter
        postprocess(qry, xml)
        
        // return MessageAdapter
	    return qry
	}


    private String wildcardize(String value, boolean needWildcard) {
        return (value && needWildcard) ? "*${value}*" : value 
    }
 

    /**
     * To be customized in derived classes. 
     */
    void postprocess(MessageAdapter qry, GPathResult xml) {
        // empty per default
    }
}
