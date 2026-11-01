/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.core.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.ihe.gazelle.hl7v3.datatypes.AD;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.datatypes.TEL;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified model of an XCPD Cross Gateway Patient Discovery query (ITI-55, PRPA_IN201305UV02).
 * <p>
 * The homeCommunityId of the Initiating Gateway is carried in the sender's
 * {@link org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device#getRepresentedOrganizationIds() represented
 * organization IDs}.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class XcpdQueryRequest extends PatientDemographicsQueryRequest {

    public static final String RESPONSE_PRIORITY_IMMEDIATE = "I";
    public static final String RESPONSE_PRIORITY_DEFERRED = "D";

    /**
     * The OID of the assigning authority of the patient IDs in the Initiating Community
     * ({@code controlActProcess/authorOrPerformer/assignedDevice/id/@root}). It shall be provided.
     */
    private String communityPatientIdAssigningAuthority;

    /**
     * The response priority code, either {@code I} (immediate, default) or {@code D} (deferred). Use
     * {@link #setDeferredResponse(String)} to request a deferred response.
     */
    private String responsePriorityCode = RESPONSE_PRIORITY_IMMEDIATE;

    /**
     * The endpoint URL the deferred response shall be sent to ({@code respondTo/telecom/@value}). It shall be
     * provided if the response priority is deferred.
     */
    private String respondTo;

    /**
     * Query parameter {@code Patient.telecom}.
     */
    private TEL patientTelecom;

    /**
     * Query parameter {@code Person.MothersMaidenName}.
     */
    private PN mothersMaidenName;

    /**
     * Query parameter {@code LivingSubject.BirthPlace.Addr}.
     */
    private AD livingSubjectBirthPlaceAddress;

    /**
     * Query parameter {@code LivingSubject.BirthPlace.Place.Name}.
     */
    private EN livingSubjectBirthPlaceName;

    /**
     * Query parameter {@code AssignedProvider.id}. Each ID is sent in its own {@code principalCareProviderId}
     * parameter. The list is never {@code null}.
     */
    private final List<II> principalCareProviderIds = new ArrayList<>();

    /**
     * Requests a deferred response to be sent to the given endpoint URL.
     *
     * @param respondTo the endpoint URL of the Initiating Gateway's deferred response service.
     */
    public void setDeferredResponse(final String respondTo) {
        this.responsePriorityCode = RESPONSE_PRIORITY_DEFERRED;
        this.respondTo = respondTo;
    }

    /**
     * @return {@code true} if the query requests a deferred response.
     */
    public boolean isDeferred() {
        return RESPONSE_PRIORITY_DEFERRED.equals(this.responsePriorityCode);
    }

    @Override
    protected void copyQueryParameters(final PatientDemographicsQueryRequest query) {
        super.copyQueryParameters(query);
        if (query instanceof XcpdQueryRequest xcpdQuery) {
            this.communityPatientIdAssigningAuthority = xcpdQuery.getCommunityPatientIdAssigningAuthority();
            this.responsePriorityCode = xcpdQuery.getResponsePriorityCode();
            this.patientTelecom = xcpdQuery.getPatientTelecom();
            this.mothersMaidenName = xcpdQuery.getMothersMaidenName();
            this.livingSubjectBirthPlaceAddress = xcpdQuery.getLivingSubjectBirthPlaceAddress();
            this.livingSubjectBirthPlaceName = xcpdQuery.getLivingSubjectBirthPlaceName();
            this.principalCareProviderIds.addAll(xcpdQuery.getPrincipalCareProviderIds());
        }
    }
}
