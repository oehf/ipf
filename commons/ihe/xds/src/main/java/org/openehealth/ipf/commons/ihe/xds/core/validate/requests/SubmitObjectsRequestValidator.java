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

import org.openehealth.ipf.commons.core.modules.api.Validator;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLSubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary;
import org.openehealth.ipf.commons.ihe.xds.core.validate.HomeCommunityIdValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.openehealth.ipf.commons.ihe.xds.XCMU.Interactions.CH_XCMU;

/**
 * Validates a {@link EbXMLSubmitObjectsRequest} request.
 * @author Jens Riemschneider
 */
public class SubmitObjectsRequestValidator implements Validator<EbXMLSubmitObjectsRequest, ValidationProfile> {
    private static final ObjectContainerValidator OBJECT_CONTAINER_VALIDATOR = new ObjectContainerValidator();

    /**
     * Validates the request.
     * @param request
     *          the request.
     * @throws XDSMetaDataException
     *          if the validation failed.
     */
    @Override
    public void validate(EbXMLSubmitObjectsRequest request, ValidationProfile profile)  {
        OBJECT_CONTAINER_VALIDATOR.validate(request, profile);

        HomeCommunityIdValidator homeCommunityIdValidator = new HomeCommunityIdValidator(profile == CH_XCMU);
        homeCommunityIdValidator.validate(request.getSingleSlotValue(Vocabulary.SLOT_NAME_HOME_COMMUNITY_ID));
    }
}
