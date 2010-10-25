/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xds.core.validate.query;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.validate.OIDValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INVALID_OID;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.metaDataAssert;

/**
 * Validator for home community ID attribute in stored queries.
 * <p>
 * See Section 3.18.4.1.2.3.8 in IHE ITI TF Vol. 2a.
 * @author Dmytro Rud
 */
public class HomeCommunityIdAttributeValidation implements QueryParameterValidation {

    @Override
    public void validate(EbXMLAdhocQueryRequest request) throws XDSMetaDataException {
        String oid = request.getHome();
        if (oid != null) {
            metaDataAssert(oid.startsWith("urn:oid:"), INVALID_OID, oid);
            new OIDValidator().validate(oid.substring(8));
        }
    }
}
