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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Base of the simplified models of a patient demographics query (PRPA_IN201305UV02), shared by
 * PDQv3 (ITI-47, {@link PdqV3QueryRequest}) and XCPD (ITI-55, {@link XcpdQueryRequest}).
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public abstract class PatientDemographicsQueryRequest extends Hl7v3QueryRequest {

    /**
     * Query parameter {@code LivingSubject.id}. Each ID is sent in its own {@code livingSubjectId} parameter.
     * The list is never {@code null}.
     */
    private final List<II> livingSubjectIds = new ArrayList<>();

    /**
     * Query parameter {@code LivingSubject.name}. Each name is sent in its own {@code livingSubjectName} parameter.
     * The list is never {@code null}.
     */
    private final List<EN> livingSubjectNames = new ArrayList<>();

    /**
     * Query parameter {@code LivingSubject.administrativeGender} ({@code M}, {@code F}, {@code UN}).
     */
    private String livingSubjectAdministrativeGenderCode;

    /**
     * Query parameter {@code LivingSubject.birthTime} in HL7 DTM format, e.g. {@code 19630804}.
     */
    private String livingSubjectBirthTime;

    /**
     * Query parameter {@code Patient.addr}.
     */
    private AD patientAddress;

    /**
     * The minimum degree of match (0-100) requested by the query ({@code matchCriterionList/minimumDegreeMatch}).
     */
    private Integer minimumDegreeMatch;

    /**
     * The match algorithm requested by the query ({@code matchCriterionList/matchAlgorithm}).
     */
    private String matchAlgorithm;

    /**
     * Copies the query ID and query parameters (but not the message header) from another query. Subclasses
     * overwrite this to copy their specific query parameters.
     *
     * @param query The query to copy from.
     */
    protected void copyQueryParameters(final PatientDemographicsQueryRequest query) {
        Objects.requireNonNull(query);
        setQueryId(query.getQueryId());
        this.livingSubjectIds.addAll(query.getLivingSubjectIds());
        this.livingSubjectNames.addAll(query.getLivingSubjectNames());
        this.livingSubjectAdministrativeGenderCode = query.getLivingSubjectAdministrativeGenderCode();
        this.livingSubjectBirthTime = query.getLivingSubjectBirthTime();
        this.patientAddress = query.getPatientAddress();
        this.minimumDegreeMatch = query.getMinimumDegreeMatch();
        this.matchAlgorithm = query.getMatchAlgorithm();
    }
}
