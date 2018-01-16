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

package org.openehealth.ipf.commons.audit.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Ohr
 */
public class DicomObjectDescriptionType implements Serializable, Validateable {

    /**
     * A single value of True or False indicating whether or not the data was encrypted.
     */
    @Getter @Setter
    private Boolean encrypted;

    /**
     * A single value of True or False indicating whether or not all patient identifying information was removed from the data
     */
    @Getter @Setter
    private Boolean anonymized;


    /**
     * MPPS Instance UID(s) associated with this participant object.
     */
    private List<String> mpps;

    /**
     * Accession Number(s) associated with this participant object.
     */
    private List<String> accession;

    /**
     * <p>The UIDs of SOP classes referred to in this participant object.</p>
     * <p>Required if ParticipantObjectIDTypeCode is (110180, DCM, "Study Instance UID") and any of the optional fields
     * (AccessionNumber, ContainsMPPS, NumberOfInstances, ContainsSOPInstances,Encrypted,Anonymized) are present in this
     * Participant Object. May be present if ParticipantObjectIDTypeCode is (110180, DCM, "Study Instance UID") even
     * though none of the optional fields are present.</p>
     */
    private List<SOPClass> sopClasses;

    /**
     * Study Instance UIDs
     */
    private List<String> studyIDs;

    public List<String> getMPPS() {
        if (mpps == null) {
            mpps = new ArrayList<>();
        }
        return this.mpps;
    }

    public List<String> getAccession() {
        if (accession == null) {
            accession = new ArrayList<>();
        }
        return this.accession;
    }

    public List<String> getStudyIDs() {
        if (studyIDs == null) {
            studyIDs = new ArrayList<>();
        }
        return this.studyIDs;
    }

    public List<SOPClass> getSOPClasses() {
        if (sopClasses == null) {
            sopClasses = new ArrayList<>();
        }
        return this.sopClasses;
    }

    @Override
    public void validate() {

    }

    public static class SOPClass {

        /**
         * The number of SOP Instances referred to by this participant object.
         */
        @Getter
        private final int numberOfInstances;

        @Getter @Setter
        private String uid;

        /**
         * SOP Instance UID value(s). Including the list of SOP Instances can create a fairly large audit message.
         * Under most circumstances, the list of SOP Instance UIDs is not needed for audit purposes.
         */
        private List<String> instanceUids;

        public SOPClass(int numberOfInstances) {
            this.numberOfInstances = numberOfInstances;
        }

        public List<String> getInstanceUids() {
            if (instanceUids == null) {
                instanceUids = new ArrayList<>();
            }
            return this.instanceUids;
        }
    }


}
