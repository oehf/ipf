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

package org.openehealth.ipf.commons.ihe.xds.core.audit;

import org.openehealth.ipf.commons.audit.model.TypeValuePairType;

import java.util.ArrayList;
import java.util.List;

import static org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditStrategy.*;

/**
 * @author Christian Ohr
 */
interface XdsBuilderUtils {

    static List<TypeValuePairType> makeDocumentDetail(String repositoryId, String homeCommunityId, String seriesInstanceId, String studyInstanceId) {
        List<TypeValuePairType> tvp = new ArrayList<>();
        if (studyInstanceId != null) {
            tvp.add(new TypeValuePairType(STUDY_INSTANCE_UNIQUE_ID, studyInstanceId));
        }
        if (seriesInstanceId != null) {
            tvp.add(new TypeValuePairType(SERIES_INSTANCE_UNIQUE_ID, seriesInstanceId));
        }
        if (repositoryId != null) {
            tvp.add(new TypeValuePairType(XdsAuditStrategy.REPOSITORY_UNIQUE_ID, repositoryId));
        }
        if (homeCommunityId != null) {
            tvp.add(new TypeValuePairType(IHE_HOME_COMMUNITY_ID, homeCommunityId));
        }
        return tvp;
    }
}
