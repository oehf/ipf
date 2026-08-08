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
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.codesystems.ObjectRole;
import org.hl7.fhir.r4.model.codesystems.RestfulInteraction;
import org.hl7.fhir.r4.model.codesystems.V3ParticipationType;
import org.openehealth.ipf.commons.audit.model.ActiveParticipantType;

import java.util.Date;

import static org.hl7.fhir.r4.model.codesystems.AuditEventType.REST;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Destination;
import static org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode.Source;

/**
 * A basic AuditEvent profile for when a RESTful Update action happens successfully.
 * The request does not have a Patient subject indicated.
 *
 * @author Christian Ohr
 */
@ResourceDef(name = "AuditEvent", id = "UpdateAuditEvent", profile = BalpConstants.BALP_UPDATE_AUDIT_PROFILE)
public class UpdateAuditEvent extends BalpAuditEvent {

    public UpdateAuditEvent() {
        super();
        setAction(AuditEventAction.U);
        setOutcome(AuditEventOutcome._0);
        setRecorded(new Date());
        setType(new Coding()
            .setCode(REST.toCode())
            .setSystem(REST.getSystem())
            .setDisplay(REST.getDisplay()));
    }

    /**
     * @return DICOM 110153, which the update pattern fixes for the client agent
     */
    @Override
    protected Coding clientAgentType() {
        return dicomAgentType(Source);
    }

    /**
     * @return DICOM 110152, which the update pattern fixes for the server agent
     */
    @Override
    protected Coding serverAgentType() {
        return dicomAgentType(Destination);
    }

    /**
     * Sets the update type (mandatory)
     *
     * @param updateType update type
     * @return this instance
     */
    public UpdateAuditEvent setUpdateType(RestfulInteraction updateType) {
        if (updateType != RestfulInteraction.UPDATE &&
            updateType != RestfulInteraction.PATCH ) {
            throw new IllegalArgumentException("Must be a update restful interaction");
        }
        addSubtype()
            .setCode(updateType.toCode())
            .setSystem(updateType.getSystem());
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
    public UpdateAuditEvent setClient(Reference clientReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        addAgent(clientAgentType(), clientReference, networkAddress, networkType);
        return this;
    }

    /**
     * Sets the client agent (mandatory) from the active participant an ATNA audit record holds it in.
     *
     * @param client active participant standing for the client of the transaction
     * @return this instance
     */
    public UpdateAuditEvent setClient(ActiveParticipantType client) {
        addAgent(clientAgentType(), client);
        return this;
    }

    /**
     * Sets the server agent (mandatory)
     *
     * @param serverReference server reference (can be display only)
     * @param networkAddress  network address
     * @param networkType     network type
     * @return this instance
     */
    public UpdateAuditEvent setServer(Reference serverReference,
                                      String networkAddress,
                                      AuditEventAgentNetworkType networkType) {
        addAgent(serverAgentType(), serverReference, networkAddress, networkType);
        return this;
    }

    /**
     * Sets the server agent (mandatory) from the active participant an ATNA audit record holds it in.
     *
     * @param server active participant standing for the server of the transaction
     * @return this instance
     */
    public UpdateAuditEvent setServer(ActiveParticipantType server) {
        addAgent(serverAgentType(), server);
        return this;
    }

    /**
     * Sets the user agent (optional), as the information recipient
     *
     * @param userReference user reference (can be display only)
     * @return this instance
     */
    public UpdateAuditEvent setUser(Reference userReference) {
        return setUser(V3ParticipationType.IRCP, userReference);
    }

    /**
     * Sets the user agent (optional)
     *
     * @param typeCode      participation type of the user
     * @param userReference user reference (can be display only)
     * @return this instance
     */
    public UpdateAuditEvent setUser(V3ParticipationType typeCode, Reference userReference) {
        addUserAgent(typeCode, userReference);
        return this;
    }

    /**
     * Sets the entity carrying the X-Request-Id of the transaction (optional)
     *
     * @param xRequestId value of the X-Request-Id header
     * @return this instance
     */
    public UpdateAuditEvent setTransaction(String xRequestId) {
        addTransactionEntity(xRequestId);
        return this;
    }

    /**
     * Sets the data entity (mandatory)
     *
     * @param entity     entity data
     * @param entityRole entity role
     * @return this instance
     */
    public UpdateAuditEvent setData(Reference entity, ObjectRole entityRole) {
        addDataEntity(entity, entityRole);
        return this;
    }
}
