/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.HOME_COMMUNITY_ID_MUST_BE_SPECIFIED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_OID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validates a homeCommunityId attribute.
 * @author Dmytro Rud
 */
public class HomeCommunityIdValidator implements ValueValidator {
    private final boolean required;

    /**
     * Constructor.
     * @param required
     *      <code>true</code> when an exception should be thrown when
     *      the homeCommunityId attribute is not present.
     */
    public HomeCommunityIdValidator(boolean required) {
        this.required = required;
    }

    @Override
    public void validate(String homeCommunityId) throws XDSMetaDataException {
        if (required) {
            metaDataAssert((homeCommunityId != null) && (! homeCommunityId.isEmpty()),
                    HOME_COMMUNITY_ID_MUST_BE_SPECIFIED);
        }
        if (homeCommunityId != null) {
            metaDataAssert(homeCommunityId.startsWith("urn:oid:"), INVALID_OID, homeCommunityId);
            new OIDValidator().validate(homeCommunityId.substring(8));
        }
    }
}
