/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.experimental.UtilityClass;

/**
 * @author Dmytro Rud
 */
@UtilityClass
public class PpqConstants {

    public static class AttributeIds {
        public static final String EHEALTH_SUISSSE_2015_EPR_SPID                = "urn:e-health-suisse:2015:epr-spid";
        public static final String EHEALTH_SUISSSE_2015_REFERENCED_POLICY_SET   = "urn:e-health-suisse:2015:policy-attributes:referenced-policy-set";
        public static final String EHEALTH_SUISSSE_PRINCIPAL_ID                 = "urn:e-health-suisse:principal-id";
        public static final String EHEALTH_SUISSSE_PRINCIPAL_NAME               = "urn:e-health-suisse:principal-name";

        public static final String XACML_1_0_ACTION_ID                          = "urn:oasis:names:tc:xacml:1.0:action:action-id";
        public static final String XACML_1_0_CURRENT_DATE                       = "urn:oasis:names:tc:xacml:1.0:environment:current-date";
        public static final String XACML_1_0_RESOURCE_ID                        = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
        public static final String XACML_1_0_SUBJECT_ID                         = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
        public static final String XACML_1_0_SUBJECT_ID_QUALIFIER               = "urn:oasis:names:tc:xacml:1.0:subject:subject-id-qualifier";

        public static final String XACML_2_0_RESOURCE_ID                        = "urn:oasis:names:tc:xacml:2.0:resource:resource-id";
        public static final String XACML_2_0_SUBJECT_ROLE                       = "urn:oasis:names:tc:xacml:2.0:subject:role";

        public static final String XCA_2010_HOME_COMMUNITY_ID                   = "urn:ihe:iti:xca:2010:homeCommunityId";
        public static final String XDS_2007_CONFIDENTIALITY_CODE                = "urn:ihe:iti:xds-b:2007:confidentiality-code";

        public static final String XSPA_1_0_SUBJECT_REAL_NAME                   = "urn:oasis:names:tc:xspa:1.0:subject:subject-id";
        public static final String XSPA_1_0_SUBJECT_PURPOSE_OF_USE              = "urn:oasis:names:tc:xspa:1.0:subject:purposeofuse";
        public static final String XSPA_1_0_SUBJECT_ORGANIZATION_ID             = "urn:oasis:names:tc:xspa:1.0:subject:organization-id";
        public static final String XSPA_1_0_SUBJECT_ORGANIZATION_NAME           = "urn:oasis:names:tc:xspa:1.0:subject:organization";
    }

    public static class ActionIds {
        public static final String PPQ_ADD_POLICY   = "urn:e-health-suisse:2015:policy-administration:AddPolicy";
        public static final String XDS_STORED_QUERY = "urn:ihe:iti:2007:RegistryStoredQuery";
    }

    public static class CodingSystemIds {
        public static final String SWISS_PATIENT_ID       = "2.16.756.5.30.1.127.3.10.3";
        public static final String SWISS_SUBJECT_ROLE_OLD = "2.16.756.5.30.1.127.3.10.4";   // obsolete
        public static final String SWISS_PURPOSE_OF_USE   = "2.16.756.5.30.1.127.3.10.5";
        public static final String SWISS_SUBJECT_ROLE     = "2.16.756.5.30.1.127.3.10.6";
        public static final String SNOMED_CT              = "2.16.840.1.113883.6.96";
    }

    public static final String POLICY_ID_EXCLUSION_LIST = "urn:e-health-suisse:2015:policies:exclusion-list";

    public static final String NAME_QUALIFIER_EHEALTH_SUISSSE_COMMUNITY_INDEX = "urn:e-health-suisse:community-index";


}
