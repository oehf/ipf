/*
 * Copyright 2013 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRemoveObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.RemoveDocumentSet.DEFAULT_DELETION_SCOPE;
/**
 * Validates a {@link org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRemoveObjectsRequest} request.
 * @author Boris Stanojevic
 */
public class RemoveObjectsRequestValidator implements Validator<EbXMLRemoveObjectsRequest, ValidationProfile> {

    /**
     * Validates the request.
     * @param request
     *          the request.
     * @throws org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException
     *          if the validation failed.
     */
    @Override
    public void validate(EbXMLRemoveObjectsRequest request, ValidationProfile profile)  {
        if (profile.getInteractionId() == IpfInteractionId.ITI_62) {
            metaDataAssert(request.getReferences().size() > 0, EMPTY_REFERENCE_LIST, "RemoveObjectsRequest");
            metaDataAssert(request.getDeletionScope().equals(DEFAULT_DELETION_SCOPE),
                           WRONG_DELETION_SCOPE, DEFAULT_DELETION_SCOPE);
            metaDataAssert(request.getId() == null &&
                           request.getHome() == null &&
                           request.getSlots().size() == 0, OBJECT_SHALL_NOT_BE_SPECIFIED, "AdhocQuery");
        }
    }
}
