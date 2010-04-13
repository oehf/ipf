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
package org.openehealth.ipf.commons.ihe.atna.custom;

import org.openhealthtools.ihe.atna.auditor.PIXAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.ihe.QueryEvent;
import org.openhealthtools.ihe.atna.auditor.utils.EventUtils;

/**
 * Implementation of an XCPD Auditor to send audit messages for
 * transactions under the consumer-side actors in
 * the Cross-Community Patient Discovery (XCPD) profile.
 * 
 * Supports sending ATNA Audit messages for the following IHE transactions:
 *  - ITI-55 
 *  - ITI-56
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 * @author <a href="mailto:unixoid@web.de">Dmytro Rud</a>
 */
public class XCPDInitiatingGatewayAuditor extends PIXAuditor
{
	/**
	 * Get an instance of the XCPD Auditor from the global context
	 * @return XCPD Auditor instance
	 */
	public static XCPDInitiatingGatewayAuditor getAuditor() {
		AuditorModuleContext ctx = AuditorModuleContext.getContext();
		return (XCPDInitiatingGatewayAuditor) ctx.getAuditor(XCPDInitiatingGatewayAuditor.class);
	}

	/**
     * Audits an ITI-55 XCPD Cross-Gateway Patient Discovery Query 
     * event for XCPD Initiating Gateway actors.
	 * 
	 * @param eventOutcome
	 * @param replyToUri
	 * @param respondingGatewayUri
	 * @param queryPayload
	 * @param queryId
	 * @param homeCommunityId
	 * @param patientIds
	 */
	public void auditXCPDPatientDiscoveryQueryEvent(
	        RFC3881EventOutcomeCodes eventOutcome,
	        String replyToUri,
			String respondingGatewayUri, 
			String queryPayload,
			String queryId,
			String homeCommunityId,
			String[] patientIds)
	{
		if (!isAuditorEnabled()) {
			return;
		}

        // Create query event
        QueryEvent queryEvent = new QueryEvent(
                true, 
                eventOutcome, 
                new CustomIHETransactionEventTypeCodes.CrossGatewayPatientDiscovery());

        queryEvent.setAuditSourceId(
                getAuditSourceId(), 
                getAuditEnterpriseSiteId());
        
        // Set the source active participant
        queryEvent.addSourceActiveParticipant(
                replyToUri, 
                getSystemAltUserId(), 
                null, 
                getSystemNetworkId(), 
                true);
        
        // Set the human requestor active participant
        if (!EventUtils.isEmptyOrNull(humanRequestor)) {
            queryEvent.addHumanRequestorActiveParticipant(
                    humanRequestor, 
                    null, null, null);
        }
        
        // Set the destination active participant
        queryEvent.addDestinationActiveParticipant(
                respondingGatewayUri, 
                null,
                null,
                EventUtils.getAddressForUrl(respondingGatewayUri, false),
                false);
        
        // Add a patient participant object for each patient id
        if (!EventUtils.isEmptyOrNull(patientIds)) {
            for (String patientId : patientIds) {
                queryEvent.addPatientParticipantObject(patientId);
            }
        }
        
        // Add the Query participant object
        queryEvent.addQueryParticipantObject(
                queryId, 
                homeCommunityId, 
                (queryPayload != null) ? queryPayload.getBytes() : null,
                null);    

        audit(queryEvent);
	}
	

	/**
     * Audits an ITI-56 XCPD Patient Location Query 
     * event for XCPD Initiating Gateway actors.
	 * 
	 * @param eventOutcome
	 * @param replyToUri
	 * @param respondingGatewayUri
	 * @param queryPayload
	 * @param patientId
	 */
    public void auditXCPDPatientLocationQueryEvent(
            RFC3881EventOutcomeCodes eventOutcome,
            String replyToUri,
            String respondingGatewayUri, 
            String queryPayload,
            String patientId)
    {
        if (!isAuditorEnabled()) {
            return;
        }

        // Create query event
        Iti56QueryEvent queryEvent = new Iti56QueryEvent(
                true, 
                eventOutcome, 
                new CustomIHETransactionEventTypeCodes.PatientLocationQuery());

        queryEvent.setAuditSourceId(
                getAuditSourceId(), 
                getAuditEnterpriseSiteId());
        
        // Set the source active participant
        queryEvent.addSourceActiveParticipant(
                replyToUri, 
                getSystemAltUserId(), 
                null, 
                getSystemNetworkId(), 
                true);
        
        // Set the human requestor active participant
        if (!EventUtils.isEmptyOrNull(humanRequestor)) {
            queryEvent.addHumanRequestorActiveParticipant(
                    humanRequestor, 
                    null, null, null);
        }
        
        // Set the destination active participant
        queryEvent.addDestinationActiveParticipant(
                respondingGatewayUri, 
                null,
                null,
                EventUtils.getAddressForUrl(respondingGatewayUri, false),
                false);
        
        // Add a patient participant object for the patient id
        queryEvent.addPatientParticipantObject(patientId);
        
        // Add the Query parameters object
        queryEvent.addQueryParametersObject(queryPayload);
        
        audit(queryEvent);
    }
    
}
