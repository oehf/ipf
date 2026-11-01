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
package org.openehealth.ipf.commons.ihe.hl7v3.core.metadata;

import lombok.Data;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.AD;
import net.ihe.gazelle.hl7v3.datatypes.EN;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.datatypes.TEL;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified model of a patient registration ({@code registrationEvent} with its {@code patient} and
 * {@code patientPerson}), shared by the patient identity feed messages (ITI-44, ITI-46) and the patient demographics
 * query responses (ITI-47, ITI-55, see {@link MatchedPatient}).
 * <p>
 * Only the commonly used parts of the HL7v3 patient model are covered; transforming a full message into this model
 * may lose data.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
public class Patient {

    /**
     * The patient IDs ({@code patient/id}). At least one ID shall be provided. The list is never {@code null}.
     */
    private final List<II> patientIds = new ArrayList<>();

    /**
     * The person names. At least one name shall be provided. The list is never {@code null}.
     */
    private final List<PN> names = new ArrayList<>();

    /**
     * The person telecom addresses. The list is never {@code null}.
     */
    private final List<TEL> telecoms = new ArrayList<>();

    /**
     * The administrative gender code ({@code M}, {@code F}, {@code UN}).
     */
    private String administrativeGenderCode;

    /**
     * The birth time in HL7 DTM format, e.g. {@code 19630804}. The precision is kept as given.
     */
    private String birthTime;

    /**
     * Whether the person is deceased.
     */
    private Boolean deceasedInd;

    /**
     * The deceased time in HL7 DTM format.
     */
    private String deceasedTime;

    /**
     * Whether the person is part of a multiple birth.
     */
    private Boolean multipleBirthInd;

    /**
     * The multiple birth order number.
     */
    private Integer multipleBirthOrderNumber;

    /**
     * The person addresses. The list is never {@code null}.
     */
    private final List<AD> addresses = new ArrayList<>();

    /**
     * Other IDs of the person, with their scoping organizations ({@code patientPerson/asOtherIDs}).
     * The list is never {@code null}.
     */
    private final List<OtherIds> otherIds = new ArrayList<>();

    /**
     * The mother's maiden name, carried as name of a personal relationship with code {@code MTH}.
     */
    private EN mothersMaidenName;

    /**
     * The address of the birth place ({@code patientPerson/birthPlace/addr}).
     */
    private AD birthPlaceAddress;

    /**
     * The name of the birth place ({@code patientPerson/birthPlace/birthplace/name}).
     */
    private EN birthPlaceName;

    /**
     * The provider organization. Required in PDQv3 (ITI-47) responses.
     */
    private COCTMT150003UV03Organization providerOrganization;

    /**
     * The custodian OID ({@code registrationEvent/custodian/assignedEntity/id/@root}). In XCPD (ITI-55), this is
     * the homeCommunityId of the responding community.
     */
    private String custodianOid;
}
