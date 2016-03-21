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

import static org.apache.commons.lang3.Validate.notNull;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryError;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

/**
 * Validate a {@link EbXMLRegistryResponse}.
 * @author Jens Riemschneider
 */
public class RegistryResponseValidator implements Validator<EbXMLRegistryResponse, ValidationProfile>{
    private final HomeCommunityIdValidator hcValidator = new HomeCommunityIdValidator(true);

    @Override
    public void validate(EbXMLRegistryResponse response, ValidationProfile profile) {
        notNull(response, "response cannot be null");
        
        metaDataAssert(response.getStatus() != null, INVALID_STATUS_IN_RESPONSE);
        for (EbXMLRegistryError registryError : response.getErrors()) {
            metaDataAssert(registryError != null, INVALID_ERROR_INFO_IN_RESPONSE);
            metaDataAssert(registryError.getErrorCode() != null, INVALID_ERROR_CODE_IN_RESPONSE);
            metaDataAssert(registryError.getSeverity() != null, INVALID_SEVERITY_IN_RESPONSE);

            if (profile.getProfile() == ValidationProfile.InteractionProfile.XCA) {
                hcValidator.validate(registryError.getLocation());
            }
        }
    }
}
