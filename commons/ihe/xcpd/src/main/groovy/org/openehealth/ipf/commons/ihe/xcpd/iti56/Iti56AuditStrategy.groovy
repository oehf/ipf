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
package org.openehealth.ipf.commons.ihe.xcpd.iti56;

import groovy.util.slurpersupport.GPathResult;
import groovy.util.slurpersupport.NodeChild;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Utils
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditStrategy

/**
 * Generic audit strategy for ITI-56 (XCPD).
 * @author Dmytro Rud
 */
abstract class Iti56AuditStrategy extends WsAuditStrategy {
    private static final transient Log LOG = LogFactory.getLog(Iti56AuditStrategy.class);

    Iti56AuditStrategy(boolean serverSide, boolean allowIncompleteAudit) {
        super(serverSide, allowIncompleteAudit)
    }
    
    
    public WsAuditDataset createAuditDataset() {
        return new Iti56AuditDataset(isServerSide());
    }    
    

    /**
     * Returns ATNA response code -- SUCCESS if the name of the root element
     * is "PatientLocationQueryResponse", SERIOUS_FAILURE otherwise,
     * MAJOR_FAILURE on exception.
     * 
     * @param xml
     *      response message as unparsed XML string.
     */
    @Override
    RFC3881EventOutcomeCodes getEventOutcomeCode(Object response) {
        try {
            GPathResult xml = Hl7v3Utils.slurp(response)
            NodeChild node = (NodeChild) xml
            return ((node.name() == 'PatientLocationQueryResponse') && 
                    (node.namespaceURI() == 'urn:ihe:iti:xcpd:2009')) ?
                            RFC3881EventOutcomeCodes.SUCCESS : RFC3881EventOutcomeCodes.SERIOUS_FAILURE
        } catch (Exception e) {
            LOG.error('Exception in ITI-56 audit strategy', e)
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

        // payload can be missing when the request is not valid
        if (auditDataset.requestPayload) {
            GPathResult patientId = Hl7v3Utils.slurp(auditDataset.requestPayload).RequestedPatientId
            auditDataset.patientId = Hl7v3Utils.iiToCx(patientId)
        }

        auditDataset.outcomeCode = getEventOutcomeCode(auditDataset.requestPayload)
    }

}
