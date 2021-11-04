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

package org.openehealth.ipf.commons.ihe.xds.core.audit.codes;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;

/**
 * ParticipantObjectIdTypeCodes for the XDS query transactions in this module
 *
 * @author Christian Ohr
 * @since 3.5
 */
public enum XdsParticipantObjectIdTypeCode implements ParticipantObjectIdType, EnumeratedCodedValue<ParticipantObjectIdType> {

    RegistryStoredQuery("ITI-18", "Registry Stored Query"),
    CrossGatewayQuery("ITI-38", "Cross Gateway Query"),
    MultiPatientStoredQuery("ITI-51", "Multi-Patient Stored Query"),
    CrossCommunityFetch("ITI-63", "XCF Fetch"),
    QueryPharmacyDocuments("PHARM-1", "Query Pharmacy Documents");

    @Getter
    private final ParticipantObjectIdType value;

    XdsParticipantObjectIdTypeCode(String code, String displayName) {
        this.value = ParticipantObjectIdType.of(code, "IHE Transactions", displayName);
    }

}


