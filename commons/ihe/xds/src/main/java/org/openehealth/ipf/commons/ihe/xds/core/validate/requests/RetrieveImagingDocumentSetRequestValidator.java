/*
 * Copyright 2012 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.validate.requests;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveImagingDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveSeries;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveStudy;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a {@link EbXMLRetrieveImagingDocumentSetRequest}.
 * 
 * @author Clay Sebourn
 */
public class RetrieveImagingDocumentSetRequestValidator implements Validator<EbXMLRetrieveImagingDocumentSetRequest, ValidationProfile> {
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Override
    public void validate(EbXMLRetrieveImagingDocumentSetRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");
        
        for (RetrieveStudy retrieveStudy : request.getRetrieveStudies()) {
            String studyInstanceUID = retrieveStudy.getStudyInstanceUID();
            metaDataAssert(studyInstanceUID != null && !studyInstanceUID.isEmpty(), STUDY_INSTANCE_UID_MUST_BE_SPECIFIED);

            for (RetrieveSeries retrieveSeries : retrieveStudy.getRetrieveSerieses()) {
                String seriesInstanceUID = retrieveSeries.getSeriesInstanceUID();
                metaDataAssert(seriesInstanceUID != null && !seriesInstanceUID.isEmpty(), SERIES_INSTANCE_UID_MUST_BE_SPECIFIED);

                for (RetrieveDocument document : retrieveSeries.getDocuments()) {
                    //todo: Eliminate this duplicate code from DocumentRequest?
                    String repoId = document.getRepositoryUniqueId();
                    metaDataAssert(repoId != null && !repoId.isEmpty(), REPO_ID_MUST_BE_SPECIFIED);

                    String docId = document.getDocumentUniqueId();
                    metaDataAssert(docId != null && !docId.isEmpty(), DOC_ID_MUST_BE_SPECIFIED);

                    if (profile.getProfile() == ValidationProfile.InteractionProfile.XCA) {
                        hcValidator.validate(document.getHomeCommunityId());
                    }
                }
            }
        }
    }
}