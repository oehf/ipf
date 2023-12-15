/*
 * Copyright 2023 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;

import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST;
import static org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfiles.UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE;

@ResourceDef(name = "List", id = "mhdUncontainedComprehensiveSubmissionSet", profile = UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST_PROFILE)
public class UncontainedComprehensiveSubmissionSetList<T extends UncontainedComprehensiveSubmissionSetList<T>> extends SubmissionSetList<T> {

    public UncontainedComprehensiveSubmissionSetList() {
        super();
        UNCONTAINED_COMPREHENSIVE_SUBMISSIONSET_TYPE_LIST.setProfile(this);
    }
}
