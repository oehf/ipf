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

import lombok.Getter;
import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.XdsIntegrationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLNonconstructiveDocumentSetRequest;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DOC_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.REPO_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a {@link EbXMLNonconstructiveDocumentSetRequest}.
 * @author Jens Riemschneider
 */
public class NonconstructiveDocumentSetRequestValidator implements Validator<EbXMLNonconstructiveDocumentSetRequest<?>, ValidationProfile> {
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Getter
    private static final NonconstructiveDocumentSetRequestValidator instance = new NonconstructiveDocumentSetRequestValidator();

    private NonconstructiveDocumentSetRequestValidator() {
    }

    @Override
    public void validate(EbXMLNonconstructiveDocumentSetRequest<?> request, ValidationProfile profile) {
        requireNonNull(request, "request cannot be null");

        request.getDocuments().forEach(document -> {
            var repoId = document.getRepositoryUniqueId();
            metaDataAssert(repoId != null && !repoId.isEmpty(), REPO_ID_MUST_BE_SPECIFIED);
            var docId = document.getDocumentUniqueId();
            metaDataAssert(docId != null && !docId.isEmpty(), DOC_ID_MUST_BE_SPECIFIED);
            if (profile.getInteractionProfile().getHomeCommunityIdOptionality() != XdsIntegrationProfile.HomeCommunityIdOptionality.NEVER) {
                hcValidator.validate(document.getHomeCommunityId());
            }
        });
    }
}
