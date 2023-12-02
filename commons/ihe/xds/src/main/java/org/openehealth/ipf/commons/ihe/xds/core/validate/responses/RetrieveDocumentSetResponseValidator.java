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
package org.openehealth.ipf.commons.ihe.xds.core.validate.responses;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRetrieveDocumentSetResponse;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MIME_TYPE_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.MISSING_DOCUMENT_FOR_DOC_ENTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.ON_DEMAND_DOC_ID_MUST_DIFFER;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.REPO_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates {@link EbXMLRetrieveDocumentSetResponse}.
 *
 * @author Jens Riemschneider
 */
public class RetrieveDocumentSetResponseValidator implements Validator<EbXMLRetrieveDocumentSetResponse, ValidationProfile> {
    private final RegistryResponseValidator regResponseValidator = new RegistryResponseValidator();
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Override
    public void validate(EbXMLRetrieveDocumentSetResponse response, ValidationProfile profile) {
        requireNonNull(response, "response cannot be null");

        regResponseValidator.validate(response, profile);

        for (var doc : response.getDocuments()) {
            var requestData = doc.getRequestData();

            var repoId = requestData.getRepositoryUniqueId();
            metaDataAssert(repoId != null && !repoId.isEmpty(), REPO_ID_MUST_BE_SPECIFIED);

            var docId = requestData.getDocumentUniqueId();
            metaDataAssert(docId != null && !docId.isEmpty(), DOC_ID_MUST_BE_SPECIFIED);

            var newDocId = doc.getNewDocumentUniqueId();
            metaDataAssert(!docId.equals(newDocId), ON_DEMAND_DOC_ID_MUST_DIFFER);

            var mimeType = doc.getMimeType();
            metaDataAssert(mimeType != null && !mimeType.isEmpty(), MIME_TYPE_MUST_BE_SPECIFIED);

            if (profile.getInteractionProfile().getHomeCommunityIdOptionality() != XdsIntegrationProfile.HomeCommunityIdOptionality.NEVER) {
                hcValidator.validate(requestData.getHomeCommunityId());
            }

            metaDataAssert(doc.getDataHandler() != null, MISSING_DOCUMENT_FOR_DOC_ENTRY, docId);
        }
    }
}
