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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidatorAssertions.*;
import static org.apache.commons.lang3.StringUtils.*;

import ca.uhn.hl7v2.model.v25.datatype.HD;
import ca.uhn.hl7v2.model.v25.datatype.XON;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Organization;

/**
 * Validates a XON string.
 * @author Jens Riemschneider
 */
public class XONValidator implements ValueValidator {
    private static final OIDValidator OID_VALIDATOR = new OIDValidator();
    private static final HDValidator HD_VALIDATOR = new HDValidator();

    @Override
    public void validate(String hl7XON) throws XDSMetaDataException {
        Organization organization = Hl7v2Based.parse(hl7XON, Organization.class);
        metaDataAssert(organization != null, ORGANIZATION_NAME_MISSING, hl7XON);

        XON xon = organization.getHapiObject();
        metaDataAssert(isNotEmpty(xon.getXon1_OrganizationName().getValue()), ORGANIZATION_NAME_MISSING, hl7XON);

        HD hd = xon.getXon6_AssigningAuthority();
        if (HD_VALIDATOR.isEmpty(hd)) {
            String idNumber = xon.getXon10_OrganizationIdentifier().getValue();
            if (isNotEmpty(idNumber)) {
                OID_VALIDATOR.validate(idNumber);
            }
        } else {
            HD_VALIDATOR.validate(hd, hl7XON);
        }

        metaDataAssert(countMatches(hl7XON, "^") <= 9, ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon2_OrganizationNameTypeCode().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon3_IDNumber().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon4_CheckDigit().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon5_CheckDigitScheme().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon7_IdentifierTypeCode().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(HD_VALIDATOR.isEmpty(xon.getXon8_AssigningFacility()), ORGANIZATION_TOO_MANY_COMPONENTS);
        metaDataAssert(isEmpty(xon.getXon9_NameRepresentationCode().getValue()), ORGANIZATION_TOO_MANY_COMPONENTS);
    }
}
