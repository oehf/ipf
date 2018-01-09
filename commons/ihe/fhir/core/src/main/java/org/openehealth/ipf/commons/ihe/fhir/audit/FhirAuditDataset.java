/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


/**
 * Generic audit dataset for FHIR-based IHE transactions.
 *
 * @author Christian Ohr
 * @since 3.1
 */
public abstract class FhirAuditDataset extends AuditDataset {

    /**
     * Request payload.
     */
    @Getter @Setter
    private String requestPayload;

    /**
     * Client user ID
     */
    @Getter @Setter
    private String userId;

    /**
     * Client IP address
     */
    @Getter @Setter
    private String clientIpAddress;

    /**
     * Local address
     */
    @Setter
    private String localAddress;

    /**
     * Remote address
     */
    @Setter @Getter
    private String remoteAddress;

    /**
     * Service endpoint URL
     */
    @Getter @Setter
    private String serviceEndpointUrl;

    /**
     * Patient IDs
     */
    @Getter
    private final Set<String> patientIds = new LinkedHashSet<>();

    /**
     * Access Control role(s) the human user holds that allows this transaction.
     */
    @Getter
    private final List<ActiveParticipantRoleId> userRoles = new ArrayList<>();


    @Getter @Setter
    private String userName;

    @Getter @Setter
    private String sourceUserId;

    @Getter @Setter
    private String destinationUserId;

    public FhirAuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * @return the first present patient ID
     * or <code>null</code> when no patient IDs have been collected.
     */
    public String getPatientId() {
        return patientIds.isEmpty() ? null : patientIds.iterator().next();
    }

    /**
     * @return The machine name or IP address
     */
    public String getLocalAddress() {
        return localAddress != null ? localAddress : AuditUtils.getLocalIPAddress();
    }

}
