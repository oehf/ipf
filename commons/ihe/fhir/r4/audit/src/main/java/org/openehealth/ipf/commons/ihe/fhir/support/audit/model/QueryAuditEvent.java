/*
 * Copyright 2026 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.AuditEntityType;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.RestfulInteraction;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;

import java.util.Base64;
import java.util.Date;

import static org.hl7.fhir.r4.model.codesystems.AuditEventType.REST;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;
import static org.openehealth.ipf.commons.ihe.fhir.support.audit.model.BalpConstants.BALP_QUERY_AUDIT_PROFILE;

/**
 * A basic AuditEvent profile for when a RESTful Query / Search action happens successfully.
 * The request does not have a Patient subject indicated.
 */
@ResourceDef(name = "AuditEvent", id = "QueryAuditEvent", profile = BALP_QUERY_AUDIT_PROFILE)
public class QueryAuditEvent extends AuditEvent {

    public QueryAuditEvent() {
        super();
        setAction(AuditEventAction.E);
        setOutcome(AuditEventOutcome._0);
        setRecorded(new Date());
        setType(new Coding()
            .setCode(REST.toCode())
            .setSystem(REST.getSystem())
            .setDisplay(REST.getDisplay()));
    }

    /**
     * Sets the search type (mandatory)
     *
     * @param searchType search type
     * @return this instance
     */
    public QueryAuditEvent setSearchType(RestfulInteraction searchType) {
        if (searchType != RestfulInteraction.SEARCH &&
            searchType != RestfulInteraction.SEARCHTYPE &&
            searchType != RestfulInteraction.SEARCHSYSTEM) {
            throw new IllegalArgumentException("Must be a search restful interaction");
        }
        addSubtype()
            .setCode(searchType.toCode())
            .setSystem(searchType.getSystem());
        return this;
    }

    /**
     * Sets the client agent (mandatory)
     *
     * @param clientReference client reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public QueryAuditEvent setClient(Reference clientReference,
                                     String networkAddress,
                                     AuditEventAgentNetworkType networkType) {
        return BalpAuditEventHelper.addAgent(this,
            Source, clientReference,
            networkAddress, networkType);
    }

    /**
     * Sets the server agent (mandatory)
     *
     * @param serverReference server reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public QueryAuditEvent setServer(Reference serverReference,
                                     String networkAddress,
                                     AuditEventAgentNetworkType networkType) {
        return BalpAuditEventHelper.addAgent(this,
            Destination, serverReference,
            networkAddress, networkType);
    }

    /**
     * Sets the user agent (optional)
     *
     * @param userReference user reference (can be display only)
     * @return this instance
     */
    public QueryAuditEvent setUser(Reference userReference) {
        return BalpAuditEventHelper.addUserAgent(this, V3ParticipationType.IRCP, userReference);
    }

    public QueryAuditEvent setTransaction(String xRequestId) {
        return BalpAuditEventHelper.addTransactionEntity(this, xRequestId);
    }

    /**
     * Sets the query entity (mandatory)
     *
     * @param query        mandatory original query, will be base64-encoded
     * @param cleanedQuery optional cleaned query, will not be base64-encoded
     * @return this instance
     */
    public QueryAuditEvent setQuery(byte[] query, String cleanedQuery) {
        addEntity()
            .setType(new Coding()
                .setCode(AuditEntityType._2.toCode())
                .setSystem(AuditEntityType._2.getSystem())
                .setDisplay(AuditEntityType._2.getDisplay()))
            .setRole(new Coding()
                .setCode(ObjectRole._24.toCode())
                .setSystem(ObjectRole._24.getSystem())
                .setDisplay(ObjectRole._24.getDisplay()))
            .setQuery(Base64.getEncoder().encode(query))
            .setDescription(cleanedQuery);
        return this;
    }
}
