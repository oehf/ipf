/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit;

import lombok.Getter;
import lombok.Setter;

public class BalpJwtExtractorProperties {

    @Getter
    @Setter
    private String[] idPath = new String[]{"jti"};

    @Getter @Setter
    private String[] issuerPath = new String[]{"iss"};

    @Getter @Setter
    private String[] clientIdPath = new String[]{"client_id", "cid"};

    @Getter @Setter
    private String[] subjectPath = new String[]{"sub"};

    @Getter @Setter
    private String[] subjectNamePath = new String[]{"extensions:ihe_iua:subject_name"};

    @Getter @Setter
    private String[] subjectOrganizationPath = new String[]{"extensions:ihe_iua:subject_organization"};

    @Getter @Setter
    private String[] subjectOrganizationIdPath = new String[]{"extensions:ihe_iua:subject_organization_id"};

    @Getter @Setter
    private String[] subjectRolePath = new String[]{"extensions:ihe_iua:subject_role"};

    @Getter @Setter
    private String[] purposeOfUsePath = new String[]{"extensions:ihe_iua:purpose_of_use"};

    @Getter @Setter
    private String[] homeCommunityIdPath = new String[]{"extensions:ihe_iua:home_community_id"};

    @Getter @Setter
    private String[] nationalProviderIdPath = new String[]{"extensions:ihe_iua:national_provider_identifier"};

    @Getter @Setter
    private String[] personIdPath = new String[]{"extensions:ihe_iua:person_id"};

    @Getter @Setter
    private String[] patientIdPath = new String[]{"extensions:ihe_bppc:patient_id"};

    @Getter @Setter
    private String[] docIdPath = new String[]{"extensions:ihe_bppc:doc_id"};

    @Getter @Setter
    private String[] acpPath = new String[]{"extensions:ihe_bppc:acp"};
}
