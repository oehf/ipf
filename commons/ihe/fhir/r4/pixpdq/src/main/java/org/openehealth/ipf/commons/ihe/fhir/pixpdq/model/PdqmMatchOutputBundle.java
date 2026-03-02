/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.Pdqm320;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Bundle for ITI-119 Patient Demographics Match Output as defined by the PDQm specification.
 * This is a searchset Bundle containing Patient resources that match the input patient demographics,
 * with each entry including a search score indicating the quality of the match.
 *
 * @author Christian Ohr
 * @since 5.2
 */
@ResourceDef(name = "Bundle", profile = PdqmProfile.ITI119_MATCH_OUTPUT_BUNDLE_PROFILE)
public class PdqmMatchOutputBundle extends Bundle implements Pdqm320 {

    private static final Predicate<BundleEntryComponent> IS_PDQ_PATIENT_ENTRY =
        entry -> entry.hasResource() && entry.getResource() instanceof PdqmPatient;

    public PdqmMatchOutputBundle() {
        super();
        setType(BundleType.SEARCHSET);
        PdqmProfile.ITI119_MATCH_OUTPUT_BUNDLE.setProfile(this);
    }

    @Override
    public PdqmMatchOutputBundle copy() {
        var dst = new PdqmMatchOutputBundle();
        copyValues(dst);
        return dst;
    }

    public void copyValues(PdqmMatchOutputBundle dst) {
        super.copyValues(dst);
    }

    /**
     * Get all matched patients from the bundle
     *
     * @return list of Patient resources
     */
    public List<Patient> getMatchedPatients() {
        return getEntry().stream()
            .filter(IS_PDQ_PATIENT_ENTRY)
            .map(entry -> (Patient) entry.getResource())
            .collect(Collectors.toList());
    }

    /**
     * Get all matched patients sorted by match score in descending order (highest score first)
     *
     * @return list of Patient resources sorted by match score, or empty list if no entries
     */
    public List<Patient> getMatchedPatientsSortedByScore() {
        return getEntry() == null ? Collections.emptyList() :
            getEntry().stream()
                .filter(IS_PDQ_PATIENT_ENTRY)
                .sorted(Comparator.comparing(
                    this::getMatchScore,
                    Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .map(entry -> (Patient) entry.getResource())
                .collect(Collectors.toList());

    }

    /**
     * Get the match score for a specific entry
     *
     * @param entry the bundle entry
     * @return the match score, or null if not set
     */
    public Double getMatchScore(BundleEntryComponent entry) {
        if (entry == null || !entry.hasSearch() || !entry.getSearch().hasScore()) {
            return null;
        }
        var score = entry.getSearch().getScoreElement().getValue();
        return score != null ? score.doubleValue() : null;
    }

    /**
     * Check if the bundle contains any matches
     *
     * @return true if there are matched patients
     */
    public boolean hasMatches() {
        return getEntry() != null && getEntry().stream()
            .anyMatch(IS_PDQ_PATIENT_ENTRY);
    }

    /**
     * Get the number of matched patients
     *
     * @return count of matched patients
     */
    public int getMatchCount() {
        return getEntry() == null ? 0 :
            (int) getEntry().stream()
                .filter(IS_PDQ_PATIENT_ENTRY)
                .count();
    }
}