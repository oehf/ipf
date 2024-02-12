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
import org.openehealth.ipf.commons.ihe.xds.XCA;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import static java.util.Objects.requireNonNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_ERROR_CODE_IN_RESPONSE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_ERROR_INFO_IN_RESPONSE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_SEVERITY_IN_RESPONSE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_STATUS_IN_RESPONSE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validate a {@link EbXMLRegistryResponse}.
 *
 * @author Jens Riemschneider
 */
public class RegistryResponseValidator implements Validator<EbXMLRegistryResponse, ValidationProfile> {
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Override
    public void validate(EbXMLRegistryResponse response, ValidationProfile profile) {
        requireNonNull(response, "response cannot be null");

        metaDataAssert(response.getStatus() != null, INVALID_STATUS_IN_RESPONSE);
        for (var registryError : response.getErrors()) {
            metaDataAssert(registryError != null, INVALID_ERROR_INFO_IN_RESPONSE);
            metaDataAssert(registryError.getErrorCode() != null, INVALID_ERROR_CODE_IN_RESPONSE);
            metaDataAssert(registryError.getSeverity() != null, INVALID_SEVERITY_IN_RESPONSE);

            if ((profile.getInteractionId() == XCA.Interactions.ITI_38) || (profile.getInteractionId() == XCA.Interactions.ITI_39)) {
                hcValidator.validate(registryError.getLocation());
            }
        }
    }
}
