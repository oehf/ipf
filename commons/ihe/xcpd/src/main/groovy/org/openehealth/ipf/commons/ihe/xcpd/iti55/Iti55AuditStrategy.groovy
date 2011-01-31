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
package org.openehealth.ipf.commons.ihe.xcpd.iti55;

import groovy.util.slurpersupport.GPathResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.xml.XmlYielder
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy

/**
 * Generic audit strategy for ITI-55 (XCPD).
 * @author Dmytro Rud
 */
abstract class Iti55AuditStrategy extends WsAuditStrategy {
    private static final transient Log LOG = LogFactory.getLog(Iti55AuditStrategy.class);

    Iti55AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }
    
    
    public WsAuditDataset createAuditDataset() {
        return new Iti55AuditDataset(isServerSide());
    }    
    

    /**
     * Returns ATNA response code on the basis of Acknowledgement.typeCode  
     * of the HL7 v3 output message:
     * <ul>
     *   <li> when the output message cannot be parsed -- "major failure",
     *   <li> when the typeCode is missing -- "major failure",
     *   <li> when the typeCode is 'OK' or 'NF' -- "success",
     *   <li> in all other cases ('AE' and 'QE') -- "serious failure".
     * </ul>
     * 
     * @param xml
     *      parsed response message.
     */
    @Override
    RFC3881EventOutcomeCodes getEventOutcomeCode(Object xml) {
        try {
            def code = xml.controlActProcess.queryAck.queryResponseCode.@code.text()
            if (!code) {
                // code not found -- bad XML
                return RFC3881EventOutcomeCodes.MAJOR_FAILURE
            }
            return (code == 'OK') || (code == 'NF') ? 
                    RFC3881EventOutcomeCodes.SUCCESS : 
                    RFC3881EventOutcomeCodes.SERIOUS_FAILURE

        } catch (Exception e) {
            LOG.error('Exception in ITI-55 audit strategy', e)
            return RFC3881EventOutcomeCodes.MAJOR_FAILURE
        }
    }

    
    /**
     * Extracts various audit information items from the <b>response</b> HL7 v3 message.
     * 
     * @param pojo
     *      String representation of response HL7 v3 message.
     * @param auditDataset
     *      target audit dataset.
     */
    @Override
    void enrichDataset(Object pojo, WsAuditDataset auditDataset) throws Exception {
        GPathResult xml = Hl7v3Utils.slurp((String) pojo)
        
        // query ID
        def queryId = xml.controlActProcess.queryByParameter.queryId
        def qidRoot = queryId.@root.text()
        def qidExtension = queryId.@extension.text()
        if (qidRoot || qidExtension) { 
             auditDataset.queryId = qidExtension + '@' + qidRoot
        }
        
        // home community ID
        auditDataset.homeCommunityId = 
            xml.receiver.device.asAgent.representedOrganization.id.@root.text() ?: null
        
        // patient IDs from request
        def patientIds = [] as Set<String>
        for (id in xml.controlActProcess.queryByParameter.parameterList.livingSubjectId) {
            addPatientIds(id.value, patientIds)
        }

        // patient IDs from response
        for (subject in xml.controlActProcess.subject) {
            def person = subject.registrationEvent.subject1.patient.patientPerson
            addPatientIds(person.id, patientIds)
            for (otherIds in person.asOtherIds) {
                addPatientIds(otherIds.id, patientIds)
            }
        }
        
        auditDataset.patientIds = patientIds.toArray() ?: null

        // event outcome code
        auditDataset.outcomeCode = getEventOutcomeCode(xml)
        
        // contents of <queryBaParameter>
        auditDataset.requestPayload = extractQueryByParameterElement(xml)
    }

    
    private static void addPatientIds(GPathResult source, Set<String> target) {
        for (node in source) {
            target << Hl7v3Utils.iiToCx(node)
        }
    }

    
    /**
     * Extracts contents of the <tt>&lt;queryByParameter&gt;</tt> element
     * from the given HL7v3 message.
     */ 
    private static String extractQueryByParameterElement(GPathResult xml) {
        def output = new ByteArrayOutputStream()
        def builder = Hl7v3Utils.getBuilder(output)
        XmlYielder.yieldElement(xml.controlActProcess.queryByParameter, builder, Hl7v3Utils.HL7V3_NSURI)
        return output.toString()
    }
    
}
