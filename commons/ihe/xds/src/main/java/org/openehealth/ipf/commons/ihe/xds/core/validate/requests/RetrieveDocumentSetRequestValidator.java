/*
 * Copyright 2009 the original author or authors.
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

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RetrieveDocument;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

/**
 * Validates a {@link EbXMLRetrieveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetRequestValidator implements Validator<EbXMLRetrieveDocumentSetRequest, ValidationProfile> {
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Override
    public void validate(EbXMLRetrieveDocumentSetRequest request, ValidationProfile profile) {
        notNull(request, "request cannot be null");
        
        for (RetrieveDocument document : request.getDocuments()) {
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
