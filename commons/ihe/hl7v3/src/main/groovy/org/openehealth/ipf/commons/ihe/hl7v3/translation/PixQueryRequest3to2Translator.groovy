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
import groovy.xml.slurpersupport.GPathResult
import org.openehealth.ipf.commons.ihe.hl7v2.PIX

import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.idString
import static org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils.slurp
import static org.openehealth.ipf.commons.ihe.hl7v3.translation.Utils.*

/**
 * PIX Query Requests translator v3 to v2.
 * @author Marek Václavík, Dmytro Rud
 */
class PixQueryRequest3to2Translator implements Hl7TranslatorV3toV2 {
    
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
     * Predefined fix value of QPD-1 (as String)
     */
    String queryName = 'IHE PIX Query' 

    /**
     * If true, MSH-9-3 of the output message will be filled. 
     * Otherwise, MSH-9-3 will remain empty.
     */
    boolean outputMessageStructure = true


    /**
     * Translates HL7 v3 request message <tt>PRPA_IN201309UV02</tt> 
     * into HL7 v2 message </tt>QBP_Q23</tt>.
     */
    Message translateV3toV2(String xmlText, Message dummy = null) {
        def xml = slurp(xmlText)
        def qry = PIX.QueryInteractions.ITI_9.hl7v2TransactionConfiguration.request('Q23')

        // Segment MSH
        fillMshFromSlurper(xml, qry, this.useSenderDeviceName, this.useReceiverDeviceName)                       
        if (!this.outputMessageStructure) {
            qry.MSH[9][3] = ''
        }

        // Segment QPD
		def queryByParameter = xml.controlActProcess.queryByParameter
        def params = queryByParameter.parameterList
        qry.QPD[1] = this.queryName
        qry.QPD[2] = idString(queryByParameter.queryId)
        fillCx(qry.QPD[3], params.patientIdentifier[0].value[0])
        for (source in params.dataSource) {
            def cx = nextRepetition(qry.QPD[4])
            cx[4][1] = source.value.@assigningAuthorityName.text()
            cx[4][2] = source.value.@root.text()
            cx[4][3] = 'ISO'
        }

        // Segment RCP
        qry.RCP[1] = 'I'

        postprocess(qry, xml)
        return qry
	}

    @Override
    void postprocess(Message qry, GPathResult xml) {
    }
}
