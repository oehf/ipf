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
package org.openehealth.ipf.platform.camel.ihe.mllp.pdqcore

import org.apache.camel.Exchange;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.AuditUtils
import org.openehealth.ipf.platform.camel.ihe.mllp.core.QueryAuditDataset

/**
 * Generic audit strategy for ITI-21 and ITI-22 (PDQ).
 * @author Dmytro Rud
 */
abstract class PdqAuditStrategy extends MllpAuditStrategy<QueryAuditDataset> {
    
    /**
     * Whether this strategy serves 
     * an ITI-21 Patient Demographic Query ("PDQ") or 
     * an ITI-22 Patient Demographic and Visit Query ("PDVQ"). 
     */
    final String transactionAbbreviation
    
    PdqAuditStrategy(boolean serverSide, String transactionAbbreviation) {
        super(serverSide)

        if( ! ['PDQ', 'PDVQ'].contains(transactionAbbreviation)) {
            throw new IllegalStateException('Bad transaction abbreviation')
        }
        
        this.transactionAbbreviation = transactionAbbreviation
    }

    
    // patient ID list is obligatory for auditing, but might
    // be not available in reality -- particularly when search
    // criteria were not based on patient ID and no patients
    // have been found
    String[] getNecessaryFields(String messageType) {
        return ['Payload', /*'PatientIds'*/] as String[]
    }


    void enrichAuditDatasetFromRequest(QueryAuditDataset auditDataset, MessageAdapter msg, Exchange exchange) {
        if(msg.QPD?.value) {
            // Try to extract a complete patient ID from query pieces.  
            // Double occurrences of components are not allowed, 
            // so we do not care of them.
            final String[] names  = ['@PID.3.1', '@PID.3.4.1', '@PID.3.4.2', '@PID.3.4.3']
            String[] pieces = new String[4]
            
            for(query in msg.QPD[3]()) {
                for(int i = 0; i < 4; ++i) {
                    if(( ! pieces[i]) && query[1].value.startsWith(names[i])) {
                        pieces[i] = query[2].value
                        break
                    }
                }
            }
            
            // concatenate found components, if their set is complete
            if(pieces[0] && (pieces[1] || (pieces[2] && pieces[3]))) {
                StringBuilder sb = new StringBuilder(pieces[0]).append('^^^')
                if(pieces[1]) {
                    sb.append(pieces[1])
                }
                if(pieces[2] && pieces[3]) {
                    sb.append('&').append(pieces[2]).append('&').append(pieces[3])
                }
                
                auditDataset.patientIds = [sb.toString()]
            }
        }
        
        // request message as String
        auditDataset.payload = AuditUtils.getRequestString(exchange, msg) 
    }

    
    void enrichAuditDatasetFromResponse(QueryAuditDataset auditDataset, MessageAdapter msg) {
        if(msg.MSH[9][1].value == 'RSP') {
            def patientIds = []
            for(group in msg.QUERY_RESPONSE()) {
                patientIds += AuditUtils.pidList(group.PID[3])
            }
            if(patientIds) {
                auditDataset.patientIds = auditDataset.patientIds ? 
                        patientIds + auditDataset.patientIds[0] :
                        patientIds
            }
        }
    }


    @Override
    QueryAuditDataset createAuditDataset() {
        return new QueryAuditDataset(serverSide)
    }
}
